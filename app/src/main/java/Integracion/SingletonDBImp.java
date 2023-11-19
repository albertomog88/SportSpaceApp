package Integracion;
import com.google.firebase.firestore.FirebaseFirestore;
public class SingletonDBImp extends SingletonDataBase {
    private static FirebaseFirestore db = null;

    public SingletonDBImp() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
    }

    public FirebaseFirestore getDB() {
        return db;
    }
}
