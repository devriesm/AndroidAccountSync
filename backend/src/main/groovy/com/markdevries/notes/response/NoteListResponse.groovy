package com.markdevries.notes.response

import com.markdevries.notes.Note

/**
 * Created by mark on 2/15/16.
 */
class NoteListResponse {

    List<Note> notes
    Integer start = 0
    Integer end = 0
    Integer total = 0

}
