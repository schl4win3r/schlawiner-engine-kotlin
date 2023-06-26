package io.schlawiner.algorithm

interface Algorithm {
    val name: String
    fun compute(a: Int, b: Int, c: Int, target: Int): Solutions
}

abstract class AbstractAlgorithm(
    override val name: String,
    private val allowedDifference: Int = DEFAULT_DIFFERENCE,
) : Algorithm {

    override fun compute(a: Int, b: Int, c: Int, target: Int): Solutions {
        val solutions = Solutions(target, allowedDifference)
        for (multiplier in MULTIPLIERS) {
            val am = a * multiplier[0]
            val bm = b * multiplier[1]
            val cm = c * multiplier[2]
            computePermutation(am, bm, cm, target, solutions)
        }
        return solutions
    }

    protected abstract fun computePermutation(
        a: Int,
        b: Int,
        c: Int,
        target: Int,
        solutions: Solutions,
    )

    protected fun differentDiceNumbers(a: Int, b: Int, c: Int): Boolean = a != b || a != c

    override fun toString(): String = name

    companion object {
        // Calculated by io.schlawiner.algorithm.FindDifferenceTest.findDifference
        internal const val DEFAULT_DIFFERENCE = 15

        // @formatter:off
        private val MULTIPLIERS = arrayOf(
            intArrayOf(1, 1, 1),
            intArrayOf(1, 1, 10), intArrayOf(1, 10, 1), intArrayOf(10, 1, 1),
            intArrayOf(1, 1, 100), intArrayOf(1, 100, 1), intArrayOf(100, 1, 1),
            intArrayOf(1, 10, 10), intArrayOf(10, 1, 10), intArrayOf(10, 10, 1),
            intArrayOf(10, 10, 10),
            intArrayOf(10, 10, 100), intArrayOf(10, 100, 10), intArrayOf(100, 10, 10),
            intArrayOf(1, 100, 100), intArrayOf(100, 1, 100), intArrayOf(100, 100, 1),
            intArrayOf(10, 100, 100), intArrayOf(100, 10, 100), intArrayOf(100, 100, 10),
            intArrayOf(100, 100, 100),
            intArrayOf(1, 10, 100), intArrayOf(1, 100, 10), intArrayOf(10, 1, 100),
            intArrayOf(10, 100, 1), intArrayOf(100, 1, 10), intArrayOf(100, 10, 1),
        )
        // @formatter:on
    }
}
