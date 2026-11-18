package com.projeto.emprestimos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmprestimoTest {

    private Emprestimo emprestimo;
    private LocalDate hoje;

    @BeforeEach
    void emprestimo(){
        hoje = LocalDate.of(2026,11,18);
        emprestimo = new Emprestimo(1L, 1L, hoje);
    }

    @Test
    void deveCalcularPrazoDeDevolucaoDe14Dias(){

        assertEquals(
                hoje.plusDays(14),
                emprestimo.getDataPrevistaDevolucao()
        );
    }

    @Test
    void deveCriarEmprestimoSemDataDeDwevolucao(){

        assertNull(emprestimo.getDataDevolucao());
    }

    @Test
    void deveRegistrarDataDoEmprestimo(){

        assertEquals(hoje, emprestimo.getDataEmprestimo());
    }

    @Test
    void deveRegistrarDataDeDevolucao(){
        LocalDate dataDevolucao = hoje.plusDays(5);

        emprestimo.devolver(dataDevolucao);

        assertEquals(dataDevolucao, emprestimo.getDataDevolucao());
    }

    @Test
    void deveImpedirDevolucaoDuplicada(){
        LocalDate primeiraDevolucao = hoje.plusDays(5);
        LocalDate segundaDevolucao = hoje.plusDays(6);

        emprestimo.devolver(primeiraDevolucao);

        IllegalStateException excecao = assertThrows(
                IllegalStateException.class,
                () -> emprestimo.devolver(segundaDevolucao)
        );

        assertEquals("Empréstimo já devolvido", excecao.getMessage());
    }

    @Test
    void deveIdentificarEmprestimoAtrasado(){
        LocalDate dataConsulta = hoje.plusDays(15);

        boolean atrasado = emprestimo.estaAtrasadoEm(dataConsulta);

        assertTrue(atrasado);
    }

    @Test
    void naoDeveConsiderarEmprestimoDevolvidoComoAtrasado(){
        LocalDate dataDevolucao = hoje.plusDays(10);
        LocalDate dataConsulta = hoje.plusDays(20);

        emprestimo.devolver(dataDevolucao);

        assertFalse(emprestimo.estaAtrasadoEm(dataConsulta));
    }

    @Test
    void naoDeveConsiderarAtrasadoNoDiaDoVencimento() {

        LocalDate dataVencimento = hoje.plusDays(14);

        assertFalse(emprestimo.estaAtrasadoEm(dataVencimento));
    }
}
