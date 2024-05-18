package com.courses.microservices.services;

import com.commons.alumnos.models.entity.Alumno;
import com.commons.microservices.services.CommonServiceImpl;
import com.courses.microservices.clients.AlumnoFeignClient;
import com.courses.microservices.clients.RespuestaFeignClient;
import com.courses.microservices.models.entity.Course;
import com.courses.microservices.models.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseServiceImpl extends CommonServiceImpl<Course, CourseRepository> implements CourseService {

    @Autowired
    private RespuestaFeignClient client;

    @Autowired
    private AlumnoFeignClient clientAlumno;
    @Override
    @Transactional(readOnly = true)
    public Course findCourseByAlumnoId(Long id) {
        return repository.findCourseByAlumnoId(id);
    }

    @Override
    public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(Long alumnoId) {
        return client.obtenerExamenesIdsConRespuestasAlumno(alumnoId);
    }

    @Override
    public Iterable<Alumno> getAlumnosPorCurso(List<Long> ids) {
        return clientAlumno.getAlumnosPorCurso(ids);
    }
}
