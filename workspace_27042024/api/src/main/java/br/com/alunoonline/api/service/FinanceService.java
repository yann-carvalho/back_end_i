package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Invoice;
import br.com.alunoonline.api.model.StudentFinance;
import br.com.alunoonline.api.repository.InvoiceRepository;
import br.com.alunoonline.api.repository.StudentFinanceRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.logging.Logger;

@Service
public class FinanceService {
    private static final Integer QUANTITY_OF_DAYS_BEFORE_GENERATE = 10;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(FinanceService.class);

    @Autowired
    StudentFinanceRepository studentFinanceRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void invoiceGeneration() {
        logger.info("Iniciando a geração de faturas...");

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime generationDeadLine = currentDate.plusDays(QUANTITY_OF_DAYS_BEFORE_GENERATE);

        //Buscar todos os registros do StudentFinance
        List<StudentFinance> studentFinanceList = studentFinanceRepository.findAll();

        for (StudentFinance studentFinance : studentFinanceList) {
            Integer dueDate = studentFinance.getDueDate();

            if (dueDate != null) {
                // Calcular a data de vencimento do mês atual
                LocalDate dueDateCurrentMonth = calculateDueDate(dueDate, currentDate.getYear(), currentDate.getMonthValue());

                // Se a data de vencimento do mês atual já passou, calcular a data de vencimento do próximo mês
                if (dueDateCurrentMonth.isBefore(currentDate.toLocalDate())) {
                    dueDateCurrentMonth = calculateDueDate(dueDate, currentDate.getYear(), currentDate.getMonthValue() + 1);
                }

                // Verificar se falta 10 dias ou menos para a data de vencimento
                if (dueDateCurrentMonth != null && (dueDateCurrentMonth.isBefore(generationDeadLine.toLocalDate()) || dueDateCurrentMonth.isEqual(generationDeadLine.toLocalDate()))) {
                    // Verificar se já existe uma fatura para este aluno e data de vencimento
                    if (invoiceRepository.existsByStudentFinanceAndDueDate(studentFinance, dueDateCurrentMonth.atTime(LocalTime.MIDNIGHT))) {
                        // logger.info("Fatura já existe para o aluno: {} com data de vencimento: {}", financeiroAluno.getId(), dueDateCurrentMonth);
                        continue;
                    }

                    logger.info("Gerando fatura para o aluno: {}");

                    // Criar uma nova fatura para o aluno
                    Invoice invoice = new Invoice();
                    invoice.setStudentFinance(studentFinance);
                    invoice.setDueDate(dueDateCurrentMonth.atTime(LocalTime.MIDNIGHT));
                    invoice.setGeneratedAt(currentDate);

                    // Salvar a fatura no repositório
                    invoiceRepository.save(invoice);

                    logger.info("Fatura gerada para o aluno: {} com data de vencimento: {}");

                }
            }
        }

        logger.info("Geração de faturas concluída.");
    }

    private LocalDate calculateDueDate(Integer dueDate, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);

        //Verificar se o dia de vencimento é válido para o mês
        int dayOfMonth = Math.min(dueDate, yearMonth.lengthOfMonth());

        return LocalDate.of(year, month, dayOfMonth);
    }
}