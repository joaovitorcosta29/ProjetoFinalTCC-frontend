/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.ProjetoFinalTCC_frontend.model;

/**
 *
 * @author joaov
 */

public class ManutencaoDTO {

    private Integer idManutencao;
    private Integer idVeiculo;
    private String descricao;
    private StatusManutencao statusManutencao;

    public enum StatusManutencao {
        PENDENTE,
        EM_ANDAMENTO,
        CONCLUIDA,
        CANCELADA
    }

    public ManutencaoDTO() {}

    public ManutencaoDTO(Integer idManutencao, Integer idVeiculo, String descricao, StatusManutencao statusManutencao) {
        this.idManutencao = idManutencao;
        this.idVeiculo = idVeiculo;
        this.descricao = descricao;
        this.statusManutencao = statusManutencao;
    }

    public Integer getIdManutencao() {
        return idManutencao;
    }

    public void setIdManutencao(Integer idManutencao) {
        this.idManutencao = idManutencao;
    }

    public Integer getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(Integer idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusManutencao getStatusManutencao() {
        return statusManutencao;
    }

    public void setStatusManutencao(StatusManutencao statusManutencao) {
        this.statusManutencao = statusManutencao;
    }
}
