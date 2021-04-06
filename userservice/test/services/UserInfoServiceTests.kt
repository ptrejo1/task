package services

import com.phoenix.userservice.api.NotFoundException
import com.phoenix.userservice.dtos.UserInfoCreateDTO
import com.phoenix.userservice.models.UserInfo
import com.phoenix.userservice.models.UserPrincipal
import com.phoenix.userservice.repositories.UserInfoRepository
import com.phoenix.userservice.services.UserInfoService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserInfoServiceTests {

    @Test fun testGetUserInfo() {
        val repo = mockk<UserInfoRepository>()
        val info = UserInfo(1, DateTime(), "abc", "foo", "email", "fn", "ln")
        coEvery { repo.getUserInfo("abc") } returns info

        val sut = UserInfoService(repo)
        val dto = runBlocking { sut.getUserInfo(UserPrincipal("abc", "foo")) }
        assertEquals("abc", dto.userId)
        assertEquals("foo", dto.username)
    }

    @Test fun testGetUserInfoNotFound() {
        val repo = mockk<UserInfoRepository>()
        coEvery { repo.getUserInfo("abc") } returns null

        val sut = UserInfoService(repo)
        assertFailsWith(NotFoundException::class) {
            runBlocking { sut.getUserInfo(UserPrincipal("abc", "foo")) }
        }
    }

    @Test fun testCreateUserInfo() {
        val repo = mockk<UserInfoRepository>()
        val user = UserPrincipal("foo", "bar")
        val dto = UserInfoCreateDTO("email", "fn", "ln")
        val info = UserInfo(1, DateTime(), "abc", "foo", "email", "fn", "ln")
        every { repo.createUserInfo(user, dto) } returns info

        val sut = UserInfoService(repo)
        val userInfoDTO = sut.createUserInfo(user, dto)
        assertEquals(info.userId, userInfoDTO.userId)
    }
}
