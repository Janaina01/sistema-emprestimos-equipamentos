package com.projeto.emprestimos.controller;

import com.projeto.emprestimos.model.Emprestimo;
import com.projeto.emprestimos.service.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Empréstimos", description = "Gerenciamento de empréstimos e devoluções de equipamentos, " +
        "com controle de disponibilidade, prazos e atrasos.")

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService service;

    public EmprestimoController(EmprestimoService service) {
        this.service = service;
    }

    // GET - Listar todos ou apenas atrasados
    @GetMapping
    public List<Emprestimo> listar(
        @RequestParam(required = false) Boolean atrasados) {
        return service.listar(atrasados);
    }

    // GET - Buscar empréstimo por ID
    @GetMapping("/{id}")
    public Emprestimo buscar(@PathVariable Long id){
        return service.buscar(id);
    }

    // POST - Realizar empréstimo
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Emprestimo emprestar(@RequestParam Long equipamentoId, @RequestParam Long colaboradorId){
        return service.emprestar(equipamentoId, colaboradorId);
    }

    // PUT - Registrar devolução
    @PutMapping("/{id}/devolucao")
    public  Emprestimo devolver(@PathVariable Long id){
        return service.devolver(id);
    }
}
