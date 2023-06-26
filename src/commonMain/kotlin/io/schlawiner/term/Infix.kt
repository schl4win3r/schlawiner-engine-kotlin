package io.schlawiner.term

import io.schlawiner.util.mutableStackOf

internal fun infixToRPN(expression: String): Array<String> = convert(split(expression))

private val OPS: Set<Char> = setOf('(', ')', '+', '-', '*', '/')

private fun split(expression: String): Array<String> {
    var number = StringBuilder()
    val tokens = mutableListOf<String>()

    for (c in expression) {
        if (OPS.contains(c) || c.isWhitespace()) {
            if (number.isNotEmpty()) {
                tokens.add(number.toString())
                number = StringBuilder()
            }
            if (OPS.contains(c)) {
                tokens.add(c.toString())
            }
        } else {
            number.append(c)
        }
    }
    if (number.isNotEmpty()) {
        tokens.add(number.toString())
    }
    return tokens.toTypedArray()
}

@Suppress("NestedBlockDepth")
private fun convert(infix: Array<String>): Array<String> {
    val rpn = mutableListOf<String>()
    val stack = mutableStackOf<String>()

    for (token in infix) {
        val operator = Operator.toOperator(token)
        if (operator != null) {
            @Suppress("LoopWithTooManyJumpStatements")
            while (!stack.isEmpty()) {
                val nextToken = stack.peek()
                val nextOperator = Operator.toOperator(nextToken)
                if (nextOperator != null) {
                    if (operator.precedence - nextOperator.precedence <= 0) {
                        rpn.add(stack.pop())
                        continue
                    }
                }
                break
            }
            stack.push(token)
        } else {
            when (token) {
                "(" -> stack.push(token)
                ")" -> {
                    while (!stack.isEmpty() && stack.peek() != "(") {
                        rpn.add(stack.pop())
                    }
                    stack.pop()
                }

                else -> rpn.add(token)
            }
        }
    }
    while (!stack.isEmpty()) {
        rpn.add(stack.pop())
    }
    return rpn.toTypedArray()
}
