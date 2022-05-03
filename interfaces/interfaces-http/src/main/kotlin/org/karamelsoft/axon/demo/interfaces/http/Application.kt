package org.karamelsoft.axon.demo.interfaces.http

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class HttpInterfaceApplication

fun main(args: Array<String>) {
    runApplication<HttpInterfaceApplication>(*args)
}
