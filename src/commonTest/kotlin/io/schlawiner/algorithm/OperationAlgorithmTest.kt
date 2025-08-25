package io.schlawiner.algorithm

import kotlin.test.Test
import kotlin.test.assertEquals

class OperationAlgorithmTest {
    @Test
    fun compute() {
        val solutions: Solutions = OperationAlgorithm().compute(2, 3, 5, 15)
        assertEquals(15, solutions.bestSolution().result)
        assertEquals("30 + 5 - 20 = 15", solutions.bestSolution().toString())
    }
}
