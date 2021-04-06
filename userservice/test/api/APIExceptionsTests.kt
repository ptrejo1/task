package api

import com.phoenix.userservice.api.APIException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class APIExceptionsTests {

    @Test fun testSerialize() {
        val sut = APIException(HttpStatusCode.InternalServerError, "Server error")
        val excJson = sut.serialize()
        assertEquals(500, excJson.status)
        assertEquals("Server error", excJson.message)

        val json = jacksonObjectMapper().writeValueAsString(excJson)
        assertEquals("{\"status\":500,\"message\":\"Server error\"}", json)
    }
}
