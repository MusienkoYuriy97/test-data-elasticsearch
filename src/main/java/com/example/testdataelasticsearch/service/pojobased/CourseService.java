package com.example.testdataelasticsearch.service.pojobased;

import com.example.testdataelasticsearch.entity.pojo.Course;
import com.example.testdataelasticsearch.entity.search.SearchRequestDTO;
import com.example.testdataelasticsearch.mapper.SearchHintsMapper;
import com.example.testdataelasticsearch.util.SearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Course> search(final SearchRequestDTO dto) {
        final Query query = Optional.ofNullable(SearchUtil.buildQuery(dto))
                .orElseThrow(() -> new RuntimeException("Query is empty"));
        final SearchHits<Course> searchHits = elasticsearchOperations
                .search(query, Course.class, IndexCoordinates.of(COURSE));
        return Optional.ofNullable(SearchHintsMapper.toList(searchHits))
                .orElseThrow(() -> new RuntimeException("There is no courses according your query"));
    }
}