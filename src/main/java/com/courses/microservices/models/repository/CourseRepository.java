package com.courses.microservices.models.repository;

import com.courses.microservices.models.entity.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {

    @Query("select c from Course c join fetch c.cursoAlumnos a where a.alumnoId = ?1")
    public Course findCourseByAlumnoId(Long id);

    @Modifying
    @Query()
    public void eliminarCursoAlumnoPorId(Long id);
}
