package org.karamelsoft.axon.demo.views.customer.dashboard

import org.axonframework.eventhandling.EventHandler
import org.karamelsoft.axon.demo.services.accounts.api.AccountClosed
import org.karamelsoft.axon.demo.services.accounts.api.AmountDeposited
import org.karamelsoft.axon.demo.services.accounts.api.AmountWithdrew
import org.karamelsoft.axon.demo.services.accounts.api.NewAccountRegistered
import org.karamelsoft.axon.demo.services.cards.api.CardBlocked
import org.karamelsoft.axon.demo.services.cards.api.NewCardRegistered
import org.karamelsoft.axon.demo.services.customers.api.CustomerAddressCorrected
import org.karamelsoft.axon.demo.services.customers.api.CustomerDetailsCorrected
import org.karamelsoft.axon.demo.services.customers.api.NewCustomerRegistered
import org.karamelsoft.axon.demo.views.customer.dashboard.domain.*
import org.springframework.stereotype.Component

@Component
class CustomerEventHandler(private val customerDashboardRepository: CustomerDashboardRepository) {

    @EventHandler
    fun on(event: NewCustomerRegistered) {
        customerDashboardRepository.findByCustomer(event.customerId)
            ?: CustomerDashboard(customerId = event.customerId.value.toString())
                .registerCustomer(
                    event.details,
                    event.address,
                    event.timestamp
                ).let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: CustomerDetailsCorrected) {
        customerDashboardRepository.findByCustomer(event.customerId)
            ?.correctCustomerDetails(event.newDetails, event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: CustomerAddressCorrected) {
        customerDashboardRepository.findByCustomer(event.customerId)
            ?.correctCustomerAddress(event.newAddress, event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: NewAccountRegistered) {
        customerDashboardRepository.findByOwner(event.owner)
            ?.registerAccount(event.accountId, event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: AmountDeposited) {
        customerDashboardRepository.findByAccount(event.accountId)
            ?.depositAmount(event.amount, event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: AmountWithdrew) {
        customerDashboardRepository.findByAccount(event.accountId)
            ?.withdrawAmount(event.amount, event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: AccountClosed) {
        customerDashboardRepository.findByAccount(event.accountId)
            ?.closeAccount(event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: NewCardRegistered) {
        customerDashboardRepository.findByOwner(event.owner)
            ?.registerCard(event.cardId, event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: CardBlocked) {
        customerDashboardRepository.findByCard(event.cardId)
            ?.blockCard(event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
    }

}
