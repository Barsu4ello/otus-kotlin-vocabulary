package ru.gorbunov.kotlin

fun main() {
    listOf<Int>().asSequence().filter { it > 0 }
}