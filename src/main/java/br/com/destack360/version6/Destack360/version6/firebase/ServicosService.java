package br.com.destack360.version6.Destack360.version6.firebase;

import br.com.destack360.version6.Destack360.version6.model.ClienteModel;
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
    private String NOME_COLLECTION_ACUMULADOS_ENTRADA_USUARIO = "ACUMULADOS_";
    private static final String NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO = "ACUMULADOS_ENTRADA_DIARIA";
    private static final String NOME_COLLECTION_LANCAMENTO_SAIDA_DIARIA_USUARIO = "ACUMULADOS_SAIDA_DIARIA";
    private static final String NOME_COLLECTION_CLIENTE = "CLIENTES";




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

    //Variaveis Cliente Cadatrar/Cadastrado
    public String identificadorCliente;
    public String razaoSocial;
    public String CNPJ;
    public String Usuario;
    public String emailCliente;
    public String telefone;
    public String celular;
    public String OBS;



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
    String mesReferencia;
    String nomeCollectionMesReferencia;
    String mensagemReturn = "";
    int quantidadeRecuperadaLancaSaida;
    int quantidadeRecuperaLancaEntrada;
    double valorRecuperadoLancadoEntrada;


    /*
     * Lancar entrada, usuario irá digitar data, valor lançamento, tipo de lancamento que seria o nome (Ex: Doação, Dizímo, etc..).
     * Talvez seja anonimo esse lancamento e irá precisar ser somado com os valores já acumulado desse usuario
     * */
    public String lancarEntrada(LancamentoEntradaModel lancamentoEntradaModel) throws ExecutionException, InterruptedException {

        //Recuperando as informações


        this.dataLancamentoEntrada = lancamentoEntradaModel.getDataLancamentoEntrada();
        this.emailUserLancandoEntrada = lancamentoEntradaModel.getEmailUserLancandoEntrada();
        this.valorLancamentoEntrada = lancamentoEntradaModel.getValorLancamentoEntrada();



        //Configurando
        String [] dataRecebida = this.dataLancamentoEntrada.split("/");
        String dataFormatadaLancamentoEntrada = dataRecebida[2]+ "/" +dataRecebida[1]+ "/" +dataRecebida[0];

        int valorMesReferenciaLancado = Integer.parseInt(dataRecebida[1]);

        switch (valorMesReferenciaLancado){
            case 1:
                this.mesReferencia = "JANEIRO";
                break;
            case 2:
                this.mesReferencia = "FEVEREIRO";
                break;
            case 3:
                this.mesReferencia = "MARÇO";
                break;
            case 4:
                this.mesReferencia = "ABRIL";
                break;
            case 5:
                this.mesReferencia = "MAIO";
                break;
            case 6:
                this.mesReferencia = "JUNHO";
                break;
            case 7:
                this.mesReferencia = "JULHO";
                break;
            case 8:
                this.mesReferencia = "AGOSTO";
                break;
            case 9:
                this.mesReferencia = "SETEMBRO";
                break;
            case 10:
                this.mesReferencia = "OUTUBRO";
                break;
            case 11:
                this.mesReferencia = "NOVEMBRO";
                break;
            case 12:
                this.mesReferencia = "DEZEMBRO";
                break;

                default:
                break;

        }


        this.nomeCollectionMesReferencia = "ACUMULADO_MES_"+this.mesReferencia;

        String valorLancamentoEntradaRecebido = this.valorLancamentoEntrada;
        String valorLancamentoEntradaLimpo = valorLancamentoEntradaRecebido.replace(",", ".");
        double valorLancamentoEntradaConvertido = Double.parseDouble(valorLancamentoEntradaLimpo);





        if(this.emailUserLancandoEntrada != null){

            String nomeUsuario = lancamentoEntradaModel.getNomeUserLancandoEntrada();
            String nomeMaiusculo = nomeUsuario.toUpperCase();
            String nomeCollectionAcumuladoUsuario = "ACUMULADO_"+nomeMaiusculo;


            Firestore firestoreCollectionMesReferencia = FirestoreClient.getFirestore();
            DocumentReference documentReferenceusuario = firestoreCollectionMesReferencia.collection(NOME_COLLECTION_ACUMULADOS)
                    .document(this.emailUserLancandoEntrada)
                   .collection(nomeCollectionAcumuladoUsuario)
                    .document(this.nomeCollectionMesReferencia);

            ApiFuture<DocumentSnapshot> usuarioLancaEntrada = documentReferenceusuario.get();
            DocumentSnapshot documentSnapshotUsuario = usuarioLancaEntrada.get();

            if(documentSnapshotUsuario.exists()){

                /*
                 * CASO O DOCUMENTOUSUARIO EXISTA, SIGNIFICA QUE O USUARIO JÁ EFETOU LANÇAMENTO ANTES, ENTÃO NO CASO SERÁ ATUALIZAÇÕES
                 */

                Firestore firestoreAcumuldoMesReferencia = FirestoreClient.getFirestore();
                DocumentReference documentReferenceusuarioMesReferencia = firestoreAcumuldoMesReferencia.collection(NOME_COLLECTION_ACUMULADOS)
                        .document(this.emailUserLancandoEntrada)
                        .collection(nomeCollectionAcumuladoUsuario)
                        .document(this.nomeCollectionMesReferencia);
                ApiFuture<DocumentSnapshot> usuarioLancaEntradaMesReferencia = documentReferenceusuarioMesReferencia.get();
                DocumentSnapshot documentSnapshotUsuarioMesReferencia = usuarioLancaEntradaMesReferencia.get();



                //RECUPERANDO DADOS DO USUARIO PARA AS ATUALIZAÇÕES REFERENTE AO LANÇAMENTO EFETUADO
                UserModel ValoresUserModelLancaEntrada = documentSnapshotUsuarioMesReferencia.toObject(UserModel.class);


                //VARIAVEIS LOCAIS RECEBENDO OS VALORES DO LANCAMENTO EFETUADO PELO USUARIO EM QUESTÃO
                this.identificador = lancamentoEntradaModel.getIdentificador();
                this.nomeUserLancandoEntrada = lancamentoEntradaModel.getNomeUserLancandoEntrada();
                this.nomeLancamentoEntrada = lancamentoEntradaModel.getNomeLancamentoEntrada();
                this.dataLancamentoEntrada = lancamentoEntradaModel.getDataLancamentoEntrada();
                this.valorLancamentoEntrada = lancamentoEntradaModel.getValorLancamentoEntrada();
                this.detalhesLancamentoEntrada = lancamentoEntradaModel.getDetalhesLancamentoEntrada();


                //VALORES RECUPERADOS DO USUARIO PARA PREPARAÇÃO DAS DEVIDAS ATUALIZAÇÕES:

                assert ValoresUserModelLancaEntrada != null;
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

                this.mensagemReturn = "Sucesso ao adicionar novo lancamento entrada";
                this.resultadoLancaEntrada = true;

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
                firestoreCollectionMesReferencia.collection(NOME_COLLECTION_ACUMULADOS)
                        .document(userModelNovo.getEmailUser())
                        .collection(nomeCollectionAcumuladoUsuario)
                        .document(this.nomeCollectionMesReferencia)
                        .set(dadosSalva);
                this.mensagemReturn = "Sucesso ao criar novo lancamento entrada";
                this.resultadoLancaEntrada = true;
            }
            //QUANDO ACABAR E CASO NÃO RETORNE ERRO SERÁ ATRIBUIDO VERDADEIRO PARA A VARIAVEIS DE RETORNO DO METODO

        }



        return this.mensagemReturn ;
    }

    //INICIO METODO
    private void atualizaValorTotalEntradaMensalUsuario(UserModel usuarioAtualiza,
                                                        LancamentoEntradaModel lancamentoEntradaModelSalva) throws ExecutionException, InterruptedException {


        String nomeUsuario = usuarioAtualiza.getNomeUser();
        String nomeMaiusculo = nomeUsuario.toUpperCase();
        String nomeCollectionAcumuladoUsuario = "ACUMULADO_" +nomeMaiusculo;




        //-------------INICIO DATA FORMATADA PARA CRIAÇÃO-------------------------//
        Date data = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String dataCreated = dataFormat.format(data);

        //-------------FIM DATA FORMATADA PARA CRIAÇÃO-------------------------//


        //-------------INICIO INSTANCIANDO FIRESTORE-------------------------//


        Firestore firestore = FirestoreClient.getFirestore();
        //firestore.collection(NOME_COLLECTION_ACUMULADOS).document(lancamentoEntradaModelSalva.getEmailUserLancandoEntrada()).collection(NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO);

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
                .collection(nomeCollectionAcumuladoUsuario)
                .document(this.nomeCollectionMesReferencia)
                .collection(NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO)
                .document(lancamentoSalvo.getIdentificador())
                .set(LancamentoEntrada);


/*
        firestore.collection(NOME_COLLECTION_ACUMULADOS)
                .document(usuarioAtualiza.getEmailUser())
                .collection(NOME_COLLECTION_ACUMULADOS_ENTRADA_USUARIO)
                .document(this.nomeCollectionMesReferencia)
                .collection(NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO)
                .document(lancamentoSalvo.getIdentificador())
                .set(LancamentoEntrada);*/
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
                .collection(nomeCollectionAcumuladoUsuario)
                .document(this.nomeCollectionMesReferencia)
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

        String nomeUsuario = nomeUserLancandoEntrada;
        String nomeMaiusculo = nomeUsuario.toUpperCase();
        String nomeCollectionAcumuladoUsuario = "ACUMULADO_" +nomeMaiusculo;


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
        lancamentoEntradaModelSalva.setDetalhesLancamentoEntrada(detalhesLancamentoEntrada);
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
                .collection(nomeCollectionAcumuladoUsuario)
                .document(this.nomeCollectionMesReferencia)
                .collection(NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO)
                .document("1")
                .set(lancamentoEntradaModelSalva);


    }

//FIM METODO

    public String lancarSaida(LancamentoEntradaModel lancamentoEntradaModel) throws ExecutionException, InterruptedException {

        //Recuperando as informações


        this.dataLancamentoEntrada = lancamentoEntradaModel.getDataLancamentoEntrada();
        this.emailUserLancandoEntrada = lancamentoEntradaModel.getEmailUserLancandoEntrada();
        this.valorLancamentoEntrada = lancamentoEntradaModel.getValorLancamentoEntrada();



        //Configurando
        String [] dataRecebida = this.dataLancamentoEntrada.split("/");
        String dataFormatadaLancamentoEntrada = dataRecebida[2]+ "/" +dataRecebida[1]+ "/" +dataRecebida[0];

        int valorMesReferenciaLancado = Integer.parseInt(dataRecebida[1]);

        switch (valorMesReferenciaLancado){
            case 1:
                this.mesReferencia = "JANEIRO";
                break;
            case 2:
                this.mesReferencia = "FEVEREIRO";
                break;
            case 3:
                this.mesReferencia = "MARÇO";
                break;
            case 4:
                this.mesReferencia = "ABRIL";
                break;
            case 5:
                this.mesReferencia = "MAIO";
                break;
            case 6:
                this.mesReferencia = "JUNHO";
                break;
            case 7:
                this.mesReferencia = "JULHO";
                break;
            case 8:
                this.mesReferencia = "AGOSTO";
                break;
            case 9:
                this.mesReferencia = "SETEMBRO";
                break;
            case 10:
                this.mesReferencia = "OUTUBRO";
                break;
            case 11:
                this.mesReferencia = "NOVEMBRO";
                break;
            case 12:
                this.mesReferencia = "DEZEMBRO";
                break;

            default:
                break;

        }


        this.nomeCollectionMesReferencia = "ACUMULADO_MES_"+this.mesReferencia;

        String valorLancamentoEntradaRecebido = this.valorLancamentoEntrada;
        String valorLancamentoEntradaLimpo = valorLancamentoEntradaRecebido.replace(",", ".");
        double valorLancamentoEntradaConvertido = Double.parseDouble(valorLancamentoEntradaLimpo);





        if(this.emailUserLancandoEntrada != null){

            String nomeUsuario = lancamentoEntradaModel.getNomeUserLancandoEntrada();
            String nomeMaiusculo = nomeUsuario.toUpperCase();
            String nomeCollectionAcumuladoUsuario = "ACUMULADO_"+nomeMaiusculo;

            Firestore firestoreCollectionMesReferenciaVerifica = FirestoreClient.getFirestore();
            DocumentReference documentReferenceusuarioVerifica = firestoreCollectionMesReferenciaVerifica.collection(NOME_COLLECTION_ACUMULADOS)
                    .document(this.emailUserLancandoEntrada)
                    .collection(nomeCollectionAcumuladoUsuario)
                    .document(this.nomeCollectionMesReferencia);

            ApiFuture<DocumentSnapshot> usuarioLancaEntradaVerifica = documentReferenceusuarioVerifica.get();
            DocumentSnapshot documentSnapshotUsuarioVerifica = usuarioLancaEntradaVerifica.get();

            if(documentSnapshotUsuarioVerifica.exists()) {

                UserModel ValoresUserModelLancaEntrada = documentSnapshotUsuarioVerifica.toObject(UserModel.class);
                this.quantidadeRecuperaLancaEntrada = ValoresUserModelLancaEntrada.getQuantidadeTotalLancamentosEntradaMensal();
                this.valorRecuperadoLancadoEntrada = ValoresUserModelLancaEntrada.getValorTotalEntradaMensal();
            }else {
                this.quantidadeRecuperaLancaEntrada = 0;
            }




                Firestore firestoreCollectionMesReferencia = FirestoreClient.getFirestore();
            DocumentReference documentReferenceusuario = firestoreCollectionMesReferencia.collection(NOME_COLLECTION_ACUMULADOS)
                    .document(this.emailUserLancandoEntrada)
                    .collection(nomeCollectionAcumuladoUsuario)
                    .document(this.nomeCollectionMesReferencia)
                    .collection(NOME_COLLECTION_LANCAMENTO_SAIDA_DIARIA_USUARIO)
                    .document("1");

            ApiFuture<DocumentSnapshot> usuarioLancaEntrada = documentReferenceusuario.get();
            DocumentSnapshot documentSnapshotUsuario = usuarioLancaEntrada.get();

            if(documentSnapshotUsuario.exists()){

                /*
                 * CASO O DOCUMENTOUSUARIO EXISTA, SIGNIFICA QUE O USUARIO JÁ EFETOU LANÇAMENTO ANTES, ENTÃO NO CASO SERÁ ATUALIZAÇÕES
                 */

                Firestore firestoreAcumuldoMesReferencia = FirestoreClient.getFirestore();
                DocumentReference documentReferenceusuarioMesReferencia = firestoreAcumuldoMesReferencia.collection(NOME_COLLECTION_ACUMULADOS)
                        .document(this.emailUserLancandoEntrada)
                        .collection(nomeCollectionAcumuladoUsuario)
                        .document(this.nomeCollectionMesReferencia);
                ApiFuture<DocumentSnapshot> usuarioLancaEntradaMesReferencia = documentReferenceusuarioMesReferencia.get();
                DocumentSnapshot documentSnapshotUsuarioMesReferencia = usuarioLancaEntradaMesReferencia.get();



                //RECUPERANDO DADOS DO USUARIO PARA AS ATUALIZAÇÕES REFERENTE AO LANÇAMENTO EFETUADO
                UserModel ValoresUserModelLancaEntrada = documentSnapshotUsuarioMesReferencia.toObject(UserModel.class);


                //VARIAVEIS LOCAIS RECEBENDO OS VALORES DO LANCAMENTO EFETUADO PELO USUARIO EM QUESTÃO
                this.identificador = lancamentoEntradaModel.getIdentificador();
                this.nomeUserLancandoEntrada = lancamentoEntradaModel.getNomeUserLancandoEntrada();
                this.nomeLancamentoEntrada = lancamentoEntradaModel.getNomeLancamentoEntrada();
                this.dataLancamentoEntrada = lancamentoEntradaModel.getDataLancamentoEntrada();
                this.valorLancamentoEntrada = lancamentoEntradaModel.getValorLancamentoEntrada();
                this.detalhesLancamentoEntrada = lancamentoEntradaModel.getDetalhesLancamentoEntrada();


                //VALORES RECUPERADOS DO USUARIO PARA PREPARAÇÃO DAS DEVIDAS ATUALIZAÇÕES:

                assert ValoresUserModelLancaEntrada != null;
                double valorTotalEntradaMensal = ValoresUserModelLancaEntrada.getValorTotalSaidaMensal();

                //PREPARANDO AS VARIAVEIS LOCAIS COM AS TEMPORARIAS PARA ATUALIZAR UM NOVO USERMODEL
                this.user_id = this.emailUserLancandoEntrada;
                this.nomeUser = ValoresUserModelLancaEntrada.getNomeUser();
                this.emailUser = ValoresUserModelLancaEntrada.getEmailUser();
                double resultado = valorLancamentoEntradaConvertido + valorTotalEntradaMensal;
                this.valorTotalEntradaMensal = ValoresUserModelLancaEntrada.getValorTotalEntradaMensal();
                this.valorTotalSaidaMensal = resultado;
                this.quantidadeTotalLancamentosEntradaMensal = ValoresUserModelLancaEntrada.getQuantidadeTotalLancamentosEntradaMensal();
                this.quantidadeTotalLancamentosSaidaMensal = ValoresUserModelLancaEntrada.getQuantidadeTotalLancamentosSaidaMensal() + 1;

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
                atualizaValorTotalSaidaMensalUsuario(usuarioAtualiza,lancamentoEntradaModelSalva);

                this.mensagemReturn = "Sucesso ao adicionar novo lancamento saida";
                this.resultadoLancaEntrada = true;

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
                adicionaLancamentoSaidaUsuario(this.emailUserLancandoEntrada,
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
                userModelNovo.setValorTotalEntradaMensal(this.valorRecuperadoLancadoEntrada);
                userModelNovo.setValorTotalSaidaMensal(valorLancamentoEntradaConvertido);
                userModelNovo.setQuantidadeTotalLancamentosEntradaMensal(this.quantidadeRecuperaLancaEntrada);
                userModelNovo.setQuantidadeTotalLancamentosSaidaMensal(1);

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
                firestoreCollectionMesReferencia.collection(NOME_COLLECTION_ACUMULADOS)
                        .document(userModelNovo.getEmailUser())
                        .collection(nomeCollectionAcumuladoUsuario)
                        .document(this.nomeCollectionMesReferencia)
                        .set(dadosSalva);
                this.mensagemReturn = "Sucesso ao criar novo lancamento entrada";
                this.resultadoLancaEntrada = true;
            }
            //QUANDO ACABAR E CASO NÃO RETORNE ERRO SERÁ ATRIBUIDO VERDADEIRO PARA A VARIAVEIS DE RETORNO DO METODO

        }



        return this.mensagemReturn ;
    }

    private void atualizaValorTotalSaidaMensalUsuario(UserModel usuarioAtualiza, LancamentoEntradaModel lancamentoEntradaModelSalva) {


        String nomeUsuario = usuarioAtualiza.getNomeUser();
        String nomeMaiusculo = nomeUsuario.toUpperCase();
        String nomeCollectionAcumuladoUsuario = "ACUMULADO_" +nomeMaiusculo;




        //-------------INICIO DATA FORMATADA PARA CRIAÇÃO-------------------------//
        Date data = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String dataCreated = dataFormat.format(data);

        //-------------FIM DATA FORMATADA PARA CRIAÇÃO-------------------------//


        //-------------INICIO INSTANCIANDO FIRESTORE-------------------------//


        Firestore firestore = FirestoreClient.getFirestore();
        //firestore.collection(NOME_COLLECTION_ACUMULADOS).document(lancamentoEntradaModelSalva.getEmailUserLancandoEntrada()).collection(NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO);

        //-------------FIM INSTANCIANDO FIRESTORE-------------------------//


        //-------------INICIO PREPARACAO DAS VARIAVEIRS DO LANCAMENTO PARA ATUALIZACAO NO FIRESTORE-------------------------//

        this.novoId = String.valueOf(usuarioAtualiza.getQuantidadeTotalLancamentosSaidaMensal());
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
                .collection(nomeCollectionAcumuladoUsuario)
                .document(this.nomeCollectionMesReferencia)
                .collection(NOME_COLLECTION_LANCAMENTO_SAIDA_DIARIA_USUARIO)
                .document(lancamentoSalvo.getIdentificador())
                .set(LancamentoEntrada);


/*
        firestore.collection(NOME_COLLECTION_ACUMULADOS)
                .document(usuarioAtualiza.getEmailUser())
                .collection(NOME_COLLECTION_ACUMULADOS_ENTRADA_USUARIO)
                .document(this.nomeCollectionMesReferencia)
                .collection(NOME_COLLECTION_LANCAMENTO_ENTRADA_DIARIA_USUARIO)
                .document(lancamentoSalvo.getIdentificador())
                .set(LancamentoEntrada);*/
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
                .collection(nomeCollectionAcumuladoUsuario)
                .document(this.nomeCollectionMesReferencia)
                .set(dadosSalva);

        //-------------FIM PREPARACAO DAS VARIAVEIRS DO USUARIO PARA ATUALIZACAO NO FIRESTORE-------------------------//


    }

    //INICIO METODO
    private void adicionaLancamentoSaidaUsuario(String emailUserLancandoSaida,
                                                String nomeUserLancandoSaida,
                                                String nomeLancamentoSaida,
                                                String dataLancamentoSaida,
                                                String valorLancamentoSaida,
                                                String detalhesLancamentoSaida) {

        String nomeUsuario = nomeUserLancandoEntrada;
        String nomeMaiusculo = nomeUsuario.toUpperCase();
        String nomeCollectionAcumuladoUsuario = "ACUMULADO_" +nomeMaiusculo;


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
        lancamentoEntradaModelSalva.setDetalhesLancamentoEntrada(detalhesLancamentoEntrada);
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
                .collection(nomeCollectionAcumuladoUsuario)
                .document(this.nomeCollectionMesReferencia)
                .collection(NOME_COLLECTION_LANCAMENTO_SAIDA_DIARIA_USUARIO)
                .document("1")
                .set(lancamentoEntradaModelSalva);


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

    public String cadastrarCliente(ClienteModel clienteModel) throws ExecutionException, InterruptedException {

        Date data = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String dataCreated = dataFormat.format(data);




        this.razaoSocial = clienteModel.getRazaoSocial();
        this.CNPJ = clienteModel.getCnpj();
        this.Usuario = clienteModel.getUsuariocliente();
        this.emailCliente = clienteModel.getEmailCliente();
        this.telefone = clienteModel.getTelefone();
        this.celular = clienteModel.getCelular();
       // this.OBS = clienteModel.getObs();

        if(this.CNPJ != null){
            Firestore firestoreCliente = FirestoreClient.getFirestore();
            DocumentReference documentReferenceCliente;
            documentReferenceCliente = firestoreCliente.collection(NOME_COLLECTION_CLIENTE)
                        .document(this.CNPJ);

            ApiFuture<DocumentSnapshot> documentSnapshotApiFutureCliente = documentReferenceCliente.get();
            DocumentSnapshot documentSnapshotCliente = documentSnapshotApiFutureCliente.get();

            if(documentSnapshotCliente.exists()){
                this.mensagemReturn = "Cliente já tem cadastro";

            }else{

                ClienteModel clienteModelSalva = new ClienteModel();
                //ATRIBUINDO AS VARIAVEIS PARA OS ATRIBUTOS
                clienteModelSalva.setIdentificador(this.CNPJ);
                clienteModelSalva.setRazaoSocial(this.razaoSocial);
                clienteModelSalva.setEmailCliente(this.emailCliente);
                clienteModelSalva.setCnpj(this.CNPJ);
                clienteModelSalva.setUsuariocliente(this.Usuario);
                clienteModelSalva.setTelefone(this.telefone);
                clienteModelSalva.setCelular(this.celular);
                clienteModelSalva.setObs("Cliente Criado");
                clienteModelSalva.setCreated(dataCreated);
                clienteModelSalva.setModified("Cliente sem modificação");

                //INSTANCIANDO UM NOVO MAP PARA SER ADICIONADO AO FIREBASE - FIRESTORE
                Map<String , Object> clienteSalva = new HashMap<>();
                //PREPARANDO AS VARIAVEIS PARA SEREM ADICIONADO AO FIREBASE
                clienteSalva.put("identificador" , clienteModelSalva.getCnpj());
                clienteSalva.put("razaoSocial" , clienteModelSalva.getRazaoSocial());
                clienteSalva.put("CNPJ" , clienteModelSalva.getCnpj());
                clienteSalva.put("Usuario" , clienteModelSalva.getUsuariocliente());
                clienteSalva.put("emailCliente" , clienteModelSalva.getEmailCliente());
                clienteSalva.put("telefone" , clienteModelSalva.getTelefone());
                clienteSalva.put("celular" , clienteModelSalva.getCelular());
                clienteSalva.put("OBS" , clienteModelSalva.getObs());
                clienteSalva.put("created" , clienteModelSalva.getCreated());
                clienteSalva.put("modified" , clienteModelSalva.getModified());
                //ADICIONADO O LANÇAMENTO QUE ESTÁ SENDO FEITO NO FIREBASE

                Firestore firestoreClienteSalva = FirestoreClient.getFirestore();
                firestoreClienteSalva.collection(NOME_COLLECTION_CLIENTE)
                        .document(clienteModelSalva.getIdentificador())
                        .set(clienteModelSalva);
                this.mensagemReturn = "Cliente cadastrado com sucesso";
            }

        }else{
            this.mensagemReturn = "CNPJ DEU NULO";
        }



       /* this.mensagemReturn = "Razao social: " + this.razaoSocial
                +"CNPJ: " + this.CNPJ
                +"Usuario: " + this.Usuario
                + "email : " + this.emailCliente
                +" telefone: " + this.telefone
                +" celular: " + this.celular
                +" OBS: " + this.OBS;*/
        return this.mensagemReturn;
    }

    public String editarCliente(ClienteModel clienteModel) throws ExecutionException, InterruptedException {


        Date data = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String dataModified = dataFormat.format(data);

        ClienteModel clienteModelRecuperadoFirebase = new ClienteModel();

        Firestore firestoreCliente = FirestoreClient.getFirestore();
        DocumentReference documentReferenceCliente;
        documentReferenceCliente = firestoreCliente.collection(NOME_COLLECTION_CLIENTE)
                .document(clienteModel.getCnpj());

        ApiFuture<DocumentSnapshot> usuarioLancaEntradaMesReferencia = documentReferenceCliente.get();
        DocumentSnapshot documentSnapshotUsuarioMesReferencia = usuarioLancaEntradaMesReferencia.get();



        //RECUPERANDO DADOS DO USUARIO PARA AS ATUALIZAÇÕES REFERENTE AO LANÇAMENTO EFETUADO
        ClienteModel clienteModelFirebase = documentSnapshotUsuarioMesReferencia.toObject(ClienteModel.class);


        clienteModelRecuperadoFirebase.setEmailCliente(clienteModelFirebase.getEmailCliente());
        clienteModelRecuperadoFirebase.setCnpj(clienteModelFirebase.getCnpj());
        clienteModelRecuperadoFirebase.setUsuariocliente(clienteModelFirebase.getUsuariocliente());
        clienteModelRecuperadoFirebase.setCelular(clienteModelFirebase.getCelular());
        clienteModelRecuperadoFirebase.setTelefone(clienteModelFirebase.getTelefone());
        clienteModelRecuperadoFirebase.setRazaoSocial(clienteModelFirebase.getRazaoSocial());
        clienteModelRecuperadoFirebase.setObs(clienteModelFirebase.getObs());
        clienteModelRecuperadoFirebase.setIdentificador(clienteModelFirebase.getIdentificador());
        clienteModelRecuperadoFirebase.setCreated(clienteModelFirebase.getCreated());
        clienteModelRecuperadoFirebase.setModified(dataModified);

        adicionarClienteAlterado(clienteModelFirebase, dataModified);



        this.identificadorCliente = clienteModel.getIdentificador();
        this.razaoSocial = clienteModel.getRazaoSocial();
        this.CNPJ = clienteModel.getCnpj();
        this.Usuario = clienteModel.getUsuariocliente();
        this.emailCliente = clienteModel.getEmailCliente();
        this.telefone = clienteModel.getTelefone();
        this.celular = clienteModel.getCelular();
        this.OBS = clienteModel.getObs();


        ClienteModel clienteModelAtualizado = new ClienteModel();

        clienteModelAtualizado.setEmailCliente(this.emailCliente);
        clienteModelAtualizado.setCnpj(this.CNPJ);
        clienteModelAtualizado.setUsuariocliente(this.Usuario);
        clienteModelAtualizado.setCelular(this.celular);
        clienteModelAtualizado.setTelefone(this.telefone);
        clienteModelAtualizado.setRazaoSocial(this.razaoSocial);
        clienteModelAtualizado.setObs("Cliente editado no dia: " + dataModified);
        clienteModelAtualizado.setIdentificador(clienteModelFirebase.getIdentificador());
        clienteModelAtualizado.setCreated(clienteModelFirebase.getCreated());
        clienteModelAtualizado.setModified(dataModified);

        documentReferenceCliente.set(clienteModelAtualizado);

this.mensagemReturn = "Cliente alterado";

/*
        if(this.CNPJ != null){
            Firestore firestoreCliente = FirestoreClient.getFirestore();
            DocumentReference documentReferenceCliente;
            documentReferenceCliente = firestoreCliente.collection(NOME_COLLECTION_CLIENTE)
                        .document(this.CNPJ);

            ApiFuture<DocumentSnapshot> usuarioLancaEntradaMesReferencia = documentReferenceCliente.get();
            DocumentSnapshot documentSnapshotUsuarioMesReferencia = usuarioLancaEntradaMesReferencia.get();



            //RECUPERANDO DADOS DO USUARIO PARA AS ATUALIZAÇÕES REFERENTE AO LANÇAMENTO EFETUADO
            ClienteModel clienteModelFirebase = documentSnapshotUsuarioMesReferencia.toObject(ClienteModel.class);




          *//*  if(documentSnapshotCliente.exists()){
                this.mensagemReturn = "Cliente já tem cadastro";

            }
            else{

                ClienteModel clienteModelSalva = new ClienteModel();
                //ATRIBUINDO AS VARIAVEIS PARA OS ATRIBUTOS
                clienteModelSalva.setIdentificador(this.CNPJ);
                clienteModelSalva.setRazaoSocial(this.razaoSocial);
                clienteModelSalva.setEmailCliente(this.emailCliente);
                clienteModelSalva.setCnpj(this.CNPJ);
                clienteModelSalva.setUsuariocliente(this.Usuario);
                clienteModelSalva.setTelefone(this.telefone);
                clienteModelSalva.setCelular(this.celular);
                clienteModelSalva.setObs("Cliente Criado");
                clienteModelSalva.setCreated(dataCreated);
                clienteModelSalva.setModified("Cliente sem modificação");

                //INSTANCIANDO UM NOVO MAP PARA SER ADICIONADO AO FIREBASE - FIRESTORE
                Map<String , Object> clienteSalva = new HashMap<>();
                //PREPARANDO AS VARIAVEIS PARA SEREM ADICIONADO AO FIREBASE
                clienteSalva.put("identificador" , clienteModelSalva.getIdentificador());
                clienteSalva.put("razaoSocial" , clienteModelSalva.getRazaoSocial());
                clienteSalva.put("CNPJ" , clienteModelSalva.getCnpj());
                clienteSalva.put("Usuario" , clienteModelSalva.getUsuariocliente());
                clienteSalva.put("emailCliente" , clienteModelSalva.getEmailCliente());
                clienteSalva.put("telefone" , clienteModelSalva.getTelefone());
                clienteSalva.put("celular" , clienteModelSalva.getCelular());
                clienteSalva.put("OBS" , clienteModelSalva.getObs());
                clienteSalva.put("created" , clienteModelSalva.getCreated());
                clienteSalva.put("modified" , clienteModelSalva.getModified());
                //ADICIONADO O LANÇAMENTO QUE ESTÁ SENDO FEITO NO FIREBASE

                Firestore firestoreClienteSalva = FirestoreClient.getFirestore();
                firestoreClienteSalva.collection(NOME_COLLECTION_CLIENTE)
                        .document(clienteModelSalva.getIdentificador())
                        .set(clienteSalva);
                this.mensagemReturn = "Cliente cadastrado com sucesso";
            }*//*

        }else{
            this.mensagemReturn = "CNPJ DEU NULO";
        }



       *//* this.mensagemReturn = "Razao social: " + this.razaoSocial
                +"CNPJ: " + this.CNPJ
                +"Usuario: " + this.Usuario
                + "email : " + this.emailCliente
                +" telefone: " + this.telefone
                +" celular: " + this.celular
                +" OBS: " + this.OBS;*/
        return this.mensagemReturn;
    }

    private void adicionarClienteAlterado(ClienteModel clienteModelRecuperadoFirebase, String dataModificacao) {


        Map<String, Object> clienteEditado = new HashMap<>();
        clienteEditado.put("identificador" , clienteModelRecuperadoFirebase.getIdentificador());
        clienteEditado.put("razaoSocial" , clienteModelRecuperadoFirebase.getRazaoSocial());
        clienteEditado.put("cnpj" , clienteModelRecuperadoFirebase.getCnpj());
        clienteEditado.put("usuariocliente" , clienteModelRecuperadoFirebase.getUsuariocliente());
        clienteEditado.put("emailCliente" , clienteModelRecuperadoFirebase.getEmailCliente());
        clienteEditado.put("telefone" , clienteModelRecuperadoFirebase.getTelefone());
        clienteEditado.put("celular" , clienteModelRecuperadoFirebase.getCelular());
        clienteEditado.put("obs" , clienteModelRecuperadoFirebase.getObs());
        clienteEditado.put("created" , clienteModelRecuperadoFirebase.getCreated());
        clienteEditado.put("modified" , dataModificacao);


        Firestore firestoreClienteEditado = FirestoreClient.getFirestore();

        firestoreClienteEditado.collection(NOME_COLLECTION_CLIENTE)
                .document(clienteModelRecuperadoFirebase.getIdentificador())
                .collection("MODIFICAÇOES_CLIENTE")
                .document(dataModificacao)
                .set(clienteEditado);
        this.mensagemReturn = "Cliente antigo salvo com sucesso";

    }
}