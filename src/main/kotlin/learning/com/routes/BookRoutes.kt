package learning.com.routes

import io.ktor.http.content.PartData
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.locations.Location
import io.ktor.server.locations.get
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.sessions.sessions
import learning.com.Constants
import learning.com.peristence.DataManagerMongoDB
import learning.com.models.Book
import learning.com.models.Session
import learning.com.templates.books.BookstoreTemplate


@Location("/book/list")
data class BookListLocation(val sort: String, val order: String, val page: Int = 1, val pageSize: Int = 10)

fun Route.books() {

    authenticate("bookStoreAuth") {
        get<BookListLocation>() {
            call.respond(DataManagerMongoDB.allBooks(it.sort, it.order, it.page, it.pageSize))
        }
    }

    get(Endpoints.BOOKS.url) {
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        val books = DataManagerMongoDB.allBooks()
        call.respondHtmlTemplate(BookstoreTemplate(session, books)) { }
    }

    put(Endpoints.BOOKS.url) {
        val book = call.receive(Book::class)
        val updateBook = DataManagerMongoDB.updateBook(book)
        call.respond(updateBook!!)
    }

    post(Endpoints.BOOKS.url) {
        val book = call.receive(Book::class)
        DataManagerMongoDB.newBook(book)
        call.respond(book)
    }

    delete("${Endpoints.BOOKS.url}/{id}") {
        val id = call.parameters["id"]
        DataManagerMongoDB.deleteBook(id)
        call.respondText { "The book $id has been deleted" }
    }

    post(Endpoints.DO_BOOK_SEARCH.url) {
        val multipart = call.receiveMultipart()
        var search = ""
        // get search parameter value from form data
        while (true) {
            val part = multipart.readPart() ?: break
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "search") {
                        search = part.value
                    }
                }

                else -> {
                    // no-op
                }
            }
            part.dispose()
        }

        val searchBooks = DataManagerMongoDB.searchBooks(search)
        val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
        call.respondHtmlTemplate(BookstoreTemplate(session, searchBooks)) {
            searchFilter {
                +"Search results for: $search"
            }
        }

    }
}