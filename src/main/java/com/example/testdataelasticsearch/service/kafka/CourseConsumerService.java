package com.example.testdataelasticsearch.service.kafka;

import com.example.testdataelasticsearch.entity.pojo.Course;
import com.example.testdataelasticsearch.service.pojobased.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public record CourseConsumerService(CourseService courseService, String topicName) {

    @KafkaListener(
            topics = "#{__listener.topicName}",
            groupId = "test-kafka-consumer"
    )
    public void consumeCourse(final Course course) {
        log.info("Consume message: " + course);
        courseService.save(course);
    }
}
