package io.schlawiner.term

import io.schlawiner.term.Operator.DIVIDED
import io.schlawiner.term.Operator.MINUS
import io.schlawiner.term.Operator.PLUS
import io.schlawiner.term.Operator.TIMES
import io.schlawiner.util.MutableStack
import io.schlawiner.util.mutableStackOf

/** Thrown when expression parsing or evaluation fails (e.g. division by zero, non-integer division). */
class TermException(
    message: String,
) : RuntimeException(message)

/**
 * Binds a variable name to a concrete integer value for template evaluation.
 *
 * @property name the variable identifier (e.g. `"a"`, `"b"`, `"c"`)
 * @property value the integer value to substitute
 */
data class Assignment(
    val name: String,
    val value: Int,
)

/**
 * A node in the expression AST (abstract syntax tree).
 *
 * The tree is a binary tree where [Term] nodes are internal (operator) nodes and [Value]/[Variable] nodes are leaves.
 */
interface Node {
    /** Parent node in the tree, or `null` for the root. */
    var parent: Node?

    /** Left child node. */
    var left: Node?

    /** Right child node. */
    var right: Node?
}

/**
 * Leaf node representing a concrete integer literal in the expression tree.
 *
 * @property value the integer literal
 */
class Value(
    val value: Int,
) : Node {
    override var parent: Node? = null
    override var left: Node? = null
    override var right: Node? = null
}

/**
 * Leaf node representing a named variable placeholder in the expression tree.
 *
 * Variables are resolved to concrete values via [Assignment]s during [Term.eval] and [Term.print].
 *
 * @property name the variable identifier (e.g. `"a"`, `"b"`, `"c"`)
 */
class Variable(
    val name: String,
) : Node {
    override var parent: Node? = null
    override var left: Node? = null
    override var right: Node? = null
}

/**
 * Internal node in the expression AST representing a binary arithmetic operation.
 *
 * A complete term has both [left] and [right] children set. Terms can be evaluated with [eval] (post-order traversal)
 * and pretty-printed with [print] (in-order traversal with parenthesization based on operator precedence).
 *
 * @property operator the arithmetic operator (+, -, *, /)
 */
class Term(
    val operator: Operator,
) : Node {
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

    /** Whether both left and right children are assigned. */
    val complete: Boolean
        get() = left != null && right != null

    /** All integer [Value] nodes in this subtree, collected via in-order traversal. */
    val values: List<Int>
        get() {
            fun inOrder(
                node: Node,
                values: MutableList<Int>,
            ) {
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

    /** All [Variable] nodes in this subtree, collected via in-order traversal. */
    val variables: List<Variable>
        get() {
            fun inOrder(
                node: Node,
                variables: MutableList<Variable>,
            ) {
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

    /**
     * Evaluates this expression tree using post-order traversal and returns the integer result.
     *
     * Variables are resolved using the provided [assignments]. Division by zero and non-integer division
     * throw [TermException].
     *
     * @param assignments variable-to-value bindings (may be empty if the tree contains only [Value] leaves)
     * @return the computed integer result
     * @throws TermException on division by zero, non-integer division, or unresolved variables
     */
    @Suppress("CyclomaticComplexMethod")
    fun eval(assignments: Array<Assignment>): Int {
        fun postOrder(
            node: Node,
            stack: MutableStack<Int>,
            assignments: Array<out Assignment>,
        ) {
            when (node) {
                is Term -> {
                    node.left?.let { postOrder(it, stack, assignments) }
                    node.right?.let { postOrder(it, stack, assignments) }
                    val right = stack.pop()
                    val left = stack.pop()
                    val result =
                        when (node.operator) {
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
            val unassigned =
                variables.filterNot { variable ->
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

    /**
     * Pretty-prints this expression tree as an infix string with minimal parenthesization.
     *
     * Parentheses are only added where operator precedence requires them. Variables are substituted with their
     * assigned values from [assignments]; unassigned variables are printed by name.
     *
     * @param assignments variable-to-value bindings (may be empty)
     * @return the formatted expression string (e.g. `"(4 + 60) * 10"`)
     */
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

        fun inOrder(
            node: Node,
            builder: StringBuilder,
            assignments: Array<out Assignment>,
        ) {
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
