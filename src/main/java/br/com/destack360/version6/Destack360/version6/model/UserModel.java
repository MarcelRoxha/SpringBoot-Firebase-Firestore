package br.com.destack360.version6.Destack360.version6.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter


public class UserModel {

    private String user_id;
    private String nomeUser;
    private String emailUser;
    private double valorTotalEntradaMensal;
    private double valorTotalSaidaMensal;
    private int quantidadeTotalLancamentosEntradaMensal;
    private int quantidadeTotalLancamentosSaidaMensal;

}
