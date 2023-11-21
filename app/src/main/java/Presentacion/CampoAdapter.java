package Presentacion;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Negocio.Campo;
import es.ucm.fdi.sportspaceapp.R;

public class CampoAdapter extends RecyclerView.Adapter<CampoAdapter.CampoViewHolder> {
    private ArrayList<Campo> listaCampos;
    private Context context;

    public CampoAdapter(ArrayList<Campo> listaCampos, Context context) {
        this.listaCampos = listaCampos;
        this.context = context;
    }

    @NonNull
    @Override
    public CampoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.campo_adapter, parent, false);
        return new CampoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampoViewHolder holder, int position) {
        Campo campo = listaCampos.get(position);
        holder.textViewNombre.setText(campo.getNombre());
        holder.textViewDeporte.setText(campo.getDeporte());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CampoActivity.class);
                intent.putExtra("nombre_campo", campo.getNombre());
                intent.putExtra("deporte_campo", campo.getDeporte());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCampos.size();
    }

    public class CampoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewDeporte;

        public CampoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.nombreCampo);
            //textViewDeporte = itemView.findViewById(R.id.deporteCampo);
        }
    }
}
