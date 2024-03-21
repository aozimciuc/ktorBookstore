package learning.com.routes

enum class Endpoints(val url: String) {
    LOGIN("/html/login"),
    LOGOUT("/html/logout"),
    HOME("/html"),
    BOOKS("/html/books"),
    DO_BOOK_SEARCH("/html/book/search"),
    DO_REMOVE_FROM_CART("/html/cart/remove"),
    DO_ADD_TO_CART("/html/cart/add"),
    DO_CHECKOUT("/html/cart/checkout"),
    CART("/html/cart"),
    RECEIPT("/html/receipt");
}