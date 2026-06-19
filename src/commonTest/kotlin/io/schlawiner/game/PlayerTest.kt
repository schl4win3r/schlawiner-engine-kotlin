package io.schlawiner.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayerTest {
    @Test
    fun humanPlayer() {
        val player = Player.human("Alice")

        assertEquals("Alice", player.name)
        assertTrue(player.human)
    }

    @Test
    fun computerPlayer() {
        val player = Player.computer("Bot")

        assertEquals("Bot", player.name)
        assertFalse(player.human)
    }
}
