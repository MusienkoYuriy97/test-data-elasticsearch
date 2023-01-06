package com.example.testdataelasticsearch.service.pojobased;

import com.example.testdataelasticsearch.entity.pojo.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.testdataelasticsearch.util.ElasticsearchIndices.COURSE;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final ElasticsearchOperations elasticsearchOperations;

    public void save(final Course course) {
        elasticsearchOperations.save(course, IndexCoordinates.of(COURSE));
    }

    public Course getById(final String courseId) {
        return Optional
                .ofNullable(elasticsearchOperations.get(courseId, Course.class, IndexCoordinates.of(COURSE)))
                .orElseThrow(RuntimeException::new);
    }
}