package io.schlawiner.game

data class Settings(
    val timeout: Int,
    val penalty: Int,
    val retries: Int,
    val numbers: Int,
    val autoDice: Boolean,
    val level: Level,
) {
    @Suppress("MagicNumber")
    companion object {
        fun defaults(): Settings = Settings(60, 5, 3, 8, false, Level.MEDIUM)
    }
}
