package learning.com.templates.carts

import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h5
import kotlinx.html.li
import kotlinx.html.p
import kotlinx.html.ul
import learning.com.models.Cart
import learning.com.models.Session
import learning.com.routes.Endpoints
import learning.com.templates.common.GeneralViewTemplate

class ReceiptTemplate(val session: Session?, private val cart: Cart) : Template<HTML> {
    private val basicTemplate = GeneralViewTemplate(session)

    override fun HTML.apply() {
        insert(basicTemplate) {
            content {
                div(classes = "mt-2 row") {
                    div(classes = "card") {
                        div(classes = "card-body") {
                            h5(classes = "card-title") {
                                +"Thank you for shopping at my Bookstore!"
                            }
                            p(classes = "card-text") {
                                +"""You just purchased ${cart.quantity} books for a total of ${cart.sum} gold coins. The titles you purchased are listed here:"""
                                ul {
                                    cart.entries.forEach {
                                        li { +"${it.book.title} written by ${it.book.author}" }
                                    }
                                }
                            }
                            p(classes = "card-text") {
                                +"When you are done reading those come back and get 15% discount on your next purchase."
                            }
                            a(href = Endpoints.BOOKS.url, classes = "btn btn-primary") {
                                +"Shop some more"
                            }
                        }
                    }
                }
            }
        }
    }
}