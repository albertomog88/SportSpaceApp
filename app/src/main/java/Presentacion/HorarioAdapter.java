package Presentacion;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Negocio.Horario;
import es.ucm.fdi.sportspaceapp.R;


    public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder> {
        private ArrayList<Horario> listaHorarios;
        private Context context;

        public HorarioAdapter(ArrayList<Horario> listaHorarios, Context context) {
            this.listaHorarios = listaHorarios;
            this.context = context;
        }

        @NonNull
        @Override
        public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horario_adapter, parent, false);
            return new HorarioViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HorarioViewHolder holder, int position) {
            Horario horario = listaHorarios.get(position);
            holder.textViewHora.setText(horario.getHora());
            holder.checkBoxDisponible.setChecked(!horario.getDisponible());
            holder.checkBoxDisponible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    horario.setSeleccionado(isChecked);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaHorarios.size();
        }

        public class HorarioViewHolder extends RecyclerView.ViewHolder {
            TextView textViewHora;
            CheckBox checkBoxDisponible;

            public HorarioViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewHora = itemView.findViewById(R.id.hora);
                checkBoxDisponible = itemView.findViewById(R.id.checkbox);
            }
        }
        public List<Horario> obtenerHorariosSeleccionados() {
            List<Horario> seleccionados = new ArrayList<>();
            for (Horario horario : listaHorarios) {
                if (horario.getSeleccionado() != null && horario.getSeleccionado()) {
                    seleccionados.add(horario);
                }
            }
            return seleccionados;
        }
    }
