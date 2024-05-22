package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Student;
import br.com.alunoonline.api.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public void create(Student student) {
        studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public void update(Long id, Student student) {
        Optional<Student> studentFromDb = findById(id);

        if (studentFromDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados.");
        }

        Student studentUpdated = studentFromDb.get();

        studentUpdated.setName(student.getName());
        studentUpdated.setEmail(student.getEmail());

        studentRepository.save(studentUpdated);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}