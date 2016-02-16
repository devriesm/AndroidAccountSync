package com.markdevries.notes.commands;

/**
 * Created by mark on 2/15/16.
 */
public class CreateNoteCommand implements grails.validation.Validateable {

    String note

    static constraints = {
        note blank: false
    }

}
