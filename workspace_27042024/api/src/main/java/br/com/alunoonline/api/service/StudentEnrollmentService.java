package br.com.alunoonline.api.service;

import br.com.alunoonline.api.dtos.UpdateGradesRequest;
import br.com.alunoonline.api.dtos.StudentSubjectsResponse;
import br.com.alunoonline.api.dtos.StudentHistoryResponse;
import br.com.alunoonline.api.enums.StudentEnrollmentStatusEnum;
import br.com.alunoonline.api.model.StudentEnrollment;
import br.com.alunoonline.api.repository.StudentEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentEnrollmentService {

    public static final double GRADE_AVG_TO_APPROVE = 7.0;

    @Autowired
    StudentEnrollmentRepository studentEnrollmentRepository;

    public void create(StudentEnrollment studentEnrollment) {
        studentEnrollment.setStatus(StudentEnrollmentStatusEnum.MATRICULADO);
        studentEnrollmentRepository.save(studentEnrollment);
    }

    public void updateGrades(Long matriculaAlunoId, UpdateGradesRequest updateGradesRequest) {
        StudentEnrollment studentEnrollment = studentEnrollmentRepository.findById(matriculaAlunoId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada"));

        updateStudentGrades(studentEnrollment, updateGradesRequest);
        updateStudentStatus(studentEnrollment);

        studentEnrollmentRepository.save(studentEnrollment);
    }

    public void updateStudentGrades(StudentEnrollment studentEnrollment, UpdateGradesRequest updateGradesRequest) {
        if (updateGradesRequest.getGrade1() != null) {
            studentEnrollment.setGrade1(updateGradesRequest.getGrade1());
        }

        if (updateGradesRequest.getGrade2() != null) {
            studentEnrollment.setGrade2(updateGradesRequest.getGrade2());
        }
    }

    public void updateStudentStatus(StudentEnrollment studentEnrollment) {
        Double nota1 = studentEnrollment.getGrade1();
        Double nota2 = studentEnrollment.getGrade2();

        if (nota1 != null && nota2 != null) ;
        {
            double average = (nota1 + nota2) / 2.0;
            studentEnrollment.setStatus(average >= GRADE_AVG_TO_APPROVE ? StudentEnrollmentStatusEnum.APROVADO : StudentEnrollmentStatusEnum.REPROVADO);
        }
    }

    public void updateStatusToBreak(Long matriculaAlunoId) {
        StudentEnrollment studentEnrollment = studentEnrollmentRepository.findById(matriculaAlunoId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada"));

        if (!StudentEnrollmentStatusEnum.MATRICULADO.equals(studentEnrollment.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Só é possível trancar uma matrícula com o status MATRICULADO");
        }

        changeStatus(studentEnrollment, StudentEnrollmentStatusEnum.TRANCADO);
    }

    public void changeStatus(StudentEnrollment studentEnrollment, StudentEnrollmentStatusEnum studentEnrollmentStatusEnum) {
        studentEnrollment.setStatus(studentEnrollmentStatusEnum);
        studentEnrollmentRepository.save(studentEnrollment);
    }

    public StudentHistoryResponse getHistoricoFromAluno(Long alunoId) {
        List<StudentEnrollment> matriculasDoAluno = studentEnrollmentRepository.findByStudentId(alunoId);

        if (matriculasDoAluno.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse aluno não possui matrículas.");
        }

        StudentHistoryResponse historico = new StudentHistoryResponse();
        historico.setStudentName(matriculasDoAluno.get(0).getStudent().getName());
        historico.setStudentEmail(matriculasDoAluno.get(0).getStudent().getEmail());

        List<StudentSubjectsResponse> disciplinasList = new ArrayList<>();

        for (StudentEnrollment matricula : matriculasDoAluno) {
            StudentSubjectsResponse studentSubjectsResponse = new StudentSubjectsResponse();
            studentSubjectsResponse.setSubjectName(matricula.getSubject().getName());
            studentSubjectsResponse.setProfessorName(matricula.getSubject().getProfessor().getName());
            studentSubjectsResponse.setGrade1(matricula.getGrade1());
            studentSubjectsResponse.setGrade2(matricula.getGrade2());
            Double average = studentSubjectsResponse.calculateAverage();
            studentSubjectsResponse.setAverage(average);
            studentSubjectsResponse.setStatus(matricula.getStatus());
            disciplinasList.add(studentSubjectsResponse);
        }

        historico.setStudentSubjectsResponseList(disciplinasList);

        return historico;
    }
}