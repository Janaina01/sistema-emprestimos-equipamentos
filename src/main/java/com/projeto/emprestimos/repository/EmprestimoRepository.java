package com.projeto.emprestimos.repository;

import com.projeto.emprestimos.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    // Histórico de empréstimos de um colaborador
    List<Emprestimo> findByColaboradorId(Long colaboradorId);

    // R9 - Verifica se o equipamento já possui empréstimos
    boolean existsByEquipamentoId(Long equipamentoId);

    // R8 - Consulta empréstimos em atraso
    List<Emprestimo>
    findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(
            LocalDate data);

    // R10 - Verifica se o colaborador possui empréstimos atrasados
    boolean existsByColaboradorIdAndDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(
            Long colaboradorId,
            LocalDate data);
}
