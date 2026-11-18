package com.projeto.emprestimos.service;

import com.projeto.emprestimos.model.Emprestimo;
import com.projeto.emprestimos.model.Equipamento;
import com.projeto.emprestimos.repository.ColaboradorRepository;
import com.projeto.emprestimos.repository.EmprestimoRepository;
import com.projeto.emprestimos.repository.EquipamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    //Registrar e consultar empréstimos
    private final EmprestimoRepository emprestimoRepository;
    //Verificar e alterar a disponibilidade do equipamento
    private final EquipamentoRepository equipamentoRepository;
    //Verificar se o colaborador existe
    private final ColaboradorRepository colaboradorRepository;

    public EmprestimoService(
            EmprestimoRepository emprestimoRepository,
            EquipamentoRepository equipamentoRepository,
            ColaboradorRepository colaboradorRepository
    ){
        this.emprestimoRepository = emprestimoRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    // GET - Listar todos ou apenas atrasados
    public List<Emprestimo> listar(Boolean atrasados) {

        if (Boolean.TRUE.equals(atrasados)) {
            return emprestimoRepository
                    .findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(
                            LocalDate.now()
                    );
        }

        return emprestimoRepository.findAll();
    }

    // GET - Buscar por ID
    public Emprestimo buscar(Long id) {

        return emprestimoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Empréstimo não encontrado"
                        )
                );
    }

    // GET - Histórico do colaborador
    public List<Emprestimo> listarDoColaborador(Long colaboradorId) {

        if (!colaboradorRepository.existsById(colaboradorId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Colaborador não encontrado"
            );
        }

        return emprestimoRepository.findByColaboradorId(colaboradorId);
    }

    @Transactional
    public Emprestimo emprestar(
            Long equipamentoId,
            Long colaboradorId) {

        // 1. Verificar se o equipamento existe
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Equipamento não encontrado"
                        )
                );

        // 2. Verificar se o colaborador existe
        if (!colaboradorRepository.existsById(colaboradorId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Colaborador não encontrado"
            );
        }

        // 3. Verificar disponibilidade do equipamento
        if (!equipamento.isDisponivel()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Equipamento indisponível"
            );
        }

        // 4. Verificar empréstimos atrasados do colaborador
        LocalDate hoje = LocalDate.now();

        boolean possuiAtrasos = emprestimoRepository
                .existsByColaboradorIdAndDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(
                        colaboradorId,
                        hoje
                );

        if (possuiAtrasos) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Colaborador possui empréstimos atrasados"
            );
        }

        // 5. Criar empréstimo com prazo de 14 dias
        Emprestimo emprestimo = new Emprestimo(
                equipamentoId,
                colaboradorId,
                hoje
        );

        // 6. Marcar equipamento como indisponível
        equipamento.emprestar();

        // 7. Salvar alterações
        equipamentoRepository.save(equipamento);

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo devolver(Long id) {

        // 1. Buscar o empréstimo
        Emprestimo emprestimo = buscar(id);

        // 2. Verificar se já foi devolvido (R6)
        if (emprestimo.getDataDevolucao() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Empréstimo já devolvido"
            );
        }

        // 3. Buscar o equipamento associado
        Equipamento equipamento = equipamentoRepository
                .findById(emprestimo.getEquipamentoId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Equipamento não encontrado"
                        )
                );

        // 4. Registrar a data de devolução (R7)
        emprestimo.devolver(LocalDate.now());

        // 5. Tornar o equipamento disponível novamente
        equipamento.devolver();

        // 6. Salvar alterações
        equipamentoRepository.save(equipamento);

        return emprestimoRepository.save(emprestimo);
    }
}
