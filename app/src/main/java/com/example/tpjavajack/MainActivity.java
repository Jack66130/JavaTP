package com.example.tpjavajack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        Button btnListe = findViewById(R.id.btnListe);
        Button btnCarte = findViewById(R.id.btnCarte);

        btnListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherListeCoiffeurs();
            }
        });

        btnCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherCarte();
            }
        });
    }

    private void afficherListeCoiffeurs() {
        // Charger le fichier JSON
        JSONObject jsonObject = JSONFileReader.loadJSONFromResource(this, R.raw.coiffeurs);

        if (jsonObject != null) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray features = data.getJSONArray("features");

                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < features.length(); i++) {
                    JSONObject feature = features.getJSONObject(i);
                    JSONObject properties = feature.getJSONObject("properties");

                    String nom = properties.getString("nom");

                    // Ajouter le nom du coiffeur au StringBuilder
                    builder.append(nom).append("\n");
                }

                // Afficher la liste des noms dans le TextView
                textView.setText(builder.toString());

            } catch (JSONException e) {
                e.printStackTrace();
                textView.setText("Erreur lors de la lecture du fichier JSON");
            }
        } else {
            textView.setText("Erreur lors du chargement du fichier JSON");
        }
    }

    private void afficherCarte() {
        Intent intent = new Intent(MainActivity.this, GoogleMapsActivity.class);
        startActivity(intent);
    }
}
