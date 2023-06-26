package io.schlawiner.game

import io.schlawiner.algorithm.OperationAlgorithm
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class GameTest {

    @Test
    fun humanVsComputerDraw() {
        val foo = Player.human("foo")
        val computer = Player.computer("computer")
        val game = Game(
            "test-game",
            Players(listOf(foo, computer)),
            Numbers(listOf(16, 23, 42)),
            OperationAlgorithm(),
            Settings.defaults().copy(level = Level.HARD),
        )

        // ------------------------------------------------------ 16
        game.dice(Dice(1, 2, 3))

        // human
        var calculation = game.calculate("10 + 2 * 3")
        game.score(Score("10 + 2 * 3", calculation.difference))

        // computer
        game.next()
        var solution = game.solve()
        game.score(Score(solution.term, abs(solution.result - game.numbers.current)))

        // ------------------------------------------------------ 23
        game.dice(Dice(4, 3, 1))

        // human
        game.next()
        calculation = game.calculate("30 - 10 + 4")
        game.score(Score("30 - 10 + 4", calculation.difference))

        // computer
        game.next()
        solution = game.solve()
        game.score(Score(solution.term, abs(solution.result - game.numbers.current)))

        // ------------------------------------------------------ 42
        game.dice(Dice(2, 5, 6))

        // human
        game.next()
        calculation = game.calculate("50 - 6 - 2")
        game.score(Score("50 - 6 - 2", calculation.difference))

        // computer
        game.next()
        solution = game.solve()
        game.score(Score(solution.term, abs(solution.result - game.numbers.current)))

        // ------------------------------------------------------ game over
        val fooScore = game.scoreboard.playerSums[foo]
        val computerScore = game.scoreboard.playerSums[computer]
        assertEquals(fooScore, computerScore)
        assertEquals(2, game.scoreboard.winners().size)
        assertContains(game.scoreboard.winners(), foo)
        assertContains(game.scoreboard.winners(), computer)
    }
}
