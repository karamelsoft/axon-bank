package org.karamelsoft.axon.bank.views.customer.dashboard.query.interfaces

import org.karamelsoft.axon.bank.services.customers.api.CustomerAddress
import org.karamelsoft.axon.bank.services.customers.api.CustomerDetails

fun CustomerDetails.toQueryAPI() = org.karamelsoft.axon.bank.views.customer.dashboard.api.CustomerDetails(
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate
)

fun CustomerAddress.toQueryAPI() = org.karamelsoft.axon.bank.views.customer.dashboard.api.CustomerAddress(
    street = street,
    number = number,
    postalCode = postalCode,
    city = city,
    country = country
)
