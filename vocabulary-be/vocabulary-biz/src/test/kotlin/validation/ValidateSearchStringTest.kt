package ru.gorbunov.vocabulary.biz.validation

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.models.VcblWordFilter
import ru.gorbunov.vocabulary.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringTest {

    @Test
    fun emptyString() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordFilterValidating = VcblWordFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(VcblState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordFilterValidating = VcblWordFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(VcblState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordFilterValidating = VcblWordFilter(searchString = "c"))
        chain.exec(ctx)
        assertEquals(VcblState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordFilterValidating = VcblWordFilter(searchString = "cat"))
        chain.exec(ctx)
        assertEquals(VcblState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordFilterValidating = VcblWordFilter(searchString = "ca".repeat(51)))
        chain.exec(ctx)
        assertEquals(VcblState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    @Test
    fun combineDifferentLanguages() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordFilterValidating = VcblWordFilter(searchString = "catкот"))
        chain.exec(ctx)
        assertEquals(VcblState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-combineDifferentLanguages", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}