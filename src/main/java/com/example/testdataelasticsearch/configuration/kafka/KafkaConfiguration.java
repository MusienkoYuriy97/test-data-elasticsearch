package com.example.testdataelasticsearch.configuration.kafka;

import com.example.testdataelasticsearch.service.kafka.CourseConsumerService;
import com.example.testdataelasticsearch.service.pojobased.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {
    private final CourseService courseService;

    @Value("${kafka.topic.name.course}")
    public String courseTopicName;

    @Bean
    public CourseConsumerService courseConsumerService() {
        return new CourseConsumerService(courseService, courseTopicName);
    }
}
