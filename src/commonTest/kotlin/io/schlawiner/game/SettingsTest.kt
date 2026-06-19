package io.schlawiner.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class SettingsTest {
    @Test
    fun defaults() {
        val settings = Settings.defaults()

        assertEquals(60, settings.timeout)
        assertEquals(5, settings.penalty)
        assertEquals(3, settings.retries)
        assertEquals(8, settings.numbers)
        assertFalse(settings.autoDice)
        assertEquals(Level.MEDIUM, settings.level)
    }
}
