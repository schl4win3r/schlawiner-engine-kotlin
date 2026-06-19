package io.schlawiner.game

import io.schlawiner.algorithm.Solution
import io.schlawiner.term.Term
import kotlin.math.abs

/**
 * Result of a human player's expression evaluation, including comparison with the algorithm's optimal solution.
 *
 * @property term the parsed expression tree from the player's input
 * @property target the target number the player was trying to reach
 * @property bestSolution the optimal solution computed by the algorithm for comparison
 */
data class Calculation(
    val term: Term,
    val target: Int,
    val bestSolution: Solution,
) {
    /** Absolute difference between the player's result and the target. */
    val difference: Int = abs(term.eval(emptyArray()) - target)

    /** Absolute difference between the algorithm's best result and the target. */
    val bestDifference: Int = abs(bestSolution.result - target)

    /** Whether the player achieved the optimal result (exact hit or matching the algorithm's best). */
    val best: Boolean = difference == 0 || difference == bestDifference
}
