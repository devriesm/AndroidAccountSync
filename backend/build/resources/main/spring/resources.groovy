import com.markdevries.notes.Note
import grails.rest.render.xml.*
import grails.rest.render.json.*

// Place your Spring DSL code here
beans = {
    noteXmlRenderer(XmlRenderer, Note) {
        excludes = ['user']
    }
    noteJsonRenderer(JsonRenderer, Note) {
        excludes = ['user']
    }
}
