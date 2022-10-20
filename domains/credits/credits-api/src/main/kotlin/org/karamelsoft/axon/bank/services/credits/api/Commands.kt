package org.karamelsoft.axon.bank.services.credits.api

import java.time.Instant

sealed interface CreditLineCommand {
    val creditLineId: CreditLineId
    val timestamp: Instant
}