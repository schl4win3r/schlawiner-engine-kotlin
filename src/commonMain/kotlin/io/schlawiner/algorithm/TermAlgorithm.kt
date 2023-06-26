package io.schlawiner.algorithm

import io.schlawiner.term.Assignment
import io.schlawiner.term.Term
import io.schlawiner.term.TermException
import io.schlawiner.term.toTerm

class TermAlgorithm : AbstractAlgorithm("Algorithm based on variable terms") {

    override fun computePermutation(a: Int, b: Int, c: Int, target: Int, solutions: Solutions) {
        val assignments: Array<Assignment> = arrayOf(
            Assignment("a", a),
            Assignment("b", b),
            Assignment("c", c),
        )

        for (term in ABC) {
            try {
                solutions.add(Solution(term.print(assignments), term.eval(assignments)))
            } catch (ignore: TermException) {
            }
        }
        if (differentDiceNumbers(a, b, c)) {
            for (term in PERMUTATIONS) {
                try {
                    solutions.add(Solution(term.print(assignments), term.eval(assignments)))
                } catch (ignore: TermException) {
                }
            }
        }
    }

    companion object {
        // a + b + c
        private val ADD_ABC: Term = "a + b + c".toTerm()

        // a - b - c
        private val SUBTRACT_ABC: Term = "a - b - c".toTerm()
        private val SUBTRACT_BAC: Term = "b - a - c".toTerm()
        private val SUBTRACT_CAB: Term = "c - a - b".toTerm()

        // a * b * c
        private val MULTIPLY_ABC: Term = "a * b * c".toTerm()

        // a / b / c
        private val DIVIDE_ABC: Term = "a / b / c".toTerm()
        private val DIVIDE_BAC: Term = "b / a / c".toTerm()
        private val DIVIDE_CAB: Term = "c / a / b".toTerm()

        // a + b - c
        private val ADD_SUBTRACT_ABC: Term = "a + b - c".toTerm()
        private val ADD_SUBTRACT_ACB: Term = "a + c - b".toTerm()
        private val ADD_SUBTRACT_BCA: Term = "b + c - a".toTerm()

        // a * b / c
        private val MULTIPLY_DIVIDE_ABC: Term = "a * b / c".toTerm()
        private val MULTIPLY_DIVIDE_ACB: Term = "a * c / b".toTerm()
        private val MULTIPLY_DIVIDE_BCA: Term = "b * c / a".toTerm()

        // a * b + c
        private val MULTIPLY_ADD_ABC: Term = "a * b + c".toTerm()
        private val MULTIPLY_ADD_ACB: Term = "a * c + b".toTerm()
        private val MULTIPLY_ADD_BCA: Term = "b * c + a".toTerm()

        // (a + b) * c
        private val ADD_MULTIPLY_ABC: Term = "(a + b) * c".toTerm()
        private val ADD_MULTIPLY_ACB: Term = "(a + c) * b".toTerm()
        private val ADD_MULTIPLY_BCA: Term = "(b + c) * a".toTerm()

        // a * b - c
        private val MULTIPLY_SUBTRACT_1_ABC: Term = "a * b - c".toTerm()
        private val MULTIPLY_SUBTRACT_1_ACB: Term = "a * c - b".toTerm()
        private val MULTIPLY_SUBTRACT_1_BCA: Term = "b * c - a".toTerm()

        // a - b * c
        private val MULTIPLY_SUBTRACT_2_ABC: Term = "a - b * c".toTerm()
        private val MULTIPLY_SUBTRACT_2_BAC: Term = "b - a * c".toTerm()
        private val MULTIPLY_SUBTRACT_2_CAB: Term = "c - a * b".toTerm()

        // (a - b) * c
        private val SUBTRACT_MULTIPLY_ABC: Term = "(a - b) * c".toTerm()
        private val SUBTRACT_MULTIPLY_BAC: Term = "(b - a) * c".toTerm()
        private val SUBTRACT_MULTIPLY_ACB: Term = "(a - c) * b".toTerm()
        private val SUBTRACT_MULTIPLY_CAB: Term = "(c - a) * b".toTerm()
        private val SUBTRACT_MULTIPLY_BCA: Term = "(b - c) * a".toTerm()
        private val SUBTRACT_MULTIPLY_CBA: Term = "(c - b) * a".toTerm()

        // a / b + c
        private val DIVIDE_ADD_ABC: Term = "a / b + c".toTerm()
        private val DIVIDE_ADD_BAC: Term = "b / a + c".toTerm()
        private val DIVIDE_ADD_ACB: Term = "a / c + b".toTerm()
        private val DIVIDE_ADD_CAB: Term = "c / a + b".toTerm()
        private val DIVIDE_ADD_BCA: Term = "b / c + a".toTerm()
        private val DIVIDE_ADD_CBA: Term = "c / b + a".toTerm()

        // (a + b) / c
        private val ADD_DIVIDE_1_ABC: Term = "(a + b) / c".toTerm()
        private val ADD_DIVIDE_1_ACB: Term = "(a + c) / b".toTerm()
        private val ADD_DIVIDE_1_BCA: Term = "(b + c) / a".toTerm()

        // a / (b + c)
        private val ADD_DIVIDE_2_ABC: Term = "a / (b + c)".toTerm()
        private val ADD_DIVIDE_2_BAC: Term = "b / (a + c)".toTerm()
        private val ADD_DIVIDE_2_CAB: Term = "c / (a + b)".toTerm()

        // a / b - c
        private val DIVIDE_SUBTRACT_1_ABC: Term = "a / b - c".toTerm()
        private val DIVIDE_SUBTRACT_1_ACB: Term = "a / c - b".toTerm()
        private val DIVIDE_SUBTRACT_1_BAC: Term = "b / a - c".toTerm()
        private val DIVIDE_SUBTRACT_1_BCA: Term = "b / c - a".toTerm()
        private val DIVIDE_SUBTRACT_1_CAB: Term = "c / a - b".toTerm()
        private val DIVIDE_SUBTRACT_1_CBA: Term = "c / b - a".toTerm()

        // a - b / c
        private val DIVIDE_SUBTRACT_2_ABC: Term = "a - b / c".toTerm()
        private val DIVIDE_SUBTRACT_2_ACB: Term = "a - c / b".toTerm()
        private val DIVIDE_SUBTRACT_2_BAC: Term = "b - a / c".toTerm()
        private val DIVIDE_SUBTRACT_2_BCA: Term = "b - c / a".toTerm()
        private val DIVIDE_SUBTRACT_2_CAB: Term = "c - a / b".toTerm()
        private val DIVIDE_SUBTRACT_2_CBA: Term = "c - b / a".toTerm()

        // (a - b) / c
        private val SUBTRACT_DIVIDE_1_ABC: Term = "(a - b) / c".toTerm()
        private val SUBTRACT_DIVIDE_1_ACB: Term = "(a - c) / b".toTerm()
        private val SUBTRACT_DIVIDE_1_BAC: Term = "(b - a) / c".toTerm()
        private val SUBTRACT_DIVIDE_1_BCA: Term = "(b - c) / a".toTerm()
        private val SUBTRACT_DIVIDE_1_CAB: Term = "(c - a) / b".toTerm()
        private val SUBTRACT_DIVIDE_1_CBA: Term = "(c - b) / a".toTerm()

        // a / (b - c)
        private val SUBTRACT_DIVIDE_2_ABC: Term = "a / (b - c)".toTerm()
        private val SUBTRACT_DIVIDE_2_ACB: Term = "a / (c - b)".toTerm()
        private val SUBTRACT_DIVIDE_2_BAC: Term = "b / (a - c)".toTerm()
        private val SUBTRACT_DIVIDE_2_BCA: Term = "b / (c - a)".toTerm()
        private val SUBTRACT_DIVIDE_2_CAB: Term = "c / (a - b)".toTerm()
        private val SUBTRACT_DIVIDE_2_CBA: Term = "c / (b - a)".toTerm()

        private val ABC = listOf(
            ADD_ABC,
            SUBTRACT_ABC,
            MULTIPLY_ABC,
            DIVIDE_ABC,
            ADD_SUBTRACT_ABC,
            MULTIPLY_DIVIDE_ABC,
            MULTIPLY_ADD_ABC,
            ADD_MULTIPLY_ABC,
            MULTIPLY_SUBTRACT_1_ABC,
            MULTIPLY_SUBTRACT_2_ABC,
            SUBTRACT_MULTIPLY_ABC,
            DIVIDE_ADD_ABC,
            ADD_DIVIDE_1_ABC,
            ADD_DIVIDE_2_ABC,
            DIVIDE_SUBTRACT_1_ABC,
            DIVIDE_SUBTRACT_2_ABC,
            SUBTRACT_DIVIDE_1_ABC,
            SUBTRACT_DIVIDE_2_ABC,
        )

        private val PERMUTATIONS = listOf(
            SUBTRACT_BAC,
            SUBTRACT_CAB,
            DIVIDE_BAC,
            DIVIDE_CAB,
            ADD_SUBTRACT_ACB,
            ADD_SUBTRACT_BCA,
            MULTIPLY_DIVIDE_ACB,
            MULTIPLY_DIVIDE_BCA,
            MULTIPLY_ADD_ACB,
            MULTIPLY_ADD_BCA,
            ADD_MULTIPLY_ACB,
            ADD_MULTIPLY_BCA,
            MULTIPLY_SUBTRACT_1_ACB,
            MULTIPLY_SUBTRACT_1_BCA,
            MULTIPLY_SUBTRACT_2_BAC,
            MULTIPLY_SUBTRACT_2_CAB,
            SUBTRACT_MULTIPLY_BAC,
            SUBTRACT_MULTIPLY_ACB,
            SUBTRACT_MULTIPLY_CAB,
            SUBTRACT_MULTIPLY_BCA,
            SUBTRACT_MULTIPLY_CBA,
            DIVIDE_ADD_BAC,
            DIVIDE_ADD_ACB,
            DIVIDE_ADD_CAB,
            DIVIDE_ADD_BCA,
            DIVIDE_ADD_CBA,
            ADD_DIVIDE_1_ACB,
            ADD_DIVIDE_1_BCA,
            ADD_DIVIDE_2_BAC,
            ADD_DIVIDE_2_CAB,
            DIVIDE_SUBTRACT_1_ACB,
            DIVIDE_SUBTRACT_1_BAC,
            DIVIDE_SUBTRACT_1_BCA,
            DIVIDE_SUBTRACT_1_CAB,
            DIVIDE_SUBTRACT_1_CBA,
            DIVIDE_SUBTRACT_2_ACB,
            DIVIDE_SUBTRACT_2_BAC,
            DIVIDE_SUBTRACT_2_BCA,
            DIVIDE_SUBTRACT_2_CAB,
            DIVIDE_SUBTRACT_2_CBA,
            SUBTRACT_DIVIDE_1_ACB,
            SUBTRACT_DIVIDE_1_BAC,
            SUBTRACT_DIVIDE_1_BCA,
            SUBTRACT_DIVIDE_1_CAB,
            SUBTRACT_DIVIDE_1_CBA,
            SUBTRACT_DIVIDE_2_ACB,
            SUBTRACT_DIVIDE_2_BAC,
            SUBTRACT_DIVIDE_2_BCA,
            SUBTRACT_DIVIDE_2_CAB,
            SUBTRACT_DIVIDE_2_CBA,
        )
    }
}
