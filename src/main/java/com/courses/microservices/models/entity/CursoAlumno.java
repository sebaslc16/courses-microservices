package com.courses.microservices.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name="cursos_alumnos")
public class CursoAlumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alumno_id", unique = true)
    private Long alumnoId;

    @JsonIgnoreProperties(value = {"cursoAlumnos"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Course curso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Course getCurso() {
        return curso;
    }

    public void setCurso(Course curso) {
        this.curso = curso;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if(!(obj instanceof CursoAlumno)){
            return false;
        }
        CursoAlumno a = (CursoAlumno) obj;
        return this.alumnoId != null && this.alumnoId.equals(a.getAlumnoId());
    }
}
