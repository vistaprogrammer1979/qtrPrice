package com.santechture.service;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
@Slf4j
@ApplicationScoped
public class KafkaTopicInitializer {
    @ConfigProperty(name = "kafka.bootstrap.servers")
    String bootstrapServers;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-responses.topic")
    String price_engine_responses_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-failure-responses.topic")
    String price_engine_failure_responses_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-logs.topic")
    String price_engine_logs_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-facts.topic")
    String price_engine_facts_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-responses-qatar.topic")
    String price_engine_responses_qatar_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-failure-responses-qatar.topic")
    String price_failure_responses_qatar_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-logs-qatar.topic")
    String price_engine_logs_qatar_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-facts-qatar.topic")
    String price_engine_facts_qatar_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-cache-management.topic")
    String price_cache_management_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-cache-management-qatar.topic")
    String price_cache_management_qatar_topic;
    @ConfigProperty(name = "price.cloud.kafka.partition")
    Integer partition;
    @ConfigProperty(name="price.cloud.kafka.replica")
    Short replica;
//            KSA Topics
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-responses-ksa.topic")
    String price_engine_responses_ksa_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-cache-management-ksa.topic")
    String price_cache_management_ksa_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-facts-ksa.topic")
    String price_engine_facts_ksa_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-logs-ksa.topic")
    String price_engine_logs_ksa_topic;
    @ConfigProperty(name= "mp.messaging.incoming.price-engine-failure-responses-ksa.topic")
    String price_failure_responses_ksa_topic;


    @Startup
    void init() {
        log.info("Initializing Kafka topic consumer");
        try (AdminClient adminClient = AdminClient.create(Collections.singletonMap("bootstrap.servers", bootstrapServers))) {
            createTopicIfNotExists(adminClient, price_engine_responses_topic, partition, replica);
            createTopicIfNotExists(adminClient, price_engine_failure_responses_topic, partition, replica);
            createTopicIfNotExists(adminClient, price_engine_logs_topic, partition, replica);
            createTopicIfNotExists(adminClient, price_engine_facts_topic, partition, replica);
            createTopicIfNotExists(adminClient, price_engine_responses_qatar_topic, partition, replica);
            createTopicIfNotExists(adminClient, price_failure_responses_qatar_topic, partition, replica);
            createTopicIfNotExists(adminClient, price_engine_logs_qatar_topic, partition, replica);
            createTopicIfNotExists(adminClient, price_engine_facts_qatar_topic, partition,  replica);
            createTopicIfNotExists(adminClient, price_cache_management_topic, partition,  replica);
            createTopicIfNotExists(adminClient, price_cache_management_qatar_topic, partition,  replica);
//            KSA
            createTopicIfNotExists(adminClient,price_engine_responses_ksa_topic, partition,replica);
            createTopicIfNotExists(adminClient,price_cache_management_ksa_topic, partition,replica);
            createTopicIfNotExists(adminClient,price_engine_facts_ksa_topic, partition,replica);
            createTopicIfNotExists(adminClient,price_engine_logs_ksa_topic, partition,replica);
            createTopicIfNotExists(adminClient,price_failure_responses_ksa_topic, partition,replica);

        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).toList().forEach(s ->log.error(s.toString()));

        }
    }

    private void createTopicIfNotExists(AdminClient adminClient, String topicName, int partitions, short replicationFactor) {
        try {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            Set<String> existingTopics = listTopicsResult.names().get();

            if (!existingTopics.contains(topicName)) {
                NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
                var res = adminClient.createTopics(Collections.singleton(newTopic));
                log.info("Created topic: {}, {}", topicName, res.values());
            } else {
                log.info("Topic already exists: {}" , topicName);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while creating topic {}", topicName, e);
            Arrays.stream(e.getStackTrace()).toList().forEach(ee ->log.error(ee.toString()));
        }
    }
}

