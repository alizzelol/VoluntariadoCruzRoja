package com.alizzelol.voluntariadocruzroja;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

/**
 * Clase ListaEventosV que permite visualizar una lista de eventos disponibles al voluntario.
 * Los usuarios pueden seleccionar eventos y añadirlos a "Mis Eventos".
 */
public class ListaEventosV extends AppCompatActivity {

    private ListView listaEv; // ListView para mostrar los eventos
    private ArrayAdapter<String> adapter; // Adaptador para gestionar la lista de eventos
    private ArrayList<String> EventoList = new ArrayList<>(); // Lista de eventos obtenidos de la BBDD SQLiteCalendario
    private String eventoSeleccionado = ""; // Variable para guardar el evento seleccionado
    TextView NoEventos; // Texto que se muestra si no hay eventos disponibles
    Button btnAñadir; // Botón para añadir eventos seleccionados a "Mis Eventos"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.listaeventosv);

        // Asociar vistas del diseño del layout con las variables de la clase
        listaEv = findViewById(R.id.listaEv);
        NoEventos = findViewById(R.id.NoEventos);
        btnAñadir = findViewById(R.id.btnAñadir); // Botón para añadir eventos seleccionados a MisEventos
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

        // Funcionalidad del Botón Añadir
        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!eventoSeleccionado.isEmpty()) {
                        // Separar el ID del resto del evento seleccionado
                        String[] partesEvento = eventoSeleccionado.split(" "); // Separar el ID del resto
                        int eventoId = Integer.parseInt(partesEvento[0]);

                        // Instanciar la BBDD SQLiteCalendario y consultar el evento por su ID
                        SQLiteCalendario BBDD = new SQLiteCalendario(ListaEventosV.this);
                        Evento evento = BBDD.consultar(eventoId); // Asegúrate de que tienes este método en SQLiteCalendario

                        if (evento != null) {
                            // Crear AlertDialog para confirmar la adición del evento
                            AlertDialog.Builder builder = new AlertDialog.Builder(ListaEventosV.this);
                            builder.setMessage("¿Desea añadir este evento a Mis Eventos?")
                                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                // Añadir el evento seleccionado a MisEventos
                                                boolean insertado = BBDD.añadirMisEventos(evento);
                                                if (insertado) {
                                                    // Borrar el evento de la tabla "eventos"
                                                    if (BBDD.borrarEvento(eventoId)) {
                                                        Toast.makeText(ListaEventosV.this, "Evento añadido a Mis Eventos", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(ListaEventosV.this, "Error al eliminar el evento de la lista", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(ListaEventosV.this, "El evento ya está en Mis Eventos", Toast.LENGTH_SHORT).show();
                                                }
                                                // Actualizar la lista de eventos
                                                cargarEventos(); // Vuelve a cargar los eventos
                                            } catch (Exception e) {
                                                Toast.makeText(ListaEventosV.this, "Seleccione un evento primero", Toast.LENGTH_SHORT).show();
                                                e.printStackTrace(); // Para logcat
                                            }
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            Toast.makeText(ListaEventosV.this, "Error al consultar el evento", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ListaEventosV.this, "Selecciona un evento primero", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Manejar errores en la adición del evento
                    Toast.makeText(ListaEventosV.this, "Error al procesar la adición", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Método para cargar eventos desde la BBDD SQLiteCalendario y mostrarlos en el ListView de ListaEventosV
     */
    private void cargarEventos() {
        try {
            // Limpiar la lista antes de cargar nuevos datos
            EventoList.clear();

            // Crear instancia de la BBDD SQLiteCalendario
            SQLiteCalendario BBDD = new SQLiteCalendario(this);
            SQLiteDatabase db = BBDD.getReadableDatabase();

            // Columnas a recuperar de la consulta
            String[] columnas = {"id", "titulo", "descripcion", "fecha", "hora", "ubicacion"};

            // Consultar la BBDD SQLiteCalendario, tabla "eventos"
            Cursor cursor = db.query("eventos", columnas, null, null, null, null, null);

            // Recorrer los resultados y añadirlos a la lista
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Obtener datos de las columnas y formar una cadena para mostrarlos
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                    String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
                    String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
                    String hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"));
                    String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion"));

                    // Agregar a la lista los datos del evento
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
            // Manejo de errores al cargar los eventos
            Toast.makeText(ListaEventosV.this, "Error al cargar eventos", Toast.LENGTH_SHORT).show();
            e.printStackTrace(); // Para logcat
        }
    }
}
