package repositories

import com.phoenix.authservice.config.DatabaseFactory
import com.phoenix.authservice.models.Users
import com.phoenix.authservice.repositories.UserRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import kotlin.test.*

class UserRepositoryImplTests {

    companion object {

        @BeforeClass
        @JvmStatic fun setup() {
            DatabaseFactory.init()
        }
    }

    private var repository: UserRepositoryImpl? = null

    @BeforeTest fun beforeEach() {
        repository = UserRepositoryImpl()
        transaction { Users.deleteAll() }
    }

    @AfterTest fun afterEach() {
        repository = null
    }

    @Test fun testCreateUser() {
        val user = runBlocking { repository!!.createUser("foo", "bar") }
        assertEquals("foo", user.username)
        assertEquals("bar", user.password)

        assertFailsWith(RuntimeException::class) {
            runBlocking { repository!!.createUser("foo", "bar") }
        }
    }

    @Test fun testGetUser() = runBlocking {
        val created = repository!!.createUser("foo", "bar")
        val user = repository!!.getUser(created.username)
        assertEquals(created.userId, user!!.userId)
        assertEquals(created.password, user.password)
    }

    @Test fun testGetUserNotFound() = runBlocking {
        val user = repository!!.getUser("foo")
        assertNull(user)
    }
}
