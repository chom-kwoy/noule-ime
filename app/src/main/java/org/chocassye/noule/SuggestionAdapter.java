package org.chocassye.noule;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.Vector;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private int position = 0;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView, SuggestionAdapter parent) {
            super(itemView);
            view = itemView;
            Chip chip = view.findViewById(R.id.chip);
            chip.setOnTouchListener(
                (v, event) -> parent.onTouchEvent(v, event, position)
            );
        }

        public Chip getChip() {
            return view.findViewById(R.id.chip);
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    public static class SuggestionEntry {
        String input;
        String output;
    }

    private Vector<SuggestionEntry> entries = null;

    public interface OnTouchListener {
        boolean onTouch(View v, MotionEvent event, SuggestionEntry entry);
    }
    private OnTouchListener onTouchListener = null;

    public SuggestionAdapter() {
        super();
    }

    public void setData(Vector<SuggestionEntry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggestion_item, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Chip chip = holder.getChip();
        if (entries != null) {
            SuggestionEntry entry = entries.get(position);
            chip.setText(entry.output);
        }
        chip.setTextSize(18);
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        if (entries == null) {
            return 0;
        }
        return entries.size();
    }

    public void setOnTouchListener(OnTouchListener listener) {
        this.onTouchListener = listener;
    }

    private boolean onTouchEvent(View v, MotionEvent event, int position) {
        if (entries == null || onTouchListener == null) {
            return false;
        }
        else if (position < entries.size()) {
            return onTouchListener.onTouch(v, event, entries.get(position));
        }
        return false;
    }
}
