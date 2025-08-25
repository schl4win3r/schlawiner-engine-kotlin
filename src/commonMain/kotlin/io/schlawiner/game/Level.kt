package io.schlawiner.game

@Suppress("MagicNumber")
enum class Level(private val maxDifference: Int) {
    EASY(4),
    MEDIUM(2),
    HARD(0),
    ;

    fun maxDifference(): Int {
        return maxDifference
    }
}
