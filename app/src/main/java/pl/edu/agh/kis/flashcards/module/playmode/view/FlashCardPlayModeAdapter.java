package pl.edu.agh.kis.flashcards.module.playmode.view;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.kis.flashcards.database.entity.NoteEntity;
import pl.edu.agh.kis.flashcards.module.playmode.fragment.FlashCard;
import pl.edu.agh.kis.flashcards.module.playmode.fragment.SessionSummary;
import pl.edu.agh.kis.flashcards.module.playmode.service.EventSessionService;

public class FlashCardPlayModeAdapter extends FragmentStateAdapter {

    private final List<NoteEntity> notes;
    private final EventSessionService eventSessionHandler;
    private SessionSummary sessionSummary;


    private String sourceLang;
    private String targetLang;

    public FlashCardPlayModeAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<NoteEntity> notes, EventSessionService eventSessionHandler) {
        super(fragmentManager, lifecycle);
        this.notes = notes.stream().sorted(new Comparator<NoteEntity>() {
            @Override
            public int compare(NoteEntity lhs, NoteEntity rhs) {
                return lhs.getRemembered() && !rhs.getRemembered() ? 1 :
                    lhs.getRemembered() == rhs.getRemembered() ? 0 : -1;

            }

            @Override
            public boolean equals(Object o) {
                return false;
            }
        }).collect(Collectors.toList());
        this.eventSessionHandler = eventSessionHandler;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position != notes.size()) {
            fragment = new FlashCard(eventSessionHandler);
            Bundle bundle = new Bundle();
            NoteEntity note = notes.get(position);
            bundle.putSerializable("note", note);
            bundle.putString("sourceLang", sourceLang);
            bundle.putString("targetLang", targetLang);
            fragment.setArguments(bundle);
        } else {
            sessionSummary = new SessionSummary(eventSessionHandler);
            return sessionSummary;
        }
        return fragment;
    }

    public void processData() {
        sessionSummary.processData();
    }

    @Override
    public int getItemCount() {
        return notes.size() == 0
                ? 0
                : notes.size() + 1;
    }

    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }
}
