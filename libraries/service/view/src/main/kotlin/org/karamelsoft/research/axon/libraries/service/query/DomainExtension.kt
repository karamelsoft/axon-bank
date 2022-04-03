package org.karamelsoft.research.axon.libraries.service.query

fun <T> T?.required(name: String) = this ?: throw NoSuchElementException("missing required element $name")