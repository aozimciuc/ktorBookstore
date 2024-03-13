package learning.com

import kotlin.reflect.full.declaredMemberProperties

class DataManager {

    var books = ArrayList<Book>()

    fun generateId(): String {
        return books.size.toString()
    }

    init {
        books.add(Book(generateId(), "The Shadow Conspiracy", "Elena Martinez", 15.99f))
        books.add(Book(generateId(), "Echoes of Eternity", "Marcus Larson", 12.50f))
        books.add(Book(generateId(), "Whispers in the Dark", "Samantha Chen", 18.75f))
        books.add(Book(generateId(), "The Forgotten Kingdom", "Gabriel Miller", 20.25f))
        books.add(Book(generateId(), "Midnight Serenade", "Olivia Thompson", 14.99f))
        books.add(Book(generateId(), "Underneath the Stars", "Nikolai Petrov", 16.50f))
        books.add(Book(generateId(), "The Art of Deception", "Isabella Smith", 22.00f))
        books.add(Book(generateId(), "Silent Echoes", "Dmitri Ivanov", 19.95f))
        books.add(Book(generateId(), "Crimson Sunset", "Hannah Johnson", 13.25f))
        books.add(Book(generateId(), "Lost in Translation", "Viktor Popescu", 17.50f))
        books.add(Book(generateId(), "Whispers of Fate", "Sophia Nguyen", 15.00f))
        books.add(Book(generateId(), "The Secret Garden", "Maximilian Schmidt", 10.99f))
        books.add(Book(generateId(), "Shadows of Destiny", "Anastasia Ivanova", 21.75f))
        books.add(Book(generateId(), "The Last Voyage", "Liam O'Malley", 23.50f))
        books.add(Book(generateId(), "Echoes of Silence", "Eva Petrova", 11.25f))
        books.add(Book(generateId(), "The Hidden Path", "Matei Popa", 24.99f))
        books.add(Book(generateId(), "Midnight Whispers", "Lara Kovacs", 16.75f))
        books.add(Book(generateId(), "Serenade in Blue", "Daniel Li", 13.99f))
        books.add(Book(generateId(), "The Enigma Code", "Natalia Dragomir", 18.25f))
        books.add(Book(generateId(), "The Silent Forest", "Anton Kovalev", 20.00f))
    }

    fun newBook(book: Book) {
        books.add(book)
    }

    fun updateBook(book: Book): Book? {
        val found = books.find { it.id == book.id }
        found?.title = book.title
        found?.author = book.author
        found?.price = book.price
        return found
    }

    fun deleteBook(book: Book): Book {
        val found = books.find { it.id == book.id }
        books.remove(found)
        return found!!
    }

    fun deleteBook(id: String?): Book {
        val found = books.find { it.id == id }
        books.remove(found)
        return found!!
    }

    fun allBooks(): ArrayList<Book> {
        return books
    }

    // Returns a list of books sorted by the given sort and order
    // sort field is detected by the reflection
    fun allBooks(sort: String, order: String): List<Book> {
        val field = Book::class.declaredMemberProperties.find { it.name == sort }
        val sorted = allBooks().sortedBy { field?.get(it).toString() }
        return if (order == "asc") sorted else sorted.reversed()
    }

}

