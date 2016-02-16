package com.markdevries.notes

import com.markdevries.notes.auth.User
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='id,note')
@ToString
class Note implements Serializable {

    private static final long serialVersionUID = 1

    String note

    Note(String note) {
        this()
        this.note = note
    }

    static belongsTo = [user:User]

    static constraints = {
    }
}
