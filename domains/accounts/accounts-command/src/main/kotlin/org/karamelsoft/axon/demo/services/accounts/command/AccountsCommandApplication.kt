package org.karamelsoft.axon.demo.services.accounts.command

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class AccountsCommandApplication

fun main(args: Array<String>) {
    runApplication<AccountsCommandApplication>(*args)
}
