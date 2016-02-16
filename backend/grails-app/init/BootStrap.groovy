import com.markdevries.notes.Note
import com.markdevries.notes.auth.Role
import com.markdevries.notes.auth.User
import com.markdevries.notes.auth.UserRole

class BootStrap {

    def init = { servletContext ->
        def adminRole = new Role('ROLE_ADMIN').save()
        def userRole = new Role('ROLE_USER').save()

        def testUser = new User('devriesm@gmail.com', 'password').save()

        UserRole.create testUser, userRole

        UserRole.withSession {
            it.flush()
            it.clear()
        }

        assert User.count() == 1
        assert Role.count() == 2
        assert UserRole.count() == 1
    }

    def destroy = {
    }
}
