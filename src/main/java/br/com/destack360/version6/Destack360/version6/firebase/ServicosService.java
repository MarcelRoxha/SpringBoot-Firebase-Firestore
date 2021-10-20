package br.com.destack360.version6.Destack360.version6.firebase;

import br.com.destack360.version6.Destack360.version6.model.LancamentoEntradaModel;
import br.com.destack360.version6.Destack360.version6.model.LancamentoSaidaModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ServicosService {

    private static final String NOME_COLLECTION_USUARIO_LANCA = "users";
    private static final String NOME_COLLECTION_ACUMULADOS = "ACUMULADOS";


/*
* Lancar entrada, usuario irá digitar data, valor lançamento, tipo de lancamento que seria o nome (Ex: Doação, Dizímo, etc..).
* Talvez seja anonimo esse lancamento e irá precisar ser somado com os valores já acumulado desse usuario
* */
    public String lancarEntrada(LancamentoEntradaModel lancamentoEntradaModel) throws ExecutionException, InterruptedException {

       String teste = "Teste";

        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> adicionarLancamentoEntradaUsuario = firestore.collection(NOME_COLLECTION_USUARIO_LANCA)
                .document(lancamentoEntradaModel.getEmailUserLancandoEntrada())
                .collection(NOME_COLLECTION_ACUMULADOS)
                .document(lancamentoEntradaModel.getIdentificador())
                .set(lancamentoEntradaModel);

        return adicionarLancamentoEntradaUsuario.get().getUpdateTime().toString();
    }

    public String lancarSaida(LancamentoSaidaModel lancamentoSaidaModel) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> adicionarLancamentoUsuario = firestore.collection(NOME_COLLECTION_ACUMULADOS).document().set(lancamentoSaidaModel);

        return adicionarLancamentoUsuario.get().getUpdateTime().toString();
    }

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
