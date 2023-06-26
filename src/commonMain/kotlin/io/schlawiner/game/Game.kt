package io.schlawiner.game

import io.schlawiner.algorithm.Algorithm
import io.schlawiner.algorithm.Solution
import io.schlawiner.term.TermException
import io.schlawiner.term.toTerm
import io.schlawiner.util.randomUUID
import kotlin.math.abs

@Suppress("TooManyFunctions")
class Game(
    val name: String,
    val players: Players,
    val numbers: Numbers,
    val algorithm: Algorithm,
    val settings: Settings,
) {

    private val id: String = randomUUID()
    private var dice: Dice = Dice.random()
    private var canceled: Boolean = false
    val scoreboard: Scoreboard = Scoreboard(players, numbers)

    /**
     * Passes the dice to the next player. If the player is the first player, then it's the next number's turn.
     */
    fun next() {
        players.next()
        if (players.first()) {
            numbers.next()
        }
    }

    /**
     * @return `true` if there are more numbers or if it's not the last player and the game was not canceled,
     * `false` otherwise
     */
    fun isOver(): Boolean {
        return (numbers.hasNext() || !players.last()) && !canceled
    }

    fun dice(dice: Dice = Dice.random()) {
        this.dice = dice
    }

    /**
     * If the current player is human and has retries left, its retry count is decreased and new dice numbers are set.
     * Otherwise, this method does nothing.
     *
     * @return `true` if retry was successful, `false` otherwise
     */
    fun retry(): Boolean = if (players.current.human && players.current.retries > 0) {
        players.current.retry()
        dice()
        true
    } else {
        false
    }

    /**
     * Skips the current number and scores [Settings.penalty] points as penalty. Does **not** call
     * [.next]
     */
    fun skip() {
        scoreboard[players.current, numbers.current] = Score("Skipped", settings.penalty)
    }

    fun timeout() {
        scoreboard[players.current, numbers.current] = Score("Timeout", settings.penalty)
    }

    fun cancel() {
        this.canceled = true
    }

    /**
     * Calculates the specified term for the current dice numbers and current target number.
     * Stores the difference in the score board.
     *
     * Meant to be called for human players.
     *
     * @return the difference between the calculated solution and the current number
     * @throws DiceException if the dice numbers aren't used correctly
     * @throws TermException if the expression isn't a valid term
     */
    fun calculate(expression: String): Calculation {
        val term = expression.toTerm()
        dice.validate(term)
        val result = term.eval(emptyArray())
        val difference = abs(result - numbers.current)
        return if (difference > 0) {
            val solutions = algorithm.compute(dice.a, dice.b, dice.c, numbers.current)
            Calculation(term, numbers.current, solutions.bestSolution())
        } else {
            Calculation(term, numbers.current, Solution(term.print(emptyArray()), result))
        }
    }

    /**
     * Computes the best solution for the current dice numbers and current target number based on the level.
     * Stores the difference in the score board.
     *
     * Meant to be called for computer players.
     *
     * @return the best solution based on the level
     */
    fun solve(): Solution =
        algorithm.compute(dice.a, dice.b, dice.c, numbers.current).bestSolution(settings.level)

    fun score(score: Score) {
        scoreboard[players.current, numbers.current] = score
    }

    override fun toString(): String = "Game($id, $name)"
}
