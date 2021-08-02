package onelemonyboi.lorebot;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class FirestoreDB {
    public static Firestore db = null;

    public static void initialize() throws IOException {
        InputStream serviceAccount = new FileInputStream("serviceaccount.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(credentials)
            .build();
        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();
    }

    public static List<QueryDocumentSnapshot> getDocuments() {
        ApiFuture<QuerySnapshot> query = db.collection("lorebot").get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return querySnapshot.getDocuments();
    }
}
