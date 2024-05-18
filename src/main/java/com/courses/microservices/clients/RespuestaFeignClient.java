package com.courses.microservices.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-respuestas")
public interface RespuestaFeignClient {

    @GetMapping("/alumno/{alumnoId}/examenes-respondidos")
    public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(@PathVariable Long alumnoId);

}
