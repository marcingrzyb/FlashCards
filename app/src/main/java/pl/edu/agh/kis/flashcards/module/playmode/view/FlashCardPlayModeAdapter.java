package pl.edu.agh.kis.flashcards.module.playmode.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import pl.edu.agh.kis.flashcards.database.entity.NoteEntity;
import pl.edu.agh.kis.flashcards.module.playmode.fragment.FlashCard;
import pl.edu.agh.kis.flashcards.module.playmode.fragment.SessionSummary;
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionService;

public class FlashCardPlayModeAdapter extends FragmentStateAdapter {

    private final List<NoteEntity> notes;
    private final EventSessionService eventSessionHandler;
    private SessionSummary sessionSummary;

    public FlashCardPlayModeAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<NoteEntity> notes, EventSessionService eventSessionHandler) {
        super(fragmentManager, lifecycle);
        this.notes = notes;
        this.eventSessionHandler = eventSessionHandler;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
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

    @Override
    public int getItemCount() {
        return notes.size() == 0
                ? 0
                : notes.size() + 1;
    }
}
