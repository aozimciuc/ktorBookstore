package learning.com.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import org.slf4j.Logger

val log: Logger = org.slf4j.LoggerFactory.getLogger("StatusPages")
fun Application.configureStatusPages() {

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
            log.error("Generic exception: $cause")
            throw cause
        }
    }
}
