package org.karamelsoft.axon.demo.libraries.service.command

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.axonframework.serialization.json.JacksonSerializer
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

val serializer: JacksonSerializer = JacksonSerializer.builder()
    .objectMapper(
        ObjectMapper().registerModules(
            JavaTimeModule(),
            KotlinModule.Builder().build()
        )
    )
    .build()

fun <T> serialize(value: T): T =
    serializer.deserialize(
        serializer.serialize(value, String::class.java)
    )

internal class ConstraintTest {

    private val fixture = AggregateTestFixture(Constraint::class.java)

    @BeforeEach
    fun beforeEach() {
        fixture.setReportIllegalStateChange(false)
    }

    @Test
    fun `claiming a constraint should return ok status`() {
        val constraintId = UUID.randomUUID().toString()
        val now = Instant.now()

        fixture.given()
            .`when`(serialize(ClaimConstraint(constraintId, now)))
            .expectEvents(serialize(ConstraintClaimed(constraintId, now)))
    }

    @Test
    fun `validate a constraint should return ok status`() {
        val constraintId = UUID.randomUUID().toString()
        val now = Instant.now()

        fixture.given()
            .`when`(serialize(ValidateConstraint(constraintId, now)))
            .expectEvents(serialize(ConstraintValidated(constraintId, now)))
    }
}
