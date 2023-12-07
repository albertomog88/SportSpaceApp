package Integracion;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
                    SingletonDataBase.getInstance().getDB().collection("user").document(id).set(
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

    public void getUsuario(String mail, Callback callback) {


        SingletonDataBase.getInstance().getDB().collection(myCol)
                .whereEqualTo("email", mail)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Usuario u = document.toObject(Usuario.class);
                            u.setFechaNacimiento(document.getString(myFecha));
                            // El documento existe, obtén los datos del usuario


                            // Haz algo con los datos obtenidos
                            Log.d("UserProfile", "Datos usuario: " + u.toString());
                            callback.success(u);
                        }
                    } else {
                        // Manejar errores
                        Log.e("UserProfile", "Error al obtener datos del usuario: " + task.getException().getMessage());
                        callback.onError(task.getException());
                    }
                });
    }




    public void existe(Usuario u, Callback callback){

        DocumentReference docRef = SingletonDataBase.getInstance().getDB().collection(myCol).document(u.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                callback.onCallback(documentSnapshot.exists());
            }
        });
    }

    public interface Callback {
        void success(Usuario u);

        void onCallback(boolean exists);

        void onError(Exception e);
    }


}
