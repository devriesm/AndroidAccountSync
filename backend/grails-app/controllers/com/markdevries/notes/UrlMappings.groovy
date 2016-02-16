package com.markdevries.notes

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/api/notes/list"(controller:'note', action:'list')
        "/api/notes/create"(controller:'note', action:'create')

        "/"(controller: 'application', action:'index')
        "/error"(view: "_errors")
        "500"(view: '/application/serverError')
        "404"(view: '/application/notFound')
    }
}
