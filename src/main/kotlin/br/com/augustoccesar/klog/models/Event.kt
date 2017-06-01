package br.com.augustoccesar.klog.models

import com.google.gson.annotations.SerializedName
import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * Author: augustoccesar
 * Date: 31/05/17
 */
open class Event(var key: String,
                 var message: String,
                 var status: String? = null,
                 var cause: Cause? = null,
                 @Transient var parent: Event? = null,
                 @SerializedName("start_time") var startTime: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC),
                 @SerializedName("end_time") var endTime: ZonedDateTime? = null,
                 @SerializedName("sub_events") var subEvents: MutableList<Event> = mutableListOf()) {

    object Status {
        val OK: String = "OK"
        val ERROR: String = "ERROR"
    }

    fun finish(status: String): Event {
        this.endTime = ZonedDateTime.now(ZoneOffset.UTC)
        this.status = status
        return this
    }
}

data class Cause(var exceptionName: String, var exceptionMessage: String)
