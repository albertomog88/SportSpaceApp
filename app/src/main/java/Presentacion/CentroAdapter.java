package Presentacion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Negocio.Centro;
import es.ucm.fdi.sportspaceapp.R;

public class CentroAdapter extends RecyclerView.Adapter<CentroAdapter.CentroViewHolder> {
    private ArrayList<Centro> listaCentros;

    public CentroAdapter(ArrayList<Centro> listaCentros) {
        this.listaCentros = listaCentros;
    }

    @NonNull
    @Override
    public CentroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.centro_adapter, parent, false);
        return new CentroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentroViewHolder holder, int position) {
        Centro centro = listaCentros.get(position);
        holder.textViewNombre.setText(centro.getNombre());
        // Configura otros elementos de diseño según tus necesidades
    }

    @Override
    public int getItemCount() {
        return listaCentros.size();
    }

    public class CentroViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        // Agrega otros elementos de diseño según tus necesidades

        public CentroViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            // Inicializa otros elementos de diseño aquí
        }
    }
}
