package com.markdevries.notes


import grails.core.GrailsApplication
import grails.util.Environment
import grails.plugins.*
import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class ApplicationController implements PluginManagerAware {

    GrailsApplication grailsApplication
    GrailsPluginManager pluginManager

    def index() {
        [grailsApplication: grailsApplication, pluginManager: pluginManager]
    }
}