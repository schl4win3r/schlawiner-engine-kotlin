package io.schlawiner.util

/** Creates a new [MutableStack] initialized with the given [elements]. */
internal fun <E> mutableStackOf(vararg elements: E) = MutableStack(*elements)

/**
 * A simple LIFO stack backed by a mutable list. Used internally by the shunting-yard parser and expression evaluator.
 */
internal class MutableStack<E>(
    vararg items: E,
) {
    private val elements = items.toMutableList()

    /** Pushes an [element] onto the top of the stack. */
    fun push(element: E) = elements.add(element)

    /** Returns the top element without removing it. */
    fun peek(): E = elements.last()

    /** Removes and returns the top element. */
    fun pop(): E {
        val item = elements.last()
        if (!isEmpty()) {
            elements.removeAt(elements.size - 1)
        }
        return item
    }

    /** Returns `true` if the stack contains no elements. */
    fun isEmpty() = elements.isEmpty()

    override fun toString() = "MutableStack(${elements.joinToString()})"
}
