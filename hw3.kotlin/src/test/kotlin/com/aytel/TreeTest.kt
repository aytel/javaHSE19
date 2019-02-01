package com.aytel

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class TreeTest {

    lateinit var tree: Tree<Int>

    @BeforeEach
    fun init() {
        tree = Tree<Int>({a, b -> a - b})
    }

    @Test
    fun `getSize$aytel_hw_main`() {
    }

    @Test
    fun `contains$aytel_hw_main`() {
    }

    @Test
    fun `iterator$aytel_hw_main`() {
    }

    @Test
    fun `descendingIterator$aytel_hw_main`() {
    }

    @Test
    fun `clear$aytel_hw_main`() {
    }

    @Test
    fun `add$aytel_hw_main`() {
    }

    @Test
    fun `remove$aytel_hw_main`() {
    }

    @Test
    fun `first$aytel_hw_main`() {
    }

    @Test
    fun `last$aytel_hw_main`() {
    }

    @Test
    fun `lower$aytel_hw_main`() {
    }

    @Test
    fun `higher$aytel_hw_main`() {
    }

    @Test
    fun `floor$aytel_hw_main`() {
    }

    @Test
    fun `ceiling$aytel_hw_main`() {
    }

    @Test
    fun getCompare() {
    }
}