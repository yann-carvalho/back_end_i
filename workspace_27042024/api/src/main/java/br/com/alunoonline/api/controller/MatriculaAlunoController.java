package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.dtos.AtualizarNotasRequest;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.service.MatriculaAlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matricula-aluno")
public class MatriculaAlunoController {

    @Autowired
    MatriculaAlunoService matriculaAlunoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody MatriculaAluno matriculaAluno) {
        matriculaAlunoService.create(matriculaAluno);
    }

    @PatchMapping("/update-grades/{matriculaAlunoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGrades(@RequestBody AtualizarNotasRequest atualizarNotasRequest, @PathVariable Long matriculaAlunoId) {
        matriculaAlunoService.updateGrades(matriculaAlunoId, atualizarNotasRequest);
    }

    @PatchMapping("/update-status-to-break/{matriculaAlunoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatusToBreak(@PathVariable Long matriculaAlunoId) {
        matriculaAlunoService.updateStatusToBreak(matriculaAlunoId);
    }
}
