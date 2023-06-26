package io.schlawiner.algorithm

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

private val DICE_NUMBERS = arrayOf(
    intArrayOf(2, 3, 5),
    intArrayOf(3, 6, 6),
    intArrayOf(4, 4, 4),
)

class AlgorithmComparisonTest {

    @Test
    fun compute() {
        val operationResults = computeAlgorithm(OperationAlgorithm())
        val termResults = computeAlgorithm(TermAlgorithm())
        for (i in DICE_NUMBERS.indices) {
            assertEquals(operationResults[i].bestSolution(), termResults[i].bestSolution())
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun computeAlgorithm(algorithm: Algorithm): List<Solutions> {
        val results = mutableListOf<Solutions>()
        for (i in DICE_NUMBERS.indices) {
            val duration = measureTime {
                for (target in 1..100) {
                    results.add(
                        algorithm.compute(
                            DICE_NUMBERS[i][0],
                            DICE_NUMBERS[i][1],
                            DICE_NUMBERS[i][2],
                            target,
                        ),
                    )
                }
            }
            println(
                """
                ${algorithm.name} finished in $duration ms for targets 1..100 
                using [${DICE_NUMBERS[i][0]},${DICE_NUMBERS[i][2]},${DICE_NUMBERS[i][2]}]
                """.trimIndent(),
            )
        }
        return results
    }
}
