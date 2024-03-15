package learning.com

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import learning.com.models.Book
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider

class DataManagerMongoDB {

    val database: MongoDatabase
    val bookCollection: MongoCollection<Book>

    init {
        val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                .automatic(true)
                .build()
        )
        val codecRegistry =
            CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry)
        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString("mongodb://localhost:27017"))
            .codecRegistry(codecRegistry)
            .build()
        val mongodbClient = MongoClients.create(clientSettings)
        database = mongodbClient.getDatabase("bookstore")
        bookCollection = database.getCollection(Book::class.java.name, Book::class.java)
        initBooks()
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

    fun deleteBook(book: Book): Book? {
        return bookCollection.findOneAndDelete(Filters.eq("_id", book.id))
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

    // Returns a list of books sorted by the given sort and order
    fun allBooks(sort: String, order: String, page: Int, pageSize: Int): List<Book> {
        return bookCollection.find()
            .sort(Document(mapOf(Pair(sort, if (order == "asc") 1 else -1), Pair("_id", 1))))
            .skip((page - 1) * pageSize)
            .limit(pageSize)
            .toList()
    }

}

