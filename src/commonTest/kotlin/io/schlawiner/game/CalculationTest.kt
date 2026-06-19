package io.schlawiner.game

import io.schlawiner.algorithm.Solution
import io.schlawiner.term.toTerm
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalculationTest {
    @Test
    fun exactMatch() {
        val term = "10 + 2 * 3".toTerm()
        val calculation = Calculation(term, 16, Solution("10 + 2 * 3", 16))

        assertEquals(0, calculation.difference)
        assertEquals(0, calculation.bestDifference)
        assertTrue(calculation.best)
    }

    @Test
    fun equalToBest() {
        val term = "2 + 3 + 5".toTerm()
        val calculation = Calculation(term, 12, Solution("best", 14))

        assertEquals(2, calculation.difference)
        assertEquals(2, calculation.bestDifference)
        assertTrue(calculation.best)
    }

    @Test
    fun worseThanBest() {
        val term = "2 + 3 + 5".toTerm()
        val calculation = Calculation(term, 12, Solution("best", 12))

        assertEquals(2, calculation.difference)
        assertEquals(0, calculation.bestDifference)
        assertFalse(calculation.best)
    }
}
