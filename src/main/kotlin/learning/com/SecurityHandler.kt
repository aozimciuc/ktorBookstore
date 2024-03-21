package learning.com

class SecurityHandler {
    fun isValid(username: String, password: String): Boolean {
        return username == "admin" && password == "admin"
    }
}