package learning.com.routes

import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.sessions.sessions
import kotlinx.html.div
import kotlinx.html.style
import learning.com.Constants
import learning.com.models.Session
import learning.com.templates.login.LoginTemplate

fun Route.common() {
    get("/") {
        call.respondText("Hello World!", ContentType.Text.Plain)
    }

    get("/login") {
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        call.respondHtmlTemplate(LoginTemplate(session)) {
            greeting {
                +"Hello from Ktor, regular user!"
            }
        }
    }

    get("/vip-login") {
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        call.respondHtmlTemplate(LoginTemplate(session)) {
            greeting {
                div {
                    style = "color: red"
                    +"You are a VIP user. Welcome!"
                }
            }
        }
    }

    authenticate("bookStoreAuth") {
        get("/api/try-auth") {
            val principal = call.principal<UserIdPrincipal>()!!
            call.respondText("Principal: ${principal.name}. You are authorized to access this API. Enjoy!")
        }
    }
}
