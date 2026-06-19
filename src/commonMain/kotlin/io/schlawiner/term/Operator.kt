package io.schlawiner.term

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
