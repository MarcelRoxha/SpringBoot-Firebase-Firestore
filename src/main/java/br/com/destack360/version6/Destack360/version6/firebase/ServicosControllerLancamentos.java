package br.com.destack360.version6.Destack360.version6.firebase;

import br.com.destack360.version6.Destack360.version6.commons.GenericServiceAPI;
import br.com.destack360.version6.Destack360.version6.model.*;
import br.com.destack360.version6.Destack360.version6.modelDTO.LancamentoEntradaModelDTO;
import br.com.destack360.version6.Destack360.version6.service.impl.LancamentoEntradaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/")
@CrossOrigin("http://localhost:4200/")
public class ServicosControllerLancamentos {


    @Autowired
    public ServicosService servicosService;

    public ServicosControllerLancamentos(ServicosService servicosService){
        this.servicosService = servicosService;
    }


    //--------------------LANÇAMENTO ENTRADA INICIO-------------------------------//

    //Salvar lancamento entrada-----------------------------

    @PostMapping("/lancarEntrada")
    public String lancarEntrada(@RequestBody LancamentoEntradaModel lancamentoEntradaModel) throws ExecutionException, InterruptedException {
        return servicosService.lancarEntrada(lancamentoEntradaModel);
    }

    //Editar lancamento entrada-----------------------------

    @PutMapping("/editarEntrada")
    public String editarEntrada(@RequestBody LancamentoEntradaModel lancamentoEntradaModel) {
        return servicosService.editarLancamentoEntrada(lancamentoEntradaModel);
    }

    //Deletar lancamento entrada----------------------------

  /*  @GetMapping("/deletarLanceEntrada")
    public String deletarLanceEntrada(@RequestParam String collection) {
        return servicosService.deletarEntradaLancada(collection);
    }*/

    //--------------------LANÇAMENTO ENTRADA FIM-------------------------------//




    //--------------------LANÇAMENTO SAIDA INICIO-------------------------------//

    //Salvar lancamento saida

    @PostMapping("/lancarSaida")
    public String lancarSaida(@RequestBody LancamentoEntradaModel lancamentoEntradaModel) throws ExecutionException, InterruptedException {
        return servicosService.lancarSaida(lancamentoEntradaModel);
    }

    //Editar lancamento saida
    @PutMapping("/editarSaida")
    public String editarSaida(@RequestBody LancamentoEntradaModel lancamentoEntradaModel) {
        return servicosService.editarLancamentoSaida(lancamentoEntradaModel);
    }

    //Deletar lancamento saida
  /*  @GetMapping("/deletarLanceSaida")
    public String deletarLanceSaida(@RequestParam String collection) {
        return servicosService.deletarSaidaLancada(collection);
    }
*/
    //--------------------LANÇAMENTO SAIDA FIM-------------------------------//



    //--------------------LISTA LANÇAMENTOS INICIO-------------------------------//


    //Lista entradas---------------------------
    @GetMapping("/listaEntradasLancadas")
    public List<LancamentoEntradaModel> listaEntradasLancadas() throws ExecutionException, InterruptedException {
        return servicosService.getLancarEntrada();
    }

    @GetMapping("/recuperarClientes")
    public List<ClienteModel> listaClientesCadastrados() throws ExecutionException, InterruptedException {
        return servicosService.getListaClientesCadastrados();
    }

    //Cadastrar Conta de Saida
    @PostMapping("/cadastrarContaEntrada")
    public ContaEntradaModel contaEntradaModel(@RequestBody ContaEntradaModel contaEntradaModel) throws ExecutionException, InterruptedException {
        return servicosService.cadastrarContaEntrada(contaEntradaModel);
    }
    @GetMapping("/recuperarContasEntrada")
    public List<ContaEntradaModel> listaContasEntradaCadastradas() throws ExecutionException, InterruptedException {
        return servicosService.getListaContasEntradaCadastradas();
    }

    //Cadastrar Conta de entrada-----------------------------

    @PutMapping("/cadastrarContaSaida")
    public ContaSaidaModel contaSaidaModel(@RequestBody ContaSaidaModel contaSaidaModel) {
        return servicosService.cadastrarContaSaida(contaSaidaModel);
    }

    //Deletar lancamento entrada----------------------------

  /*  @GetMapping("/deletarLanceEntrada")
    public String deletarLanceEntrada(@RequestParam String collection) {
        return servicosService.deletarEntradaLancada(collection);
    }*/

    //--------------------LANÇAMENTO ENTRADA FIM-------------------------------//

    @GetMapping("/teste")
    public ResponseEntity<String> testGetEndpoint(){
        return ResponseEntity.ok("<h1>Teste get Endapoint is Working</h1>");
    }


    //CLIENTES
    @PostMapping("/cadastrarCliente")
    public ClienteModel cadastrarCliente(@RequestBody ClienteModel clienteModel) throws ExecutionException, InterruptedException {
        return servicosService.cadastrarCliente(clienteModel);
    }
    @PostMapping("/editarCliente")
    public String editarCliente(@RequestBody ClienteModel clienteModel) throws ExecutionException, InterruptedException {
        return servicosService.editarCliente(clienteModel);
    }


}
