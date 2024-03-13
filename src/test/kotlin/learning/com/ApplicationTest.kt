package learning.com

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import learning.com.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureTemplating()
            configureSecurity()
            configureMonitoring()
            configureHTTP()
            configureSerialization()
            configureRouting()
            configureStatusPages()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
