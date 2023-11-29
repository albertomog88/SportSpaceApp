package Integracion;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    public void getUsuario(Callback callback){

        SingletonDataBase.getInstance().getDB().collection(myCol).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // La consulta se completó exitosamente
                            QuerySnapshot querySnapshot = task.getResult();
                            // Lista para almacenar los centros recuperados
                            // Recorre los documentos en la colección
                            String nombre, apellidos, email, fechNac, pass;
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Accede a los datos de cada documento
                                nombre = document.getString(myNombre);
                                apellidos = document.getString(myApellidos);
                                email = document.getString(myEmail);
                                fechNac = document.getString(myFecha);
                                pass = document.getString(myPass);
                            }
                            Usuario u = new Usuario(nombre, apellidos, email, pass, fechNac);
                            // Llama al método de la interfaz cuando se hayan recuperado todos los centros
                            callback.getUsuario(u);
                        } else {
                            // Llama al método de la interfaz en caso de error
                            callback.onError(task.getException());
                        }
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
        void getUsuario(Usuario u);
        void onCallback(boolean exists);

        void onError(Exception e);
    }


}
