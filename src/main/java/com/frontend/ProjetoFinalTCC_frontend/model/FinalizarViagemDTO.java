package com.frontend.ProjetoFinalTCC_frontend.model;

public class FinalizarViagemDTO {
    private Long idViagem;
    private Double kmFinal;

    public FinalizarViagemDTO() {}

    public FinalizarViagemDTO(Long idViagem, Double kmFinal) {
        this.idViagem = idViagem;
        this.kmFinal = kmFinal;
    }

    public Long getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(Long idViagem) {
        this.idViagem = idViagem;
    }

    public Double getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(Double kmFinal) {
        this.kmFinal = kmFinal;
    }
}