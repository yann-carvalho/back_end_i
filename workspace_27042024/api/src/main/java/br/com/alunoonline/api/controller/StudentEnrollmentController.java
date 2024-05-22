package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.dtos.UpdateGradesRequest;
import br.com.alunoonline.api.model.StudentEnrollment;
import br.com.alunoonline.api.service.StudentEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-enrollment")
public class StudentEnrollmentController {

    @Autowired
    StudentEnrollmentService studentEnrollmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody StudentEnrollment studentEnrollment) {
        studentEnrollmentService.create(studentEnrollment);
    }

    @PatchMapping("/update-grades/{studentEnrollmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGrades(@RequestBody UpdateGradesRequest updateGradesRequest, @PathVariable Long studentEnrollmentId) {
        studentEnrollmentService.updateGrades(studentEnrollmentId, updateGradesRequest);
    }

    @PatchMapping("/update-status-to-break/{studentEnrollmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatusToBreak(@PathVariable Long studentEnrollmentId) {
        studentEnrollmentService.updateStatusToBreak(studentEnrollmentId);
    }
}
