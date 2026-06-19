package io.schlawiner.algorithm

/**
 * Strategy interface for computing arithmetic solutions from three dice values and a target number.
 *
 * Two implementations exist — [OperationAlgorithm] (hard-coded operations) and [TermAlgorithm] (pre-parsed expression
 * templates) — both producing identical results. The abstraction allows swapping strategies and serves as a
 * correctness cross-check between implementations.
 */
interface Algorithm {
    /** Human-readable name identifying this algorithm implementation. */
    val name: String

    /**
     * Finds the best arithmetic combination of three dice values to reach a [target] number.
     *
     * Each dice value may be multiplied by 1, 10, or 100 before being combined with +, -, *, /. All 27 multiplier
     * combinations are tried for every operation permutation.
     *
     * @param a first dice value (1–6)
     * @param b second dice value (1–6)
     * @param c third dice value (1–6)
     * @param target the number to reach (1–100)
     * @return a [Solutions] container holding the best solution found
     */
    fun compute(
        a: Int,
        b: Int,
        c: Int,
        target: Int,
    ): Solutions
}

/**
 * Base class for [Algorithm] implementations that handles multiplier iteration.
 *
 * Subclasses only need to implement [computePermutation] for a single set of (already-multiplied) dice values.
 *
 * @param name human-readable algorithm name
 * @param allowedDifference maximum acceptable distance between a solution's result and the target
 */
abstract class AbstractAlgorithm(
    override val name: String,
    private val allowedDifference: Int = DEFAULT_DIFFERENCE,
) : Algorithm {
    override fun compute(
        a: Int,
        b: Int,
        c: Int,
        target: Int,
    ): Solutions {
        val solutions = Solutions(target, allowedDifference)
        for (multiplier in MULTIPLIERS) {
            val am = a * multiplier[0]
            val bm = b * multiplier[1]
            val cm = c * multiplier[2]
            computePermutation(am, bm, cm, target, solutions)
        }
        return solutions
    }

    /**
     * Tries all operation permutations for a single multiplier combination and adds valid solutions.
     *
     * @param a first dice value (already multiplied)
     * @param b second dice value (already multiplied)
     * @param c third dice value (already multiplied)
     * @param target the number to reach
     * @param solutions collector for valid solutions
     */
    protected abstract fun computePermutation(
        a: Int,
        b: Int,
        c: Int,
        target: Int,
        solutions: Solutions,
    )

    /** Returns `true` if at least two of the three dice values differ, enabling variable-ordering permutations. */
    protected fun differentDiceNumbers(
        a: Int,
        b: Int,
        c: Int,
    ): Boolean = a != b || a != c

    override fun toString(): String = name

    companion object {
        /**
         * Maximum allowed distance between a solution result and the target. Empirically computed by
         * `FindDifferenceTest.findDifference` to guarantee that at least one solution exists for every
         * dice combination and every target 1–100.
         */
        internal const val DEFAULT_DIFFERENCE = 15

        // @formatter:off

        /** All 27 multiplier combinations (1/10/100 per die). */
        private val MULTIPLIERS =
            arrayOf(
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 10),
                intArrayOf(1, 10, 1),
                intArrayOf(10, 1, 1),
                intArrayOf(1, 1, 100),
                intArrayOf(1, 100, 1),
                intArrayOf(100, 1, 1),
                intArrayOf(1, 10, 10),
                intArrayOf(10, 1, 10),
                intArrayOf(10, 10, 1),
                intArrayOf(10, 10, 10),
                intArrayOf(10, 10, 100),
                intArrayOf(10, 100, 10),
                intArrayOf(100, 10, 10),
                intArrayOf(1, 100, 100),
                intArrayOf(100, 1, 100),
                intArrayOf(100, 100, 1),
                intArrayOf(10, 100, 100),
                intArrayOf(100, 10, 100),
                intArrayOf(100, 100, 10),
                intArrayOf(100, 100, 100),
                intArrayOf(1, 10, 100),
                intArrayOf(1, 100, 10),
                intArrayOf(10, 1, 100),
                intArrayOf(10, 100, 1),
                intArrayOf(100, 1, 10),
                intArrayOf(100, 10, 1),
            )
        // @formatter:on
    }
}
