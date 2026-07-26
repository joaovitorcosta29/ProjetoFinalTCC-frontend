/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.ProjetoFinalTCC_frontend.model;

/**
 *
 * @author joaov
 */

public class ViagemDTO {

    private Long idViagem;
    private Integer idUsuario;
    private Integer idVeiculo;
    private String cidadeDestino;
    private Estado estadoDestino;
    private Double kmInicial;
    private Double kmFinal;
    private StatusViagem statusViagem;
    private AlertaManutencao alertaManutencao;

    public enum Estado {
        AC, AL, AP, AM, BA, CE, DF, ES, GO, MA, 
        MT, MS, MG, PA, PB, PR, PE, PI, RJ, RN, 
        RS, RO, RR, SC, SP, SE, TO
    }

    public enum StatusViagem {
        DISPONIVEL,
        EM_ANDAMENTO,
        FINALIZADA
    }

    public enum AlertaManutencao {
        OK,
        REVISAO_NECESSARIA
    }

    public ViagemDTO() {
    }

    public Long getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(Long idViagem) {
        this.idViagem = idViagem;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(Integer idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getCidadeDestino() {
        return cidadeDestino;
    }

    public void setCidadeDestino(String cidadeDestino) {
        this.cidadeDestino = cidadeDestino;
    }

    public Estado getEstadoDestino() {
        return estadoDestino;
    }

    public void setEstadoDestino(Estado estadoDestino) {
        this.estadoDestino = estadoDestino;
    }

    public Double getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(Double kmInicial) {
        this.kmInicial = kmInicial;
    }

    public Double getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(Double kmFinal) {
        this.kmFinal = kmFinal;
    }

    public StatusViagem getStatusViagem() {
        return statusViagem;
    }

    public void setStatusViagem(StatusViagem statusViagem) {
        this.statusViagem = statusViagem;
    }

    public AlertaManutencao getAlertaManutencao() {
        return alertaManutencao;
    }

    public void setAlertaManutencao(AlertaManutencao alertaManutencao) {
        this.alertaManutencao = alertaManutencao;
    }
}
