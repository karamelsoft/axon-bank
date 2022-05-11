package org.karamelsoft.axon.demo.libraries.service.command

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [CommandAutoConfiguration::class])
class CommandAutoConfiguration
