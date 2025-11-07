package com.example.pm1e2grupo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pm1e2grupo3.api.Persona;
import com.example.pm1e2grupo3.api.PersonaApi;
import com.example.pm1e2grupo3.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    EditText txtNombre, txtTelefono;
    Button btnGuardar, btnCancelar;
    Persona persona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        txtNombre = findViewById(R.id.txtUpdateNombre);
        txtTelefono = findViewById(R.id.txtUpdateTelefono);
        btnGuardar = findViewById(R.id.btnGuardarActualizacion);
        btnCancelar = findViewById(R.id.btnCancelarActualizacion);

        Intent intent = getIntent();
        if (intent.hasExtra("CONTACTO_A_ACTUALIZAR")) {
            persona = (Persona) intent.getSerializableExtra("CONTACTO_A_ACTUALIZAR");
            if (persona != null) {
                // Llenar los campos
                txtNombre.setText(persona.getNombre());
                txtTelefono.setText(persona.getTelefono());
            } else {
                Toast.makeText(this, "Error al cargar datos del contacto", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "No se recibió ningún contacto", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnGuardar.setOnClickListener(v -> actualizarContacto());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void actualizarContacto() {
        String nombre = txtNombre.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();

        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Nombre y teléfono no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            return;
        }

        persona.setNombre(nombre);
        persona.setTelefono(telefono);

        PersonaApi personaApi = RetrofitClient.getClient().create(PersonaApi.class);
        Call<Void> call = personaApi.updatePerson(persona);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateActivity.this, "Contacto actualizado", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(UpdateActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Respuesta no exitosa (UPDATE): " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Fallo en la llamada (UPDATE): ", t);
            }
        });
    }
}