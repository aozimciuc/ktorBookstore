package learning.com.models

import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonRepresentation

class Book(id: String?, title: String, author: String, price: Float) {

    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    var id: String?
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
