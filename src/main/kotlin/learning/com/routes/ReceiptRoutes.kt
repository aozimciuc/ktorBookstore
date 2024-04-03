package learning.com.routes

import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.sessions.sessions
import learning.com.Constants
import learning.com.peristence.DataManagerMongoDB
import learning.com.models.Session
import learning.com.templates.carts.ReceiptTemplate

fun Route.receipt() {
    get(Endpoints.RECEIPT.url) {
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        val cart = DataManagerMongoDB.findCartBySession(session)
        call.respondHtmlTemplate(ReceiptTemplate(session, cart)) { }
    }
}