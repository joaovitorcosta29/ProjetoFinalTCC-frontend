package com.projetofinalTCC.backendTCC.model;

public class VeiculoDTO {
    
    private Long idVeiculo;
    private String placa;
    private String modelo;
    private Integer anoFabricacao;
    private Double kmAtual;
    private Double kmUltimaManutencao;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}