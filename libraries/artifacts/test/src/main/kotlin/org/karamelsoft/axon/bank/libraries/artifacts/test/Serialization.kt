package org.karamelsoft.axon.bank.libraries.artifacts.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.axonframework.serialization.json.JacksonSerializer

private val serializer: JacksonSerializer = JacksonSerializer.builder()
    .objectMapper(
        ObjectMapper().registerModules(
            JavaTimeModule(),
            KotlinModule.Builder().build()
        )
    )
    .build()

fun <T> serialize(value: T): T = serializer.deserialize(
    serializer.serialize(value, String::class.java)
)
