package learning.com.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import learning.com.entities.UserService
import learning.com.routes.books
import learning.com.routes.cart
import learning.com.routes.common
import learning.com.routes.login
import learning.com.routes.receipt
import learning.com.services.LogService

fun Application.configureRouting(logService: LogService, userService: UserService) {
    routing {
        common()
        books()
        cart()
        login(logService, userService)
        receipt()
    }
}
