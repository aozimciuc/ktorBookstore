package learning.com.templates.common

import io.ktor.server.html.Placeholder
import io.ktor.server.html.Template
import io.ktor.server.html.TemplatePlaceholder
import io.ktor.server.html.insert
import kotlinx.html.HTML
import kotlinx.html.HtmlBlockTag
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.script
import learning.com.models.Session
import learning.com.routes.Endpoints

class GeneralViewTemplate(val session: Session?) : Template<HTML> {

    val content = Placeholder<HtmlBlockTag>()
    val menu = TemplatePlaceholder<NavigationTemplate>()
    override fun HTML.apply() {
        head {
            link(
                rel = "stylesheet",
                href = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css",
                type = "text/css"
            ) { this.attributes.put("crossorigin", "anonymous") }
        }

        body {
            insert(NavigationTemplate()) {
                menuitems {
                    a(classes = "nav-link", href = Endpoints.HOME.url) { +"Home" }
                }
                if (session == null) {
                    menuitems {
                        a(classes = "nav-link", href = Endpoints.LOGIN.url) { +"Login" }
                    }
                } else {
                    menuitems {
                        a(classes = "nav-link", href = Endpoints.LOGOUT.url) { +"Logout" }
                    }
                    menuitems {
                        a(classes = "nav-link", href = Endpoints.BOOKS.url) { +"Books" }
                    }
                    menuitems {
                        a(classes = "nav-link", href = Endpoints.CART.url) { +"Shopping cart" }
                    }
                }
            }

            div(classes = "container") {
                div(classes = "row") {
                    div(classes = "col-md-12") {
                        insert(content)
                    }
                }
            }
            script {
                type = "javascript"
                src = "https://code.jquery.com/jquery-3.3.1.slim.min.js"
            }
            script {
                type = "javascript"
                src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            }
            script {
                type = "javascript"
                src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            }
        }
    }
}