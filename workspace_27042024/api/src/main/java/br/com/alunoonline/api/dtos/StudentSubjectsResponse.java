package br.com.alunoonline.api.dtos;

import br.com.alunoonline.api.enums.StudentEnrollmentStatusEnum;
import lombok.Data;

@Data
public class StudentSubjectsResponse {
    private String subjectName;
    private String professorName;
    private Double grade1;
    private Double grade2;
    private Double average;
    private StudentEnrollmentStatusEnum status;

    public Double calculateAverage() {
        if (grade1 != null && grade2 != null) {
            return (grade1 + grade2) / 2.0;
        } else {
            return null;
        }
    }
}