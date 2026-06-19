package io.schlawiner.game

/**
 * Game configuration controlling timeouts, penalties, retries, and difficulty.
 *
 * @property timeout seconds before a player's turn times out
 * @property penalty difference points added for skips and timeouts
 * @property retries number of re-rolls allowed per human player per game
 * @property numbers how many target numbers to play
 * @property autoDice whether dice are rolled automatically at the start of each turn
 * @property level difficulty level affecting computer player behavior
 */
data class Settings(
    val timeout: Int,
    val penalty: Int,
    val retries: Int,
    val numbers: Int,
    val autoDice: Boolean,
    val level: Level,
) {
    companion object {
        /** Creates a [Settings] instance with sensible defaults: 60s timeout, 5 penalty, 3 retries, 8 numbers. */
        @Suppress("MagicNumber")
        fun defaults(): Settings = Settings(60, 5, 3, 8, false, Level.MEDIUM)
    }
}
