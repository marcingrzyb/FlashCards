package pl.edu.agh.kis.flashcards.module.playmode.service

import java.util.*

class Event(id: Int?, event: EventType?) {

    val id: Int? = id
    val event: EventType? = event

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val event1 =
            o as Event
        return id == event1.id &&
                event === event1.event
    }

    override fun hashCode(): Int {
        return Objects.hash(id, event)
    }

}