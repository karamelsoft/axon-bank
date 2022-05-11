package org.karamelsoft.research.axon.libraries.service.rest

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [RestExceptionHandler::class])
class RestAutoConfiguration
