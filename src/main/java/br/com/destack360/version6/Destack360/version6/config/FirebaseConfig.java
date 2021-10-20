package br.com.destack360.version6.Destack360.version6.config;

import br.com.destack360.version6.Destack360.version6.Application;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;


public class FirebaseConfig {

/*
    public Firestore firestore() throws Exception{

        ClassLoader classLoader = Application.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());

        FileInputStream serviceAccount =
                new FileInputStream(file.getAbsolutePath());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://destack360-default-rtdb.firebaseio.com")
                .build();


        return FirestoreClient.getFirestore(FirebaseApp.initializeApp(options));

    }*/
}
