package pl.edu.agh.kis.flashcards;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FlashCardCollectionAdapter extends FragmentStatePagerAdapter {

    public FlashCardCollectionAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        FlashCard flashCardKt = new FlashCard();
        Bundle bundle = new Bundle();
        position = position + 1;
        bundle.putString("message", "dupa: " + position);
        flashCardKt.setArguments(bundle);
        return flashCardKt;
    }

    @Override
    public int getCount() {
        return 10;
    }
}
