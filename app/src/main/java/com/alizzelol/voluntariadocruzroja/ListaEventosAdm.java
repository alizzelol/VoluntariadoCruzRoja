package com.alizzelol.voluntariadocruzroja;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

/**
 * Clase ListaEventosAdm que muestra una lista de eventos almacenados en la BBDD SQLiteCalendario.
 * Permite al administrador visualizar los eventos.
 */
public class ListaEventosAdm extends AppCompatActivity {

    private ListView listaEv; //Listview para mostrar los eventos
    private ArrayAdapter<String> adapter; // Adaptador para gestionar la lista
    private ArrayList<String> EventoList = new ArrayList<>(); // Lista para almacenar los eventos
    private String eventoSeleccionado = ""; // Variable para guardar el evento seleccionado
    TextView NoEventos; // Texto que se muestra si no hay eventos disponibles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.listaeventosadm);

        // Asociar vistas del diseño del layout con las variables de la clase
        listaEv = findViewById(R.id.listaEv);
        NoEventos = findViewById(R.id.NoEventos);
        EventoList = new ArrayList<>(); // Iniciar la lista de eventos

        // Inicializar el adaptador
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, EventoList);
        listaEv.setAdapter(adapter);

        // Cargar los eventos desde la BBDD SQLiteCalendario
        cargarEventos();

        // Para seleccionar un evento de la lista
        listaEv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Guardar el evento seleccionado
                eventoSeleccionado = EventoList.get(position);
            }
        });
    }

    //  Método para cargar eventos desde la base de datos y mostrarlos en el ListView.
    private void cargarEventos() {
        try {
            // Limpiar la lista antes de cargar datos
            EventoList.clear();

            // Crear instancia de la BBDD SQLiteCalendario
            SQLiteCalendario BBDD = new SQLiteCalendario(this);
            SQLiteDatabase db = BBDD.getReadableDatabase();

            // Especificar las columnas que se desean recuperar
            String[] columnas = {"id", "titulo", "descripcion", "fecha", "hora", "ubicacion"};

            // Consultar la BBDD SQLiteCalendario, tabla "eventos".
            Cursor cursor = db.query("eventos", columnas, null, null, null, null, null);

            // Verificar si hay registros en el cursor
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Obtener los datos de cada columna
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                    String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
                    String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
                    String hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"));
                    String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion"));

                    // Agregar los datos recuperados a la lista
                    EventoList.add(id + " " + titulo + " " + descripcion + " " + fecha + " " + hora + " " + ubicacion);
                } while (cursor.moveToNext());
            }

            // Cerrar cursor y BBDD SQLiteCalendario
            if (cursor != null) {
                cursor.close();
            }
            db.close();

            // Notificar cambios al adaptador
            adapter.notifyDataSetChanged();

            // Mostrar si no hay eventos en la lista
            if (EventoList.isEmpty()) {
                NoEventos.setVisibility(View.VISIBLE); // Mostrar mensaje
                listaEv.setVisibility(View.GONE); // Ocultar lista
            } else {
                NoEventos.setVisibility(View.GONE);
                listaEv.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            // Manejo de la excepción y mensaje al usuario
            Toast.makeText(ListaEventosAdm.this, "Error al cargar eventos", Toast.LENGTH_SHORT).show();
            e.printStackTrace(); // Para registrar en logcat la excepción en caso de depuración
        }
    }
}
