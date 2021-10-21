package Result;

import Model.Event;

/**
 * Result object of the Event query.
 */
public class EventResult extends Result {
    /**
     * Event object returned from database query.
     */
    private Event event;

    /**
     * Array of event objects associated with the given user.
     */
    private Event[] events;

    /**
     * To tell if Result has an array of Event objects or a single Event object.
     */
    private boolean array;

    /**
     * Constructs a new successful Event query result.
     *
     * @param event Event returned from database query.
     */
    public EventResult(Event event) {
        this.event = event;
        this.success = true;
        this.array = false;
    }

    /**
     * Constructs a new successful Events query result.
     *
     * @param events Event array returned from database query.
     */
    public EventResult(Event[] events) {
        this.events = events;
        this.success = true;
        this.array = true;
    }

    /**
     * Constructs a new failed Event Query result.
     *
     * @param message Error message of query.
     */
    public EventResult(String message) {
        this.message = message;
        this.success = false;
        this.array = false;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }
}
