package io.schlawiner.term

import io.schlawiner.util.MutableStack
import io.schlawiner.util.mutableStackOf

fun String.toTerm(): Term {
    val rpn = infixToRPN(this)
    if (rpn.isEmpty()) {
        throw TermException("Invalid term $this")
    }

    val builder = TermBuilder(this)
    for (i in rpn.size - 1 downTo 0) {
        val token = rpn[i]
        val operator = Operator.toOperator(token)
        if (operator != null) {
            builder.operator(operator)
        } else {
            try {
                builder.value(token.toInt())
            } catch (_: NumberFormatException) {
                builder.variable(token)
            }
        }
    }

    val term = builder.build()
    if (!term.complete) {
        throw TermException("Invalid term: $this")
    }
    return term
}

private class TermBuilder(private val expression: String) {
    private val terms: MutableStack<Term> = mutableStackOf()
    private var current: Term? = null

    fun operator(op: Operator) {
        val e = Term(op)
        if (!terms.isEmpty()) {
            add(terms.peek(), e)
        }
        terms.push(e)
    }

    fun variable(name: String) {
        assign(Variable(name))
    }

    fun value(value: Int) {
        assign(Value(value))
    }

    fun assign(node: Node) {
        if (terms.isEmpty()) {
            throw TermException("Invalid term: $expression")
        }
        add(terms.peek(), node)
        while (!terms.isEmpty() && terms.peek().complete) {
            current = terms.pop()
        }
    }

    fun add(parent: Term, child: Node) {
        // assign right then left, order is important!
        if (parent.right == null) {
            parent.right = child
        } else if (parent.left == null) {
            parent.left = child
        }
    }

    fun build(): Term {
        if (!terms.isEmpty()) {
            throw TermException("Invalid term: $expression")
        }
        return current ?: throw TermException("Invalid term: $expression")
    }
}
