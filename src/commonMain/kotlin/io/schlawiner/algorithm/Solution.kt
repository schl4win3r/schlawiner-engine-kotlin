package io.schlawiner.algorithm

import io.schlawiner.game.Level
import kotlin.math.abs

data class Solution(val term: String, val result: Int) : Comparable<Solution> {

    override fun compareTo(other: Solution): Int =
        if (result != other.result) result - other.result else compareValues(term, other.term)

    override fun toString(): String = "$term = $result"

    companion object {
        val MAX = Solution("", Int.MAX_VALUE)
        val INVALID = Solution("Invalid term", Int.MAX_VALUE)
    }
}

class Solutions(private val target: Int, private val allowedDifference: Int) {

    private var bestSolution: Solution = Solution.MAX

    fun add(solution: Solution) {
        if (solution.result >= target - allowedDifference && solution.result <= target + allowedDifference) {
            if (abs(solution.result - target) < abs(bestSolution.result - target)) {
                bestSolution = solution
            }
        }
    }

    fun bestSolution(): Solution = bestSolution

    @Suppress("UNUSED_PARAMETER")
    fun bestSolution(level: Level): Solution = bestSolution()

    override fun toString(): String =
        "Solutions(target: $target, allowedDifference: $allowedDifference, bestSolution: $bestSolution)"
}
