package io.schlawiner.game

/**
 * A game participant, either human or computer-controlled.
 *
 * @property name display name of the player
 * @property human `true` for human players, `false` for computer players
 */
data class Player(
    val name: String,
    val human: Boolean,
) {
    override fun toString(): String = "Player($name ${if (human) "human" else "computer"})"

    companion object {
        /** Creates a human player with the given [name]. */
        fun human(name: String) = Player(name, true)

        /** Creates a computer player with the given [name]. */
        fun computer(name: String) = Player(name, false)
    }
}

/**
 * An ordered, cyclically iterable collection of [Player]s.
 *
 * Calling [next] repeatedly cycles endlessly through the players. The [current] property always reflects the
 * most recently advanced player.
 */
class Players(
    private val players: List<Player>,
) : Iterable<Player> {
    init {
        require(players.isNotEmpty()) {
            "No empty players allowed!"
        }
    }

    private var iterator: ListIterator<Player> = players.listIterator()
    private var _current: Player = iterator.next()

    /** The player whose turn it currently is. */
    val current: Player
        get() = _current

    /** Advances to the next player, cycling back to the first after the last. */
    fun next(): Player {
        if (!iterator.hasNext()) {
            iterator = players.listIterator()
        }
        _current = iterator.next()
        return _current
    }

    /** Returns `true` if the current player is the first in the list. */
    fun first(): Boolean = current == players.first()

    /** Returns `true` if the current player is the last in the list. */
    fun last(): Boolean = current == players.last()

    // must be independent from private var iterator!
    override fun iterator(): Iterator<Player> = players.iterator()

    override fun toString(): String = "Players(current: $current, players: $players)"
}
