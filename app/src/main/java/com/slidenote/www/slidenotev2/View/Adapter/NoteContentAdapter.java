package com.slidenote.www.slidenotev2.View.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slidenote.www.slidenotev2.Model.Note;
import com.slidenote.www.slidenotev2.R;
import com.slidenote.www.slidenotev2.SlideNoteApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/28/2017.
 */

public class NoteContentAdapter extends RecyclerView.Adapter {
    private List<Note> notes;
    private RecyclerViewListener.OnNoteClickListener onNoteClickListener;
    private List<Integer> selected;

    public NoteContentAdapter(RecyclerViewListener.OnNoteClickListener onNoteClickListener) {
        this.notes = new ArrayList<>();
        this.onNoteClickListener = onNoteClickListener;
        selected = new ArrayList<>();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        selected.clear();
        notifyDataSetChanged();
    }

    public void changeItemState(int position) {
        if (position == 0){
            return;
        }
        if (selected.contains(position)) {
            selected.remove(Integer.valueOf(position));
        } else {
            selected.add(position);
        }
        notifyItemChanged(position);
    }

    public void clearItemState() {
        List<Integer> oldSelected = new ArrayList<>(selected);
        selected.clear();
        for (int position : oldSelected) {
            notifyItemChanged(position);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        NoteItemHolder itemHolder = (NoteItemHolder) holder;
        int position = holder.getAdapterPosition();
        if (position == 0){
            return;
        }
        if (selected.contains(position)) {
            itemHolder.checkSign.setVisibility(View.VISIBLE);
        } else {
            itemHolder.checkSign.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position!=0){
            return 1;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new NoteItemHolder(LayoutInflater.from(SlideNoteApplication.getContext()).inflate(R.layout.item_note_add_one_main_list,parent,false));
        }else {
            return new NoteItemHolder(LayoutInflater.from(SlideNoteApplication.getContext()).inflate(R.layout.item_note, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NoteItemHolder itemHolder = (NoteItemHolder) holder;
        if (position != 0){
            final Note note = notes.get(position-1);
            itemHolder.date.setText(note.getDate());
            itemHolder.title.setText(note.getTitle());
            if (note.getContent() != null){
                itemHolder.content.setText(note.getContent());
            }
            itemHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNoteClickListener.noteClick(note, position);
                }
            });
        } else {
            itemHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNoteClickListener.noteClick(null, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return notes.size()+1;
    }

    public class NoteItemHolder extends RecyclerView.ViewHolder {

        private LinearLayout item;
        private TextView title, date, content;
        private ImageView checkSign;

        public NoteItemHolder(View view) {
            super(view);
            checkSign = (ImageView) view.findViewById(R.id.checkSign);
            item = (LinearLayout) view.findViewById(R.id.item);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            content = (TextView) view.findViewById(R.id.content);
        }
    }
}
