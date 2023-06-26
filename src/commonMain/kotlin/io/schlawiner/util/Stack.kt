package io.schlawiner.util

internal fun <E> mutableStackOf(vararg elements: E) = MutableStack(*elements)

internal class MutableStack<E>(vararg items: E) {

    private val elements = items.toMutableList()

    fun push(element: E) = elements.add(element)

    fun peek(): E = elements.last()

    fun pop(): E {
        val item = elements.last()
        if (!isEmpty()) {
            elements.removeAt(elements.size - 1)
        }
        return item
    }

    fun isEmpty() = elements.isEmpty()

    override fun toString() = "MutableStack(${elements.joinToString()})"
}
