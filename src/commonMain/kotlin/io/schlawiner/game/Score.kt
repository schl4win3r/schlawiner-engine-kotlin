package io.schlawiner.game

import io.schlawiner.game.Score.Companion.EMPTY

/**
 * A single score entry recording an expression and its distance from the target number.
 *
 * Uses `String` instead of `Term` for [term] because the value may also be `"Skipped"` or `"Timeout"`.
 *
 * @property term the arithmetic expression string, or a status label like `"Skipped"` / `"Timeout"`
 * @property difference the absolute difference between the expression result and the target number
 */
data class Score(
    val term: String,
    val difference: Int,
) {
    override fun toString(): String = "$term Δ $difference"

    companion object {
        /** Sentinel representing an unscored slot (difference = -1). */
        val EMPTY: Score = Score("", -1)
    }
}

/**
 * Scores for a single target [number] across all players (one row of the number-indexed score table).
 *
 * Conceptually represents one row of the [Scoreboard] with the numbers as rows and the players as columns:
 *
 * |    | Player 1 | Player 2 |
 * |---:|---------:|---------:|
 * | 12 | 3        | 1        |
 * | 34 | 1        | 2        |
 *
 * @property number the target number this row represents
 */
class NumberScore(
    val number: Int,
    players: Players,
) {
    private val scores: MutableMap<Player, Score> =
        players.associateWith { Score.EMPTY }.toMutableMap()

    /** Whether all players have been scored for this number. */
    val complete: Boolean
        get() = scores.values.all { it != EMPTY }

    /** Returns the [Score] for the given [player], or [Score.EMPTY] if not yet scored. */
    operator fun get(player: Player) = scores[player] ?: Score.EMPTY

    /** Records a [score] for the given [player]. */
    operator fun set(
        player: Player,
        score: Score,
    ) {
        scores[player] = score
    }

    override fun toString(): String = "NumberScore($number, $scores)"
}

/**
 * Scores for a single [player] across all target numbers (one row of the player-indexed score table).
 *
 * Conceptually represents one row of the [Scoreboard] with the players as rows and the numbers as columns:
 *
 * |          | 12 | 34 |  4 | 52 |
 * |----------|---:|---:|---:|---:|
 * | Player 1 |  1 |  0 |  2 |  1 |
 * | Player 2 |  0 |  0 |  1 |  2 |
 *
 * @property player the player this row represents
 */
class PlayerScore(
    val player: Player,
    numbers: Numbers,
) {
    private val scores: MutableMap<Int, Score> =
        numbers.associateWith { Score.EMPTY }.toMutableMap()

    /** Whether this player has been scored for all numbers. */
    val complete: Boolean
        get() = scores.values.all { it != EMPTY }

    /** Returns the [Score] for the given target [number], or [Score.EMPTY] if not yet scored. */
    operator fun get(number: Int) = scores[number] ?: Score.EMPTY

    /** Records a [score] for the given target [number]. */
    operator fun set(
        number: Int,
        score: Score,
    ) {
        scores[number] = score
    }

    override fun toString(): String = "PlayerScore($player, $scores)"
}

/**
 * Dual-indexed score board providing both number-oriented and player-oriented views.
 *
 * Maintains running [playerSums] (total difference per player) and determines [winners] (lowest total difference).
 */
class Scoreboard(
    players: Players,
    numbers: Numbers,
) {
    /** Scores indexed by target number — each entry contains all players' scores for that number. */
    val numberScores: List<NumberScore> = numbers.map { NumberScore(it, players) }

    /** Scores indexed by player — each entry contains all numbers' scores for that player. */
    val playerScores: List<PlayerScore> = players.map { PlayerScore(it, numbers) }

    /** Whether all players have been scored for all numbers. */
    val complete: Boolean
        get() = numberScores.all { it.complete } && playerScores.all { it.complete }

    private val _playerSums: MutableMap<Player, Int> = players.associateWith { 0 }.toMutableMap()

    /** Running total of differences per player (lower is better). */
    val playerSums: Map<Player, Int>
        get() = _playerSums

    /** Returns the [Score] for the given [player] and target [number]. */
    operator fun get(
        player: Player,
        number: Int,
    ): Score = numberScores.find { it.number == number }?.get(player) ?: Score.EMPTY

    /** Records a [score] for the given [player] on the given target [number], updating both indexes and the running sum. */
    operator fun set(
        player: Player,
        number: Int,
        score: Score,
    ) {
        numberScores.find { it.number == number }?.let { numberScore ->
            numberScore[player] = score
        }
        playerScores.find { it.player == player }?.let { playerScore ->
            playerScore[number] = score
        }
        _playerSums[player] = checkNotNull(_playerSums[player]) + score.difference
    }

    /** Returns the player(s) with the lowest total difference. May return multiple players in case of a tie. */
    fun winners(): List<Player> {
        val min = playerSums.values.min()
        return playerSums.filterValues { it == min }.keys.toList()
    }

    override fun toString(): String = "Scoreboard(playerSums: $playerSums)"
}
