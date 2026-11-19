package com.projeto.emprestimos.controller;

import com.projeto.emprestimos.model.Equipamento;
import com.projeto.emprestimos.model.TipoEquipamento;
import com.projeto.emprestimos.service.EquipamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "Equipamentos", description = "Cadastro, consulta, atualização e exclusão de equipamentos")

@RestController
@RequestMapping("/equipamentos")    //Rota base /equipamentos
public class EquipamentoController {

    private final EquipamentoService service;

    public EquipamentoController(EquipamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Equipamento> listar(
            @RequestParam(required = false) TipoEquipamento tipo,
            @RequestParam(required = false) Boolean disponivel) {

        return service.listar(tipo, disponivel);
    }

    @GetMapping("/{id}")
    public Equipamento buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipamento cadastrar(@Valid @RequestBody Equipamento equipamento) {
        return service.cadastrar(equipamento);
    }

    @PutMapping("/{id}")
    public Equipamento atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Equipamento equipamento) {

        return service.atualizar(id, equipamento);
    }

    // DELETE - Remover equipamento por ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
