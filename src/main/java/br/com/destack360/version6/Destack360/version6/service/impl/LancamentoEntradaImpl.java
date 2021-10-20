package br.com.destack360.version6.Destack360.version6.service.impl;

import br.com.destack360.version6.Destack360.version6.commons.GenericServiceImpl;
import br.com.destack360.version6.Destack360.version6.config.FirebaseConfig;
import br.com.destack360.version6.Destack360.version6.model.LancamentoEntradaModel;
import br.com.destack360.version6.Destack360.version6.modelDTO.LancamentoEntradaModelDTO;
import br.com.destack360.version6.Destack360.version6.service.api.LancamentoEntradaApi;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class LancamentoEntradaImpl extends GenericServiceImpl<LancamentoEntradaModel, LancamentoEntradaModelDTO> implements LancamentoEntradaApi {


    private Firestore firestore;

    public CollectionReference getCollection() throws Exception {
        return firestore.collection("ACUMULADOS");
    }
}
