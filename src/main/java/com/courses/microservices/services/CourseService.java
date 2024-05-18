package com.courses.microservices.services;

import com.commons.alumnos.models.entity.Alumno;
import com.commons.microservices.services.CommonService;
import com.courses.microservices.models.entity.Course;
import java.util.List;

public interface CourseService extends CommonService<Course> {
    public Course findCourseByAlumnoId(Long id);

    public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(Long alumnoId);

    public Iterable<Alumno> getAlumnosPorCurso(List<Long> ids);
}
