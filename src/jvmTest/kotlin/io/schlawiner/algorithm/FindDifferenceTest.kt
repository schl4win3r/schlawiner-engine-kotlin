package io.schlawiner.algorithm

import org.junit.jupiter.api.Test

private val DICE_NUMBER_COMBINATIONS = arrayOf(
    @Suppress("MaxLineLength")
    // ktlint-disable wrapping
    // @formatter:off
    intArrayOf(1, 1, 1),
    intArrayOf(1, 1, 2), intArrayOf(1, 1, 3), intArrayOf(1, 1, 4), intArrayOf(1, 1, 5),
    intArrayOf(1, 1, 6),
    intArrayOf(1, 2, 2), intArrayOf(1, 2, 3), intArrayOf(1, 2, 4), intArrayOf(1, 2, 5), intArrayOf(1, 2, 6),
    intArrayOf(1, 3, 3), intArrayOf(1, 3, 4), intArrayOf(1, 3, 5), intArrayOf(1, 3, 6),
    intArrayOf(1, 4, 4), intArrayOf(1, 4, 5), intArrayOf(1, 4, 6),
    intArrayOf(1, 5, 5), intArrayOf(1, 5, 6),
    intArrayOf(1, 6, 6),
    intArrayOf(2, 2, 2), intArrayOf(2, 2, 3), intArrayOf(2, 2, 4), intArrayOf(2, 2, 5), intArrayOf(2, 2, 6),
    intArrayOf(2, 3, 3), intArrayOf(2, 3, 4), intArrayOf(2, 3, 5), intArrayOf(2, 3, 6),
    intArrayOf(2, 4, 4), intArrayOf(2, 4, 5), intArrayOf(2, 4, 6),
    intArrayOf(2, 5, 5), intArrayOf(2, 5, 6),
    intArrayOf(2, 6, 6),
    intArrayOf(3, 3, 3), intArrayOf(3, 3, 4), intArrayOf(3, 3, 5), intArrayOf(3, 3, 6),
    intArrayOf(3, 4, 4), intArrayOf(3, 4, 5), intArrayOf(3, 4, 6),
    intArrayOf(3, 5, 5), intArrayOf(3, 5, 6),
    intArrayOf(3, 6, 6),
    intArrayOf(4, 4, 4), intArrayOf(4, 4, 5), intArrayOf(4, 4, 6),
    intArrayOf(4, 5, 5), intArrayOf(4, 5, 6),
    intArrayOf(4, 6, 6),
    intArrayOf(5, 5, 5), intArrayOf(5, 5, 6), intArrayOf(5, 6, 6),
    intArrayOf(6, 6, 6),
    // @formatter:on
)

class FindDifferenceTest {

    @Test
    @Suppress("NestedBlockDepth")
    fun findDifference() {
        var allowedDifference = 0
        while (true) {
            var solutionForAnyCombination = true
            println("Checking $allowedDifference")
            val algorithm: Algorithm = OperationAlgorithm(allowedDifference)
            mainLoop@ for (target in 1..100) {
                for (dnc in DICE_NUMBER_COMBINATIONS) {
                    val solutions = algorithm.compute(dnc[0], dnc[1], dnc[2], target)
                    if (solutions.bestSolution() == Solution.MAX) {
                        solutionForAnyCombination = false
                        break@mainLoop
                    }
                }
            }
            if (solutionForAnyCombination) {
                println("\nMax difference: $allowedDifference")
                break
            }
            allowedDifference++
        }
    }
}
