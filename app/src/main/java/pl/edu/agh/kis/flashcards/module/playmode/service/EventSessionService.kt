package pl.edu.agh.kis.flashcards.module.playmode.service

import java.time.Instant
import java.util.*

class EventSessionService(count: Int?) {

    private var eventMap: MutableMap<Event, Boolean>? = null
    private var startTime: Instant? = null
    private var count: Int? = count

    init {
        eventMap = HashMap()
        startTime = Instant.now()
    }

    fun addEvent(id: Int?, event: EventType?, value: Boolean) {
        eventMap!!.put(Event(id, event), value)
    }

    fun deleteEvent(id: Int?, event: EventType?, value: Boolean?) {
        eventMap!!.remove(Event(id, event))
    }

    //TODO: better solution
    fun processData(): EventModel? {
        val remembered = eventMap!!.entries
            .stream()
            .filter { entry: Map.Entry<Event, Boolean> -> isTrue(entry, EventType.REMEMBER) }
            .count()
        val favourite = eventMap!!.entries
            .stream()
            .filter { entry: Map.Entry<Event, Boolean> -> isTrue(entry, EventType.FAVOURITE) }
            .count()
        val eventModel = EventModel()
        eventModel.count = Math.toIntExact(count!!.toLong())
        eventModel.addToFavourite = favourite.toInt()
        eventModel.addToRemebered = remembered.toInt()
        eventModel.duration = resolveDuration()
        return eventModel
    }

    private fun isTrue(entry: Map.Entry<Event, Boolean>, eventType: EventType): Boolean {
        return eventType == entry.key.event && java.lang.Boolean.TRUE == entry.value
    }

    private fun resolveDuration(): Long {
        return Instant.now().epochSecond - startTime!!.epochSecond
    }

}