package br.com.alunoonline.api.repository;

import br.com.alunoonline.api.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByProfessorId(Long professorId);
}