package learning.com

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import learning.com.models.Session
import learning.com.peristence.BookstoreDatabaseFactory.createDataSource
import learning.com.plugins.configureHTTP
import learning.com.plugins.configureLocations
import learning.com.plugins.configureMonitoring
import learning.com.plugins.configureRouting
import learning.com.plugins.configureSecurity
import learning.com.plugins.configureSerialization
import learning.com.plugins.configureStatusPages
import learning.com.plugins.configureTemplating
import learning.com.services.LogService

fun main(args: Array<String>): Unit = EngineMain.main(args)


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

    val dataSource = createDataSource(environment)
    val logService = LogService(dataSource = dataSource)
    configureRouting(logService)

    install(Sessions) {
        cookie<Session>(Constants.COOKIE_NAME.value)
    }
}
