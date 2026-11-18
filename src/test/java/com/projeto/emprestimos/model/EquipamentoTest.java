package com.projeto.emprestimos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EquipamentoTest {
    private Equipamento equipamento;

    @BeforeEach
    void equipamento(){ equipamento = new Equipamento();}

    @Test
    void deveImpedirEmprestimoDeEquipamentoIndisponivel(){

        // Arrange - Preparar o cenário
        equipamento.setDisponivel(true);

        // Act - Realizar o primeiro empréstimo
        equipamento.emprestar();

        // Assert - Verificar se o segundo empréstimo falha
        assertThrows(
                IllegalStateException.class,
                () -> equipamento.emprestar()
        );
    }

    @Test
    void deveMarcarEquipamentoComoIndisponivelAoEmprestar(){
        equipamento.setDisponivel(true);

        equipamento.emprestar();

        assertFalse(equipamento.isDisponivel());
    }

    @Test
    void deveMarcarEquipamentoComoDisponivelAoDevolver(){
        equipamento.setDisponivel(false);

        equipamento.devolver();
        assertTrue(equipamento.isDisponivel());
    }

}
