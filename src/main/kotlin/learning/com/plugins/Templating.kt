package learning.com.plugins

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.sessions.sessions
import kotlinx.css.CSSBuilder
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.body
import kotlinx.css.color
import kotlinx.css.margin
import kotlinx.css.px
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.ul
import learning.com.Constants
import learning.com.models.Book
import learning.com.models.Session
import learning.com.templates.books.BookstoreTemplate

fun Application.configureTemplating() {
    routing {

        staticResources("/static", "static")
        staticResources("/favicon.ico", "static")

        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }
        get("/custom_styles.css") {
            val theme = call.parameters["theme"]
            call.respondCss {
                body {
                    if (theme == "dark") {
                        color = Color.white
                        backgroundColor = Color.black
                    } else {
                        color = Color.black
                        backgroundColor = Color.white
                    }
                    margin(0.px)
                }
                rule("h1.page-title") {
                    color = Color.white
                }
            }
        }

        get("/html-css-dsl") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/static/styles.css", type = "text/css")
                }
                body {
                    h1(classes = "page-title") {
                        +"Hello from Ktor!"
                    }
                }
            }
        }

        get("/html-css-dsl-placeholder") {
            val session = call.sessions.get(Constants.COOKIE_NAME.value) as Session?
            val books: List<Book> = listOf()

            call.respondHtmlTemplate(template = BookstoreTemplate(session, books)) {
                bookOnSale {
                    +"The best of kotlin - By learning.com"
                }
                bookRecommended {
                    +"Kotlin in action - By learning.com"
                }
                usefulLinks {
                    ul {
                        li {
                            a(href = "/html-css-dsl") { +"HTML CSS DSL" }
                        }
                        li {
                            a(href = "/html-dsl") { +"HTML DLS" }
                        }
                        li {
                            a(href = "/custom_styles.css") { +"custom styles" }
                        }
                    }
                }
                theme = call.parameters["theme"]
            }
        }

    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

