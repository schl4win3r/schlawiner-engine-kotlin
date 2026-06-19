package io.schlawiner.util

/** Generates a random UUID string. Platform-specific: `java.util.UUID` on JVM, npm `uuid` on JS. */
expect fun randomUUID(): String
