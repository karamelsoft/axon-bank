package org.karamelsoft.research.axon.libraries.artifacts.module

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JsonSerializationAutoConfiguration {

    @Bean
    fun javaModule(): com.fasterxml.jackson.databind.Module = JavaTimeModule()

    @Bean
    fun kotlinModule(): com.fasterxml.jackson.databind.Module = KotlinModule.Builder().build()

}
