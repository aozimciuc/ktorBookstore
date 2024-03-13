package learning.com.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic

fun Application.configureSecurity() {
    val users = listOf("user", "user1", "user2")
    val admins = listOf("admin")
    install(Authentication) {
        basic(name = "bookStoreAuth") {
            realm = "Book Store"
            validate { credentials ->
                // if username is in users or admins list and password is equal `password`
                // then return the principal
                if (users.contains(credentials.name) || admins.contains(credentials.name)
                    && credentials.password == "password"
                ) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}
