package com.projeto.emprestimos.service;

import com.projeto.emprestimos.model.Equipamento;
import com.projeto.emprestimos.model.TipoEquipamento;
import com.projeto.emprestimos.repository.EmprestimoRepository;
import com.projeto.emprestimos.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service    //componente da camada de serviços
public class EquipamentoService {

    private final EquipamentoRepository repository;     //Declara dependência
    private final EmprestimoRepository emprestimoRepository;

    public EquipamentoService(EquipamentoRepository repository, EmprestimoRepository emprestimoRepository) {
        this.repository = repository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public Equipamento cadastrar(Equipamento equipamento) {

        // R1 - Impedir patrimônio duplicado
        if (repository.existsByPatrimonio(equipamento.getPatrimonio())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Patrimônio já cadastrado");
        }

        // R2 - Todo equipamento novo nasce disponível
        equipamento.setDisponivel(true);
        return repository.save(equipamento);
    }
    public List<Equipamento> listar(TipoEquipamento tipo, Boolean disponivel) {

        if (tipo != null && disponivel != null) {
            return repository.findByTipoAndDisponivel(tipo, disponivel);
        }

        if (tipo != null) {
            return repository.findByTipo(tipo);
        }

        if (disponivel != null) {
            return repository.findByDisponivel(disponivel);
        }

        return repository.findAll();
    }

    public Equipamento buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Equipamento não encontrado"));
    }

    public Equipamento atualizar(Long id, Equipamento dados) {
        // Busca o equipamento existente no banco
        Equipamento equipamento = buscar(id);

        // Atualiza somente os campos permitidos
        equipamento.setNome(dados.getNome());
        equipamento.setTipo(dados.getTipo());

        // Salva as alterações no banco
        return repository.save(equipamento);
    }

    public void remover(Long id) {

        // Verificar se o equipamento existe
        buscar(id);

        // Verificar se possui histórico de empréstimos
        if (emprestimoRepository.existsByEquipamentoId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Equipamento possui histórico de empréstimos"
            );
        }

        // Excluir equipamento
        repository.deleteById(id);
    }
}
