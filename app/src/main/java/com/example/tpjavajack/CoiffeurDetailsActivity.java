package com.example.tpjavajack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CoiffeurDetailsActivity extends AppCompatActivity {

    private double userLat;
    private double userLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coiffeur_details);

        // Obtenir le nom du coiffeur et ses coordonnées de l'intent
        String coiffeurNom = getIntent().getStringExtra("coiffeur_nom");
        double coiffeurLat = getIntent().getDoubleExtra("coiffeur_lat", 0.0);
        double coiffeurLng = getIntent().getDoubleExtra("coiffeur_lng", 0.0);

        // Obtenir les coordonnées de la caméra par défaut
        userLat = getIntent().getDoubleExtra("camera_lat", 0.0);
        userLng = getIntent().getDoubleExtra("camera_lng", 0.0);

        // Afficher le nom du coiffeur dans un TextView
        TextView nomTextView = findViewById(R.id.nomTextView);
        nomTextView.setText(coiffeurNom);

        // Gérer l'action du bouton Itinéraire
        Button itineraireButton = findViewById(R.id.itineraireButton);
        itineraireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir l'application de cartes avec l'itinéraire depuis la position de l'utilisateur jusqu'au coiffeur
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + coiffeurLat + "," + coiffeurLng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        // Gérer l'action du bouton Boussole
        Button boussoleButton = findViewById(R.id.boussoleButton);
        boussoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gérer l'action du bouton Boussole si nécessaire
            }
        });

        // Gérer l'action du bouton Retour
        Button retourButton = findViewById(R.id.retourButton);
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fermer l'activité actuelle et revenir à l'activité précédente
                finish();
            }
        });
    }
}
