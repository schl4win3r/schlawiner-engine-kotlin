# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Schlawiner Engine is a Kotlin Multiplatform (KMP) library implementing a mathematical dice game. Players roll three dice and combine the values (optionally multiplied by 10 or 100) using +, -, *, / to match target numbers 1-100. The engine supports human and computer players.

## Build Commands

```shell
./gradlew build                    # Full build (compile + test + lint)
./gradlew allTests                 # Run all tests (JVM + JS)
./gradlew jvmTest                  # Run JVM tests only
./gradlew jsTest                   # Run JS tests (requires Chrome headless)
./gradlew jvmTest --tests "io.schlawiner.game.GameTest"  # Single test class
./gradlew detekt                   # Static analysis
./gradlew ktlintCheck              # Lint check
./gradlew ktlintFormat             # Auto-format
./gradlew publishToMavenLocal      # Publish to local Maven repo (required before play.kts)
```

Play the game interactively (requires `kscript` and prior `publishToMavenLocal`):

```shell
kscript play.kts
```

## Architecture

### Kotlin Multiplatform Structure

All game logic lives in `commonMain` -- only UUID generation has platform-specific `expect`/`actual` implementations in `jsMain` (npm `uuid`) and `jvmMain` (`java.util.UUID`).

```
src/
  commonMain/   # All domain logic (18 files)
  commonTest/   # Cross-platform tests (10 files, run on JVM + JS)
  jvmMain/      # UUID actual (1 file)
  jvmTest/      # Algorithm comparison + brute-force analysis (2 files)
  jsMain/       # UUID actual via npm uuid package (1 file)
```

### Package Layout (`io.schlawiner.*`)

**`algorithm`** -- Two interchangeable `Algorithm` implementations that find optimal solutions for any dice + target combination:
- `OperationAlgorithm`: hard-coded arithmetic permutations (18 operation patterns across all variable orderings)
- `TermAlgorithm`: pre-parsed `Term` templates (68 static terms) evaluated with variable assignments
- Both produce identical results (verified by `AlgorithmComparisonTest`). Both iterate 27 multiplier combinations (1/10/100 per die).
- `DEFAULT_DIFFERENCE = 15`: empirically computed minimum window guaranteeing a solution exists for every dice combo and every target 1-100 (computed by `FindDifferenceTest`).

**`term`** -- Expression tree (AST) and parser:
- `Term`/`Value`/`Variable` form the tree nodes; `Operator` enum defines +, -, *, / with precedence
- `Infix.infixToRPN()`: shunting-yard algorithm converts infix strings to RPN
- `String.toTerm()`: parses infix expression string into a `Term` tree (via RPN intermediate)
- `Term.eval()`: post-order evaluation; throws `TermException` for division by zero or non-integer division
- `Term.print()`: in-order pretty-print with parenthesization

**`game`** -- Game model and orchestration:
- `Game`: central orchestrator owning `Players`, `Numbers`, `Algorithm`, `Settings`, `Dice`, `Scoreboard`
- `Dice`: validates that a player's expression uses exactly the three rolled dice (with multipliers)
- `Numbers`: manages the sequence of random target numbers (1-100)
- `Players`: cycles through players endlessly via `next()`
- `Scoreboard`: dual-indexed scores (by number and by player), computes sums and winners
- `Settings`: game configuration (timeout, penalty, retries, number count, level)

**`util`** -- Internal utilities: `MutableStack` (used by parser) and `expect fun randomUUID()`.

### Key Design Decisions

- The `Algorithm` abstraction allows swapping computation strategies. Both implementations exist as a correctness cross-check.
- `Term` supports both concrete `Value` nodes (from user input) and `Variable` nodes (for algorithm templates with `Assignment` bindings).
- `Dice.validate(term)` enforces that each die is used exactly once, accepting 1x/10x/100x multipliers of the original roll.
- Computer players use `Game.solve()` (delegates to `Algorithm`); human players use `Game.calculate(expression)` which parses, validates, and compares against the algorithm's best.

## Code Style

- Kotlin conventions, JVM toolchain 17
- Detekt for static analysis (config: `config/detekt/detekt.yml`, only override: `ThrowsCount.max = 3`)
- ktlint for formatting
