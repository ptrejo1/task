package com.phoenix.authservice.config

import com.phoenix.authservice.services.AuthService
import org.koin.core.module.Module
import org.koin.dsl.module

fun serviceModule(): Module {
    return module {
        single { AuthService() }
    }
}
