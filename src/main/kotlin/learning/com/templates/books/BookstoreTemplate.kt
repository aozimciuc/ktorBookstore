package learning.com.templates.books

import io.ktor.server.html.Placeholder
import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.ThScope
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.input
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr
import learning.com.models.Book
import learning.com.models.Session
import learning.com.routes.Endpoints
import learning.com.templates.common.GeneralViewTemplate

class BookstoreTemplate(val session: Session?, val books: List<Book>) : Template<HTML> {

    val bookOnSale = Placeholder<FlowContent>()
    val bookRecommended = Placeholder<FlowContent>()
    val usefulLinks = Placeholder<FlowContent>()
    var theme: String? = "light"

    val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(session)
    val searchFilter = Placeholder<FlowContent>()

    override fun HTML.apply() {
        insert(basicTemplate) {
            content {

                h1 { +"Bookstore" }

                div {
                    insert(searchFilter)
                }
                form(
                    method = FormMethod.post,
                    encType = FormEncType.multipartFormData,
                    action = Endpoints.DO_BOOK_SEARCH.url
                ) {
                    div(classes = "row mb-3 mt-3") {
                        if (session == null) {
                            div(classes = "alert") {
                                +"Please "
                                a(href = Endpoints.LOGIN.url) { +"Login" }
                                +" to see the books"
                            }
                        } else {
                            div(classes = "col-md-6") {
                                form(classes = "form-search form-inline") {
                                    input(
                                        type = InputType.text,
                                        classes = "form-control input-medium search-query",
                                        name = "search"
                                    ) {
                                        placeholder = "Search for books"
                                        attributes["aria-label"] = "Search"
                                        attributes["aria-describedby"] = "basic-addon1"
                                    }
                                    button(classes = "btn btn-primary", type = ButtonType.submit) {
                                        +"Search"
                                    }
                                }
                            }
                        }
                        div {

                            table(classes = "table table-striped") {
                                tr {
                                    th(scope = ThScope.col) { +"Id" }
                                    th(scope = ThScope.col) { +"Title" }
                                    th(scope = ThScope.col) { +"Author" }
                                    th(scope = ThScope.col) { +"Price" }
                                    th(scope = ThScope.col) { +"" }
                                }
                                tbody {
                                    books.forEach() {
                                        tr {
                                            td { +it.id.toString() }
                                            td { +it.title }
                                            td { +it.author }
                                            td { +"$${it.price}" }
                                            td {
                                                form(
                                                    action = Endpoints.DO_ADD_TO_CART.url,
                                                    method = FormMethod.post,
                                                    encType = FormEncType.multipartFormData
                                                ) {
                                                    input(type = InputType.hidden, name = "bookId") {
                                                        this.value = it.id.toString()
                                                    }
                                                    button(
                                                        classes = "btn btn-primary",
                                                        type = ButtonType.submit
                                                    ) { +"Add to cart" }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}