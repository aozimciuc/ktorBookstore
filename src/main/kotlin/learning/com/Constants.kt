package learning.com

enum class Constants(val value: String) {
    COOKIE_NAME("BOOK_STORE_COOKIE"),
    MONGO_DB_NAME("bookstore"),
    MONGO_DB_CONNECTION_PROPERTY("mongodb.uri"),
    DEFAULT_MONGO_DB_CONNECTION_STRING("mongodb://localhost:27017"),
    APPLICATION_PORT_PROPERTY("ktor.deployment.port"),
    DEFAULT_APPLICATION_PORT("8080")
}