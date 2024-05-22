package br.com.alunoonline.api.dtos;

import lombok.Data;

import java.util.List;

@Data
public class StudentHistoryResponse {
    private String studentName;
    private String studentEmail;
    private List<StudentSubjectsResponse> studentSubjectsResponseList;
}
