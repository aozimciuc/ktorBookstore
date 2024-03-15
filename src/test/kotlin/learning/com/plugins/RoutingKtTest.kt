package learning.com.plugins

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class RoutingKtTest {

    @Test
    fun `assert that 1 equals 1`() {
        assertEquals(1, 1)
    }

    @Test
    fun `should test root endpoint`() = testApplication {
        application {
            configureLocations()
            configureSecurity()
            configureRouting()
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello World!", response.bodyAsText())
    }

    @Test
    fun `should test client mock`() {
        runBlocking {
            val client = HttpClient(MockEngine) {
                engine {
                    addHandler { request ->
                        when (request.url.fullPath) {
                            "/" -> respond(
                                content = "Hello World!",
                                status = HttpStatusCode.NotModified,
                                headers = headersOf("X-MyHeader", "MyValue")
                            )

                            else -> respond(
                                content = "Unhandled ${request.url.encodedPath}",
                                status = HttpStatusCode.NotFound
                            )
                        }
                    }
                }
                expectSuccess = false
                followRedirects = false
            }
            assertEquals("Hello World!", client.get("/").bodyAsText())
            assertEquals("Unhandled /other", client.get("/other").bodyAsText())
        }
    }


}