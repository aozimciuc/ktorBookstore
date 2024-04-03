package learning.com.routes

import io.ktor.http.content.PartData.FormItem
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.sessions
import learning.com.Constants
import learning.com.peristence.DataManagerMongoDB
import learning.com.models.Session
import learning.com.templates.books.BookstoreTemplate
import learning.com.templates.carts.CartTemplate
import learning.com.templates.carts.ReceiptTemplate
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.cart() {
    val log: Logger = LoggerFactory.getLogger("CartRoutesLogger")

    get(Endpoints.CART.url) {
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        val cart = DataManagerMongoDB.findCartBySession(session)
        call.respondHtmlTemplate(CartTemplate(session, cart)) {}
    }

    post(Endpoints.DO_ADD_TO_CART.url) {
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        val bookId = getBookIdFromFormData(call)
        log.info("Adding book with id $bookId to cart")
        val book = DataManagerMongoDB.findBookById(bookId)
        log.info("Book found: $book")
        DataManagerMongoDB.addBook(session, book)
        call.respondHtmlTemplate(BookstoreTemplate(session, DataManagerMongoDB.allBooks())) {
            searchFilter {
                +"Book added to cart"
            }
        }
        log.info("Book added to cart")
    }

    post(Endpoints.DO_REMOVE_FROM_CART.url) {
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        val book = DataManagerMongoDB.findBookById(getBookIdFromFormData(call))
        DataManagerMongoDB.removeBookFromCart(session, book)
        log.info("Book $book removed from cart")
        val cart = DataManagerMongoDB.findCartBySession(session)
        call.respondHtmlTemplate(CartTemplate(session, cart)) {}
    }

    post(Endpoints.DO_CHECKOUT.url) {
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        val cart = DataManagerMongoDB.findCartBySession(session)
        DataManagerMongoDB.checkoutCart(cart)
        call.respondHtmlTemplate(ReceiptTemplate(session, cart)) {}
    }
}

private suspend fun getBookIdFromFormData(call: ApplicationCall): String {
    val multipart = call.receiveMultipart()
    while (true) {
        val part = multipart.readPart() ?: break
        when (part) {
            is FormItem -> {
                if (part.name == "bookId") {
                    return part.value
                }
            }

            else -> {
                // no-op
            }
        }
        part.dispose()
    }
    return ""
}