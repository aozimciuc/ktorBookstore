package learning.com

import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.locations.Location
import io.ktor.server.locations.get
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route


@Location("/book/list")
data class BookListLocation(val sort: String, val order: String)

fun Route.books() {
    val dataManager = DataManager()

    authenticate("bookStoreAuth") {
        get<BookListLocation>() {
            call.respond(dataManager.allBooks(it.sort, it.order))
        }
    }

    route("/book") {
        get("/") {
            call.respond(dataManager.allBooks())
        }

        put("") {
            val book = call.receive(Book::class)
            val updateBook = dataManager.updateBook(book)
            call.respond { updateBook }
        }

        post("/{id}") {
            val id = call.parameters["id"]
            val book = call.receive(Book::class)
            book.id = id ?: book.id
            dataManager.newBook(book)
            call.respond(book)
        }

        delete("/{id}") {
            val id = call.parameters["id"]
            dataManager.deleteBook(id)
            call.respondText { "The book $id has been deleted" }
        }
    }
}