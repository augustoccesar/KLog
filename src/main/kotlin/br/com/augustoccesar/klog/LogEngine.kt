package br.com.augustoccesar.klog

import br.com.augustoccesar.klog.adapters.ZonedDateTimeAdapter
import br.com.augustoccesar.klog.models.Event
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.time.ZonedDateTime


/**
 * Author: augustoccesar
 * Date: 31/05/17
 */
class LogEngine() {
    private val events: MutableMap<String, Event> = mutableMapOf()
    private var outputFile: File? = null

    constructor(outputFile: File) : this() {
        this.registerOutputFile(outputFile)
    }

    fun findEvent(eventKey: String): Event? {
        return this.events[eventKey]
    }

    fun registerOutputFile(outputFile: File) {
        this.outputFile = outputFile
    }

    fun registerEvent(event: Event, parentKey: String? = null) {
        if (parentKey == null) {
            this.events.put(event.key, event)
        } else {
            event.parent = findEvent(parentKey)
            this.events.put(event.key, event)
        }
    }

    fun buildPrintableStructure(): List<Event> {
        val list = mutableListOf<Event>()

        this.events.forEach { key, value ->
            if (value.parent != null) {
                value.parent?.subEvents?.add(value)
            } else {
                list.add(value)
            }
        }

        return list
    }

    fun write() {
        val output = buildPrintableStructure()

        val gson = GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeAdapter())
                .create()

        if (this.outputFile?.parent != null) {
            File(this.outputFile?.parent).mkdirs()
        }

        this.outputFile?.printWriter().use { out ->
            out?.print(gson.toJson(output))
        }
    }
}
