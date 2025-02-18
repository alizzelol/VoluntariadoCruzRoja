package com.alizzelol.voluntariadocruzroja;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase MainCalendario que representa la actividad principal del calendario,
 * con opciones para gestionar usuarios, eventos y visualizar eventos.
 */
public class MainCalendario extends AppCompatActivity {

    // Declaración de variables para la BBDD SQLiteCalendario y las vistas
    private SQLiteCalendario BBDD; // Instancia para gestionar la BBDD SQLiteCalendario
    CalendarView calView; // Vista del calendario
    Button btnGV, btnGEv, btnVEv; // Botones para gestionar usuarios, eventos y visualizar eventos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.calendario);

        //Inicializar el SQLiteCalendario
        BBDD = new SQLiteCalendario(this);
        SQLiteDatabase db = BBDD.getWritableDatabase(); //Crear BBDD SQLiteCalendario

        //Verificar que la BBDD está creada correctamente
        if(db !=null){
            Toast.makeText(this, "Base de Datos creada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al crear la BBDD", Toast.LENGTH_SHORT).show();
        }

        //Crea un evento inicial como ejemplo y añadirlo a la BBDD SQLiteCalendario
        Evento Nevento = new Evento("Acompañamiento", "Médico", "12/12/12", "17:00", "Macarena");
        boolean insertado = BBDD.añadirEvento(Nevento); //Insertar en la BBDD

        //Inicializar las vistas del diseño del layout
        calView = findViewById(R.id.calView);
        btnGV = findViewById(R.id.btnGV);
        btnGEv = findViewById(R.id.btnGEv);
        btnVEv = findViewById(R.id.btnVEv);

        //Funcionalidad del Botón Gestionar Usuario
        btnGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar mensaje de acción
                Toast.makeText(MainCalendario.this, "Gestionar Usuarios", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainCalendario.this, Admin.class);
                startActivity(i);
            }
        });

        //Funcionalidad del Botón Gestionar Eventos
        btnGEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar mensaje de acción
                Toast.makeText(MainCalendario.this, "Gestionar Eventos", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainCalendario.this, AdminCalendario.class);
                startActivity(i);
            }
        });

        //Funcionalidad del Botón Lista de Eventos para visualizar la lista de eventos
        btnVEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar mensaje de acción
                Toast.makeText(MainCalendario.this, "Lista de eventos", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainCalendario.this, ListaEventosAdm.class);
                startActivity(i);
            }
        });

    }
}