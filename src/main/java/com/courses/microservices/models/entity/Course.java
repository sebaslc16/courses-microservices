package com.courses.microservices.models.entity;

import com.commons.alumnos.models.entity.Alumno;
import com.commonsexamns.entity.Exam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nombre;

    @Column(name ="create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Exam> examenes;

    @JsonIgnoreProperties(value = {"curso"}, allowSetters = true)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CursoAlumno> cursoAlumnos;

    //Relacion uno a muchos, un curso, muchos alumnos
    //@OneToMany(fetch = FetchType.LAZY)
    @Transient
    private List<Alumno> alumnos;

    @PrePersist
    public void prePersisteDate() {
        this.createAt = new Date();
    }

    //Inicializar la lista e instancia alumnos
    public Course() {
        this.alumnos = new ArrayList<>();
        this.examenes = new ArrayList<>();
        this.cursoAlumnos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    //Agregar alumnos a la lista, asignar un alumno al curso
    public void addAlumno(Alumno alumno) {
        this.alumnos.add(alumno);
    }

    //Eliminar alumnos del curso
    public void removeAlumno(Alumno alumno) {
        this.alumnos.remove(alumno);
    }

    public List<Exam> getExamenes() {
        return examenes;
    }

    public void setExamenes(List<Exam> examenes) {
        this.examenes = examenes;
    }

    public void addExamen(Exam examen) {
        this.examenes.add(examen);
    }

    public void removeExamen(Exam examen) {
        this.examenes.remove(examen);
    }

    public List<CursoAlumno> getCursoAlumnos() {
        return cursoAlumnos;
    }

    public void setCursoAlumnos(List<CursoAlumno> cursoAlumnos) {
        this.cursoAlumnos = cursoAlumnos;
    }

    public void addCursoAlumno(CursoAlumno cursoAlumno) {
        this.cursoAlumnos.add(cursoAlumno);
    }

    public void removeCursoAlumno(CursoAlumno cursoAlumno) {
        this.cursoAlumnos.remove(cursoAlumno);
    }
}
