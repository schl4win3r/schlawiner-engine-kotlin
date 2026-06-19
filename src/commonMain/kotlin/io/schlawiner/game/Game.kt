package io.schlawiner.game

import io.schlawiner.algorithm.Algorithm
import io.schlawiner.algorithm.Solution
import io.schlawiner.term.TermException
import io.schlawiner.term.toTerm
import io.schlawiner.util.randomUUID
import kotlin.math.abs

/**
 * Central game orchestrator that owns all game state and coordinates gameplay.
 *
 * A game consists of [players] taking turns to reach a sequence of target [numbers] using three [dice] and basic
 * arithmetic. Human players submit expressions via [calculate]; computer players use [solve] to find optimal
 * solutions via the [algorithm]. Scores are tracked on the [scoreboard].
 *
 * @property name display name for this game
 * @property players the participating players (human and/or computer)
 * @property numbers the sequence of target numbers to reach
 * @property algorithm the strategy used to compute optimal solutions
 * @property settings game configuration (timeout, penalty, retries, etc.)
 */
@Suppress("TooManyFunctions")
class Game(
    val name: String,
    val players: Players,
    val numbers: Numbers,
    val algorithm: Algorithm,
    val settings: Settings,
) {
    private val id: String = randomUUID()
    private var _dice: Dice = Dice.random()
    private var _canceled: Boolean = false
    private val retries: MutableMap<Player, Int> =
        players.filter { it.human }.associateWith { settings.retries }.toMutableMap()

    /** Whether this game has been canceled. */
    val canceled: Boolean
        get() = _canceled

    /** The current dice roll. */
    val dice: Dice
        get() = _dice

    /** The game's score board tracking all players' scores across all numbers. */
    val scoreboard: Scoreboard = Scoreboard(players, numbers)

    /** Number of retries remaining for the current player (0 for computer players). */
    val retriesOfCurrentPlayer: Int
        get() = retries[players.current] ?: 0

    /**
     * Passes the dice to the next player. If the player is the first player, then it's the next number's turn.
     */
    fun next() {
        if (!isOver()) {
            players.next()
            if (players.first()) {
                numbers.next()
            }
        }
    }

    /**
     * @return `true` if there are more numbers or if it's not the last player and the game was not canceled,
     * `false` otherwise
     */
    fun isOver(): Boolean = _canceled || scoreboard.complete

    /** Rolls new dice (or sets specific [dice] values for testing). */
    fun rollDice(dice: Dice = Dice.random()) {
        this._dice = dice
    }

    /**
     * If the current player is human and has retries left, its retry count is decreased and new dice numbers are set.
     * Otherwise, this method does nothing.
     *
     * @return `true` if retry was successful, `false` otherwise
     */
    fun retry(): Boolean =
        if (players.current.human && retriesOfCurrentPlayer > 0) {
            retries[players.current] = retriesOfCurrentPlayer - 1
            rollDice()
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

    /** Records a timeout penalty for the current player on the current number. */
    fun timeout() {
        scoreboard[players.current, numbers.current] = Score("Timeout", settings.penalty)
    }

    /** Cancels this game, causing [isOver] to return `true`. */
    fun cancel() {
        this._canceled = true
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
        _dice.validate(term)
        val result = term.eval(emptyArray())
        val difference = abs(result - numbers.current)
        return if (difference > 0) {
            val solutions = algorithm.compute(_dice.a, _dice.b, _dice.c, numbers.current)
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
    fun solve(): Solution = algorithm.compute(_dice.a, _dice.b, _dice.c, numbers.current).bestSolution(settings.level)

    /** Records a [score] for the current player on the current number. */
    fun score(score: Score) {
        scoreboard[players.current, numbers.current] = score
    }

    override fun toString(): String = "Game($id, $name)"
}
