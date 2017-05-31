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
    private val gson by lazy { buildDefaultJson() }
    private val events: MutableList<Event> = mutableListOf()
    private val indices: MutableList<Event> = mutableListOf()
    private val watchers: MutableList<Event> = mutableListOf()
    private var outputFile: File? = null

    constructor(outputFile: File) : this() {
        this.registerOutputFile(outputFile)
    }

    private fun buildDefaultJson(): Gson {
        return GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeAdapter())
                .create()
    }

    fun findEvent(eventKey: String): Event? {
        return indices.filter { it.key == eventKey }.firstOrNull()
    }

    fun registerOutputFile(outputFile: File) {
        this.outputFile = outputFile
    }

    fun registerEvent(event: Event, parentKey: String? = null) {
        if (parentKey == null) {
            this.events.add(event)
        } else {
            findEvent(parentKey)?.subEvents?.add(event)
        }

        this.indices.add(event)
    }

    fun write() {
        if(this.outputFile?.parent != null) {
            File(this.outputFile?.parent).mkdirs()
        }

        this.outputFile?.printWriter().use { out ->
            out?.print(gson.toJson(this.events.sortedWith(compareBy({ it.startTime }))))
        }
    }
}
