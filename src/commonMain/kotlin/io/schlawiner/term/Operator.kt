package io.schlawiner.term

/**
 * The four basic arithmetic operators used in Schlawiner expressions.
 *
 * Each operator has a [precedence] value used by the shunting-yard parser and the pretty-printer
 * to determine parenthesization. Addition and subtraction have lower precedence (0) than
 * multiplication and division (5).
 *
 * @property precedence numeric precedence level (higher binds tighter)
 */
@Suppress("MagicNumber")
enum class Operator(
    val precedence: Int,
) {
    PLUS(0) {
        override fun toString(): String = "+"
    },
    MINUS(0) {
        override fun toString(): String = "-"
    },
    TIMES(5) {
        override fun toString(): String = "*"
    },
    DIVIDED(5) {
        override fun toString(): String = "/"
    },
    ;

    companion object {
        /** Parses a single-character operator [token] (`+`, `-`, `*`, `/`) or returns `null` if unrecognized. */
        fun toOperator(token: String): Operator? =
            when (token) {
                "+" -> PLUS
                "-" -> MINUS
                "*" -> TIMES
                "/" -> DIVIDED
                else -> null
            }
    }
}
