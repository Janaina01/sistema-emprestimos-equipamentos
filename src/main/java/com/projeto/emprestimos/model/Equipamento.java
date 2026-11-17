package com.projeto.emprestimos.model;

import jakarta.persistence.*;

@Entity //Representa uma entidade persistida no banco
public class Equipamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Gera automaticamente o ID
    private Long id; //Chave primária

    private String patrimonio;

    private String nome;

    @Enumerated(EnumType.STRING)    //Armazena o tipo como texto
    private TipoEquipamento tipo;

    private boolean disponivel;

    public Equipamento(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(String patrimonio) {
        this.patrimonio = patrimonio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoEquipamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEquipamento tipo) {
        this.tipo = tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public void emprestar(){
        if (!disponivel){
            throw new IllegalStateException("Equipamento indisponível");
        }
        this.disponivel = false;
    }

    public void devolver() {
        this.disponivel = true;
    }
}
