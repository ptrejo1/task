package services

import com.auth0.jwt.JWT
import com.phoenix.authservice.api.BadRequestException
import com.phoenix.authservice.api.UnauthorizedException
import com.phoenix.authservice.dtos.UserDTO
import com.phoenix.authservice.models.User
import com.phoenix.authservice.repositories.UserRepository
import com.phoenix.authservice.services.AuthService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthServiceTests {

    @Test fun testSignUp() {
        val userRepo = mockk<UserRepository>()
        val password = "bar12345"
        val user = User(1, DateTime(), "123", "foo", password)
        coEvery { userRepo.createUser("foo", any()) } returns user

        val sut = AuthService(userRepo)
        val token = runBlocking { sut.signUp(UserDTO("foo", password)) }
        val decoded = JWT.decode(token.token)
        assertEquals(user.userId, decoded.subject)
        assertEquals(user.username, decoded.getClaim("username").asString())
    }

    @Test fun testSignUpInvalidPassword() {
        val sut = AuthService()
        assertFailsWith(BadRequestException::class) {
            runBlocking { sut.signUp(UserDTO("foo", "bar")) }
        }
    }

    @Test fun testLogin() {
        val userRepo = mockk<UserRepository>()
        val hashed = BCrypt.hashpw("bar", BCrypt.gensalt())
        val user = User(1, DateTime(), "123", "foo", hashed)
        coEvery { userRepo.getUser("foo") } returns user

        val sut = AuthService(userRepo)
        val token = runBlocking { sut.login(UserDTO("foo", "bar")) }
        val decoded = JWT.decode(token.token)
        assertEquals(user.userId, decoded.subject)
        assertEquals(user.username, decoded.getClaim("username").asString())
    }

    @Test fun testLoginNotFound() {
        val userRepo = mockk<UserRepository>()
        coEvery { userRepo.getUser("foo") } returns null

        val sut = AuthService(userRepo)
        assertFailsWith(UnauthorizedException::class) {
            runBlocking { sut.login(UserDTO("foo", "bar")) }
        }
    }

    @Test fun testLoginInvalidCredentials() {
        val userRepo = mockk<UserRepository>()
        val hashed = BCrypt.hashpw("bar", BCrypt.gensalt())
        val user = User(1, DateTime(), "123", "foo", hashed)
        coEvery { userRepo.getUser("foo") } returns user

        val sut = AuthService(userRepo)
        assertFailsWith(UnauthorizedException::class) {
            runBlocking { sut.login(UserDTO("foo", "baz")) }
        }
    }
}
