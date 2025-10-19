package org.chocassye.noule;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.chocassye.noule.lang.ManchuData;

import java.util.Vector;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private int position = 0;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView, SuggestionAdapter parent) {
            super(itemView);
            view = itemView;
            TextView textView = view.findViewById(R.id.textView);
            textView.setOnTouchListener(
                (v, event) -> parent.onTouchEvent(v, event, position)
            );
        }

        public TextView getTextView() {
            return view.findViewById(R.id.textView);
        }

        public TextView getHintTextView() {
            return view.findViewById(R.id.hintTextView);
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    public static class SuggestionEntry {
        String input;
        String output;
        String annotation;
        int freq;
    }

    private Vector<SuggestionEntry> entries = null;

    public interface OnTouchListener {
        boolean onTouch(View v, MotionEvent event, SuggestionEntry entry);
    }
    private OnTouchListener onTouchListener = null;

    private Typeface manchuType;

    public SuggestionAdapter(Typeface manchuType) {
        super();
        this.manchuType = manchuType;
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
        TextView textView = holder.getTextView();
        TextView hintTextView = holder.getHintTextView();
        if (entries != null) {
            SuggestionEntry entry = entries.get(position);
            textView.setText(entry.output);
            if (!entry.output.isEmpty() && ManchuData.isManchu(entry.output)) {
                textView.setTypeface(manchuType);
                textView.setTextSize(23);
            }
            else {
                textView.setTypeface(Typeface.DEFAULT);
                textView.setTextSize(18);
            }
            if (entry.annotation != null) {
                hintTextView.setText(entry.annotation);
                if (entry.output.codePointCount(0, entry.output.length()) == 1
                        && entry.annotation.length() >= 4) {
                    hintTextView.setScaleX(0.8f);
                    hintTextView.setLetterSpacing(-0.1f);
                } else {
                    hintTextView.setScaleX(1.0f);
                    hintTextView.setLetterSpacing(0.0f);
                }
            } else {
                hintTextView.setText("");
            }
        }
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
