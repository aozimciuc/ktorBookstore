package learning.com.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import learning.com.routes.books
import learning.com.routes.cart
import learning.com.routes.common
import learning.com.routes.login
import learning.com.routes.receipt

fun Application.configureRouting() {
    routing {
        common()
        books()
        cart()
        login()
        receipt()
    }
}
