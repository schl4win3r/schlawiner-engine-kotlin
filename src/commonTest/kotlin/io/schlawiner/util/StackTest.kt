package io.schlawiner.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StackTest {
    @Test
    fun pushAndPop() {
        val stack = mutableStackOf<Int>()
        stack.push(1)
        stack.push(2)
        stack.push(3)

        assertEquals(3, stack.pop())
        assertEquals(2, stack.pop())
        assertEquals(1, stack.pop())
    }

    @Test
    fun peek() {
        val stack = mutableStackOf(1, 2, 3)

        assertEquals(3, stack.peek())
        assertEquals(3, stack.peek())
    }

    @Test
    fun popEmptyStack() {
        val stack = mutableStackOf<Int>()
        assertFailsWith<NoSuchElementException> { stack.pop() }
    }

    @Test
    fun peekEmptyStack() {
        val stack = mutableStackOf<Int>()
        assertFailsWith<NoSuchElementException> { stack.peek() }
    }

    @Test
    fun isEmpty() {
        val stack = mutableStackOf<Int>()
        assertTrue(stack.isEmpty())

        stack.push(1)
        assertFalse(stack.isEmpty())

        stack.pop()
        assertTrue(stack.isEmpty())
    }
}
