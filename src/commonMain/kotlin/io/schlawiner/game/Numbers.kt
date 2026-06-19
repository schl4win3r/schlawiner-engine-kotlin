package io.schlawiner.game

import kotlin.random.Random

/**
 * A sequence of random target numbers between 1 and 100 that players must reach.
 *
 * Numbers are iterated one by one during gameplay via [next]. The [current] property always reflects the
 * active target number.
 */
class Numbers internal constructor(
    private val numbers: List<Int>,
) : Iterable<Int> {
    /** Creates a sequence of [count] random target numbers between 1 and 100. */
    constructor(count: Int) : this(
        buildList {
            repeat(count) {
                add(Random.nextInt(MIN, MAX + 1))
            }
        },
    )

    private var iterator: ListIterator<Int> = numbers.listIterator()
    private var _current: Int = iterator.next()

    /** The current target number. */
    val current: Int
        get() = _current

    /** Advances to the next target number in the sequence. */
    fun next(): Int {
        _current = iterator.next()
        return _current
    }

    /** Returns `true` if the current number is the first in the sequence. */
    fun first(): Boolean = current == numbers.first()

    /** Returns `true` if the current number is the last in the sequence. */
    fun last(): Boolean = current == numbers.last()

    // must be independent from private var iterator!
    override fun iterator(): Iterator<Int> = numbers.iterator()

    override fun toString(): String = "Numbers(current: $current, numbers: $numbers)"

    companion object {
        private const val MIN = 1
        private const val MAX = 100
    }
}
