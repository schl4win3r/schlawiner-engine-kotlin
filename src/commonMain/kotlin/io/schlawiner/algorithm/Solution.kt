package io.schlawiner.algorithm

import io.schlawiner.game.Level
import kotlin.math.abs

/**
 * A single arithmetic solution consisting of a human-readable expression and its computed result.
 *
 * Solutions are ordered first by result (ascending), then by expression string for deterministic tie-breaking.
 *
 * @property term the arithmetic expression as a string (e.g. `"4 + 60 - 10"`)
 * @property result the evaluated integer result of the expression
 */
data class Solution(
    val term: String,
    val result: Int,
) : Comparable<Solution> {
    override fun compareTo(other: Solution): Int =
        if (result != other.result) result - other.result else compareValues(term, other.term)

    override fun toString(): String = "$term = $result"

    companion object {
        /** Sentinel representing no solution found (result = [Int.MAX_VALUE]). */
        val MAX = Solution("", Int.MAX_VALUE)

        /** Sentinel for invalid operations like division by zero or non-integer division. */
        val INVALID = Solution("Invalid term", Int.MAX_VALUE)
    }
}

/**
 * Accumulator that tracks the best [Solution] found during an algorithm run.
 *
 * Only solutions within [allowedDifference] of the [target] are considered. Among those, the solution closest to
 * the target wins.
 *
 * @param target the number to reach (1–100)
 * @param allowedDifference maximum acceptable distance from target
 */
class Solutions(
    private val target: Int,
    private val allowedDifference: Int,
) {
    private var bestSolution: Solution = Solution.MAX

    /** Adds a candidate [solution], keeping it only if it's within the allowed window and closer than the current best. */
    fun add(solution: Solution) {
        if (solution.result >= target - allowedDifference && solution.result <= target + allowedDifference) {
            if (abs(solution.result - target) < abs(bestSolution.result - target)) {
                bestSolution = solution
            }
        }
    }

    /** Returns the best solution found so far, or [Solution.MAX] if none qualifies. */
    fun bestSolution(): Solution = bestSolution

    /** Returns the best solution for the given [level]. Currently delegates to [bestSolution]. */
    @Suppress("UNUSED_PARAMETER")
    fun bestSolution(level: Level): Solution = bestSolution()

    override fun toString(): String =
        "Solutions(target: $target, allowedDifference: $allowedDifference, bestSolution: $bestSolution)"
}
