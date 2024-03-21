package learning.com.templates.carts

import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.ButtonType
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.input
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import learning.com.models.Cart
import learning.com.models.Session
import learning.com.routes.Endpoints
import learning.com.templates.common.GeneralViewTemplate

class CartTemplate(val session: Session?, val cart: Cart) : Template<HTML> {
    val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(session)
    override fun HTML.apply() {
        insert(basicTemplate) {
            content {
                div(classes = "mt-2 row") {
                    h2 { +"Shopping Cart" }
                }

                table(classes = "table table-striped") {
                    thead {
                        tr {
                            th { +"Title" }
                            th { +"Price" }
                            th { +"Quantity" }
                            th { +"Total" }
                            th { +"" }
                        }
                    }
                    tbody {
                        cart.entries.forEach() {

                            tr {
                                td { +it.book.title }
                                td { +"${it.book.price}" }
                                td { +"${it.quantity}" }
                                td { +"${it.book.price * cart.quantity}" }
                                td {
                                    form(
                                        action = Endpoints.DO_REMOVE_FROM_CART.url,
                                        method = FormMethod.post,
                                        encType = FormEncType.multipartFormData
                                    ) {
                                        input(type = InputType.hidden, name = "bookId") {
                                            this.value = "${it.book.id}"
                                        }
                                        button(classes = "btn btn-primary", type = ButtonType.submit) {
                                            +"Remove"
                                        }
                                    }
                                }

                            }
                        }
                        tr {
                            td { +"Total" }
                            td { +"" }
                            td { +"${cart.quantity}" }
                            td { +"${cart.sum}" }
                            td { +"" }
                        }
                    }
                }

                div(classes = "row mt-3") {
                    form(
                        method = FormMethod.post,
                        encType = FormEncType.multipartFormData,
                        action = Endpoints.DO_CHECKOUT.url
                    ) {
                        button(classes = "btn btn-primary", type = ButtonType.submit) {
                            +"Checkout"
                        }

                    }
                }
            }
        }
    }
}