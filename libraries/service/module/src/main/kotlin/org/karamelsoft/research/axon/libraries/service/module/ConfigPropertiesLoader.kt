package org.karamelsoft.research.axon.libraries.service.module

import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePropertySource

const val CONFIG_PROPERTIES_RESOURCE_PATTERN: String = "classpath*:config/*.properties"
const val CONFIG_YML_RESOURCE_PATTERN: String = "classpath*:config/*.yml"

class ConfigPropertiesLoader : EnvironmentPostProcessor {

    private val ymlLoader = YamlPropertySourceLoader()

    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        val resolver = PathMatchingResourcePatternResolver()

        resolver.getResources(CONFIG_PROPERTIES_RESOURCE_PATTERN).forEach { resource ->
            environment.propertySources.addLast(ResourcePropertySource(resource))
        }

        resolver.getResources(CONFIG_YML_RESOURCE_PATTERN).forEach { resource ->
            ymlLoader.load("custom-config", resource).forEach { propertySource ->
                environment.propertySources.addLast(propertySource)
            }
        }
    }
}
