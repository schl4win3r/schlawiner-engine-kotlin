package io.schlawiner.game

/**
 * Difficulty level controlling computer player accuracy.
 *
 * Each level defines a [maxDifference] — the maximum acceptable distance from the target for the computer's solution.
 * Lower values make the computer more precise.
 *
 * @property maxDifference maximum acceptable distance from the target number
 */
@Suppress("MagicNumber")
enum class Level(
    private val maxDifference: Int,
) {
    /** Computer may be up to 4 away from the target. */
    EASY(4),

    /** Computer may be up to 2 away from the target. */
    MEDIUM(2),

    /** Computer must hit the target exactly. */
    HARD(0),
    ;

    /** Returns the maximum difference allowed for this level. */
    fun maxDifference(): Int = maxDifference
}
