package com.example.quotes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*

fun runTestWithDispatcher(
    testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
    test: suspend TestScope.() -> Unit,
): TestResult {
    Dispatchers.setMain(testDispatcher)

    return kotlinx.coroutines.test.runTest {
        test()
        Dispatchers.resetMain()
    }
}