package learning.com.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import learning.com.books

fun Application.configureRouting() {
    routing {
        books()
        get("/") {
            call.respondText("Hello World!")
        }

        authenticate("bookStoreAuth") {
            get("/api/try-auth") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Principal: ${principal.name}. You are authorized to access this API. Enjoy!")
            }
        }
    }
}
