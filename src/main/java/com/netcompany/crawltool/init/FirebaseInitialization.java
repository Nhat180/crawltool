package com.netcompany.crawltool.init;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseInitialization {

    @PostConstruct
    public void initialization() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("serviceAccountKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();



        FirebaseApp.initializeApp(options);

    }
}
