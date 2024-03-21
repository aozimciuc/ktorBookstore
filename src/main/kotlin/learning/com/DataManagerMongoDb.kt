package learning.com

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import learning.com.Constants.MONGO_DB_CONNECTION_PROPERTY
import learning.com.Constants.DEFAULT_MONGO_DB_CONNECTION_STRING
import learning.com.Constants.MONGO_DB_NAME
import learning.com.models.Book
import learning.com.models.Cart
import learning.com.models.Session
import org.bson.BsonDocument
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.ObjectId

object DataManagerMongoDB {

    val database: MongoDatabase
    val bookCollection: MongoCollection<Book>
    val cartCollection: MongoCollection<Cart>

    init {
        val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                .automatic(true)
                .build()
        )
        val codecRegistry =
            CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry)

        val connectionString = System.getProperty(MONGO_DB_CONNECTION_PROPERTY.value)
            ?: DEFAULT_MONGO_DB_CONNECTION_STRING.value

        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .codecRegistry(codecRegistry)
            .build()

        val mongodbClient = MongoClients.create(clientSettings)
        database = mongodbClient.getDatabase(MONGO_DB_NAME.value)
        bookCollection = database.getCollection(Book::class.java.name, Book::class.java)
        cartCollection = database.getCollection(Cart::class.java.name, Cart::class.java)
        initBooks()
        initCarts()
    }

    private fun initCarts() {
        cartCollection.deleteMany(BsonDocument())
    }

    private fun initBooks() {
        deleteAllBooks()
        bookCollection.insertOne(Book(null, "The Shadow Conspiracy", "Elena Martinez", 15.99f))
        bookCollection.insertOne(Book(null, "Echoes of Eternity", "Marcus Larson", 12.50f))
        bookCollection.insertOne(Book(null, "Whispers in the Dark", "Samantha Chen", 18.75f))
        bookCollection.insertOne(Book(null, "The Forgotten Kingdom", "Gabriel Miller", 20.25f))
        bookCollection.insertOne(Book(null, "Midnight Serenade", "Olivia Thompson", 14.99f))
        bookCollection.insertOne(Book(null, "Underneath the Stars", "Nikolai Petrov", 16.50f))
        bookCollection.insertOne(Book(null, "The Art of Deception", "Isabella Smith", 22.00f))
        bookCollection.insertOne(Book(null, "Silent Echoes", "Dmitri Ivanov", 19.95f))
        bookCollection.insertOne(Book(null, "Crimson Sunset", "Hannah Johnson", 13.25f))
        bookCollection.insertOne(Book(null, "Lost in Translation", "Viktor Popescu", 17.50f))
        bookCollection.insertOne(Book(null, "Whispers of Fate", "Sophia Nguyen", 15.00f))
        bookCollection.insertOne(Book(null, "The Secret Garden", "Maximilian Schmidt", 10.99f))
        bookCollection.insertOne(Book(null, "Shadows of Destiny", "Anastasia Ivanova", 21.75f))
        bookCollection.insertOne(Book(null, "The Last Voyage", "Liam O'Malley", 23.50f))
        bookCollection.insertOne(Book(null, "Echoes of Silence", "Eva Petrova", 11.25f))
        bookCollection.insertOne(Book(null, "The Hidden Path", "Matei Popa", 24.99f))
        bookCollection.insertOne(Book(null, "Midnight Whispers", "Lara Kovacs", 16.75f))
        bookCollection.insertOne(Book(null, "Serenade in Blue", "Daniel Li", 13.99f))
        bookCollection.insertOne(Book(null, "The Enigma Code", "Natalia Dragomir", 18.25f))
        bookCollection.insertOne(Book(null, "The Silent Forest", "Anton Kovalev", 20.00f))
    }

    fun newBook(book: Book) {
        bookCollection.insertOne(book)
    }

    fun updateBook(book: Book): Book? {
        bookCollection.replaceOne(Filters.eq("_id", book.id), book)
        return bookCollection.find(Filters.eq("_id", book.id)).first()
    }

    fun deleteBook(id: String?): Book? {
        return bookCollection.findOneAndDelete(Filters.eq("_id", id))
    }

    fun deleteAllBooks() {
        bookCollection.deleteMany(Document())
    }

    fun allBooks(): List<Book> {
        return bookCollection.find().toList()
    }

    fun allBooks(sort: String, order: String, page: Int, pageSize: Int): List<Book> {
        return bookCollection.find()
            .sort(Document(mapOf(Pair(sort, if (order == "asc") 1 else -1), Pair("_id", 1))))
            .skip((page - 1) * pageSize)
            .limit(pageSize)
            .toList()
    }

    fun searchBooks(search: String): List<Book> {
        return bookCollection.find(
            Filters.or(
                Filters.regex("title", ".*$search.*", "i"),
                Filters.regex("author", ".*$search.*", "i")
            )
        ).toList()
    }

    fun findBookById(id: String): Book {
        return bookCollection.find(Filters.eq("_id", ObjectId(id))).first() ?: Book()
    }

    fun findCartBySession(session: Session?): Cart {
        requireNotNull(session) { "Session is null" }
        val cartFound = cartCollection.find(Filters.eq("username", session.username)).first()
        if (cartFound == null) {
            val newCart = Cart(null, session.username, 0, 0.0f, mutableListOf())
            cartCollection.insertOne(newCart)
            return newCart
        }
        return cartFound
    }

    fun updateCart(cart: Cart) {
        cartCollection.replaceOne(Filters.eq("username", cart.username), cart)
    }

    fun removeBookFromCart(session: Session?, book: Book) {
        val cart = findCartBySession(session)
        cart.removeBook(book)
        updateCart(cart)
    }

    fun addBook(session: Session?, book: Book) {
        val cart = findCartBySession(session)
        cart.addBook(book)
        updateCart(cart)
    }

    fun checkoutCart(cart: Cart) {
        cartCollection.deleteOne(Filters.eq("_id", cart.id))
    }

}

