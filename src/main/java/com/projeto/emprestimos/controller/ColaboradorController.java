package com.projeto.emprestimos.controller;

import com.projeto.emprestimos.model.Colaborador;
import com.projeto.emprestimos.service.ColaboradorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    private  final ColaboradorService service;

    public ColaboradorController(ColaboradorService service) {
        this.service = service;
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

}
