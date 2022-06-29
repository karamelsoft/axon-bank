package org.karamelsoft.research.axon.libraries.artifacts.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class StatusSerializerTest {
    private val objectMapper = ObjectMapper().registerModules(
        JavaTimeModule(),
        KotlinModule.Builder().build()
    )

    @Test
    fun `ok status should be serializable`() {
        val status = Status.of { TestData("tag", "ok") }
        val marshalling = objectMapper.writeValueAsString(status)
        val newStatus = objectMapper.readValue(marshalling, Ok::class.java)

        Assertions.assertThat(newStatus).isEqualTo(status)
    }
}

data class TestData(
    val name: String,
    val value: String
)
