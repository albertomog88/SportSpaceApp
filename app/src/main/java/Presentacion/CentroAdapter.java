package Presentacion;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Negocio.Centro;
import es.ucm.fdi.sportspaceapp.R;

public class CentroAdapter extends RecyclerView.Adapter<CentroAdapter.CentroViewHolder> {
    private ArrayList<Centro> listaCentros;
    private ArrayList<Centro> listaCentrosFiltrada;
    private Context context;

    public CentroAdapter(ArrayList<Centro> listaCentros, Context context) {
        this.listaCentros = listaCentros;
        this.listaCentrosFiltrada = new ArrayList<>(listaCentros);
        this.context = context;
    }

    @NonNull
    @Override
    public CentroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.centro_adapter, parent, false);
        return new CentroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentroViewHolder holder, int position) {
        Centro centro = listaCentrosFiltrada.get(position);
        holder.textViewNombre.setText(centro.getNombre());
        holder.textViewDistancia.setText(String.valueOf(centro.getDistancia()+ " km"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CentroActivity.class);
                intent.putExtra("centro", centro.getNombre()); // Suponiendo que Centro tiene un método getId()
                intent.putStringArrayListExtra("camposID", centro.getIdCampos());// Suponiendo que Centro tiene un método getId()// Suponiendo que Centro tiene un método getId()
                context.startActivity(intent);
            }
        });
        // Configura otros elementos de diseño según tus necesidades
    }

    @Override
    public int getItemCount() {
        return listaCentrosFiltrada.size();
    }
    public void setListaCentros(ArrayList<Centro> listaCentros) {
        this.listaCentros.clear();
        this.listaCentros.addAll(listaCentros);
        this.listaCentrosFiltrada.clear();
        this.listaCentrosFiltrada.addAll(listaCentros);
        notifyDataSetChanged();
    }


    public class CentroViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewDistancia;
        // Agrega otros elementos de diseño según tus necesidades

        public CentroViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewDistancia = itemView.findViewById(R.id.textViewDistancia);
            // Inicializa otros elementos de diseño aquí
        }
    }
    public Filter getFilter() {
        return new Filter() {
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Centro> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(listaCentros); // Usa la lista completa si no hay filtro
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Centro item : listaCentros) {
                        if (item.getNombre().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaCentrosFiltrada.clear();
                listaCentrosFiltrada.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

}
