package org.karamelsoft.axon.demo.views.customer.dashboard.query.domain

import org.karamelsoft.axon.demo.services.accounts.api.AccountId
import org.karamelsoft.axon.demo.services.accounts.api.AccountOwner
import org.karamelsoft.axon.demo.services.cards.api.CardId
import org.karamelsoft.axon.demo.services.cards.api.CardOwner
import org.karamelsoft.axon.demo.services.customers.api.CustomerId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerDashboardRepository : MongoRepository<CustomerDashboard, UUID> {
    fun findByCustomerId(customerId: String): CustomerDashboard?
    fun findByAccountId(accountId: String): CustomerDashboard?
    fun findByCardId(cardId: String): CustomerDashboard?
}

fun CustomerDashboardRepository.findByCustomer(customerId: org.karamelsoft.axon.demo.views.customer.dashboard.api.CustomerId) = this.findByCustomerId(customerId.value.toString())
fun CustomerDashboardRepository.findByCustomer(customerId: CustomerId) = this.findByCustomerId(customerId.value.toString())
fun CustomerDashboardRepository.findByOwner(accountOwner: AccountOwner) = this.findByCustomerId(accountOwner.reference)
fun CustomerDashboardRepository.findByOwner(cardOwner: CardOwner) = this.findByCustomerId(cardOwner.reference)
fun CustomerDashboardRepository.findByAccount(accountId: AccountId) = this.findByAccountId(accountId.number)
fun CustomerDashboardRepository.findByCard(cardId: CardId) = this.findByCardId(cardId.number)

