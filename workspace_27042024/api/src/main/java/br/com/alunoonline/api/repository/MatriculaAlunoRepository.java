package br.com.alunoonline.api.repository;

import br.com.alunoonline.api.model.MatriculaAluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaAlunoRepository extends JpaRepository<MatriculaAluno, Long> {
}
