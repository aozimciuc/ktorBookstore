package learning.com.templates.login

import io.ktor.server.html.Template
import io.ktor.server.html.insert
import kotlinx.html.HTML
import kotlinx.html.div
import kotlinx.html.h2
import learning.com.models.Session
import learning.com.templates.common.GeneralViewTemplate

class LogoutTemplate(val session: Session?, val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(session)) : Template<HTML> {
    override fun HTML.apply() {
        insert(basicTemplate) {
            menu {
                menuitems {
                    attributes["href"] = "/login"
                    +"Login"
                }
                menuitems {
                    attributes["href"] = "/vip-login"
                    +"VIP Login"
                }
            }
            content {
                div(classes = "mt-2") {
                    h2 {
                        +"You have been logged out."
                    }
                }
            }
        }
    }
}