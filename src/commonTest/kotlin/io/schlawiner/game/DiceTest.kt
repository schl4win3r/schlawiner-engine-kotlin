package io.schlawiner.game

import io.schlawiner.term.toTerm
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith

class DiceTest {
    private val dice = Dice(1, 2, 3)

    @Test
    fun twoNumbers() {
        assertFailsWith<DiceException> { dice.validate("1 + 2".toTerm()) }
    }

    @Test
    fun fourNumbers() {
        assertFailsWith<DiceException> { dice.validate("1 + 2 + 3 + 4".toTerm()) }
    }

    @Test
    fun wrongNumbers() {
        assertFailsWith<DiceException> { dice.validate("1 + 2 + 4".toTerm()) }
    }

    @Test
    fun wrongMultiplier() {
        assertFailsWith<DiceException> { dice.validate("1 + 2 + 3000".toTerm()) }
    }

    @Test
    fun validate() {
        dice.validate("1 + 2 + 3".toTerm())
        dice.validate("1 + 20 + 300".toTerm())
    }

    @Test
    fun used() {
        assertContentEquals(booleanArrayOf(false, false, false), dice.used(""))
        assertContentEquals(booleanArrayOf(false, false, false), dice.used("    "))

        assertContentEquals(booleanArrayOf(true, false, false), dice.used("1"))
        assertContentEquals(booleanArrayOf(false, true, false), dice.used("2"))
        assertContentEquals(booleanArrayOf(false, false, true), dice.used("3"))

        assertContentEquals(booleanArrayOf(true, false, false), dice.used("4 1"))
        assertContentEquals(booleanArrayOf(false, true, false), dice.used("5 2"))
        assertContentEquals(booleanArrayOf(false, false, true), dice.used("6 3"))

        assertContentEquals(booleanArrayOf(true, false, false), dice.used("1 4"))
        assertContentEquals(booleanArrayOf(false, true, false), dice.used("2 5"))
        assertContentEquals(booleanArrayOf(false, false, true), dice.used("3 6"))

        assertContentEquals(booleanArrayOf(true, false, false), dice.used("4 1 4"))
        assertContentEquals(booleanArrayOf(false, true, false), dice.used("5 2 5"))
        assertContentEquals(booleanArrayOf(false, false, true), dice.used("6 3 6"))

        assertContentEquals(booleanArrayOf(true, false, false), dice.used("10"))
        assertContentEquals(booleanArrayOf(true, false, false), dice.used("100"))
        assertContentEquals(booleanArrayOf(true, false, false), dice.used("1 10 100"))
        assertContentEquals(booleanArrayOf(true, false, false), dice.used("4 5 100"))
    }
}
