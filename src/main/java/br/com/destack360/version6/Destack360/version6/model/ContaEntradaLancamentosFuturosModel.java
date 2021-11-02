package br.com.destack360.version6.Destack360.version6.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaEntradaLancamentosFuturosModel {

    private String identificador;
    private String nomeContaEntradaLancamentoFuturos;
    private String emailClienteQueSugeriu;
    private String identificadorCliente;
    private int quantasVezesUsadaContaSalvaFuturo;
}
