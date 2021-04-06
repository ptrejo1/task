package repositories

import com.phoenix.userservice.config.DatabaseFactory
import com.phoenix.userservice.dtos.UserInfoCreateDTO
import com.phoenix.userservice.models.UserPrincipal
import com.phoenix.userservice.models.UsersInfo
import com.phoenix.userservice.repositories.UserInfoRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import kotlin.test.*

class UserInfoRepositoryImplTests {

    companion object {

        @BeforeClass
        @JvmStatic fun setup() {
            DatabaseFactory.init()
        }
    }

    private var repository: UserInfoRepositoryImpl? = null

    @BeforeTest fun beforeEach() {
        repository = UserInfoRepositoryImpl()
        transaction { UsersInfo.deleteAll() }
    }

    @AfterTest fun afterEach() {
        repository = null
    }

    @Test fun testCreateUserInfo() {
        val user = UserPrincipal("foo", "bar")
        val dto = UserInfoCreateDTO("email", "fn", "ln")
        val info = repository!!.createUserInfo(user, dto)
        assertEquals("email", info.email)

        assertFailsWith(RuntimeException::class) {
            repository!!.createUserInfo(user, dto)
        }
    }

    @Test fun testGetUserInfo() {
        val user = UserPrincipal("foo", "bar")
        val dto = UserInfoCreateDTO("email", "fn", "ln")
        val created = repository!!.createUserInfo(user, dto)
        val info = runBlocking { repository!!.getUserInfo(created.userId) }

        assertEquals(created.userId, info!!.userId)
        assertEquals(created.email, info.email)
    }

    @Test fun testGetUserInfoNotFound() = runBlocking {
        val info = repository!!.getUserInfo("foo")
        assertNull(info)
    }
}
