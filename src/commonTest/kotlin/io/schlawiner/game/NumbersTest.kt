package io.schlawiner.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NumbersTest {
    @Test
    fun newInstance() {
        val numbers = Numbers(listOf(1, 2, 3, 4, 5))

        assertEquals(1, numbers.current)
        assertTrue(numbers.first())
        assertFalse(numbers.last())
    }

    @Test
    fun count() {
        assertEquals(23, Numbers(23).count())
    }

    @Test
    fun iterate() {
        val numbers = Numbers(listOf(1, 2, 3, 4, 5))

        // round 1
        assertEquals(2, numbers.next())
        assertEquals(2, numbers.current)

        // round 2
        assertEquals(3, numbers.next())
        assertEquals(3, numbers.current)

        // round 3
        assertEquals(4, numbers.next())
        assertEquals(4, numbers.current)

        // round 4
        assertEquals(5, numbers.next())
        assertEquals(5, numbers.current)
    }
}
