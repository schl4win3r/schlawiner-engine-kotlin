# Schlawiner Engine

Schlawiner Engine is a Kotlin Multiplatform (KMP) library implementing a mathematical dice game. Players roll three dice and must reach target numbers between 1 and 100 using the basic arithmetic operators +, −, ×, and ÷. Each die value can be multiplied by 10 or 100 and must be used exactly once.

## Examples

| Target | Dice | Possible Solution | Difference |
|-------:|:-----|:------------------|:----------:|
| 53     | 4 6 1 | 4 + 60 − 10      | 1          |
| 40     | 2 2 1 | 2 × (20 + 1)     | 2          |
| 22     | 3 2 1 | 30 + 2 − 10      | 0          |
| 96     | 5 1 5 | (500 − 10) ÷ 5   | 2          |
| 42     | 6 6 6 | 6 × 6 + 6        | 0          |

The differences between the target numbers and calculated results are summed up. The player with the smallest total difference wins.

## Build

Requires JDK 21+.

```shell
./gradlew build                    # Full build (compile + test + lint)
./gradlew allTests                 # Run all tests (JVM + JS)
./gradlew jvmTest                  # Run JVM tests only
./gradlew jsTest                   # Run JS tests (requires Chrome headless)
./gradlew jvmTest --tests "io.schlawiner.game.GameTest"  # Single test class
./gradlew detekt                   # Static analysis
./gradlew ktlintCheck              # Lint check
./gradlew ktlintFormat             # Auto-format
./gradlew publishToMavenLocal      # Publish to local Maven repo
```

## Play

To play Schlawiner interactively in the terminal, use the provided [KScript](https://github.com/kscripting/kscript) script (requires prior `./gradlew publishToMavenLocal`):

```shell
kscript play.kts
```

## Architecture

### Kotlin Multiplatform Structure

All game logic lives in `commonMain` — only UUID generation has platform-specific `expect`/`actual` implementations in `jvmMain` (`java.util.UUID`) and `jsMain` (npm `uuid`).

```
src/
  commonMain/   # All domain logic (18 source files)
  commonTest/   # Cross-platform tests (16 test files, run on JVM + JS)
  jvmMain/      # UUID actual (1 file)
  jvmTest/      # Algorithm comparison + brute-force analysis (2 files)
  jsMain/       # UUID actual via npm uuid package (1 file)
```

### Packages (`io.schlawiner.*`)

#### `algorithm`

Two interchangeable `Algorithm` implementations that find optimal solutions for any dice + target combination:

- **`OperationAlgorithm`**: Hard-coded arithmetic permutations — 18 operation patterns across all variable orderings.
- **`TermAlgorithm`**: Pre-parsed `Term` templates — 68 static expression trees evaluated with variable assignments.

Both produce identical results (verified by `AlgorithmComparisonTest`). Both iterate 27 multiplier combinations (1/10/100 per die). The `DEFAULT_DIFFERENCE = 15` is the empirically computed minimum window guaranteeing a solution exists for every dice combo and every target 1–100 (computed by `FindDifferenceTest`).

#### `term`

Expression tree (AST) and parser:

- **`Term`** / **`Value`** / **`Variable`**: Binary tree nodes — `Term` is the internal (operator) node, `Value` and `Variable` are leaves.
- **`Operator`**: Enum for +, -, *, / with precedence levels.
- **`infixToRPN()`**: Shunting-yard algorithm converting infix strings to reverse Polish notation.
- **`String.toTerm()`**: Extension function parsing an infix expression into a `Term` tree via RPN.
- **`Term.eval()`**: Post-order evaluation; throws `TermException` for division by zero or non-integer division.
- **`Term.print()`**: In-order pretty-print with minimal parenthesization.

#### `game`

Game model and orchestration:

- **`Game`**: Central orchestrator owning `Players`, `Numbers`, `Algorithm`, `Settings`, `Dice`, and `Scoreboard`.
- **`Dice`**: Represents three dice values; validates that a player's expression uses each die exactly once (with 1x/10x/100x multipliers).
- **`Numbers`**: Manages the sequence of random target numbers (1–100).
- **`Players`**: Cycles endlessly through the player list via `next()`.
- **`Scoreboard`**: Dual-indexed scores (by number and by player) with running sums and winner calculation.
- **`Settings`**: Game configuration — timeout, penalty, retries, number count, auto-dice, difficulty level.
- **`Calculation`**: Result of a human player's expression, including comparison with the algorithm's best solution.
- **`Level`**: Difficulty enum (EASY/MEDIUM/HARD) controlling computer player accuracy.

#### `util`

Internal utilities: `MutableStack` (LIFO stack used by the parser) and `expect fun randomUUID()` (platform-specific UUID generation).

## License

Have fun!
