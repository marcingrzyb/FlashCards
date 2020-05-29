package pl.edu.agh.kis.flashcards.module.playmode.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class EventSessionHandler {

    private Map<Integer, EventType> event;
    private Instant startTime;

    public EventSessionHandler() {
        this.event = new HashMap<>();
        this.startTime = Instant.now();
    }

    public void addEvent(Integer noteId, EventType eventType) {
        event.put(noteId, eventType);
    }

    public EventModel processData() {
        EventModel eventModel = new EventModel();
        eventModel.setAddToFavourite(2L);
        eventModel.setAddToRemebered(2L);
        eventModel.setCount(5L);
        eventModel.setDuration(66L);
        return eventModel;
    }

}
