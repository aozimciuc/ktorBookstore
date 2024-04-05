package learning.com.routes

import io.ktor.http.content.PartData.FormItem
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.sessions
import learning.com.Constants
import learning.com.SecurityHandler
import learning.com.entities.Severity
import learning.com.models.Session
import learning.com.services.LogService
import learning.com.templates.login.HomeTemplate
import learning.com.templates.login.LoginTemplate
import learning.com.templates.login.LogoutTemplate
import org.slf4j.LoggerFactory


val log = LoggerFactory.getLogger("LoginRoutes")
fun Route.login(logService: LogService) {

    get(Endpoints.LOGIN.url) {
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?

        call.respondHtmlTemplate(LoginTemplate(session)) {
            greeting {
                +"Hello from Ktor, regular user!"
            }
        }
    }

    get(Endpoints.HOME.url) {
        val id = logService.logEvent(Endpoints.HOME.url, Severity.INFO, "User visited home page")
        logService.updateMessage(id, "User visited home page (updated)")
        logService.getAllWithSubselect().forEach { logEvent -> log.info("Log event (subselect): $logEvent") }
        logService.getAllWithJoin().forEach { logEvent -> log.info("Log event (join): $logEvent") }

        logService.getAllGroupedByDate()

        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        call.respondHtmlTemplate(HomeTemplate(session)) {
        }
    }

    get(Endpoints.LOGOUT.url) {
        call.sessions.clear(Constants.COOKIE_NAME.value)
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        call.respondHtmlTemplate(LogoutTemplate(session)) {
        }
    }

    post(Endpoints.LOGIN.url) {

        val multipart = call.receiveMultipart()
        call.request.headers.forEach { key, list -> log.info("Headers in request: $key: $list") }
        var username = ""
        var password = ""
        while (true) {
            val part = multipart.readPart() ?: break
            when (part) {
                is FormItem -> {
                    when (part.name) {
                        "username" -> username = part.value
                        "password" -> password = part.value
                    }
                }

                else -> {
                    // no-op
                }
            }
            part.dispose()
        }
        if (SecurityHandler().isValid(username, password)) {
            val session = Session(username)
            call.sessions.set(Constants.COOKIE_NAME.value, session)
            call.respondHtmlTemplate(HomeTemplate(session)) {
            }
        } else {
            val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
            call.respondHtmlTemplate(LoginTemplate(session)) {
                greeting {
                    +"Invalid username or password. Please try again."
                }
            }
        }

    }

}