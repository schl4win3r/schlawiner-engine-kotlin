package io.schlawiner.game

// use String instead of Term here, since the 'term' could also be "skipped" or "timeout"
data class Score(val term: String, val difference: Int) {

    override fun toString(): String = "$term Δ $difference"

    companion object {
        val EMPTY: Score = Score("", -1)
    }
}

/**
 * Represents one row of the [score board](Scoreboard) with the numbers as rows and the players as columns.
 *
 * |    | Player 1 | Player 2 |
 * |---:|---------:|---------:|
 * | 12 | 3        | 1        |
 * | 34 | 1        | 2        |
 * | 4  | 1        | 2        |
 * | 52 | 1        | 2        |
 * | 57 | 1        | 2        |
 * | 80 | 1        | 2        |
 */
class NumberScore(val number: Int, players: Players) {

    private val scores: MutableMap<Player, Score> =
        players.associateWith { Score.EMPTY }.toMutableMap()

    operator fun get(player: Player) = scores[player] ?: Score.EMPTY

    operator fun set(player: Player, score: Score) {
        scores[player] = score
    }

    override fun toString(): String = "NumberScore($number, $scores)"
}

/**
 * Represents one row of the [score board](Scoreboard) with the players as rows and the numbers as columns.
 *
 * |          | 12 | 34 |  4 | 52 | 57 | 80 |
 * |----------|---:|---:|---:|---:|---:|---:|
 * | Player 1 |  1 |  0 |  2 |  1 |  0 |  4 |
 * | Player 2 |  0 |  0 |  1 |  2 |  0 |  3 |
 */
class PlayerScore(val player: Player, numbers: Numbers) {

    private val scores: MutableMap<Int, Score> =
        numbers.associateWith { Score.EMPTY }.toMutableMap()

    operator fun get(number: Int) = scores[number] ?: Score.EMPTY

    operator fun set(number: Int, score: Score) {
        scores[number] = score
    }

    override fun toString(): String = "PlayerScore($player, $scores)"
}

class Scoreboard(players: Players, numbers: Numbers) {

    val numberScores: List<NumberScore> = numbers.map { NumberScore(it, players) }
    val playerScores: List<PlayerScore> = players.map { PlayerScore(it, numbers) }

    private val _playerSums: MutableMap<Player, Int> = players.associateWith { 0 }.toMutableMap()
    val playerSums: Map<Player, Int>
        get() = _playerSums

    operator fun get(player: Player, number: Int): Score =
        numberScores.find { it.number == number }?.get(player) ?: Score.EMPTY

    operator fun set(player: Player, number: Int, score: Score) {
        numberScores.find { it.number == number }?.let { numberScore ->
            numberScore[player] = score
        }
        playerScores.find { it.player == player }?.let { playerScore ->
            playerScore[number] = score
        }
        _playerSums[player]?.let { current ->
            _playerSums[player] = current + score.difference
        }
    }

    fun winners(): List<Player> {
        val min = playerSums.values.min()
        return playerSums.filterValues { it == min }.keys.toList()
    }

    override fun toString(): String = "Scoreboard(playerSums: $playerSums)"
}
