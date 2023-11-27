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

import Negocio.Reserva;
import es.ucm.fdi.sportspaceapp.R;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>{
    private ArrayList<Reserva> listaReservas;
    private Context context;

    public interface OnReservaListener {
        void onReservaSelected(Reserva reserva);
    }

    private OnReservaListener listener;

    // Constructor modificado
    public ReservaAdapter(ArrayList<Reserva> listaReservas, Context context) {
        this.listaReservas = listaReservas;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ReservaAdapter.ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserva_adapter, parent, false);
        return new ReservaAdapter.ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaAdapter.ReservaViewHolder holder, int position) {
        Reserva reserva = listaReservas.get(position);
        holder.textViewNombreCentro.setText(reserva.getCentro());
        holder.textViewFecha.setText(reserva.getFecha() + " " + reserva.getHora());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VerDetallesReservaActivity.class);
                intent.putExtra("nombreCentro", reserva.getCentro());
                intent.putExtra("fecha", reserva.getFecha());
                intent.putExtra("hora", reserva.getHora());
                intent.putExtra("idCampo", reserva.getIdCampo());

                context.startActivity(intent);
            }
        });
        // Configura otros elementos de diseño según tus necesidades
    }

    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    public class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreCentro, textViewFecha;
        // Agrega otros elementos de diseño según tus necesidades

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreCentro = itemView.findViewById(R.id.textViewNombreCentro);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            // Inicializa otros elementos de diseño aquí
        }
    }
}
