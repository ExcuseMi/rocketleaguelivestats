package com.excuseme.rocketleaguelivestats.scanner.model;

public class Event {
    private String identifier;
    private EventType eventType;

    public Event(String identifier, EventType eventType) {
        this.identifier = identifier;
        this.eventType = eventType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "identifier='" + identifier + '\'' +
                ", eventType=" + eventType +
                '}';
    }

    public enum EventType {
        GAME_ENDED, GAME_LOADED
    }

}
