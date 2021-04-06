package interactors

import com.auth0.jwt.JWT
import com.phoenix.authservice.interactors.JwtFactory
import com.phoenix.authservice.models.User
import org.joda.time.DateTime
import java.util.*
import kotlin.test.*

class JwtFactoryTests {

    @Test fun testCreateToken() {
        val user = User(1, DateTime(), "abc", "foo", "bar")
        val created = Date()
        val token = JwtFactory.createToken(user)
        val decoded = JWT.decode(token)

        assertEquals("phoenix.io", decoded.issuer)
        assertEquals("userinfoservice", decoded.audience.first())
        assertEquals(user.userId, decoded.subject)
        assertEquals(user.username, decoded.getClaim("username").asString())
        assertEquals("RS256", decoded.algorithm)

        val expires = decoded.expiresAt
        val after = Date(created.time + (1000 * 60 * 5) - 1000)
        val before = Date(created.time + (1000 * 60 * 5) + 500)
        assertTrue(expires.before(before) && expires.after(after))
    }
}
