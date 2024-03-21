package learning.com.templates.login

import io.ktor.server.html.Placeholder
import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.input
import learning.com.models.Session
import learning.com.routes.Endpoints
import learning.com.templates.common.GeneralViewTemplate

class LoginTemplate(val session: Session?, val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(session)) :
    Template<HTML> {

    val greeting = Placeholder<FlowContent>()
    override fun HTML.apply() {

        insert(basicTemplate) {

            menu {
                menuitems {
                    attributes["href"] = "/vip-login"
                    +"VIP Login"
                }
                menuitems {
                    attributes["href"] = Endpoints.LOGOUT.url
                    +"Logout"
                }
            }
            content {
                div(classes = "mt-2") {
                    h2 {
                        insert(greeting)
                    }
                }
                form(
                    action = Endpoints.LOGIN.url,
                    method = FormMethod.post,
                    encType = FormEncType.multipartFormData
                ) {

                    div(classes = "mb-3") {
                        input(type = InputType.text, classes = "form-control", name = "username") {
                            placeholder = "Type your username"
                            attributes["aria-label"] = "Username"
                            attributes["aria-describedby"] = "basic-addon1"
                        }
                    }
                    div(classes = "mb-3") {
                        input(type = InputType.password, classes = "form-control", name = "password") {
                            placeholder = "Type your password"
                            attributes["aria-label"] = "Password"
                            attributes["aria-describedby"] = "basic-addon1"
                        }
                    }
                    div(classes = "mb-3") {
                        button(type = ButtonType.submit, classes = "btn-primary") {
                            +"Login"
                        }
                    }
                }
            }

        }
    }
}