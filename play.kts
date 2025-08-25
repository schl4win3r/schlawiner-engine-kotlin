#!/usr/bin/env kscript
@file:DependsOn("com.github.ajalt.mordant:mordant-jvm:2.0.1")
@file:DependsOn("io.schlawiner:schlawiner-engine-kotlin-jvm:0.0.1")

import com.github.ajalt.mordant.rendering.BorderType.Companion.SQUARE_DOUBLE_SECTION_SEPARATOR
import com.github.ajalt.mordant.rendering.TextAlign.LEFT
import com.github.ajalt.mordant.rendering.TextAlign.RIGHT
import com.github.ajalt.mordant.rendering.TextColors.cyan
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.ConversionResult
import com.github.ajalt.mordant.terminal.Prompt
import com.github.ajalt.mordant.terminal.StringPrompt
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.YesNoPrompt
import io.schlawiner.algorithm.OperationAlgorithm
import io.schlawiner.game.Dice
import io.schlawiner.game.Game
import io.schlawiner.game.Level
import io.schlawiner.game.Numbers
import io.schlawiner.game.Player
import io.schlawiner.game.Players
import io.schlawiner.game.Score
import io.schlawiner.game.Settings
import kotlin.math.abs
import kotlin.system.exitProcess

val banner =
    """

                                                               ____
                                                              /\' .\    _____
                                                             /: \___\  / .  /\
                                                             \' / . / /____/..\
                                                              \/___/  \'  '\  /
      _________      .__    .__                .__                     \'__'\/
     /   _____/ ____ |  |__ |  | _____ __  _  _|__| ____   ___________
     \_____  \_/ ___\|  |  \|  | \__  \\ \/ \/ /  |/    \_/ __ \_  __ \
     /        \  \___|   Y  \  |__/ __ \\     /|  |   |  \  ___/|  | \/
    /_______  /\___  >___|  /____(____  /\/\_/ |__|___|  /\___  >__|
            \/     \/     \/          \/               \/     \/


    """.trimIndent()

var settings = Settings.defaults()
val players = mutableListOf(Player.human("Player 1"), Player.computer("Computer"))

val t = Terminal()
t.print(banner)
t.println()
t.main()

fun Terminal.main() {
    println()
    println(cyan("Main Menu"))
    println()
    println("[1] Settings")
    println("[2] Players")
    println("[3] Play")
    println("[0] Exit")
    println()
    when (RangePrompt(terminal = this, range = 0..3).ask()) {
        1 -> settings()
        2 -> players()
        3 -> play()
        0 -> exitProcess(0)
    }
}

fun Terminal.settings() {
    println()
    println(cyan("Settings"))
    println()
    println("[1] Numbers\t${settings.numbers}")
    println("[2] Retries\t${settings.retries}")
    println("[3] Level\t${settings.level.name.lowercase()}")
    println("[0] Back")
    println()
    when (RangePrompt(terminal = this, range = 0..3).ask()) {
        1 -> {
            RangePrompt(prompt = "Number of numbers (2..20)", terminal = this, range = 2..20).ask()?.let {
                settings = settings.copy(numbers = it)
            }
            settings()
        }

        2 -> {
            RangePrompt(prompt = "Number of retries (0..5)", terminal = this, range = 0..5).ask()?.let {
                settings = settings.copy(retries = it)
            }
            settings()
        }

        3 -> {
            LevelPrompt(prompt = "Level", terminal = this, showChoices = true).ask()?.let {
                settings = settings.copy(level = it)
            }
            settings()
        }

        0 -> {
            main()
        }
    }
}

fun Terminal.players() {
    println()
    println(cyan("Players"))
    println()
    println("[1] Add player")
    println("[2] Show players")
    println("[3] Remove player")
    println("[0] Back")
    println()
    when (RangePrompt(terminal = this, range = 0..3).ask()) {
        1 -> {
            val name = StringPrompt(prompt = "Name", terminal = this, allowBlank = false).ask()
            val human = YesNoPrompt(prompt = "Human", terminal = this).ask()
            players.add(Player(name!!, human!!))
            players()
        }

        2 -> {
            println()
            if (players.isEmpty()) {
                println("No players!")
            } else {
                players.forEach { println(it) }
            }
            players()
        }

        3 -> {
            println()
            if (players.isEmpty()) {
                println("No players!")
            } else {
                players.forEachIndexed { index, player ->
                    println("[${index + 1}] $player")
                }
                println("[0] Back")
                println()
                RangePrompt(prompt = "Please choose", terminal = this, range = 0..players.size).ask()?.let {
                    if (it != 0) {
                        players.removeAt(it - 1)
                    }
                }
            }
            players()
        }

        0 -> {
            main()
        }
    }
}

fun Terminal.play() {
    if (players.isEmpty()) {
        println("No players!")
    } else {
        println("To dice again, enter 'retry'.")
        println("To skip the current number, enter 'skip'")
        println("To cancel the game, enter 'cancel'")
        println()

        with(Game("console-game", Players(players), Numbers(settings.numbers), OperationAlgorithm(), settings)) {
            while (!isOver()) {
                rollDice(Dice.random())
                if (players.first()) {
                    printScoreboard(this)
                }
                if (players.current.human) {
                    var validTerm = false
                    var expression: String?
                    while (!validTerm && !isOver()) {
                        expression =
                            StringPrompt(
                                prompt = "${players.current.name} try to reach ${numbers.current} using $dice",
                                terminal = this@play,
                            ).ask()
                        when (expression) {
                            "retry" -> {
                                if (retry()) {
                                    println("You have $retriesOfCurrentPlayer retries left.")
                                } else {
                                    println("Sorry you have no retries left.")
                                }
                            }

                            "skip" -> {
                                skip()
                                validTerm = true
                            }

                            "cancel" -> {
                                cancel()
                            }

                            else -> {
                                expression?.let {
                                    try {
                                        val calculation = calculate(it)
                                        if (calculation.best) {
                                            println("Well done, your solution is the best.")
                                        } else {
                                            println(
                                                "Your difference is ${calculation.difference}. " +
                                                    "The best solution is ${calculation.bestSolution} " +
                                                    "(difference ${calculation.bestDifference})",
                                            )
                                        }
                                        score(Score(calculation.term.toString(), calculation.difference))
                                        validTerm = true
                                    } catch (e: Exception) {
                                        println(e.message)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    val solution = solve()
                    score(Score(solution.term, abs(solution.result - numbers.current)))
                    println("${players.current.name} diced $dice, Solution: $solution")
                }
                next()
            }

            if (!canceled) {
                printScoreboard(this)
                println("Game over")
                val winners = scoreboard.winners()
                if (winners.size == 1) {
                    println("The winner is ${winners[0].name}")
                } else {
                    println("The winners are ${winners.joinToString(", ") { it.name }}")
                }
            }
        }
    }
    main()
}

fun Terminal.printScoreboard(game: Game) {
    println()
    println(
        table {
            align = RIGHT
            borderType = SQUARE_DOUBLE_SECTION_SEPARATOR
            header {
                row {
                    cell("")
                    game.players.forEach {
                        cell(it.name) {
                            align = LEFT
                            columnSpan = 2
                        }
                    }
                }
            }
            body {
                game.numbers.forEach { number ->
                    row {
                        cell(number)
                        game.players.forEach { player ->
                            val score = game.scoreboard[player, number]
                            cell(score.term)
                            cell(if (score.difference == -1) "" else score.difference.toString())
                        }
                    }
                }
            }
            footer {
                row {
                    cell("")
                    game.players.forEach {
                        cell(game.scoreboard.playerSums[it]) {
                            columnSpan = 2
                        }
                    }
                }
            }
        },
    )
    println()
}

class RangePrompt(
    prompt: String = "Please choose",
    terminal: Terminal,
    default: Int? = null,
    showDefault: Boolean = false,
    showChoices: Boolean = false,
    hideInput: Boolean = false,
    range: IntRange = 0..0,
    promptSuffix: String = ": ",
    invalidChoiceMessage: String = "Invalid value, choose from ",
) : Prompt<Int>(
        prompt,
        terminal,
        default,
        showDefault,
        showChoices,
        hideInput,
        range.toList(),
        promptSuffix,
        invalidChoiceMessage,
    ) {
    override fun convert(input: String): ConversionResult<Int> {
        val choice = input.toIntOrNull()
        if (choice != null) {
            if (choice in choices) {
                return ConversionResult.Valid(choice)
            }
        }
        return ConversionResult.Invalid(
            buildString {
                append(invalidChoiceMessage)
                append(makePromptChoices())
            },
        )
    }
}

class LevelPrompt(
    prompt: String = "Please choose",
    terminal: Terminal,
    default: Level? = Level.MEDIUM,
    showDefault: Boolean = false,
    showChoices: Boolean = false,
    hideInput: Boolean = false,
    choices: List<Level> = Level.values().asList(),
    promptSuffix: String = ": ",
    invalidChoiceMessage: String = "Invalid value, choose from ",
) : Prompt<Level>(
        prompt,
        terminal,
        default,
        showDefault,
        showChoices,
        hideInput,
        choices,
        promptSuffix,
        invalidChoiceMessage,
    ) {
    override fun convert(input: String): ConversionResult<Level> {
        val level = choices.firstOrNull { it.name.equals(input, true) }
        if (level != null) {
            return ConversionResult.Valid(level)
        }
        return ConversionResult.Invalid(
            buildString {
                append(invalidChoiceMessage)
                append(makePromptChoices())
            },
        )
    }

    override fun renderValue(value: Level): String = value.name.lowercase()
}
