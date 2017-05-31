package br.com.augustoccesar.klog

import br.com.augustoccesar.klog.models.Event
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.File
import kotlin.test.assertEquals

/**
 * Author: augustoccesar
 * Date: 31/05/17
 */
object LogEngineSpec : Spek({
    given("a log engine with file defined") {
        val logEngine: LogEngine = LogEngine(File("test.json"))

        on("set the event") {
            val event = Event("EVENT_KEY", "Event message")
            logEngine.registerEvent(event)

            it("should add the event") {
                assertEquals("EVENT_KEY", logEngine.findEvent("EVENT_KEY")?.key)
            }
        }

        on("set multiple events") {
            val e = Event("KEY1", "M1")
            val e2 = Event("KEY2", "M2")
            val e3 = Event("KEY3", "M3")
            val e4 = Event("KEY4", "M4")
            val e5 = Event("KEY5", "M5")
            val e6 = Event("KEY6", "M6")
            val e7 = Event("KEY7", "M7")
            val e8 = Event("KEY8", "M8")

            logEngine.registerEvent(e)
            logEngine.registerEvent(e2)
            logEngine.registerEvent(e3)
            logEngine.registerEvent(e4, e3.key)
            logEngine.registerEvent(e5, e4.key)
            logEngine.registerEvent(e6, e4.key)
            logEngine.registerEvent(e7, e4.key)
            logEngine.registerEvent(e8)

            it("should be able to search nested") {
                assertEquals("KEY6", logEngine.findEvent("KEY6")?.key)
            }

            it("should be able to search after trees") {
                assertEquals("KEY8", logEngine.findEvent("KEY8")?.key)
            }
        }
    }
})