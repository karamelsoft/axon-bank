package org.karamelsoft.axon.bank.views.customer.dashboard.api

data class GetCustomerDashboard(val customerId: CustomerId)
data class CustomerDashboardResponse(val customerDashboard: CustomerDashboard?)
