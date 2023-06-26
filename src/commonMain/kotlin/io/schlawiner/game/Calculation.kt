package io.schlawiner.game

import io.schlawiner.algorithm.Solution
import io.schlawiner.term.Term
import kotlin.math.abs

data class Calculation(val term: Term, val target: Int, val bestSolution: Solution) {

    val difference: Int = abs(term.eval(emptyArray()) - target)

    val bestDifference: Int = abs(bestSolution.result - target)

    val best: Boolean = difference == 0 || difference == bestDifference
}
