package com.frontend.ProjetoFinalTCC_frontend.model;

public class VeiculoDTO {

    public enum StatusVeiculo {
        DISPONIVEL,
        EM_USO,
        MANUTENCAO,
        DESATIVADO
    }

    private Long idVeiculo;
    private String placa;
    private String modelo;
    private Integer anoFabricacao;
    private Double kmAtual;
    private Double kmUltimaManutencao;
    private StatusVeiculo status;

    public VeiculoDTO() {
    }

    public VeiculoDTO(Long idVeiculo, String placa, String modelo, Integer anoFabricacao, Double kmAtual, Double kmUltimaManutencao, StatusVeiculo status) {
        this.idVeiculo = idVeiculo;
        this.placa = placa;
        this.modelo = modelo;
        this.anoFabricacao = anoFabricacao;
        this.kmAtual = kmAtual;
        this.kmUltimaManutencao = kmUltimaManutencao;
        this.status = status;
    }

    public Long getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(Long idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public Double getKmAtual() {
        return kmAtual;
    }

    public void setKmAtual(Double kmAtual) {
        this.kmAtual = kmAtual;
    }

    public Double getKmUltimaManutencao() {
        return kmUltimaManutencao;
    }

    public void setKmUltimaManutencao(Double kmUltimaManutencao) {
        this.kmUltimaManutencao = kmUltimaManutencao;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }
}