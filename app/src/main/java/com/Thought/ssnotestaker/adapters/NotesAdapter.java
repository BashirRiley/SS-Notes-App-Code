package com.Thought.ssnotestaker.adapters;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Thought.ssnotestaker.R;
import com.Thought.ssnotestaker.entities.Note;
import com.Thought.ssnotestaker.listeners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> notes;
    private final NotesListener notesListener;
    private Timer timer;
    private List<Note> notesSource;

    public NotesAdapter(List<Note> notes, NotesListener notesListener) {
        this.notes = notes;
        this.notesListener = notesListener;
        notesSource = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    notesListener.onNoteClicked(notes.get(adapterPosition), adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textSubtitle, textDateTime;
        RoundedImageView imageNote;
        LinearLayout layoutNote;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            imageNote = itemView.findViewById(R.id.imageNotePic);
            layoutNote = itemView.findViewById(R.id.layoutNote);
        }

        void setNote(Note note) {
            textTitle.setText(note.getTitle());
            if (note.getSubtitle().trim().isEmpty()) {
                textSubtitle.setVisibility(View.GONE);
            } else {
                textSubtitle.setText(note.getSubtitle());
            }
            textDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if (note.getImagePath() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(note.getImagePath(), null);
                if (bitmap != null) {
                    imageNote.setImageBitmap(bitmap);
                    imageNote.setVisibility(View.VISIBLE);
                } else {
                    imageNote.setVisibility(View.GONE);
                }
            } else {
                imageNote.setVisibility(View.GONE);
            }
        }
    }

    public void searchNotes(final String searchKeyword) {
        if (searchKeyword.trim().isEmpty()) {
            notes = notesSource;
        } else {
            ArrayList<Note> temp = new ArrayList<>();
            for (Note note : notesSource) {
                if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                        || note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())) {
                    temp.add(note);
                }
            }
            notes = temp;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }


    public void cancelTimer(){
        if (timer != null){
            timer.cancel();
        }
    }
}

