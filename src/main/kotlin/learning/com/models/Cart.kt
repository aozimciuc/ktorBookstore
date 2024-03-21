package learning.com.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

class CartEntry(var bookId: String?, var book: Book, var quantity: Int, var sum: Float) {
    constructor() : this(null, Book(), 0, 0.0f)
}

class Cart(id: ObjectId?, username: String, quantity: Int, sum: Float, entries: MutableList<CartEntry> = ArrayList()) {

    @BsonId
    var id: ObjectId?
    var username: String
    var quantity: Int = 0
    var sum: Float = 0.0f
    var entries: MutableList<CartEntry>

    init {
        this.id = id
        this.username = username
        this.quantity = quantity
        this.sum = sum
        this.entries = entries
    }

    constructor() : this(null, "anonymous", 0, 0.0f, mutableListOf())

    fun addBook(book: Book) {
        val bookFound = entries.find { it.bookId == book.id }
        if (bookFound == null) {
            entries.add(CartEntry(book.id, book, 1, book.price))
        } else {
            bookFound.quantity++
            bookFound.sum += book.price
        }
        this.quantity += 1
        this.sum += book.price
    }

    fun removeBook(book: Book) {
        val bookFound = entries.find { it.bookId == book.id }
        if (bookFound == null) {
            return
        }
        if (bookFound.quantity <= 1) {
            entries.remove(bookFound)
            quantity -= 1
            sum -= book.price
            return
        }
        bookFound.quantity--
        bookFound.sum -= book.price
        quantity -= 1
        sum -= book.price
    }
}