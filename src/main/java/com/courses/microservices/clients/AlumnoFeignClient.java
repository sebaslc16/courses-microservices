package com.courses.microservices.clients;

import com.commons.alumnos.models.entity.Alumno;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "microservice-users")
public interface AlumnoFeignClient {

    @GetMapping("/alumnos-por-curso")
    public Iterable<Alumno> getAlumnosPorCurso(@RequestParam List<Long> ids);
}
