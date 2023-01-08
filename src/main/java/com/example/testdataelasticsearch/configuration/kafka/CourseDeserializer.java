package com.example.testdataelasticsearch.configuration.kafka;

import com.example.testdataelasticsearch.entity.pojo.Course;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import static java.util.Objects.isNull;

public class CourseDeserializer implements Deserializer<Course> {
    @Override
    public Course deserialize(final String topicName, final byte[] bytes) {
        try {
            if (isNull(bytes) || bytes.length == 0) {
                return null;
            }
            return new ObjectMapper().readValue(bytes, Course.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to MessageDto");
        }
    }
}
