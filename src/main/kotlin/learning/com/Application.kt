package learning.com

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import learning.com.models.Session
import learning.com.plugins.configureHTTP
import learning.com.plugins.configureLocations
import learning.com.plugins.configureMonitoring
import learning.com.plugins.configureRouting
import learning.com.plugins.configureSecurity
import learning.com.plugins.configureSerialization
import learning.com.plugins.configureStatusPages
import learning.com.plugins.configureTemplating

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(testing: Boolean = false) {
    // Locations should be installed first, before any other routing feature
    configureLocations()
    configureTemplating()
    // security should be installed before routing
    configureSecurity()
    configureMonitoring()
    configureHTTP()
    configureSerialization()
    configureStatusPages()
    configureRouting()

    install(Sessions) {
        cookie<Session>(Constants.COOKIE_NAME.value)
    }
}
