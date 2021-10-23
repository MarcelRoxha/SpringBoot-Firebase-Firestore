package br.com.destack360.version6.Destack360.version6.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcumuladoMesReferenciaSaida {

    private String nomeUser;
    private int mesReferenciaSaida;
    private String emailUser;
    private double valorTotalEntradaMensal;
    private int quantidadeTotalLancamentosEntradaMensal;
    private String created;

}
