package com.markdevries.notes.api

import com.markdevries.notes.Note
import com.markdevries.notes.commands.CreateNoteCommand
import com.markdevries.notes.response.NoteListResponse
import grails.rest.RestfulController
import grails.plugin.springsecurity.annotation.Secured

import static grails.async.Promises.*

class NoteController extends RestfulController {

    static responseFormats = ['json']

    static allowedMethods = [list:'POST', create:'POST', delete: 'DELETE']

    def noteService
    def asyncNoteService
    def springSecurityService

    NoteController() {
        super(Note)
    }

    @Secured('ROLE_USER')
    def list(Integer max, Integer offset) {
        def user = springSecurityService.currentUser
        /*asyncNoteService.list(user, params, max, offset).onComplete { def result ->
            def listResponse = new NoteListResponse()
            listResponse.start = 0
            listResponse.end = 0
            listResponse.total = Note.count()
            listResponse.notes = result.value
            respond listResponse
        }.onError { Throwable t ->
            println "An error occured ${t.message}"
        }*/
        def noteServiceResponse = noteService.list(user, params, max, offset)
        respond noteServiceResponse
    }

    @Secured('ROLE_USER')
    def create(CreateNoteCommand cmd) {
        bindData(cmd, request.JSON)
        cmd.validate()

        if( cmd.hasErrors()) {
            response.status = 400
            def json = [:]
            cmd.errors.allErrors.each {
                println it
            }
            json.message = "Invalid request"
            json.error = 400
            respond json
        }else {
            def user = springSecurityService.currentUser
            response.status = 201
            respond noteService.create(user, cmd)
        }

        respond [:]
    }
}