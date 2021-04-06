package com.phoenix.userservice.config

import com.phoenix.userservice.services.UserInfoService
import org.koin.core.module.Module
import org.koin.dsl.module

fun serviceModule(): Module {
    return module {
        single { UserInfoService() }
    }
}
