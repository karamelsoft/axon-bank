package org.karamelsoft.axon.demo.interfaces.http.cards

import org.junit.jupiter.api.Test
import org.karamelsoft.axon.demo.libraries.service.test.serialize
import org.karamelsoft.axon.demo.services.cards.api.CardPinCode
import java.time.Instant

class ModelTest {

    private val pinCode = "1234"
    private val newPinCode = "2341"
    private val now = Instant.now()

    @Test
    fun `test model serialization`() {
        listOf(
            CardSetup("1234", now),
            CardPinChange(pinCode, newPinCode, now),
            CardPinValidation(pinCode, now),
        ).forEach { serialize(it) }
    }
}
