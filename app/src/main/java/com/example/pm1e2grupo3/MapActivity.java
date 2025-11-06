package com.example.pm1e2grupo3;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.pm1e2grupo3.databinding.ActivityMapBinding;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitud;
    private double longitud;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtener los datos pasados desde ListContactsActivity
        Intent intent = getIntent();
        try {
            latitud = Double.parseDouble(intent.getStringExtra("latitud"));
            longitud = Double.parseDouble(intent.getStringExtra("longitud"));
            nombre = intent.getStringExtra("nombre");
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error al leer coordenadas.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Obtener el SupportMapFragment y notificar cuando el mapa esté listo
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Se llama cuando el mapa está listo para usarse.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Crear objeto LatLng con las coordenadas
        LatLng ubicacion = new LatLng(latitud, longitud);

        // Añadir un marcador en la ubicación
        mMap.addMarker(new MarkerOptions()
                .position(ubicacion)
                .title("Ubicación de " + nombre));

        // Mover la cámara al marcador y hacer zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f)); // Zoom de 15

        // Opcional: Habilitar controles de zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}