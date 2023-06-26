package io.schlawiner.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayersTest {

    private val foo = Player.human("foo")
    private val bar = Player.human("bar")

    @Test
    fun newInstance() {
        val players = Players(listOf(foo, bar))

        assertEquals(foo, players.current)
        assertTrue(players.first())
        assertFalse(players.last())
    }

    @Test
    fun cycle() {
        val players = Players(listOf(foo, bar))

        // round 1
        assertEquals(bar, players.next())
        assertEquals(bar, players.current)

        // round 2
        assertEquals(foo, players.next())
        assertEquals(foo, players.current)

        // round 3
        assertEquals(bar, players.next())
        assertEquals(bar, players.current)

        // round 4
        assertEquals(foo, players.next())
        assertEquals(foo, players.current)
    }
}
