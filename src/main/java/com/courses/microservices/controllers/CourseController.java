package com.courses.microservices.controllers;

import com.commons.alumnos.models.entity.Alumno;
import com.commons.microservices.controllers.CommonController;
import com.commonsexamns.entity.Exam;
import com.courses.microservices.models.entity.Course;
import com.courses.microservices.models.entity.CursoAlumno;
import com.courses.microservices.services.CourseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CourseController extends CommonController<Course, CourseService> {

    @Value("${config.balanceador.test}")
    private String balanceadorTest;

    @GetMapping
    @Override
    public ResponseEntity<?> getList() {
        List<Course> cursos = ((List<Course>) service.findAll()).stream().map(c -> {
            c.getCursoAlumnos().forEach(ca -> {
                Alumno alumno = new Alumno();
                alumno.setId(ca.getAlumnoId());
                c.addAlumno(alumno);
            });
            return c;
        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(cursos);
    }

    @Override
    @GetMapping("/paginado")
    public ResponseEntity<?> getList(Pageable pageable) {
        Page<Course> cursos = service.findAll(pageable).map(c -> {
            c.getCursoAlumnos().forEach(ca -> {
                Alumno alumno = new Alumno();
                alumno.setId(ca.getAlumnoId());
                c.addAlumno(alumno);
            });
            return c;
        });
        return ResponseEntity.ok().body(cursos);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Course> optionalE = service.findById(id);
        //Validar si existe el registro
        if(optionalE.isEmpty()) {
            //Build construye la respuesta con un body vacio
            return ResponseEntity.notFound().build();
        }
        Course curso = optionalE.get();
        if(curso.getCursoAlumnos().isEmpty() == false) {
            List<Long> ids = curso.getCursoAlumnos().stream().map(ca -> {
               return ca.getAlumnoId();
            }).collect(Collectors.toList());

            List<Alumno> alumnos = (List<Alumno>) service.getAlumnosPorCurso(ids);

            curso.setAlumnos(alumnos);
        }
        return ResponseEntity.ok().body(curso);
    }

    @GetMapping("/balanceador-test")
    public ResponseEntity<?> balanceadorTest() {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("balanceador", balanceadorTest);
        response.put("cursos", service.findAll());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@Valid @RequestBody Course course, BindingResult result, @PathVariable Long id){

        if(result.hasErrors()){
            return this.validar(result);
        }

        Optional<Course> optionalCourse = service.findById(id);

        if(optionalCourse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Course courseDB = optionalCourse.get();
        courseDB.setNombre(course.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDB));
    }

    @PutMapping("/asignar-alumnos/{id}")
    public ResponseEntity<?> asignAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id){
        Optional<Course> optionalCourse = service.findById(id);

        if(optionalCourse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Course courseDB = optionalCourse.get();

        alumnos.forEach(a -> {
            CursoAlumno cursoAlumno = new CursoAlumno();
            cursoAlumno.setAlumnoId(a.getId());
            cursoAlumno.setCurso(courseDB);
            courseDB.addCursoAlumno(cursoAlumno);
        });
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDB));
    }

    @DeleteMapping("/delete-alumno/{id}")
    public ResponseEntity<?> deleteOneAlumno(@RequestBody Alumno alumno, @PathVariable Long id) {
        Optional<Course> optionalCourse = service.findById(id);

        if(optionalCourse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Course courseDB = optionalCourse.get();
        CursoAlumno cursoAlumno = new CursoAlumno();
        cursoAlumno.setAlumnoId(alumno.getId());
        courseDB.removeCursoAlumno(cursoAlumno);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDB));

    }

    @DeleteMapping("/delete-alumnos/{id}")
    public ResponseEntity<?> deleteMultipleAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id) {
        Optional<Course> optionalCourse = service.findById(id);

        if(optionalCourse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Course courseDB = optionalCourse.get();

        alumnos.forEach(a -> {
            courseDB.removeAlumno(a);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDB));

    }

    @GetMapping("/alumno/{id}")
    public ResponseEntity<?> findCourseByAlumnoId(@PathVariable Long id) {
        Course curso = service.findCourseByAlumnoId(id);

        if(curso != null) {
            List<Long> examenesIds = (List<Long>) service.obtenerExamenesIdsConRespuestasAlumno(id);

            List<Exam> examenes = curso.getExamenes().stream().peek(examen -> {
               if(examenesIds.contains(examen.getId())){
                   examen.setRespondido(true);
               }
            }).collect(Collectors.toList());

            curso.setExamenes(examenes);

        }

        return  ResponseEntity.ok().body(curso);
    }

    @PutMapping("/asignar-examenes/{id}")
    public ResponseEntity<?> asignExamenes(@RequestBody List<Exam> examenes, @PathVariable Long id){
        Optional<Course> optionalCourse = service.findById(id);

        if(optionalCourse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Course courseDB = optionalCourse.get();

        /*examenes.forEach(e -> {
            courseDB.addExamen(e);
        });*/
        //Expresion lambda

        examenes.forEach(courseDB::addExamen);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDB));
    }

    @DeleteMapping("/delete-examen/{id}")
    public ResponseEntity<?> deleteOneExamen(@RequestBody Exam examen, @PathVariable Long id) {
        Optional<Course> optionalCourse = service.findById(id);

        if(optionalCourse.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Course courseDB = optionalCourse.get();
        courseDB.removeExamen(examen);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDB));

    }

}
