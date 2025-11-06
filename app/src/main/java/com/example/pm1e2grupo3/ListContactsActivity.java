package com.example.pm1e2grupo3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.pm1e2grupo3.api.Persona;
import com.example.pm1e2grupo3.api.PersonaApi;
import com.example.pm1e2grupo3.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListContactsActivity extends AppCompatActivity implements ContactAdapter.OnItemClickListener {

    private static final int UPDATE_REQUEST_CODE = 1;

    RecyclerView recyclerView;
    SearchView searchView;
    Button btnEliminar, btnActualizar, btnAtras;
    ContactAdapter adapter;
    List<Persona> contactList = new ArrayList<>();
    Persona selectedPersona = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);

        recyclerView = findViewById(R.id.recyclerViewContacts);
        searchView = findViewById(R.id.searchView);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnAtras = findViewById(R.id.btnAtras);

        setupRecyclerView();
        setupSearchView();

        btnAtras.setOnClickListener(v -> finish());
        btnEliminar.setOnClickListener(v -> eliminarContacto());
        btnActualizar.setOnClickListener(v -> irActualizarContacto());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cargar contactos cada vez que la actividad se vuelve visible
        loadContacts();
    }

    private void setupRecyclerView() {
        adapter = new ContactAdapter(this, contactList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void loadContacts() {
        PersonaApi personaApi = RetrofitClient.getClient().create(PersonaApi.class);
        Call<List<Persona>> call = personaApi.getPersons();

        call.enqueue(new Callback<List<Persona>>() {
            @Override
            public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // contactList.clear();
                    // contactList.addAll(response.body());
                    // adapter.notifyDataSetChanged();
                    adapter.updateListFull(response.body()); // Usar el método del adapter
                    Log.i("API_SUCCESS", "Contactos cargados: " + response.body().size());
                } else {
                    Toast.makeText(ListContactsActivity.this, "No se encontraron contactos.", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Respuesta no exitosa: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Persona>> call, Throwable t) {
                Toast.makeText(ListContactsActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Fallo en la llamada: ", t);
            }
        });
    }

    @Override
    public void onItemClick(Persona persona) {
        selectedPersona = persona;
        // Mostrar diálogo para ir al mapa
        showMapDialog(persona);
    }

    private void showMapDialog(Persona persona) {
        new AlertDialog.Builder(this)
                .setTitle("Acción")
                .setMessage("Desea ir a la ubicación de " + persona.getNombre() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(ListContactsActivity.this, MapActivity.class);
                    intent.putExtra("latitud", persona.getLatitud());
                    intent.putExtra("longitud", persona.getLongitud());
                    intent.putExtra("nombre", persona.getNombre());
                    startActivity(intent);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Si dice "No", mantenemos la selección para Eliminar/Actualizar
                    dialog.dismiss();
                })
                .show();
    }

    private void eliminarContacto() {
        if (selectedPersona == null) {
            Toast.makeText(this, "Seleccione un contacto para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Contacto")
                .setMessage("¿Está seguro que desea eliminar a " + selectedPersona.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Llamada a la API para eliminar
                    PersonaApi personaApi = RetrofitClient.getClient().create(PersonaApi.class);
                    Call<Void> call = personaApi.deletePerson(selectedPersona); // Enviamos el objeto con el ID

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ListContactsActivity.this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                                loadContacts(); // Recargar lista
                                selectedPersona = null;
                                adapter.clearSelection();
                            } else {
                                Toast.makeText(ListContactsActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                                Log.e("API_ERROR", "Respuesta no exitosa (DELETE): " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ListContactsActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("API_ERROR", "Fallo en la llamada (DELETE): ", t);
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void irActualizarContacto() {
        if (selectedPersona == null) {
            Toast.makeText(this, "Seleccione un contacto para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra("CONTACTO_A_ACTUALIZAR", selectedPersona); // Pasamos el objeto entero
        startActivityForResult(intent, UPDATE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            // El contacto fue actualizado, recargamos la lista
            loadContacts();
            selectedPersona = null; // Limpiar selección
            adapter.clearSelection();
        }
    }
}
