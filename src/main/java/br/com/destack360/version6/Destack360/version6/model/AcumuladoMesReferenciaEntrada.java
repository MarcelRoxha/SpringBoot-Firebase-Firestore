package br.com.destack360.version6.Destack360.version6.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcumuladoMesReferenciaEntrada {

    private String identificador;
    private String nomeUser;
    private int mesReferenciaEntrada;
    private String emailUser;
    private double valorTotalEntradaMensal;
    private int quantidadeTotalLancamentosEntradaMensal;
    private String created;
}

