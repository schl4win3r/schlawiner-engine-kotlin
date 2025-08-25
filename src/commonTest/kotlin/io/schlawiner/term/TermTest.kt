package io.schlawiner.term

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TermTest {
    @Test
    fun empty() {
        assertFailsWith<TermException> { "".toTerm() }
    }

    @Test
    fun blank() {
        assertFailsWith<TermException> { "   ".toTerm() }
    }

    @Test
    fun invalid() {
        assertFailsWith<TermException> { "foo".toTerm() }
    }

    @Test
    fun oneNumber() {
        assertFailsWith<TermException> { "1".toTerm() }
    }

    @Test
    fun oneOperator() {
        assertFailsWith<TermException> { "+".toTerm() }
    }

    @Test
    fun noOperator() {
        assertFailsWith<TermException> { "1 2".toTerm() }
    }

    @Test
    fun wrongOperator() {
        assertFailsWith<TermException> { "10 & 2 % 3".toTerm() }
    }

    @Test
    fun eval() {
        assertEquals(10, "2 + 3 + 5".toTerm().eval(emptyArray()))
        assertEquals(11, "2 * 3 + 5".toTerm().eval(emptyArray()))
        assertEquals(16, "2 * (3 + 5)".toTerm().eval(emptyArray()))
        assertEquals(25, "(2 + 3) * 5".toTerm().eval(emptyArray()))
        assertEquals(12, "10 * (3 - 2) + (4 + 6) / n".toTerm().eval(arrayOf(Assignment("n", 5))))
        assertEquals(16, "10+2*3".toTerm().eval(emptyArray()))
        assertEquals(36, "(10+2)*3".toTerm().eval(emptyArray()))
        assertEquals(16, "10 + 2 * 3".toTerm().eval(emptyArray()))
        assertEquals(36, "(10 + 2) * 3".toTerm().eval(emptyArray()))
        assertEquals(36, "((10 + 2) * (3))".toTerm().eval(emptyArray()))
    }

    @Test
    fun print() {
        assertEquals("2 + 3 + 5", "2 + 3 + 5".toTerm().print(emptyArray()))
        assertEquals("2 * 3 + 5", "2 * 3 + 5".toTerm().print(emptyArray()))
        assertEquals("2 * (3 + 5)", "2 * (3 + 5)".toTerm().print(emptyArray()))
        assertEquals("(2 + 3) * 5", "(2 + 3) * 5".toTerm().print(emptyArray()))
        assertEquals("10 * (3 - 2) + (4 + 6) / n", "10 * (3 - 2) + (4 + 6) / n".toTerm().print(emptyArray()))
        assertEquals("10 + 2 * 3", "10+2*3".toTerm().print(emptyArray()))
        assertEquals("(10 + 2) * 3", "(10+2)*3".toTerm().print(emptyArray()))
        assertEquals("10 + 2 * 3", "10 + 2 * 3".toTerm().print(emptyArray()))
        assertEquals("(10 + 2) * 3", "(10 + 2) * 3".toTerm().print(emptyArray()))
        assertEquals("(10 + 2) * 3", "((10 + 2) * (3))".toTerm().print(emptyArray()))
    }

    @Test
    fun values() {
        assertContentEquals(listOf(2, 3, 5), "2 + 3 + 5".toTerm().values)
        assertContentEquals(listOf(2, 3, 5), "2 * 3 + 5".toTerm().values)
        assertContentEquals(listOf(2, 3, 5), "2 * (3 + 5)".toTerm().values)
        assertContentEquals(listOf(2, 3, 5), "(2 + 3) * 5".toTerm().values)
        assertContentEquals(listOf(10, 3, 2, 4, 6), "10 * (3 - 2) + (4 + 6) / n".toTerm().values)
        assertContentEquals(listOf(10, 2, 3), "10+2*3".toTerm().values)
        assertContentEquals(listOf(10, 2, 3), "(10+2)*3".toTerm().values)
        assertContentEquals(listOf(10, 2, 3), "10 + 2 * 3".toTerm().values)
        assertContentEquals(listOf(10, 2, 3), "(10 + 2) * 3".toTerm().values)
        assertContentEquals(listOf(10, 2, 3), "((10 + 2) * (3))".toTerm().values)
    }
}
