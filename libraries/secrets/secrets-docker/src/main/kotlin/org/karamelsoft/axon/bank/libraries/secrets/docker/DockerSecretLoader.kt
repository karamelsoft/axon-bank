package org.karamelsoft.axon.bank.libraries.secrets.docker

import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import java.io.File
import java.nio.file.Paths

const val DOCKER_SECRET_FOLDER = "/run/secrets"

@Order(Ordered.HIGHEST_PRECEDENCE)
internal class DockerSecretLoader : EnvironmentPostProcessor {

    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        fun loadSecrets() = secretFiles().associate { file ->
            val secretName = file.name
            val secretValue = file.readLines().first()

            Pair(secretName, secretValue)
        }

        environment.propertySources.addLast(
            MapPropertySource("docker", loadSecrets())
        )
    }

    private fun secretFiles(): List<File> {
        return Paths.get(DOCKER_SECRET_FOLDER).toFile().listFiles()
            ?.toList()
            ?: listOf()
    }
}
