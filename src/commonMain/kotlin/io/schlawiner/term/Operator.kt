package io.schlawiner.term

@Suppress("MagicNumber")
enum class Operator(val precedence: Int) {
    PLUS(0) {
        override fun toString(): String {
            return "+"
        }
    },
    MINUS(0) {
        override fun toString(): String {
            return "-"
        }
    },
    TIMES(5) {
        override fun toString(): String {
            return "*"
        }
    },
    DIVIDED(5) {
        override fun toString(): String {
            return "/"
        }
    },
    ;

    companion object {
        fun toOperator(token: String): Operator? {
            return when (token) {
                "+" -> PLUS
                "-" -> MINUS
                "*" -> TIMES
                "/" -> DIVIDED
                else -> null
            }
        }
    }
}
