package pl.edu.agh.kis.flashcards.module.main.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import pl.edu.agh.kis.flashcards.R;

public class SpinnerCustomAdapter extends ArrayAdapter<CharSequence> {

    Map<String, Integer> images;
    Context context;

    public SpinnerCustomAdapter(@NonNull Context context, int textViewResourceId, Map<String, Integer> images) {
        super(context, textViewResourceId);
        this.images = images;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflter.inflate(R.layout.custom_spinner_item, null);
        ImageView flag = row.findViewById(R.id.flag);

        flag.setImageResource((Integer) images.values().toArray()[position]);
        return row;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflter.inflate(R.layout.custom_spinner_item, null);
        ImageView flag = row.findViewById(R.id.flag);
        if (position < 4) {
            flag.setImageResource((Integer) images.values().toArray()[position]);
        }
        return row;
    }

    @Override
    public int getPosition(@Nullable CharSequence item) {
        return new ArrayList<>(images.keySet()).indexOf(item);
    }

    @Override
    public int getCount() {
        return images.size();
    }

}
