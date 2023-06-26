package io.schlawiner.term

import io.schlawiner.term.Operator.DIVIDED
import io.schlawiner.term.Operator.MINUS
import io.schlawiner.term.Operator.PLUS
import io.schlawiner.term.Operator.TIMES
import io.schlawiner.util.MutableStack
import io.schlawiner.util.mutableStackOf

class TermException(message: String) : RuntimeException(message)

data class Assignment(val name: String, val value: Int)

interface Node {

    var parent: Node?
    var left: Node?
    var right: Node?
}

class Value(val value: Int) : Node {

    override var parent: Node? = null
    override var left: Node? = null
    override var right: Node? = null
}

class Variable(val name: String) : Node {

    override var parent: Node? = null
    override var left: Node? = null
    override var right: Node? = null
}

class Term(val operator: Operator) : Node {

    override var parent: Node? = null
    override var left: Node? = null
        set(value) {
            field = value
            field?.parent = this
        }

    override var right: Node? = null
        set(value) {
            field = value
            field?.parent = this
        }

    val complete: Boolean
        get() = left != null && right != null

    val values: List<Int>
        get() {
            fun inOrder(node: Node, values: MutableList<Int>) {
                node.left?.let { inOrder(it, values) }
                if (node is Value) {
                    values.add(node.value)
                }
                node.right?.let { inOrder(it, values) }
            }

            val values = mutableListOf<Int>()
            inOrder(this, values)
            return values
        }

    val variables: List<Variable>
        get() {
            fun inOrder(node: Node, variables: MutableList<Variable>) {
                node.left?.let { inOrder(it, variables) }
                if (node is Variable) {
                    variables.add(node)
                }
                node.right?.let { inOrder(it, variables) }
            }

            val variables = mutableListOf<Variable>()
            inOrder(this, variables)
            return variables
        }

    @Suppress("CyclomaticComplexMethod")
    fun eval(assignments: Array<Assignment>): Int {
        fun postOrder(node: Node, stack: MutableStack<Int>, assignments: Array<out Assignment>) {
            when (node) {
                is Term -> {
                    node.left?.let { postOrder(it, stack, assignments) }
                    node.right?.let { postOrder(it, stack, assignments) }
                    val right = stack.pop()
                    val left = stack.pop()
                    val result = when (node.operator) {
                        PLUS -> left + right
                        MINUS -> left - right
                        TIMES -> left * right
                        DIVIDED -> {
                            if (right == 0 || left % right != 0) {
                                throw TermException("Illegal division: $left / $right")
                            }
                            left / right
                        }
                    }
                    stack.push(result)
                }

                is Variable -> {
                    val assignment = assignments.find { it.name == node.name }
                    if (assignment != null) {
                        stack.push(assignment.value)
                    } else {
                        throw TermException("Unable to eval term. Missing assignment ${node.name}")
                    }
                }

                is Value -> stack.push(node.value)
            }
        }

        if (assignments.isNotEmpty()) {
            val unassigned = variables.filterNot { variable ->
                assignments.find { assignment -> variable.name == assignment.name } != null
            }
            if (unassigned.isNotEmpty()) {
                throw TermException(
                    buildString {
                        append("Unable to eval term. No assignment for ")
                        append(unassigned.joinTo(this, ", ") { it.name })
                    },
                )
            }
        }
        val stack = mutableStackOf<Int>()
        postOrder(this, stack, assignments)
        return stack.pop()
    }

    @Suppress("CyclomaticComplexMethod")
    fun print(assignments: Array<Assignment>): String {
        fun needsBracket(node: Node): Boolean {
            val parent = node.parent
            val grandparent = parent?.parent
            return if (parent is Term && grandparent is Term) {
                parent.operator.precedence < grandparent.operator.precedence
            } else {
                false
            }
        }

        fun inOrder(node: Node, builder: StringBuilder, assignments: Array<out Assignment>) {
            node.left?.let { inOrder(it, builder, assignments) }
            when (node) {
                is Term -> builder.append(" ${node.operator} ")
                is Variable, is Value -> {
                    val needsBracket: Boolean = needsBracket(node)
                    if (needsBracket && node === node.parent?.left) {
                        builder.append("(")
                    }
                    if (node is Variable) {
                        val assignment = assignments.find { it.name == node.name }
                        if (assignment != null) {
                            builder.append(assignment.value)
                        } else {
                            builder.append(node.name)
                        }
                    } else if (node is Value) {
                        builder.append(node.value)
                    }
                    if (needsBracket && node == node.parent?.right) {
                        builder.append(")")
                    }
                }
            }
            node.right?.let { inOrder(it, builder, assignments) }
        }

        val builder = StringBuilder()
        inOrder(this, builder, assignments)
        return builder.toString()
    }

    override fun toString(): String = print(emptyArray())
}
