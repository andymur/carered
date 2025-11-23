package com.andymur.carered

import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class CareredApplication

fun main(args: Array<String>) {
    org.springframework.boot.runApplication<CareredApplication>(*args)
}
