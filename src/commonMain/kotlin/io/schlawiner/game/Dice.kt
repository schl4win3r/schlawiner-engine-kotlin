package io.schlawiner.game

import io.schlawiner.term.Term
import kotlin.random.Random

/** Thrown when a player's expression does not correctly use all three dice values. */
class DiceException(
    message: String,
) : RuntimeException(message)

/**
 * Represents a roll of three dice with values between 1 and 6.
 *
 * Provides validation that a player's expression uses each die exactly once (with optional 10x/100x multipliers)
 * and tracking of which dice have been used in a partial expression.
 *
 * @property a first die value (1–6)
 * @property b second die value (1–6)
 * @property c third die value (1–6)
 */
data class Dice(
    val a: Int,
    val b: Int,
    val c: Int,
) {
    private val diceNumbers: IntArray = intArrayOf(a, b, c)

    /**
     * Validates that the given [term] uses each of the three dice values exactly once.
     *
     * Each die may be multiplied by 1, 10, or 100 (e.g. die value 3 can appear as 3, 30, or 300).
     *
     * @throws DiceException if the term uses too few, too many, or incorrect dice values
     */
    fun validate(term: Term) {
        val numbers = term.values
        if (numbers.size < diceNumbers.size) {
            throw DiceException("The term contains not all dice numbers.")
        } else if (numbers.size > diceNumbers.size) {
            throw DiceException("The term contains more numbers than diced.")
        } else {
            val used = internalUsed(numbers.toIntArray())
            for (b in used) {
                if (!b) {
                    throw DiceException("You have not used all the dice numbers.")
                }
            }
        }
    }

    /** Returns a boolean array indicating which of the three dice are used in the given [expression] string. */
    fun used(expression: String): BooleanArray = internalUsed(extractNumbers(expression))

    private fun extractNumbers(expression: String): IntArray =
        NUMBERS
            .findAll(expression)
            .map {
                try {
                    it.value.toInt()
                } catch (e: NumberFormatException) {
                    throw DiceException("Invalid number $it")
                }
            }.toList()
            .toIntArray()

    @Suppress("NestedBlockDepth")
    private fun internalUsed(termNumbers: IntArray): BooleanArray {
        val used = BooleanArray(diceNumbers.size) { false }
        number@ for (termNumber in termNumbers) {
            for ((i, diceNumber) in diceNumbers.withIndex()) {
                if (!used[i]) {
                    for (multiplier in MULTIPLIERS) {
                        used[i] = termNumber == diceNumber * multiplier
                        if (used[i]) {
                            continue@number
                        }
                    }
                }
            }
        }
        return used
    }

    override fun toString(): String = diceNumbers.joinToString(" ")

    companion object {
        private const val MAX_DICE_NUMBER = 7
        private val NUMBERS: Regex = "\\d+".toRegex()
        private val MULTIPLIERS: IntArray = intArrayOf(1, 10, 100)

        /** Creates a new [Dice] with three random values between 1 and 6. */
        fun random(): Dice =
            Dice(
                Random.nextInt(1, MAX_DICE_NUMBER),
                Random.nextInt(1, MAX_DICE_NUMBER),
                Random.nextInt(1, MAX_DICE_NUMBER),
            )
    }
}
