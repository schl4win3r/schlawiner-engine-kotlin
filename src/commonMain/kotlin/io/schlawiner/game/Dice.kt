package io.schlawiner.game

import io.schlawiner.term.Term
import kotlin.random.Random

class DiceException(message: String) : RuntimeException(message)

data class Dice(val a: Int, val b: Int, val c: Int) {

    private val diceNumbers: IntArray = intArrayOf(a, b, c)

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

    fun used(expression: String): BooleanArray = internalUsed(extractNumbers(expression))

    private fun extractNumbers(expression: String): IntArray = NUMBERS.findAll(expression).map {
        try {
            it.value.toInt()
        } catch (e: NumberFormatException) {
            throw DiceException("Invalid number $it")
        }
    }.toList().toIntArray()

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

    override fun toString(): String = "Dice($a, $b, $c)"

    companion object {

        private const val MAX_DICE_NUMBER = 7
        private val NUMBERS: Regex = "\\d+".toRegex()
        private val MULTIPLIERS: IntArray = intArrayOf(1, 10, 100)

        fun random(): Dice = Dice(
            Random.nextInt(1, MAX_DICE_NUMBER),
            Random.nextInt(1, MAX_DICE_NUMBER),
            Random.nextInt(1, MAX_DICE_NUMBER),
        )
    }
}
