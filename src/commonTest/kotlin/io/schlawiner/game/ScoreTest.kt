package io.schlawiner.game

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ScoreTest {

    @Test
    fun numberScore() {
        val foo = Player.human("foo")
        val bar = Player.human("bar")
        val players = Players(listOf(foo, bar))
        val numberScore = NumberScore(23, players)

        assertEquals(23, numberScore.number)
        assertEquals(Score.EMPTY, numberScore[foo])
        assertEquals(Score.EMPTY, numberScore[bar])

        val one = Score("1", 1)
        val two = Score("2", 2)
        numberScore[foo] = one
        numberScore[bar] = two

        assertEquals(one, numberScore[foo])
        assertEquals(two, numberScore[bar])
    }

    @Test
    fun playerScore() {
        val numbers = Numbers(listOf(23, 42))
        val foo = Player.human("foo")
        val playerScore = PlayerScore(foo, numbers)

        assertEquals(foo, playerScore.player)
        assertEquals(Score.EMPTY, playerScore[23])
        assertEquals(Score.EMPTY, playerScore[42])

        val one = Score("1", 1)
        val two = Score("2", 2)
        playerScore[23] = one
        playerScore[42] = two

        assertEquals(one, playerScore[23])
        assertEquals(two, playerScore[42])
    }

    @Test
    fun scoreboard() {
        val foo = Player.human("foo")
        val bar = Player.human("bar")
        val players = Players(listOf(foo, bar))
        val numbers = Numbers(listOf(23, 42))

        // one winner
        var scoreboard = Scoreboard(players, numbers)
        scoreboard[foo, 23] = Score("1", 1)
        scoreboard[foo, 42] = Score("2", 2)
        scoreboard[bar, 23] = Score("3", 3)
        scoreboard[bar, 42] = Score("4", 4)

        assertEquals(3, scoreboard.playerSums[foo])
        assertEquals(7, scoreboard.playerSums[bar])
        assertEquals(foo, scoreboard.winners().first())

        // two winners
        scoreboard = Scoreboard(players, numbers)
        scoreboard[foo, 23] = Score("1", 1)
        scoreboard[foo, 42] = Score("2", 2)
        scoreboard[bar, 23] = Score("2", 2)
        scoreboard[bar, 42] = Score("1", 1)

        assertEquals(3, scoreboard.playerSums[foo])
        assertEquals(3, scoreboard.playerSums[bar])
        assertContains(scoreboard.winners(), foo)
        assertContains(scoreboard.winners(), bar)
    }
}
