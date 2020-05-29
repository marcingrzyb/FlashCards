package pl.edu.agh.kis.flashcards.module.playmode.viewmodel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import pl.edu.agh.kis.flashcards.database.entity.NoteEntity;
import pl.edu.agh.kis.flashcards.module.playmode.fragment.FlashCard;
import pl.edu.agh.kis.flashcards.module.playmode.fragment.SessionSummary;

public class FlashCardCollectionAdapter extends FragmentStatePagerAdapter {

    private final List<NoteEntity> notes;
    private final Long sessionId;
    private SessionSummary sessionSummary;

    public FlashCardCollectionAdapter(@NonNull FragmentManager fm, int behavior, List<NoteEntity> notes, Long sessionId) {
        super(fm, behavior);
        this.notes = notes;
        this.sessionId = sessionId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position != notes.size()) {
            fragment = new FlashCard();
            Bundle bundle = new Bundle();
            NoteEntity noteEntity = notes.get(position);
            bundle.putSerializable("note", noteEntity);
            fragment.setArguments(bundle);
        } else {
            sessionSummary = new SessionSummary(sessionId);
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
