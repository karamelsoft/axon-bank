package org.karamelsoft.research.axon.libraries.mongo

import com.mongodb.client.MongoClient
import org.axonframework.config.Configurer
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.extensions.mongo.DefaultMongoTemplate
import org.axonframework.extensions.mongo.MongoTemplate
import org.axonframework.extensions.mongo.eventhandling.saga.repository.MongoSagaStore
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore
import org.axonframework.modelling.saga.repository.SagaStore
import org.axonframework.serialization.Serializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

private const val AXON_SAGAS = "axon_sagas"
private const val AXON_TOKENS = "axon_tokens"

@Configuration
class MongoAutoConfiguration {

    private val logger: Logger = LoggerFactory.getLogger(MongoAutoConfiguration::class.java)

    @Autowired
    fun configuration(configurer: Configurer, mongoClient: MongoClient, serializer: Serializer) {
        val mongoTemplate = DefaultMongoTemplate.builder()
            .mongoDatabase(mongoClient)
            .sagasCollectionName(AXON_SAGAS)
            .trackingTokensCollectionName(AXON_TOKENS)
            .build()

        configurer
            .eventProcessing { eventProcessingConfigurer ->
                eventProcessingConfigurer
                    .registerSagaStore { conf -> mongoSagaStore(mongoTemplate, conf.serializer()) }
                    .registerTokenStore { conf -> mongoTokenStore(mongoTemplate, conf.serializer()) }
            }
    }

    fun mongoTokenStore(mongoTemplate: MongoTemplate, serializer: Serializer): TokenStore {
        logger.info("Creating mongodb token store")
        return MongoTokenStore.builder()
            .mongoTemplate(mongoTemplate)
            .serializer(serializer)
            .build()
    }

    fun mongoSagaStore(mongoTemplate: MongoTemplate, serializer: Serializer): SagaStore<Any> {
        logger.info("Creating mongodb saga store")
        return MongoSagaStore.builder()
            .mongoTemplate(mongoTemplate)
            .serializer(serializer)
            .build()
    }
}
