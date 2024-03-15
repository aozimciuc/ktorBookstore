package learning.com.models

import org.bson.types.ObjectId

class Book(id: ObjectId?, title: String, author: String, price: Float) {

    var id: ObjectId?
    var title: String
    var author: String
    var price: Float

    constructor() : this(null, "no data", "no data", 0.0f)

    init {
        this.id = id
        this.title = title
        this.author = author
        this.price = price
    }

    override fun toString(): String {
        return "Book(id=$id, title='$title', author='$author', price=$price)"
    }
}
