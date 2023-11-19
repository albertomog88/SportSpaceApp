package Integracion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.HashMap;
import Negocio.Usuario;

public class UsuarioDB {
    private String myCol = "Usuarios";
    private String myColUsers = "users";
    private String myNombre = "nombre";
    private String myApellidos = "apellidos";
    private String myEmail = "email";
    private String myPass = "password";
    private String myFecha = "fechaNacimiento";

    public boolean guardar(Usuario u){
        String id = u.getEmail();
        SingletonDataBase.getInstance().getDB().collection(myCol).document(id).set(
            new HashMap<String, Object>() {{
                put(myNombre, u.getNombre());
                put(myApellidos, u.getApellidos());
                put(myEmail, u.getEmail());
                put(myPass, u.getPass());
                put(myFecha, u.getFecha());
            }}
        );

        //Intento de añadir a los usuarios tmb en la tabla de users para q puedan iniciar sesion
        /*SingletonDataBase.getInstance().getDB().collection(myColUsers).document(id).set(
            new HashMap<String, Object>() {{
                put(myEmail, u.getEmail());
                put(myPass, u.getPass());
            }}
        );*/

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
