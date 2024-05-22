package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Subject;
import br.com.alunoonline.api.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subject")

public class SubjectController {
    @Autowired
    SubjectService subjectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public void create(@RequestBody Subject subject) {
        subjectService.create(subject);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> findAll() {
        return subjectService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Subject> findById(@PathVariable Long id) {
        return subjectService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Subject subject, @PathVariable Long id) {
        subjectService.update(id, subject);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        subjectService.deleteById(id);
    }

    @GetMapping("/professor/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> findByProfessorId(@PathVariable Long id) {
        return subjectService.findByProfessorId(id);
    }
}