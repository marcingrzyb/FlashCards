package pl.edu.agh.kis.flashcards.module.playmode.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import pl.edu.agh.kis.flashcards.database.entity.NoteEntity;
import pl.edu.agh.kis.flashcards.module.playmode.fragment.FlashCard;
import pl.edu.agh.kis.flashcards.module.playmode.fragment.SessionSummary;
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionHandler;

public class FlashCardCollectionAdapter extends FragmentStatePagerAdapter {

    private final List<NoteEntity> notes;
    private final EventSessionHandler eventSessionHandler;
    private SessionSummary sessionSummary;

    public FlashCardCollectionAdapter(@NonNull FragmentManager fm, int behavior, List<NoteEntity> notes, EventSessionHandler eventSessionHandler) {
        super(fm, behavior);
        this.notes = notes;
        this.eventSessionHandler = eventSessionHandler;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position != notes.size()) {
            fragment = new FlashCard(eventSessionHandler);
            Bundle bundle = new Bundle();
            NoteEntity noteEntity = notes.get(position);
            bundle.putSerializable("note", noteEntity);
            fragment.setArguments(bundle);
        } else {
            sessionSummary = new SessionSummary(eventSessionHandler);
            return sessionSummary;
        }
        return fragment;
    }

    public SessionSummary getSessionSummary() {
        return sessionSummary;
    }

    @Override
    public int getCount() {
        return notes.size() == 0
                ? 0
                : notes.size() + 1;
    }

}
