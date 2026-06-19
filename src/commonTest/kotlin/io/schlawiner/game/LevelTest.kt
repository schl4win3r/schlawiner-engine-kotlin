package io.schlawiner.game

import kotlin.test.Test
import kotlin.test.assertEquals

class LevelTest {
    @Test
    fun maxDifference() {
        assertEquals(4, Level.EASY.maxDifference())
        assertEquals(2, Level.MEDIUM.maxDifference())
        assertEquals(0, Level.HARD.maxDifference())
    }
}
