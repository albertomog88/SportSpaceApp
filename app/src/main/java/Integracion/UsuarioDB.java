package Integracion;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

import Negocio.Usuario;

public class UsuarioDB {
    private String myCol = "Usuarios";
    private String myNombre = "nombre";
    private String myApellidos = "apellidos";
    private String myEmail = "email";
    private String myPass = "pass";
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

        return true;
    }

    public boolean existe(Usuario u){
        String id = u.getEmail();
        DocumentReference docRef = SingletonDataBase.getInstance().getDB().collection(myCol).document(id);
        DocumentSnapshot doc = docRef.get().getResult();
        return doc.exists();
    }

}
