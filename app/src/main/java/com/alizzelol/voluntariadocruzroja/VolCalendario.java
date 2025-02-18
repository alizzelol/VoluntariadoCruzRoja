package com.alizzelol.voluntariadocruzroja;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase VolCalendario que representa la actividad principal del calendario para voluntarios
 * Esta clase permite navegar a las opciones de Mis eventos y ver la lista de eventos
 */
public class VolCalendario extends AppCompatActivity {


    Button btnME; // Botón para Mis Eventos
    Button btnLE; // Botón para ver la lista de eventos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.volcalendario);

        // Inicializa los botones en el diseño del layout
        btnME = findViewById(R.id.btnME);
        btnLE = findViewById(R.id.btnLE);

        //Funcionalidad del Botón Mis Eventos
        btnME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VolCalendario.this, MisEventos.class);
                startActivity(i);
            }
        });

        //Funcionalidad del Botón Lista de Eventos
        btnLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VolCalendario.this, ListaEventosV.class);
                startActivity(i);
            }
        });

    }

}