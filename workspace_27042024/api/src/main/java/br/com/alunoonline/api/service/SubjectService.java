package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Subject;
import br.com.alunoonline.api.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    public void create(Subject subject) {
        subjectRepository.save(subject);
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> findById(Long id) {
        return subjectRepository.findById(id);
    }

    public void update(Long id, Subject subject) {
        Optional<Subject> subjectFromDb = findById(id);

        if (subjectFromDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada no banco de dados.");
        }

        Subject subjectUpdated = subjectFromDb.get();

        subjectUpdated.setName(subject.getName());

        subjectRepository.save(subjectUpdated);
    }

    public void deleteById(Long id) {
        subjectRepository.deleteById(id);
    }

    public List<Subject> findByProfessorId(Long professorId) {
        return subjectRepository.findByProfessorId(professorId);
    }
}