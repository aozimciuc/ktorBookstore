package learning.com.templates.common

import io.ktor.server.html.PlaceholderList
import io.ktor.server.html.Template
import io.ktor.server.html.each
import io.ktor.server.html.insert
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.UL
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.li
import kotlinx.html.nav
import kotlinx.html.ul

class NavigationTemplate : Template<FlowContent> {

    val menuitems = PlaceholderList<UL, FlowContent>()

    override fun FlowContent.apply() {
        div {
            nav(classes = "navbar navbar-expand-lg navbar-light bg-light") {
                a(classes = "navbar-brand", href = "/html") { +"Bookstore" }
                button(classes = "navbar-toggler", type = ButtonType.button) {
                    attributes["data-toggle"] = "collapse"
                    attributes["data-target"] = "#navbarNav"
                    attributes["aria-controls"] = "navbarNav"
                    attributes["aria-expanded"] = "false"
                    attributes["aria-label"] = "Toggle navigation"
                }
                div(classes = "collapse navbar-collapse") {
                    attributes["id"] = "navbarNav"
                    ul(classes = "navbar-nav") {
                        each(menuitems) {
                            li { insert(it) }
                        }
                    }
                }
            }
        }
    }
}