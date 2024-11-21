package com.task.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

@Configuration
public class KafkaAdminConfig {
    @Autowired
    private KafkaProperties properties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaAdmin.NewTopics newTopics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("taskCreate-topic").partitions(2).build(),
                TopicBuilder.name("taskDelete-topic").partitions(2).build(),
                TopicBuilder.name("taskUpdate-topic").partitions(2).build(),
                TopicBuilder.name("userCreate-topic").partitions(2).build(),
                TopicBuilder.name("userDelete-topic").partitions(2).build(),
                TopicBuilder.name("userUpdate-topic").partitions(2).build()
        );
    }
}