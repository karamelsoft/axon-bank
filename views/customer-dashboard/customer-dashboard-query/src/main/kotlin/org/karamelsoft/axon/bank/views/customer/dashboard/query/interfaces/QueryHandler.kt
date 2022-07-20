package org.karamelsoft.axon.bank.views.customer.dashboard.query.interfaces

import org.axonframework.queryhandling.QueryHandler
import org.karamelsoft.axon.bank.views.customer.dashboard.api.CustomerDashboardResponse
import org.karamelsoft.axon.bank.views.customer.dashboard.api.GetCustomerDashboard
import org.karamelsoft.axon.bank.views.customer.dashboard.query.domain.CustomerDashboardRepository
import org.karamelsoft.axon.bank.views.customer.dashboard.query.domain.findByCustomer
import org.springframework.stereotype.Component

@Component
class CustomerDashboardQueryHandler(private val repository: CustomerDashboardRepository) {

    @QueryHandler
    fun handle(query: GetCustomerDashboard) = CustomerDashboardResponse(
        customerDashboard = repository.findByCustomer(query.customerId)?.toQueryAPI()
    )
}
