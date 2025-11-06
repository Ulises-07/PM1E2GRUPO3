package com.example.pm1e2grupo3;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm1e2grupo3.api.Persona;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements Filterable {

    private List<Persona> contactList;
    private List<Persona> contactListFull; // Lista original para el filtro
    private Context context;
    private OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public ContactAdapter(Context context, List<Persona> contactList, OnItemClickListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.contactListFull = new ArrayList<>(contactList); // Copia de la lista
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Persona persona = contactList.get(position);
        holder.txtNombre.setText(persona.getNombre());
        holder.txtTelefono.setText(persona.getTelefono());

        // Resaltar item seleccionado
        if (selectedPosition == position) {
            holder.itemLayout.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(selectedPosition); // Deseleccionar el anterior
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition); // Seleccionar el nuevo
            listener.onItemClick(persona);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    // Para el filtro de búsqueda
    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    private Filter contactFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Persona> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(contactListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Persona item : contactListFull) {
                    if (item.getNombre().toLowerCase().contains(filterPattern) ||
                            item.getTelefono().toLowerCase().contains(filterPattern)) {
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
            contactList.clear();
            contactList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    // Método para actualizar la lista completa (cuando se carga desde la API)
    public void updateListFull(List<Persona> newList) {
        contactListFull.clear();
        contactListFull.addAll(newList);
        contactList.clear();
        contactList.addAll(newList);
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void clearSelection() {
        notifyItemChanged(selectedPosition);
        selectedPosition = RecyclerView.NO_POSITION;
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtTelefono;
        LinearLayout itemLayout;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtItemNombre);
            txtTelefono = itemView.findViewById(R.id.txtItemTelefono);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }

    // Interfaz para el click
    public interface OnItemClickListener {
        void onItemClick(Persona persona);
    }
}
