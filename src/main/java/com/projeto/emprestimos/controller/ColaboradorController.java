package com.projeto.emprestimos.controller;

import com.projeto.emprestimos.model.Colaborador;
import com.projeto.emprestimos.model.Emprestimo;
import com.projeto.emprestimos.service.ColaboradorService;
import com.projeto.emprestimos.service.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    private  final ColaboradorService service;
    private final EmprestimoService emprestimoService;

    public ColaboradorController(ColaboradorService service,  EmprestimoService emprestimoService) {
        this.service = service;
        this.emprestimoService = emprestimoService;
    }

    // GET - Listar todos os colaboradores
    @GetMapping
    public List<Colaborador> listar(){
        return service.listar();
    }

    // GET - Buscar colaborador pelo ID
    @GetMapping("/{id}")
    public Colaborador buscar(@PathVariable Long id){
        return service.buscar(id);
    }

    // POST - Cadastrar colaborador
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Colaborador cadastrar(@RequestBody Colaborador colaborador){
        return service.cadastrar(colaborador);
    }

    // GET - Histórico de empréstimos do colaborador
    @GetMapping("/{id}/emprestimos")
    public List<Emprestimo> listarEmprestimos(
            @PathVariable Long id) {

        return emprestimoService.listarDoColaborador(id);
    }
}
