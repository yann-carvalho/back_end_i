package br.com.alunoonline.api.service;

import br.com.alunoonline.api.dtos.UpdateGradesRequest;
import br.com.alunoonline.api.dtos.StudentSubjectsResponse;
import br.com.alunoonline.api.dtos.AcademicTranscriptResponse;
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

    public void updateGrades(Long studentEnrollmentId, UpdateGradesRequest updateGradesRequest) {
        StudentEnrollment studentEnrollment = studentEnrollmentRepository.findById(studentEnrollmentId)
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
        Double grade1 = studentEnrollment.getGrade1();
        Double grade2 = studentEnrollment.getGrade2();

        if (grade1 != null && grade2 != null) ;
        {
            double average = (grade1 + grade2) / 2.0;
            studentEnrollment.setStatus(average >= GRADE_AVG_TO_APPROVE ? StudentEnrollmentStatusEnum.APROVADO : StudentEnrollmentStatusEnum.REPROVADO);
        }
    }

    public void updateStatusToBreak(Long studentEnrollmentId) {
        StudentEnrollment studentEnrollment = studentEnrollmentRepository.findById(studentEnrollmentId)
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

    public AcademicTranscriptResponse getAcademicTranscript(Long studentId) {
        List<StudentEnrollment> studentEnrollments = studentEnrollmentRepository.findByStudentId(studentId);

        if (studentEnrollments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse aluno não possui matrículas.");
        }

        AcademicTranscriptResponse academicTranscript = new AcademicTranscriptResponse();
        academicTranscript.setStudentName(studentEnrollments.get(0).getStudent().getName());
        academicTranscript.setStudentEmail(studentEnrollments.get(0).getStudent().getEmail());

        List<StudentSubjectsResponse> subjectsList = new ArrayList<>();

        for (StudentEnrollment subject : studentEnrollments) {
            StudentSubjectsResponse studentSubjectsResponse = new StudentSubjectsResponse();
            studentSubjectsResponse.setSubjectName(subject.getSubject().getName());
            studentSubjectsResponse.setProfessorName(subject.getSubject().getProfessor().getName());
            studentSubjectsResponse.setGrade1(subject.getGrade1());
            studentSubjectsResponse.setGrade2(subject.getGrade2());
            Double average = studentSubjectsResponse.calculateAverage();
            studentSubjectsResponse.setAverage(average);
            studentSubjectsResponse.setStatus(subject.getStatus());
            subjectsList.add(studentSubjectsResponse);
        }

        academicTranscript.setStudentSubjectsResponseList(subjectsList);

        return academicTranscript;
    }
}