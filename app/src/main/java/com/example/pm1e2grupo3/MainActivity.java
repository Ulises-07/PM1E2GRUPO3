package com.example.pm1e2grupo3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.pm1e2grupo3.api.Persona;
import com.example.pm1e2grupo3.api.PersonaApi;
import com.example.pm1e2grupo3.api.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;
    private static final int REQUEST_PERMISSIONS = 100;

    EditText txtNombre, txtTelefono, txtLatitud, txtLongitud;
    Button btnSalvarContacto, btnContactosSalvados, btnTomarVideo;
    ImageView imgVideoPlaceholder;
    VideoView videoView;

    FusedLocationProviderClient fusedLocationClient;
    String videoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);
        btnSalvarContacto = findViewById(R.id.btnSalvarContacto);
        btnContactosSalvados = findViewById(R.id.btnContactosSalvados);
        btnTomarVideo = findViewById(R.id.btnTomarVideo);
        imgVideoPlaceholder = findViewById(R.id.imgVideoPlaceholder);
        videoView = findViewById(R.id.videoView);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Pedir permisos de localización
        checkLocationPermission();

        btnTomarVideo.setOnClickListener(v -> checkCameraPermission());
        btnSalvarContacto.setOnClickListener(v -> salvarContacto());
        btnContactosSalvados.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListContactsActivity.class);
            startActivity(intent);
        });
    }

    private void checkCameraPermission() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        } else {
            dispatchTakeVideoIntent();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getLastLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        txtLatitud.setText(String.valueOf(location.getLatitude()));
                        txtLongitud.setText(String.valueOf(location.getLongitude()));
                    } else {
                        Toast.makeText(MainActivity.this, "No se pudo obtener la ubicación.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
                Log.e("Error", "Error creando archivo de video", ex);
                Toast.makeText(this, "Error al crear archivo de video", Toast.LENGTH_SHORT).show();
            }
            if (videoFile != null) {
                Uri videoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider",
                        videoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
    }

    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = "MP4_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(
                videoFileName,
                ".mp4",
                storageDir
        );
        videoPath = video.getAbsolutePath();
        return video;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            imgVideoPlaceholder.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath(videoPath);
            videoView.start();
            Toast.makeText(this, "Video guardado en: " + videoPath, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Permiso de localización denegado.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PERMISSIONS) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                dispatchTakeVideoIntent();
            } else {
                Toast.makeText(this, "Permisos de cámara o almacenamiento denegados.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void salvarContacto() {
        String nombre = txtNombre.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String latitud = txtLatitud.getText().toString().trim();
        String longitud = txtLongitud.getText().toString().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || latitud.isEmpty() || longitud.isEmpty()) {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (videoPath.isEmpty()) {
            Toast.makeText(this, "Debe tomar un video", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creamos el objeto Persona
        Persona persona = new Persona(nombre, telefono, latitud, longitud, videoPath);

        // Obtenemos el servicio de la API
        PersonaApi personaApi = RetrofitClient.getClient().create(PersonaApi.class);
        Call<Void> call = personaApi.createPerson(persona);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Contacto guardado exitosamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } else {
                    Toast.makeText(MainActivity.this, "Error al guardar (Respuesta no exitosa)", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Respuesta no exitosa: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Fallo en la llamada: ", t);
            }
        });
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
        videoPath = "";
        imgVideoPlaceholder.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        videoView.stopPlayback();
        // Mantenemos la última ubicación
        // getLastLocation();
    }
}