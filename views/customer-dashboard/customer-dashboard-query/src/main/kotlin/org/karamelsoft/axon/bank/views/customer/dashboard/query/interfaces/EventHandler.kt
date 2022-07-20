package org.karamelsoft.axon.bank.views.customer.dashboard.query.interfaces

import org.axonframework.eventhandling.EventHandler
import org.karamelsoft.axon.bank.services.accounts.api.AccountClosed
import org.karamelsoft.axon.bank.services.accounts.api.AmountDeposited
import org.karamelsoft.axon.bank.services.accounts.api.AmountWithdrew
import org.karamelsoft.axon.bank.services.accounts.api.NewAccountOpened
import org.karamelsoft.axon.bank.services.cards.api.CardBlocked
import org.karamelsoft.axon.bank.services.cards.api.CardCreated
import org.karamelsoft.axon.bank.services.customers.api.CustomerAddressCorrected
import org.karamelsoft.axon.bank.services.customers.api.CustomerDetailsCorrected
import org.karamelsoft.axon.bank.services.customers.api.NewCustomerRegistered
import org.karamelsoft.axon.bank.views.customer.dashboard.query.domain.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CustomerDashboardEventHandler(private val customerDashboardRepository: CustomerDashboardRepository) {

    private val logger = LoggerFactory.getLogger(CustomerDashboardEventHandler::class.java)

    @EventHandler
    fun on(event: NewCustomerRegistered) {
        logger.trace("Received NewCustomerRegistered for customer id: ${event.customerId}")
        customerDashboardRepository.findByCustomer(event.customerId)
            ?.let { logger.warn("Found already existing customer dashboard for customer id: ${event.customerId}") }
            ?: CustomerDashboard(customerId = event.customerId.value.toString())
                .registerCustomer(
                    event.details.toQueryAPI(),
                    event.address.toQueryAPI(),
                    event.timestamp
                ).let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: CustomerDetailsCorrected) {
        logger.trace("Received CustomerDetailsCorrected for customer id: ${event.customerId}")
        customerDashboardRepository.findByCustomer(event.customerId)
            ?.correctCustomerDetails(event.newDetails.toQueryAPI(), event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
            ?: throw IllegalStateException("Could not find customer dashboard for customer id: ${event.customerId}")
    }

    @EventHandler
    fun on(event: CustomerAddressCorrected) {
        logger.trace("Received CustomerAddressCorrected for customer id: ${event.customerId}")
        customerDashboardRepository.findByCustomer(event.customerId)
            ?.correctCustomerAddress(event.newAddress.toQueryAPI(), event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
            ?: throw IllegalStateException("Could not find customer dashboard for customer id: ${event.customerId}")
    }

    @EventHandler
    fun on(event: NewAccountOpened) {
        logger.trace("Received NewAccountRegistered for account id: ${event.accountId} and owner: ${event.owner}")
        (customerDashboardRepository.findByOwner(event.owner)
            ?: throw IllegalStateException("Could not find customer dashboard for account owner: ${event.owner}"))
            .registerAccount(event.accountId, event.timestamp)
            .let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: AmountDeposited) {
        logger.trace("Received AmountDeposited for account id: ${event.accountId}")
        customerDashboardRepository.findByAccount(event.accountId)
            ?.depositAmount(event.amount, event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
            ?: throw IllegalStateException("Could not find customer dashboard for account id: ${event.accountId}")
    }

    @EventHandler
    fun on(event: AmountWithdrew) {
        logger.trace("Received AmountWithdrew for account id: ${event.accountId}")
        customerDashboardRepository.findByAccount(event.accountId)
            ?.withdrawAmount(event.amount, event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
            ?: throw IllegalStateException("Could not find customer dashboard for account id: ${event.accountId}")
    }

    @EventHandler
    fun on(event: AccountClosed) {
        logger.trace("Received AccountClosed for account id: ${event.accountId}")
        customerDashboardRepository.findByAccount(event.accountId)
            ?.closeAccount(event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
            ?: throw IllegalStateException("Could not find customer dashboard for account id: ${event.accountId}")
    }

    @EventHandler
    fun on(event: CardCreated) {
        logger.trace("Received NewCardRegistered for card id: ${event.cardId} and owner: ${event.owner}")
        (customerDashboardRepository.findByOwner(event.owner)
            ?: throw IllegalStateException("Could not find customer dashboard for card owner: ${event.owner}"))
            .registerCard(event.cardId, event.timestamp)
            .let { customerDashboardRepository.save(it) }
    }

    @EventHandler
    fun on(event: CardBlocked) {
        customerDashboardRepository.findByCard(event.cardId)
            ?.blockCard(event.timestamp)
            ?.let { customerDashboardRepository.save(it) }
            ?: throw IllegalStateException("Could not find customer dashboard for card id: ${event.cardId}")
    }

}
