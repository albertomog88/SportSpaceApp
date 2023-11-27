package Integracion;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import java.util.HashMap;
import Negocio.Usuario;

public class UsuarioDB {
    private String myCol = "user";
    private String myNombre = "nombre";
    private String myApellidos = "apellidos";
    private String myEmail = "email";
    private String myPass = "pass";
    private String myFecha = "fechaNacimiento";
    private FirebaseAuth auth;
    public boolean guardar(Usuario u){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(u.getEmail(), u.getPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = auth.getCurrentUser().getUid();
                    SingletonDataBase.getInstance().getDB().collection(myCol).document(id).set(
                        new HashMap<String, Object>() {{
                            put(myNombre, u.getNombre());
                            put(myApellidos, u.getApellidos());
                            put(myEmail, u.getEmail());
                            put(myPass, u.getPass());
                            put(myFecha, u.getFecha());
                        }}
                    );
                }
            }
        });

        return true;
    }

    public void existe(Usuario u, Callback callback){
        String id = u.getEmail();
        DocumentReference docRef = SingletonDataBase.getInstance().getDB().collection(myCol).document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                callback.onCallback(documentSnapshot.exists());
            }
        });
    }

    public interface Callback {
        void onCallback(boolean exists);
    }


}
