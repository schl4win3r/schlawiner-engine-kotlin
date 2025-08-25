package io.schlawiner.term

import io.schlawiner.term.Operator.DIVIDED
import io.schlawiner.term.Operator.MINUS
import io.schlawiner.term.Operator.PLUS
import io.schlawiner.term.Operator.TIMES
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OperatorTest {
    @Test
    fun invalid() {
        assertNull(Operator.toOperator(""))
        assertNull(Operator.toOperator("    "))
        assertNull(Operator.toOperator("%"))
        assertNull(Operator.toOperator("foo"))
    }

    @Test
    fun operators() {
        assertEquals(PLUS, Operator.toOperator("+"))
        assertEquals(MINUS, Operator.toOperator("-"))
        assertEquals(TIMES, Operator.toOperator("*"))
        assertEquals(DIVIDED, Operator.toOperator("/"))
    }
}
