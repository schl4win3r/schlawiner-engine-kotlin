package io.schlawiner.game

import kotlin.random.Random

class Numbers internal constructor(private val numbers: List<Int>) : Iterable<Int> {
    constructor(count: Int) : this(
        buildList {
            repeat(count) {
                add(Random.nextInt(MIN, MAX + 1))
            }
        },
    )

    private var iterator: ListIterator<Int> = numbers.listIterator()
    private var _current: Int = iterator.next()

    val current: Int
        get() = _current

    fun next(): Int {
        _current = iterator.next()
        return _current
    }

    fun first(): Boolean = current == numbers.first()

    fun last(): Boolean = current == numbers.last()

    // must be independent from private var iterator!
    override fun iterator(): Iterator<Int> = numbers.iterator()

    override fun toString(): String = "Numbers(current: $current, numbers: $numbers)"

    companion object {
        private const val MIN = 1
        private const val MAX = 100
    }
}
