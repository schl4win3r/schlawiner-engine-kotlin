package io.schlawiner.term

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

// @formatter:off
private val PERMUTATIONS =
    arrayOf(
        arrayOf("a + b + c", "a b + c +"),
        arrayOf("a - b - c", "a b - c -"),
        arrayOf("a * b * c", "a b * c *"),
        arrayOf("a / b / c", "a b / c /"),
        arrayOf("a + b - c", "a b + c -"),
        arrayOf("a * b / c", "a b * c /"),
        arrayOf("a * b + c", "a b * c +"),
        arrayOf("(a + b) * c", "a b + c *"),
        arrayOf("a * b - c", "a b * c -"),
        arrayOf("a - b * c", "a b c * -"),
        arrayOf("(a - b) * c", "a b - c *"),
        arrayOf("a / b + c", "a b / c +"),
        arrayOf("(a + b) / c", "a b + c /"),
        arrayOf("a / (b + c)", "a b c + /"),
        arrayOf("a / b - c", "a b / c -"),
        arrayOf("a - b / c", "a b c / -"),
        arrayOf("(a - b) / c", "a b - c /"),
        arrayOf("a / (b - c)", "a b c - /"),
    )
// @formatter:on

class InfixTest {
    @Test
    fun empty() {
        assertContentEquals(emptyArray(), infixToRPN(""))
    }

    @Test
    fun blank() {
        assertContentEquals(emptyArray(), infixToRPN("  "))
    }

    @Test
    fun permutations() {
        for (i in PERMUTATIONS.indices) {
            val permutation = PERMUTATIONS[i]
            assertEquals(
                permutation[1],
                infixToRPN(permutation[0]).joinToString(" "),
                "Failed at index $i",
            )
        }
    }
}
