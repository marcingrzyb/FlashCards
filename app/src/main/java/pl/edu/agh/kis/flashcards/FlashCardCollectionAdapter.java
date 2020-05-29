package pl.edu.agh.kis.flashcards;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import pl.edu.agh.kis.flashcards.database.entities.NoteEntity;
import pl.edu.agh.kis.flashcards.fragments.FlashCard;

public class FlashCardCollectionAdapter extends FragmentStatePagerAdapter {

    private List<NoteEntity> notes;

    public FlashCardCollectionAdapter(@NonNull FragmentManager fm, int behavior, List<NoteEntity> notes) {
        super(fm, behavior);
        this.notes = notes;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        FlashCard flashCardKt = new FlashCard();
        Bundle bundle = new Bundle();
        NoteEntity noteEntity = notes.get(position);
        bundle.putSerializable("note", noteEntity);
        flashCardKt.setArguments(bundle);
        return flashCardKt;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

}
