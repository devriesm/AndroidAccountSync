package com.markdevries.notes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.markdevries.notes.R;
import com.markdevries.notes.models.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 2/15/16.
 */
public class NoteListAdapter extends BaseAdapter {


    private List<Note> mNotes = new ArrayList<>();


    public void setNotes(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Note getItem(int position) {
        return mNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(getItem(position).note);

        return convertView;
    }
}
