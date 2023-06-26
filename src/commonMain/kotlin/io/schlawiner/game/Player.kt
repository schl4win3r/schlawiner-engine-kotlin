package io.schlawiner.game

class Player(val name: String, val human: Boolean, var retries: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Player

        if (name != other.name) return false
        return human == other.human
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + human.hashCode()
        return result
    }

    override fun toString(): String = "Player($name ${if (human) "human" else "computer"})"

    fun retry() {
        retries--
    }

    companion object {
        fun human(name: String) = Player(name, true, 0)
        fun computer(name: String) = Player(name, false, 0)
    }
}

class Players(private val players: List<Player>) : Iterable<Player> {

    init {
        require(players.isNotEmpty()) {
            "No empty players allowed!"
        }
    }

    private var iterator: ListIterator<Player> = players.listIterator()
    private var _current: Player = iterator.next()

    val current: Player
        get() = _current

    /**
     * Cycles endless through the players.
     */
    fun next(): Player {
        if (!iterator.hasNext()) {
            iterator = players.listIterator()
        }
        _current = iterator.next()
        return _current
    }

    fun first(): Boolean = current == players.first()
    fun last(): Boolean = current == players.last()

    // must be independent from private var iterator!
    override fun iterator(): Iterator<Player> = players.iterator()

    override fun toString(): String = "Players(current: $current, players: $players)"
}
