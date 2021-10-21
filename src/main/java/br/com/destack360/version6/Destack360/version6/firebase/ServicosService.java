package br.com.destack360.version6.Destack360.version6.firebase;

import br.com.destack360.version6.Destack360.version6.model.LancamentoEntradaModel;
import br.com.destack360.version6.Destack360.version6.model.LancamentoSaidaModel;
import br.com.destack360.version6.Destack360.version6.model.UserModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class ServicosService {

    //Firebase
    private static final String NOME_COLLECTION_USUARIO_LANCA = "users";
    private static final String NOME_COLLECTION_ACUMULADOS = "ACUMULADOS";
    private static final String NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO = "ACUMULADOS_ENTRADA_DIARIA";
    private static final String NOME_COLLECTION_LANCAMENTO_SAIDA_DIARIA_USUARIO = "ACUMULADOS_ENTRADA_SAIDA";
    public String mensagemReturn = "";



    //Variaveis Lancamento entrada:

    public String identificador;
    public String emailUserLancandoEntrada;
    public String nomeUserLancandoEntrada;
    public String nomeLancamentoEntrada;
    public String dataLancamentoEntrada;
    public String valorLancamentoEntrada;
    public String detalhesLancamentoEntrada;


    //Variaveis Lancamento entrada:

    public String identificadorSaida;
    public String emailUserLancandoSaida;
    public String nomeUserLancandoSaida;
    public String nomeLancamentoSaida;
    public String dataLancamentoSaida;
    public String valorLancamentoSaida;
    public String detalhesLancamentoSaida;



    //Variaveis Usuario:

    public String user_id;
    public String nomeUser;
    public String emailUser;
    public double valorTotalEntradaMensal;
    public double valorTotalSaidaMensal;
    public int quantidadeTotalLancamentosEntradaMensal;
    public int quantidadeTotalLancamentosSaidaMensal;

    //Variaveis Retorno:
    public boolean resultadoLancaEntrada = false;

    //Variareis Teste
    String testeDados;

    //Variaveis helper
    String novoId;



/*
* Lancar entrada, usuario irá digitar data, valor lançamento, tipo de lancamento que seria o nome (Ex: Doação, Dizímo, etc..).
* Talvez seja anonimo esse lancamento e irá precisar ser somado com os valores já acumulado desse usuario
* */
    public String lancarEntrada(LancamentoEntradaModel lancamentoEntradaModel) throws ExecutionException, InterruptedException {

        //Recuperando as informações


        this.dataLancamentoEntrada = lancamentoEntradaModel.getDataLancamentoEntrada();
        this.emailUserLancandoEntrada = lancamentoEntradaModel.getEmailUserLancandoEntrada();
        this.valorLancamentoEntrada = lancamentoEntradaModel.getValorLancamentoEntrada();

        String [] dataRecebida = this.dataLancamentoEntrada.split("/");
        String dataFormatadaLancamentoEntrada = dataRecebida[2]+ "/" +dataRecebida[1]+ "/" +dataRecebida[0];


        String valorLancamentoEntradaRecebido = this.valorLancamentoEntrada;
        String valorLancamentoEntradaLimpo = valorLancamentoEntradaRecebido.replace(",", ".");
        double valorLancamentoEntradaConvertido = Double.parseDouble(valorLancamentoEntradaLimpo);

        if(this.emailUserLancandoEntrada != null){


            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference documentReferenceusuario = firestore.collection(NOME_COLLECTION_ACUMULADOS).document(this.emailUserLancandoEntrada);
            ApiFuture<DocumentSnapshot> usuarioLancaEntrada = documentReferenceusuario.get();
            DocumentSnapshot documentSnapshotUsuario = usuarioLancaEntrada.get();

            if(documentSnapshotUsuario.exists()){

                /*
                 * CASO O DOCUMENTOUSUARIO EXISTA, SIGNIFICA QUE O USUARIO JÁ EFETOU LANÇAMENTO ANTES, ENTÃO NO CASO SERÁ ATUALIZAÇÕES
                 */


                //RECUPERANDO DADOS DO USUARIO PARA AS ATUALIZAÇÕES REFERENTE AO LANÇAMENTO EFETUADO
                UserModel ValoresUserModelLancaEntrada = documentSnapshotUsuario.toObject(UserModel.class);


                //VARIAVEIS LOCAIS RECEBENDO OS VALORES DO LANCAMENTO EFETUADO PELO USUARIO EM QUESTÃO
                this.identificador = lancamentoEntradaModel.getIdentificador();
                this.nomeUserLancandoEntrada = lancamentoEntradaModel.getNomeUserLancandoEntrada();
                this.nomeLancamentoEntrada = lancamentoEntradaModel.getNomeLancamentoEntrada();
                this.dataLancamentoEntrada = lancamentoEntradaModel.getDataLancamentoEntrada();
                this.valorLancamentoEntrada = lancamentoEntradaModel.getValorLancamentoEntrada();
                this.detalhesLancamentoEntrada = lancamentoEntradaModel.getDetalhesLancamentoEntrada();


                //VALORES RECUPERADOS DO USUARIO PARA PREPARAÇÃO DAS DEVIDAS ATUALIZAÇÕES:

                double valorTotalEntradaMensal = ValoresUserModelLancaEntrada.getValorTotalEntradaMensal();

                //PREPARANDO AS VARIAVEIS LOCAIS COM AS TEMPORARIAS PARA ATUALIZAR UM NOVO USERMODEL
                this.user_id = this.emailUserLancandoEntrada;
                this.nomeUser = ValoresUserModelLancaEntrada.getNomeUser();
                this.emailUser = ValoresUserModelLancaEntrada.getEmailUser();
                double resultado = valorLancamentoEntradaConvertido + valorTotalEntradaMensal;
                this.valorTotalEntradaMensal = resultado;
                this.valorTotalSaidaMensal = ValoresUserModelLancaEntrada.getValorTotalSaidaMensal();
                this.quantidadeTotalLancamentosEntradaMensal = ValoresUserModelLancaEntrada.getQuantidadeTotalLancamentosEntradaMensal() + 1;
                this.quantidadeTotalLancamentosSaidaMensal = ValoresUserModelLancaEntrada.getQuantidadeTotalLancamentosSaidaMensal();

               //NOVO USUARIO SENTO INSTANCIADO PARA RECEBER VALORES JÁ TRATADOS-----------------------------
               UserModel usuarioAtualiza = new UserModel();
               //ATRIBUINDO AS VARIAVEIS PARA OS ATRIBUTOS---------------------------------------------------
               usuarioAtualiza.setUser_id(this.user_id);
               usuarioAtualiza.setNomeUser(this.nomeUser);
               usuarioAtualiza.setEmailUser(this.emailUser);
               usuarioAtualiza.setValorTotalEntradaMensal(this.valorTotalEntradaMensal);
               usuarioAtualiza.setValorTotalSaidaMensal(this.valorTotalSaidaMensal);
               usuarioAtualiza.setQuantidadeTotalLancamentosEntradaMensal(this.quantidadeTotalLancamentosEntradaMensal);
               usuarioAtualiza.setQuantidadeTotalLancamentosSaidaMensal(this.quantidadeTotalLancamentosSaidaMensal);

               //NOVO LANCAMENTOMODEL SENTO INSTANCIADO PARA RECEBER VALORES JÁ TRATADOS------------
                LancamentoEntradaModel lancamentoEntradaModelSalva = new LancamentoEntradaModel();
                //ATRIBUINDO AS VARIAVEIS PARA OS ATRIBUTOS------------------------------------------
                lancamentoEntradaModelSalva.setIdentificador(this.identificador);
                lancamentoEntradaModelSalva.setEmailUserLancandoEntrada( this.emailUserLancandoEntrada);
                lancamentoEntradaModelSalva.setNomeUserLancandoEntrada(this.nomeUserLancandoEntrada);
                lancamentoEntradaModelSalva.setNomeLancamentoEntrada(this.nomeLancamentoEntrada);
                lancamentoEntradaModelSalva.setDataLancamentoEntrada(dataFormatadaLancamentoEntrada);
                lancamentoEntradaModelSalva.setValorLancamentoEntrada(this.valorLancamentoEntrada);
                lancamentoEntradaModelSalva.setDetalhesLancamentoEntrada(this.detalhesLancamentoEntrada);

                //CHAMANDO O METODO PARA ATUALIZAR
                atualizaValorTotalEntradaMensalUsuario(usuarioAtualiza,lancamentoEntradaModelSalva);


            }else{
                /*
                 * CASO O DOCUMENTOUSUARIO NÃO EXISTA, SIGNIFICA QUE O USUARIO NUNCA EFETOU LANÇAMENTO ANTES, ENTÃO NO CASO
                 * SERÁ UMA INICIALIZAÇÃO DE LANCAMENTO DE ENTRDA
                 */

                this.nomeUserLancandoEntrada = lancamentoEntradaModel.getNomeUserLancandoEntrada();
                this.nomeLancamentoEntrada = lancamentoEntradaModel.getNomeLancamentoEntrada();
                this.dataLancamentoEntrada = lancamentoEntradaModel.getDataLancamentoEntrada();
                this.valorLancamentoEntrada = lancamentoEntradaModel.getValorLancamentoEntrada();
                this.detalhesLancamentoEntrada = lancamentoEntradaModel.getDetalhesLancamentoEntrada();

                //CHAMANDO O METODO PARA ADICIONAR O LANCAMENTO QUE ESTÁ SENDO FEITO PELO USUARIO PELA 1º VEZ
                adicionaLancamentoUsuario(this.emailUserLancandoEntrada,
                        this.nomeUserLancandoEntrada,
                        this.nomeLancamentoEntrada,
                        this.dataLancamentoEntrada,
                        this.valorLancamentoEntrada,
                        this.detalhesLancamentoEntrada);

                //NOVO USUARIO SENTO INSTANCIADO PARA RECEBER VALORES INICIAIS
                UserModel userModelNovo = new UserModel();
                //ATRIBUINDO AS VARIAVEIS PARA OS ATRIBUTOS
                userModelNovo.setEmailUser(this.emailUserLancandoEntrada);
                userModelNovo.setNomeUser(this.nomeUserLancandoEntrada);
                userModelNovo.setValorTotalEntradaMensal(valorLancamentoEntradaConvertido);
                userModelNovo.setValorTotalSaidaMensal(0);
                userModelNovo.setQuantidadeTotalLancamentosEntradaMensal(1);
                userModelNovo.setQuantidadeTotalLancamentosSaidaMensal(0);

                //INSTANCIANDO UM NOVO MAP PARA SER ADICIONADO AO FIREBASE - FIRESTORE
                Map<String , Object> dadosSalva = new HashMap<>();
                //PREPARANDO AS VARIAVEIS PARA SEREM ADICIONADO AO FIREBASE
                dadosSalva.put("nomeUser" , userModelNovo.getNomeUser());
                dadosSalva.put("emailUser" , userModelNovo.getEmailUser());
                dadosSalva.put("valorTotalEntradaMensal" , userModelNovo.getValorTotalEntradaMensal());
                dadosSalva.put("valorTotalSaidaMensal" , userModelNovo.getValorTotalSaidaMensal());
                dadosSalva.put("quantidadeTotalLancamentosEntradaMensal" , userModelNovo.getQuantidadeTotalLancamentosEntradaMensal());
                dadosSalva.put("quantidadeTotalLancamentosSaidaMensal" , userModelNovo.getQuantidadeTotalLancamentosSaidaMensal());
                //ADICIONADO O LANÇAMENTO QUE ESTÁ SENDO FEITO NO FIREBASE
                firestore.collection(NOME_COLLECTION_ACUMULADOS)
                        .document(userModelNovo.getEmailUser())
                        .set(dadosSalva);

            }
            //QUANDO ACABAR E CASO NÃO RETORNE ERRO SERÁ ATRIBUIDO VERDADEIRO PARA A VARIAVEIS DE RETORNO DO METODO
            this.resultadoLancaEntrada = true;
        }

        if(this.resultadoLancaEntrada){
           this.mensagemReturn = "SUCCESS";
        }else{
            this.mensagemReturn = "RETORNOU FALSO ERROR";
        }

        return this.mensagemReturn ;
    }

//INICIO METODO
    private void atualizaValorTotalEntradaMensalUsuario(UserModel usuarioAtualiza,
                                                        LancamentoEntradaModel lancamentoEntradaModelSalva) throws ExecutionException, InterruptedException {


        //-------------INICIO DATA FORMATADA PARA CRIAÇÃO-------------------------//
        Date data = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String dataCreated = dataFormat.format(data);

        //-------------FIM DATA FORMATADA PARA CRIAÇÃO-------------------------//


        //-------------INICIO INSTANCIANDO FIRESTORE-------------------------//


        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection(NOME_COLLECTION_ACUMULADOS).document(lancamentoEntradaModelSalva.getEmailUserLancandoEntrada()).collection(NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO);

        //-------------FIM INSTANCIANDO FIRESTORE-------------------------//


        //-------------INICIO PREPARACAO DAS VARIAVEIRS DO LANCAMENTO PARA ATUALIZACAO NO FIRESTORE-------------------------//

        this.novoId = String.valueOf(usuarioAtualiza.getQuantidadeTotalLancamentosEntradaMensal());
        this.testeDados = this.novoId;

        LancamentoEntradaModel lancamentoSalvo = new LancamentoEntradaModel();

        lancamentoSalvo.setIdentificador(this.novoId);
        lancamentoSalvo.setEmailUserLancandoEntrada(lancamentoEntradaModelSalva.getEmailUserLancandoEntrada());
        lancamentoSalvo.setNomeUserLancandoEntrada(lancamentoEntradaModelSalva.getNomeUserLancandoEntrada());
        lancamentoSalvo.setNomeLancamentoEntrada(lancamentoEntradaModelSalva.getNomeLancamentoEntrada());
        lancamentoSalvo.setDetalhesLancamentoEntrada(lancamentoEntradaModelSalva.getDetalhesLancamentoEntrada());
        lancamentoSalvo.setValorLancamentoEntrada(lancamentoEntradaModelSalva.getValorLancamentoEntrada());
        lancamentoSalvo.setDataLancamentoEntrada(lancamentoEntradaModelSalva.getDataLancamentoEntrada());
        lancamentoSalvo.setCreatedLancamentoEntrada(dataCreated);
        lancamentoSalvo.setModifieldLancamentoEntrada("Nenhuma Modificação");


        Map<String , Object> LancamentoEntrada = new HashMap<>();

        LancamentoEntrada.put("identificador" , lancamentoSalvo.getIdentificador());
        LancamentoEntrada.put("emailUserLancandoEntrada" , lancamentoSalvo.getEmailUserLancandoEntrada());
        LancamentoEntrada.put("nomeUserLancandoEntrada" , lancamentoSalvo.getNomeUserLancandoEntrada());
        LancamentoEntrada.put("nomeLancamentoEntrada" , lancamentoSalvo.getNomeLancamentoEntrada());
        LancamentoEntrada.put("dataLancamentoEntrada" , lancamentoSalvo.getDataLancamentoEntrada());
        LancamentoEntrada.put("valorLancamentoEntrada" , lancamentoSalvo.getValorLancamentoEntrada());
        LancamentoEntrada.put("detalhesLancamentoEntrada" , lancamentoSalvo.getDetalhesLancamentoEntrada());
        LancamentoEntrada.put("createdLancamentoEntrada" , lancamentoSalvo.getCreatedLancamentoEntrada());
        LancamentoEntrada.put("modifieldLancamentoEntrada" , lancamentoSalvo.getModifieldLancamentoEntrada());



        firestore.collection(NOME_COLLECTION_ACUMULADOS)
                .document(lancamentoEntradaModelSalva.getEmailUserLancandoEntrada())
                .collection(NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO)
                .document(lancamentoSalvo.getIdentificador())
                .set(LancamentoEntrada);
        //-------------FIM PREPARACAO DAS VARIAVEIRS DO LANCAMENTO PARA ATUALIZACAO NO FIRESTORE-------------------------//


        //-------------INICIO PREPARACAO DAS VARIAVEIRS DO USUARIO PARA ATUALIZACAO NO FIRESTORE-------------------------//



        Map<String , Object> dadosSalva = new HashMap<>();

        dadosSalva.put("nomeUser" , usuarioAtualiza.getNomeUser());
        dadosSalva.put("emailUser" , usuarioAtualiza.getEmailUser());
        dadosSalva.put("valorTotalEntradaMensal" , usuarioAtualiza.getValorTotalEntradaMensal());
        dadosSalva.put("valorTotalSaidaMensal" , usuarioAtualiza.getValorTotalSaidaMensal());
        dadosSalva.put("quantidadeTotalLancamentosEntradaMensal" , usuarioAtualiza.getQuantidadeTotalLancamentosEntradaMensal());
        dadosSalva.put("quantidadeTotalLancamentosSaidaMensal" , usuarioAtualiza.getQuantidadeTotalLancamentosSaidaMensal());

        firestore.collection(NOME_COLLECTION_ACUMULADOS)
                .document(usuarioAtualiza.getEmailUser())
                .set(dadosSalva);

    //-------------FIM PREPARACAO DAS VARIAVEIRS DO USUARIO PARA ATUALIZACAO NO FIRESTORE-------------------------//

    }
//FIM METODO

//INICIO METODO
    private void adicionaLancamentoUsuario(String emailUserLancandoEntrada,
                                           String nomeUserLancandoEntrada,
                                           String nomeLancamentoEntrada,
                                           String dataLancamentoEntrada,
                                           String valorLancamentoEntrada,
                                           String detalhesLancamentoEntrada) {
        //INSTANCIANDO UMA NOVA REFERENCIA AO FIREBASE CONECTADO
        Firestore firestore = FirestoreClient.getFirestore();

        //-------------INICIO DATA FORMATADA PARA CRIAÇÃO-------------------------//
        Date data = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String dataCreated = dataFormat.format(data);

        //-------------FIM DATA FORMATADA PARA CRIAÇÃO-------------------------//


        //Valores para Lancamento Entrada
        String [] dataRecebida = dataLancamentoEntrada.split("/");
        String dataFormatadaLancamentoEntrada = dataRecebida[2]+ "/" +dataRecebida[1]+ "/" +dataRecebida[0];

        //INSTANCIANDO  NOVO LANCAMENTO DE ENTRADA
        LancamentoEntradaModel lancamentoEntradaModelSalva = new LancamentoEntradaModel();
        //ATRIBUINDO OS PARAMETROS RECEBIDOS AOS ATRIBUTOS DO NOVO LANCAMENTO DE ENTRADA INSTANCIADO
        lancamentoEntradaModelSalva.setIdentificador("1");
        lancamentoEntradaModelSalva.setEmailUserLancandoEntrada(emailUserLancandoEntrada);
        lancamentoEntradaModelSalva.setNomeUserLancandoEntrada(nomeUserLancandoEntrada);
        lancamentoEntradaModelSalva.setNomeLancamentoEntrada(nomeLancamentoEntrada);
        lancamentoEntradaModelSalva.setDataLancamentoEntrada(dataFormatadaLancamentoEntrada);
        lancamentoEntradaModelSalva.setValorLancamentoEntrada(valorLancamentoEntrada);
        lancamentoEntradaModelSalva.setDataLancamentoEntrada(detalhesLancamentoEntrada);
        lancamentoEntradaModelSalva.setCreatedLancamentoEntrada(dataCreated);
        lancamentoEntradaModelSalva.setModifieldLancamentoEntrada("Nenhuma Modificação");

        //INSTANCIANDO 1º OBJETO DO TIPO MAP PARA SER ADICIONADO AO FIRESTORE DO FIREBASE
        Map<String , Object> LancamentoEntrada = new HashMap<>();
        //ATRIBUINDO OS VALORES DO 1º OBJETO LANCAMENTO DE ENTRADA AOS VALORES DOS ATRIBUTOS DO 1º OBJETO MAP
        LancamentoEntrada.put("identificador" , "1");
        LancamentoEntrada.put("emailUserLancandoEntrada" , lancamentoEntradaModelSalva.getEmailUserLancandoEntrada());
        LancamentoEntrada.put("nomeUserLancandoEntrada" , lancamentoEntradaModelSalva.getNomeUserLancandoEntrada());
        LancamentoEntrada.put("nomeLancamentoEntrada" , lancamentoEntradaModelSalva.getNomeLancamentoEntrada());
        LancamentoEntrada.put("dataLancamentoEntrada" , lancamentoEntradaModelSalva.getDataLancamentoEntrada());
        LancamentoEntrada.put("valorLancamentoEntrada" , lancamentoEntradaModelSalva.getValorLancamentoEntrada());
        LancamentoEntrada.put("detalhesLancamentoEntrada" , lancamentoEntradaModelSalva.getDetalhesLancamentoEntrada());
        LancamentoEntrada.put("createdLancamentoEntrada" , lancamentoEntradaModelSalva.getCreatedLancamentoEntrada());
        LancamentoEntrada.put("modifieldLancamentoEntrada" , lancamentoEntradaModelSalva.getModifieldLancamentoEntrada());


            //CRIANDO O 1º NÓ DE LANCAMENTO DE ENTRADA DO USUARIO E ADICIONADO O OBJETO DO TIPO MAP AO FIRESTORE DO FIREBASE
                firestore.collection(NOME_COLLECTION_ACUMULADOS)
                .document(lancamentoEntradaModelSalva.getEmailUserLancandoEntrada())
                .collection(NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO)
                .document("1")
                .set(lancamentoEntradaModelSalva);


    }

//FIM METODO

    public String lancarSaida(LancamentoSaidaModel lancamentoSaidaModel) throws ExecutionException, InterruptedException {

        //Recuperando as informações


        this.dataLancamentoSaida = lancamentoSaidaModel.getDataLancamentoSaida();
        this.emailUserLancandoSaida = lancamentoSaidaModel.getEmailUserLancandoSaida();
        this.valorLancamentoSaida = lancamentoSaidaModel.getValorLancamentoSaida();

        String [] dataRecebida = this.dataLancamentoSaida.split("/");
        String dataFormatadaLancamentoEntrada = dataRecebida[2]+ "/" +dataRecebida[1]+ "/" +dataRecebida[0];


        String valorLancamentoEntradaRecebido = this.valorLancamentoEntrada;
        String valorLancamentoEntradaLimpo = valorLancamentoEntradaRecebido.replace(",", ".");
        double valorLancamentoEntradaConvertido = Double.parseDouble(valorLancamentoEntradaLimpo);

        if(this.emailUserLancandoSaida != null){


            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference documentReferenceusuario = firestore.collection(NOME_COLLECTION_ACUMULADOS).document(this.emailUserLancandoSaida);
            ApiFuture<DocumentSnapshot> usuarioLancaEntrada = documentReferenceusuario.get();
            DocumentSnapshot documentSnapshotUsuario = usuarioLancaEntrada.get();

            if(documentSnapshotUsuario.exists()){

                /*
                 * CASO O DOCUMENTOUSUARIO EXISTA, SIGNIFICA QUE O USUARIO JÁ EFETOU LANÇAMENTO ANTES, ENTÃO NO CASO SERÁ ATUALIZAÇÕES
                 */


                //RECUPERANDO DADOS DO USUARIO PARA AS ATUALIZAÇÕES REFERENTE AO LANÇAMENTO EFETUADO
                UserModel ValoresUserModelLancaEntrada = documentSnapshotUsuario.toObject(UserModel.class);


                //VARIAVEIS LOCAIS RECEBENDO OS VALORES DO LANCAMENTO EFETUADO PELO USUARIO EM QUESTÃO
                this.identificadorSaida = lancamentoSaidaModel.getIdentificador();
                this.nomeUserLancandoSaida = lancamentoSaidaModel.getNomeUserLancandoSaida();
                this.emailUserLancandoSaida = lancamentoSaidaModel.getEmailUserLancandoSaida();
                this.nomeLancamentoSaida = lancamentoSaidaModel.getNomeLancamentoSaida();
                this.dataLancamentoSaida = lancamentoSaidaModel.getDataLancamentoSaida();
                this.valorLancamentoSaida = lancamentoSaidaModel.getValorLancamentoSaida();
                this.detalhesLancamentoSaida = lancamentoSaidaModel.getDetalhesLancamentoSaida();


                //VALORES RECUPERADOS DO USUARIO PARA PREPARAÇÃO DAS DEVIDAS ATUALIZAÇÕES:

                double valorTotalEntradaMensal = ValoresUserModelLancaEntrada.getValorTotalEntradaMensal();


                //PREPARANDO AS VARIAVEIS LOCAIS COM AS TEMPORARIAS PARA ATUALIZAR UM NOVO USERMODEL
                this.user_id = this.emailUserLancandoSaida;
                this.nomeUser = ValoresUserModelLancaEntrada.getNomeUser();
                this.emailUser = ValoresUserModelLancaEntrada.getEmailUser();
                double resultado = valorLancamentoEntradaConvertido + valorTotalEntradaMensal;
                this.valorTotalEntradaMensal = resultado;
                this.valorTotalSaidaMensal = ValoresUserModelLancaEntrada.getValorTotalSaidaMensal();
                this.quantidadeTotalLancamentosEntradaMensal = ValoresUserModelLancaEntrada.getQuantidadeTotalLancamentosEntradaMensal() + 1;
                this.quantidadeTotalLancamentosSaidaMensal = ValoresUserModelLancaEntrada.getQuantidadeTotalLancamentosSaidaMensal();

                //NOVO USUARIO SENTO INSTANCIADO PARA RECEBER VALORES JÁ TRATADOS-----------------------------
                UserModel usuarioAtualiza = new UserModel();
                //ATRIBUINDO AS VARIAVEIS PARA OS ATRIBUTOS---------------------------------------------------
                usuarioAtualiza.setUser_id(this.user_id);
                usuarioAtualiza.setNomeUser(this.nomeUser);
                usuarioAtualiza.setEmailUser(this.emailUser);
                usuarioAtualiza.setValorTotalEntradaMensal(this.valorTotalEntradaMensal);
                usuarioAtualiza.setValorTotalSaidaMensal(this.valorTotalSaidaMensal);
                usuarioAtualiza.setQuantidadeTotalLancamentosEntradaMensal(this.quantidadeTotalLancamentosEntradaMensal);
                usuarioAtualiza.setQuantidadeTotalLancamentosSaidaMensal(this.quantidadeTotalLancamentosSaidaMensal);

                //NOVO LANCAMENTOMODEL SENTO INSTANCIADO PARA RECEBER VALORES JÁ TRATADOS------------
                LancamentoEntradaModel lancamentoEntradaModelSalva = new LancamentoEntradaModel();
                //ATRIBUINDO AS VARIAVEIS PARA OS ATRIBUTOS------------------------------------------
                lancamentoEntradaModelSalva.setIdentificador(this.identificador);
                lancamentoEntradaModelSalva.setEmailUserLancandoEntrada( this.emailUserLancandoEntrada);
                lancamentoEntradaModelSalva.setNomeUserLancandoEntrada(this.nomeUserLancandoEntrada);
                lancamentoEntradaModelSalva.setNomeLancamentoEntrada(this.nomeLancamentoEntrada);
                lancamentoEntradaModelSalva.setDataLancamentoEntrada(dataFormatadaLancamentoEntrada);
                lancamentoEntradaModelSalva.setValorLancamentoEntrada(this.valorLancamentoEntrada);
                lancamentoEntradaModelSalva.setDetalhesLancamentoEntrada(this.detalhesLancamentoEntrada);

                //CHAMANDO O METODO PARA ATUALIZAR
                atualizaValorTotalEntradaMensalUsuario(usuarioAtualiza,lancamentoEntradaModelSalva);


            }else{
                /*
                 * CASO O DOCUMENTOUSUARIO NÃO EXISTA, SIGNIFICA QUE O USUARIO NUNCA EFETOU LANÇAMENTO ANTES, ENTÃO NO CASO
                 * SERÁ UMA INICIALIZAÇÃO DE LANCAMENTO DE SAIDA
                 */

                this.nomeUserLancandoSaida = lancamentoSaidaModel.getNomeUserLancandoSaida();
                this.emailUserLancandoSaida = lancamentoSaidaModel.getEmailUserLancandoSaida();
                this.nomeLancamentoSaida = lancamentoSaidaModel.getNomeLancamentoSaida();
                this.dataLancamentoSaida = lancamentoSaidaModel.getDataLancamentoSaida();
                this.valorLancamentoSaida = lancamentoSaidaModel.getValorLancamentoSaida();
                this.detalhesLancamentoSaida = lancamentoSaidaModel.getDetalhesLancamentoSaida();

                //CHAMANDO O METODO PARA ADICIONAR O LANCAMENTO QUE ESTÁ SENDO FEITO PELO USUARIO PELA 1º VEZ
                adicionaLancamentoSaidaUsuario(this.emailUserLancandoSaida,
                        this.nomeUserLancandoSaida,
                        this.nomeLancamentoSaida,
                        this.dataLancamentoSaida,
                        this.valorLancamentoSaida,
                        this.detalhesLancamentoSaida);

                //NOVO USUARIO SENTO INSTANCIADO PARA RECEBER VALORES INICIAIS
                UserModel userModelNovo = new UserModel();
                //ATRIBUINDO AS VARIAVEIS PARA OS ATRIBUTOS
                userModelNovo.setEmailUser(this.emailUserLancandoEntrada);
                userModelNovo.setNomeUser(this.nomeUserLancandoEntrada);
                userModelNovo.setValorTotalEntradaMensal(valorLancamentoEntradaConvertido);
                userModelNovo.setValorTotalSaidaMensal(0);
                userModelNovo.setQuantidadeTotalLancamentosEntradaMensal(1);
                userModelNovo.setQuantidadeTotalLancamentosSaidaMensal(0);

                //INSTANCIANDO UM NOVO MAP PARA SER ADICIONADO AO FIREBASE - FIRESTORE
                Map<String , Object> dadosSalva = new HashMap<>();
                //PREPARANDO AS VARIAVEIS PARA SEREM ADICIONADO AO FIREBASE
                dadosSalva.put("nomeUser" , userModelNovo.getNomeUser());
                dadosSalva.put("emailUser" , userModelNovo.getEmailUser());
                dadosSalva.put("valorTotalEntradaMensal" , userModelNovo.getValorTotalEntradaMensal());
                dadosSalva.put("valorTotalSaidaMensal" , userModelNovo.getValorTotalSaidaMensal());
                dadosSalva.put("quantidadeTotalLancamentosEntradaMensal" , userModelNovo.getQuantidadeTotalLancamentosEntradaMensal());
                dadosSalva.put("quantidadeTotalLancamentosSaidaMensal" , userModelNovo.getQuantidadeTotalLancamentosSaidaMensal());
                //ADICIONADO O LANÇAMENTO QUE ESTÁ SENDO FEITO NO FIREBASE
                firestore.collection(NOME_COLLECTION_ACUMULADOS)
                        .document(userModelNovo.getEmailUser())
                        .set(dadosSalva);

            }
            //QUANDO ACABAR E CASO NÃO RETORNE ERRO SERÁ ATRIBUIDO VERDADEIRO PARA A VARIAVEIS DE RETORNO DO METODO
            this.resultadoLancaEntrada = true;
        }

        if(this.resultadoLancaEntrada){
            this.mensagemReturn = "SUCCESS";
        }else{
            this.mensagemReturn = "RETORNOU FALSO ERROR";
        }

        return this.mensagemReturn ;
    }

    //INICIO METODO
    private void adicionaLancamentoSaidaUsuario(String emailUserLancandoSaida,
                                                String nomeUserLancandoSaida,
                                                String nomeLancamentoSaida,
                                                String dataLancamentoSaida,
                                                String valorLancamentoSaida,
                                                String detalhesLancamentoSaida) {

        //INSTANCIANDO UMA NOVA REFERENCIA AO FIREBASE CONECTADO
        Firestore firestore = FirestoreClient.getFirestore();

        //-------------INICIO DATA FORMATADA PARA CRIAÇÃO-------------------------//
        Date data = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String dataCreated = dataFormat.format(data);

        //-------------FIM DATA FORMATADA PARA CRIAÇÃO-------------------------//


        //Valores para Lancamento Entrada
        String [] dataRecebida = dataLancamentoSaida.split("/");
        String dataFormatadaLancamentoEntrada = dataRecebida[2]+ "/" +dataRecebida[1]+ "/" +dataRecebida[0];

        //INSTANCIANDO  NOVO LANCAMENTO DE ENTRADA
        LancamentoSaidaModel lancamentoSaidaModel = new LancamentoSaidaModel();
        //ATRIBUINDO OS PARAMETROS RECEBIDOS AOS ATRIBUTOS DO NOVO LANCAMENTO DE ENTRADA INSTANCIADO
        lancamentoSaidaModel.setIdentificador("1");
        lancamentoSaidaModel.setEmailUserLancandoSaida(emailUserLancandoSaida);
        lancamentoSaidaModel.setNomeUserLancandoSaida(nomeUserLancandoSaida);
        lancamentoSaidaModel.setNomeLancamentoSaida(nomeLancamentoSaida);
        lancamentoSaidaModel.setDataLancamentoSaida(dataFormatadaLancamentoEntrada);
        lancamentoSaidaModel.setValorLancamentoSaida(valorLancamentoSaida);
        lancamentoSaidaModel.setDetalhesLancamentoSaida(detalhesLancamentoSaida);
        lancamentoSaidaModel.setCreatedLancamentoSaida(dataCreated);
        lancamentoSaidaModel.setModifieldLancamentoSaida("Nenhuma Modificação");

        //INSTANCIANDO 1º OBJETO DO TIPO MAP PARA SER ADICIONADO AO FIRESTORE DO FIREBASE
        Map<String , Object> LancamentoSaida = new HashMap<>();
        //ATRIBUINDO OS VALORES DO 1º OBJETO LANCAMENTO DE ENTRADA AOS VALORES DOS ATRIBUTOS DO 1º OBJETO MAP
        LancamentoSaida.put("identificador" , "1");
        LancamentoSaida.put("emailUserLancandoSaida" , lancamentoSaidaModel.getEmailUserLancandoSaida());
        LancamentoSaida.put("nomeUserLancandoSaida" , lancamentoSaidaModel.getNomeUserLancandoSaida());
        LancamentoSaida.put("nomeLancamentoSaida" , lancamentoSaidaModel.getNomeLancamentoSaida());
        LancamentoSaida.put("dataLancamentoSaida" , lancamentoSaidaModel.getDataLancamentoSaida());
        LancamentoSaida.put("valorLancamentoSaida" , lancamentoSaidaModel.getValorLancamentoSaida());
        LancamentoSaida.put("detalhesLancamentoSaida" , lancamentoSaidaModel.getDetalhesLancamentoSaida());
        LancamentoSaida.put("createdLancamentoSaida" , lancamentoSaidaModel.getCreatedLancamentoSaida());
        LancamentoSaida.put("modifieldLancamentoSaida" , lancamentoSaidaModel.getModifieldLancamentoSaida());


        //CRIANDO O 1º NÓ DE LANCAMENTO DE ENTRADA DO USUARIO E ADICIONADO O OBJETO DO TIPO MAP AO FIRESTORE DO FIREBASE
        firestore.collection(NOME_COLLECTION_ACUMULADOS)
                .document(lancamentoSaidaModel.getEmailUserLancandoSaida())
                .collection(NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO)
                .document("1")
                .set(LancamentoSaida);


    }
//FIM METODO



    public String editarLancamentoEntrada(LancamentoEntradaModel lancamentoEntradaModel) {
        return null;
    }

    public String editarLancamentoSaida(LancamentoEntradaModel lancamentoEntradaModel) {
        return null;
    }

    public List<LancamentoEntradaModel> getLancarEntrada() throws ExecutionException, InterruptedException {
        List<LancamentoEntradaModel> resultado = new ArrayList<>();

            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference collectionReference = firestore.collection(NOME_COLLECTION_ACUMULADOS);

            ApiFuture<QuerySnapshot> query = collectionReference.get();
            List<QueryDocumentSnapshot> documentSnapshots = query.get().getDocuments();
            for(QueryDocumentSnapshot doc : documentSnapshots){
                LancamentoEntradaModel entradaSalda = doc.toObject(LancamentoEntradaModel.class);
                resultado.add(entradaSalda);




    }
        return resultado;
    /*public LancamentoSaidaModel getLancarSaida(String collection) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(NOME_COLLECTION_ACUMULADOS_ENTRADA).document(collection).collection("LANCAMENTOS_ENTRADA_DIARIA").document();
        ApiFuture<DocumentSnapshot> listaEntradaLancada = documentReference.get();
        DocumentSnapshot documentSnapshot = listaEntradaLancada.get();

        LancamentoSaidaModel lancamentoSaidaModel;
        if(documentSnapshot.exists()){
            lancamentoSaidaModel  = documentSnapshot.toObject(LancamentoSaidaModel.class);
            return lancamentoSaidaModel;
        }

        return null;
    }

    public String deletarSaidaLancada(String collection) {
        return "";
    }

    public String deletarEntradaLancada(String collection) {
        return "";
    }*/
}
}
