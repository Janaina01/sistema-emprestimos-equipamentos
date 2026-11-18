package com.projeto.emprestimos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long equipamentoId;
    private Long colaboradorId;

    private LocalDate dataEmprestimo;   //LocalDate - data sem horário
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;

    private static final int PRAZO_DEVOLUCAO_DIAS = 14;

    public Emprestimo() {
    }

    //R5: a data do empréstimo será hoje e a previsão de devolução será 14 dias depois.
    public Emprestimo(Long equipamentoId, Long colaboradorId, LocalDate hoje) {
        this.equipamentoId = equipamentoId;
        this.colaboradorId = colaboradorId;
        this.dataEmprestimo = hoje;
        this.dataPrevistaDevolucao = hoje.plusDays(PRAZO_DEVOLUCAO_DIAS);

    }

    //R6: um empréstimo já devolvido não pode ser devolvido novamente.
    //R7: ao devolver, registrar a data da devolução.
    public void devolver(LocalDate data) {

        if (this.dataDevolucao != null) {
            throw new IllegalStateException(
                    "Empréstimo já devolvido"
            );
        }

        this.dataDevolucao = data;
    }

    //R8: um empréstimo estará atrasado quando não tiver sido devolvido e sua data prevista já tiver passado.
    public boolean estaAtrasadoEm(LocalDate data) {

        return this.dataDevolucao == null
                && data.isAfter(this.dataPrevistaDevolucao);
    }

    public boolean isAtrasado() {
        return estaAtrasadoEm(LocalDate.now());
    }

    public Long getId() {
        return id;
    }

    public Long getEquipamentoId() {
        return equipamentoId;
    }

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }
}
