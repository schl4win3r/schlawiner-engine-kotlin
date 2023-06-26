package io.schlawiner.util

actual fun randomUUID(): String = uuid.v4() as String

@JsNonModule
@JsModule("uuid")
external val uuid: dynamic
