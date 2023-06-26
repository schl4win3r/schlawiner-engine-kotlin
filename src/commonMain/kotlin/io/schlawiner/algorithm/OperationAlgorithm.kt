package io.schlawiner.algorithm

@Suppress("TooManyFunctions")
class OperationAlgorithm(allowedDifference: Int = DEFAULT_DIFFERENCE) :
    AbstractAlgorithm("Algorithm based on static operations", allowedDifference) {

    @Suppress("CyclomaticComplexMethod", "LongMethod")
    override fun computePermutation(a: Int, b: Int, c: Int, target: Int, solutions: Solutions) {
        // a + b + c
        solutions.add(add(a, b, c))

        // a - b - c
        solutions.add(subtract(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(subtract(b, a, c))
            solutions.add(subtract(c, a, b))
        }

        // a * b * c
        solutions.add(multiply(a, b, c))

        // a / b / c
        solutions.add(divide(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(divide(b, a, c))
            solutions.add(divide(c, a, b))
        }

        // a + b - c
        solutions.add(addSubtract(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(addSubtract(a, c, b))
            solutions.add(addSubtract(b, c, a))
        }

        // a * b / c
        solutions.add(multiplyDivide(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(multiplyDivide(a, c, b))
            solutions.add(multiplyDivide(b, c, a))
        }

        // a * b + c
        solutions.add(multiplyAdd(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(multiplyAdd(a, c, b))
            solutions.add(multiplyAdd(b, c, a))
        }

        // (a + b) * c
        solutions.add(addMultiply(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(addMultiply(a, c, b))
            solutions.add(addMultiply(b, c, a))
        }

        // a * b - c
        solutions.add(multiplySubtract1(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(multiplySubtract1(a, c, b))
            solutions.add(multiplySubtract1(b, c, a))
        }

        // a - b * c
        solutions.add(multiplySubtract2(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(multiplySubtract2(b, a, c))
            solutions.add(multiplySubtract2(c, a, b))
        }

        // (a - b) * c
        solutions.add(subtractMultiply(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(subtractMultiply(b, a, c))
            solutions.add(subtractMultiply(a, c, b))
            solutions.add(subtractMultiply(c, a, b))
            solutions.add(subtractMultiply(b, c, a))
            solutions.add(subtractMultiply(c, b, a))
        }

        // a / b + c
        solutions.add(divideAdd(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(divideAdd(b, a, c))
            solutions.add(divideAdd(a, c, b))
            solutions.add(divideAdd(c, a, b))
            solutions.add(divideAdd(b, c, a))
            solutions.add(divideAdd(c, b, a))
        }

        // (a + b) / c
        solutions.add(addDivide1(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(addDivide1(a, c, b))
            solutions.add(addDivide1(b, c, a))
        }

        // a / (b + c)
        solutions.add(addDivide2(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(addDivide2(b, a, c))
            solutions.add(addDivide2(c, a, b))
        }

        // a / b - c
        solutions.add(divideSubtract1(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(divideSubtract1(a, c, b))
            solutions.add(divideSubtract1(b, a, c))
            solutions.add(divideSubtract1(b, c, a))
            solutions.add(divideSubtract1(c, a, b))
            solutions.add(divideSubtract1(c, b, a))
        }

        // a - b / c
        solutions.add(divideSubtract2(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(divideSubtract2(a, c, b))
            solutions.add(divideSubtract2(b, a, c))
            solutions.add(divideSubtract2(b, c, a))
            solutions.add(divideSubtract2(c, a, b))
            solutions.add(divideSubtract2(c, b, a))
        }

        // (a - b) / c
        solutions.add(subtractDivide1(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(subtractDivide1(a, c, b))
            solutions.add(subtractDivide1(b, a, c))
            solutions.add(subtractDivide1(b, c, a))
            solutions.add(subtractDivide1(c, a, b))
            solutions.add(subtractDivide1(c, b, a))
        }

        // a / (b - c)
        solutions.add(subtractDivide2(a, b, c))
        if (differentDiceNumbers(a, b, c)) {
            solutions.add(subtractDivide2(a, c, b))
            solutions.add(subtractDivide2(b, a, c))
            solutions.add(subtractDivide2(b, c, a))
            solutions.add(subtractDivide2(c, a, b))
            solutions.add(subtractDivide2(c, b, a))
        }
    }

    private fun add(a: Int, b: Int, c: Int): Solution = Solution("$a + $b + $c", a + b + c)

    private fun addDivide1(a: Int, b: Int, c: Int): Solution = if ((a + b) % c != 0) {
        Solution.INVALID
    } else {
        Solution("($a + $b) / $c", (a + b) / c)
    }

    private fun addDivide2(a: Int, b: Int, c: Int): Solution = if (a % (b + c) != 0) {
        Solution.INVALID
    } else {
        Solution("$a / ($b + $c)", a / (b + c))
    }

    private fun addMultiply(a: Int, b: Int, c: Int): Solution =
        Solution("($a + $b) * $c", (a + b) * c)

    private fun addSubtract(a: Int, b: Int, c: Int): Solution = Solution("$a + $b - $c", a + b - c)

    private fun divide(a: Int, b: Int, c: Int): Solution = if (a % b != 0 || a / b % c != 0) {
        Solution.INVALID
    } else {
        Solution("$a / $b / $c", a / b / c)
    }

    private fun divideAdd(a: Int, b: Int, c: Int): Solution = if (a % b != 0) {
        Solution.INVALID
    } else {
        Solution("$a / $b + $c", a / b + c)
    }

    private fun divideSubtract1(a: Int, b: Int, c: Int): Solution = if (a % b != 0) {
        Solution.INVALID
    } else {
        Solution("$a / $b - $c", a / b - c)
    }

    private fun divideSubtract2(a: Int, b: Int, c: Int): Solution = if (b % c != 0) {
        Solution.INVALID
    } else {
        Solution("$a - $b / $c", a - b / c)
    }

    private fun multiply(a: Int, b: Int, c: Int): Solution = Solution("$a * $b * $c", a * b * c)

    private fun multiplyAdd(a: Int, b: Int, c: Int): Solution = Solution("$a * $b + $c", a * b + c)

    private fun multiplyDivide(a: Int, b: Int, c: Int): Solution = if (a * b % c != 0) {
        Solution.INVALID
    } else {
        Solution("$a * $b / $c", a * b / c)
    }

    private fun multiplySubtract1(a: Int, b: Int, c: Int): Solution =
        Solution("$a * $b - $c", a * b - c)

    private fun multiplySubtract2(a: Int, b: Int, c: Int): Solution =
        Solution("$a - $b * $c", a - b * c)

    private fun subtract(a: Int, b: Int, c: Int): Solution = Solution("$a - $b - $c", a - b - c)

    private fun subtractDivide1(a: Int, b: Int, c: Int): Solution = if ((a - b) % c != 0) {
        Solution.INVALID
    } else {
        Solution("($a - $b) / $c", (a - b) / c)
    }

    private fun subtractDivide2(a: Int, b: Int, c: Int): Solution =
        if (b - c == 0 || a % (b - c) != 0) {
            Solution.INVALID
        } else {
            Solution("$a / ($b - $c)", a / (b - c))
        }

    private fun subtractMultiply(a: Int, b: Int, c: Int): Solution =
        Solution("($a - $b) * $c", (a - b) * c)
}
