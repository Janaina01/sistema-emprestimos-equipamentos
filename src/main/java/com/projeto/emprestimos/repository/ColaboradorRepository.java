package com.projeto.emprestimos.repository;

import com.projeto.emprestimos.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    //Verifica se já existe algum colaborador cadastrado no email
    boolean existsByEmail(String email);


}
