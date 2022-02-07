package com.example.android.politicalpreparedness

import org.junit.After
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

abstract class BaseTest : KoinTest {

    @After
    fun autoClose() {
        stopKoin()
    }
}
