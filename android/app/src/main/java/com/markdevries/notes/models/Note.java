package com.markdevries.notes.models;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;
import com.markdevries.notes.models.auth.User;

/**
 * Created by mark on 2/15/16.
 */
@Table(name = "Notes", id = BaseColumns._ID)
public class Note extends Model {

    @SerializedName("id")
    @Column(name = "ServerId", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long serverId;

    @Column(name = "Note", index = true)
    @SerializedName("note")
    public String note;

    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public User user;

    @Column(name = "IsSynced")
    public boolean isSynced = false;


    public static Note fromCursor(Cursor cursor) {
        Note note = new Note();
        note.loadFromCursor(cursor);
        return note;
    }

}
