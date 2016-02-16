package com.markdevries.notes

import grails.async.*

class AsyncNoteService {
    @DelegateAsync NoteService noteService
}
