package com.example.testdataelasticsearch.controller.pojobased;

import com.example.testdataelasticsearch.entity.pojo.Course;
import com.example.testdataelasticsearch.service.pojobased.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public void save(@RequestBody final Course course) {
        courseService.save(course);
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable final String id) {
        return courseService.getById(id);
    }


}
