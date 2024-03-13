package learning.com

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.locations.Locations
import io.ktor.server.netty.Netty
import learning.com.plugins.configureHTTP
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

fun Application.module() {
    // Locations should be installed first, before any other routing feature
    install(Locations)

    configureTemplating()
    configureSecurity()
    configureMonitoring()
    configureHTTP()
    configureSerialization()
    configureStatusPages()
    configureRouting()
}
