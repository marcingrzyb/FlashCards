package pl.edu.agh.kis.flashcards.module.playmode.service;

public class EventModel {

    private Long duration;
    private Long count;
    private Long addToFavourite;
    private Long addToRemebered;

    public EventModel() {
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getAddToFavourite() {
        return addToFavourite;
    }

    public void setAddToFavourite(Long addToFavourite) {
        this.addToFavourite = addToFavourite;
    }

    public Long getAddToRemebered() {
        return addToRemebered;
    }

    public void setAddToRemebered(Long addToRemebered) {
        this.addToRemebered = addToRemebered;
    }

}
