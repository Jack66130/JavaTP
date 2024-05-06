package com.example.tpjavajack;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.ref.WeakReference;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private JSONArray features;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        loadData();
    }

    private void loadData() {
        new LoadDataTask(this, new LoadDataTask.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(JSONObject jsonObject) {
                try {
                    features = jsonObject.getJSONObject("data").getJSONArray("features");
                    if (mMap != null) {
                        addMarkers();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataLoadFailed() {
                Toast.makeText(GoogleMapsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private void addMarkers() {
        for (int i = 0; i < features.length(); i++) {
            try {
                JSONObject coiffeur = features.getJSONObject(i);
                JSONObject properties = coiffeur.getJSONObject("properties");
                String nom = properties.getString("nom");
                double lat = properties.getDouble("lat");
                double lng = properties.getDouble("lng");
                LatLng position = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(position).title(nom));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Center map at Perpignan
        LatLng perpignan = new LatLng(42.6884, 2.8949);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(perpignan, 12)); // Zoom level 12

        // Gérer les clics sur les marqueurs
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Récupérer le titre du marqueur (nom du coiffeur)
                String coiffeurNom = marker.getTitle();
                LatLng coiffeurPosition = marker.getPosition();

                // Obtenir la position de la caméra
                LatLng cameraPosition = mMap.getCameraPosition().target;

                // Créer une intention pour ouvrir une nouvelle activité avec le nom du coiffeur, ses coordonnées et la position de la caméra
                Intent intent = new Intent(GoogleMapsActivity.this, CoiffeurDetailsActivity.class);
                intent.putExtra("coiffeur_nom", coiffeurNom);
                intent.putExtra("coiffeur_lat", coiffeurPosition.latitude);
                intent.putExtra("coiffeur_lng", coiffeurPosition.longitude);
                intent.putExtra("camera_lat", cameraPosition.latitude);
                intent.putExtra("camera_lng", cameraPosition.longitude);
                startActivity(intent);

                // Retourner true pour indiquer que l'événement a été consommé
                return true;
            }
        });
    }

    public static class LoadDataTask extends AsyncTask<Void, Void, JSONObject> {

        private WeakReference<Context> contextRef;
        private OnDataLoadedListener listener;

        public LoadDataTask(Context context, OnDataLoadedListener listener) {
            this.contextRef = new WeakReference<>(context);
            this.listener = listener;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            Context context = contextRef.get();
            if (context != null) {
                return JSONFileReader.loadJSONFromResource(context, R.raw.coiffeurs);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {
                listener.onDataLoaded(jsonObject);
            } else {
                listener.onDataLoadFailed();
            }
        }

        public interface OnDataLoadedListener {
            void onDataLoaded(JSONObject jsonObject);
            void onDataLoadFailed();
        }
    }
}
