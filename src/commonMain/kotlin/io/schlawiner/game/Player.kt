package io.schlawiner.game

data class Player(val name: String, val human: Boolean) {
    override fun toString(): String = "Player($name ${if (human) "human" else "computer"})"

    companion object {
        fun human(name: String) = Player(name, true)

        fun computer(name: String) = Player(name, false)
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
