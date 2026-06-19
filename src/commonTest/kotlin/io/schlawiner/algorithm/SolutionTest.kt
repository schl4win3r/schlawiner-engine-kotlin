package io.schlawiner.algorithm

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SolutionTest {
    @Test
    fun compareByResult() {
        val lower = Solution("1 + 2", 3)
        val higher = Solution("2 + 3", 5)

        assertTrue(lower < higher)
    }

    @Test
    fun compareByTermWhenResultsEqual() {
        val a = Solution("1 + 4", 5)
        val b = Solution("2 + 3", 5)

        assertTrue(a < b)
    }

    @Test
    fun addWithinAllowedDifference() {
        val solutions = Solutions(target = 10, allowedDifference = 3)

        solutions.add(Solution("1 + 2 + 3", 8))
        assertEquals(8, solutions.bestSolution().result)
    }

    @Test
    fun addOutsideAllowedDifference() {
        val solutions = Solutions(target = 10, allowedDifference = 3)

        solutions.add(Solution("1 + 2 + 3", 2))
        assertEquals(Int.MAX_VALUE, solutions.bestSolution().result)
    }

    @Test
    fun bestSolutionClosestToTarget() {
        val solutions = Solutions(target = 10, allowedDifference = 5)

        solutions.add(Solution("far", 6))
        solutions.add(Solution("close", 9))
        solutions.add(Solution("exact", 10))

        assertEquals(10, solutions.bestSolution().result)
    }

    @Test
    fun bestSolutionWithLevel() {
        val solutions = Solutions(target = 10, allowedDifference = 5)
        solutions.add(Solution("close", 9))

        assertEquals(solutions.bestSolution(), solutions.bestSolution(io.schlawiner.game.Level.EASY))
    }
}
