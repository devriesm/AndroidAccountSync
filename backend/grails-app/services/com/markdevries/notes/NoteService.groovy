package com.markdevries.notes

import com.markdevries.notes.auth.User
import com.markdevries.notes.commands.CreateNoteCommand
import com.markdevries.notes.response.NoteListResponse
import grails.transaction.Transactional

@Transactional
class NoteService {

    Integer count() {
        Note.count()
    }

    @Transactional(readOnly = true)
    def list(User user, def params, Integer max, Integer offset) {

        params.max = Math.min(max ?: 10, 100)
        params.offset = Math.max(offset ?: 0, 0)

        def notes = Note.findAllByUser(user, params)
        def notesSize = notes.size() ?: 0
        def end = params.offset + notesSize
        def count = Note.count()

        return new NoteListResponse(notes: notes, start: params.offset, end: end, total: count)
    }

    Note create(User user, CreateNoteCommand cmd) {
        def note = new Note(cmd.note)
        user.addToNotes(note).save(flush:true)

        return note
    }
}
