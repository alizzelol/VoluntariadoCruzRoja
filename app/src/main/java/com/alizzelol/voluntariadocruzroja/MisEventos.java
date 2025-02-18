package com.alizzelol.voluntariadocruzroja;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.List;
/**
 * Clase MisEventos que permite visualizar una lista de eventos seleccionados por el voluntario por llevar a cabo
 *  * Los voluntarios pueden seleccionar eventos y borrarlos de la lista de "MisEventos".
 */
public class MisEventos extends AppCompatActivity {

    private ListView listaMisEventos; // Lista donde se mostrarán los eventos del usuario
    private ArrayAdapter<String> adapter; // Adaptador para manejar los datos de la lista
    private ArrayList<String> MisEventosList; // Lista que contiene los eventos que el usuario ha añadido
    private SQLiteCalendario BBDD; // Instancia de la base de datos SQLite para acceder a los eventos
    private String eventoSeleccionado = ""; // Variable para guardar el evento seleccionado por el usuario
    private Button btnHecho; // Botón para eliminar el evento seleccionado
    TextView NoEventos; // TextView para mostrar mensaje cuando no haya eventos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.miseventos);

        // Inicializar las vistas
        listaMisEventos = findViewById(R.id.listaMisEventos);
        MisEventosList = new ArrayList<>(); // Inicializar la lista de eventos
        btnHecho = findViewById(R.id.btnHecho); //Botón con funcionalidades de borrar
        NoEventos = findViewById(R.id.NoEventos); //Muestra un mensaje indicando que no hay eventos en la lista de "MisEventos"

        // Iniciar la BBDD SQLiteCalendario
        BBDD = new SQLiteCalendario(this);

        // Cargar los eventos añadidos a MisEventos desde la BBDD SQLiteCalendario
        cargarMisEventos();

        // Inicializar el adaptador si hay eventos en la lista
        if (!MisEventosList.isEmpty()) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MisEventosList);
            listaMisEventos.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No hay eventos", Toast.LENGTH_SHORT).show();
        }

        // Manejar la selección de un evento de la lista
        listaMisEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Guardar el evento seleccionado
                eventoSeleccionado = MisEventosList.get(position);
            }
        });

        // Manejar el clic del botón para eliminar un evento
        btnHecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Comprobar si hay un evento seleccionado
                    if (!eventoSeleccionado.isEmpty()) {
                        // Extraer el ID del evento seleccionado
                        String[] partesEvento = eventoSeleccionado.split(" ");
                        int eventoId = Integer.parseInt(partesEvento[0]);

                        // Crear AlertDialog para confirmar la eliminación del evento
                        AlertDialog.Builder builder = new AlertDialog.Builder(MisEventos.this);
                        builder.setMessage("¿Desea eliminar este evento?")
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            // Borrar el evento de la BBDD SQLiteCalendario, tabla "MisEventos"
                                            boolean borrado = BBDD.borrarMisEventos(eventoId);
                                            if (borrado) {
                                                // Si el evento se borró, mostrar mensaje y recargar los eventos
                                                Toast.makeText(MisEventos.this, "Evento borrado", Toast.LENGTH_SHORT).show();
                                                cargarMisEventos(); // Recargar la lista de eventos
                                                adapter.notifyDataSetChanged(); // Notificar cambios al adaptador
                                            } else {
                                                Toast.makeText(MisEventos.this, "Error al borrar el evento", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            // Manejo de errores al intentar borrar el evento
                                            Toast.makeText(MisEventos.this, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    } else {
                        // Si no hay un evento seleccionado, mostrar este mensaje
                        Toast.makeText(MisEventos.this, "Selecciona un evento primero", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Manejo de errores generales en el proceso de eliminación
                    Toast.makeText(MisEventos.this, "Error al intentar eliminar el evento", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Función para cargar los eventos desde la BBDD SQLiteCalendario y mostrarlos en la lista "MisEventos".
     * Se maneja la visibilidad del mensaje "No hay eventos" y de la lista si hay eventos.
     */
    private void cargarMisEventos() {
        try {
            // Limpiar la lista antes de cargar los nuevos datos
            MisEventosList.clear();

            // Obtener la lista de eventos desde la BBDD SQLiteCalendario
            List<Evento> listaEventos = BBDD.obtenerListaMisEventos();

            // Verificar si hay eventos en la lista y agregarlos a MisEventosList
            if (listaEventos != null && !listaEventos.isEmpty()) {
                for (Evento evento : listaEventos) {
                    MisEventosList.add(evento.getId() + " " + evento.getTitulo() + " " + evento.getDescripcion() + " " +
                            evento.getFecha() + " " + evento.getHora() + " " + evento.getUbicacion());
                }
                NoEventos.setVisibility(View.GONE); // Ocultar el mensaje de "No hay eventos"
                listaMisEventos.setVisibility(View.VISIBLE); // Mostrar la lista
            } else {
                NoEventos.setVisibility(View.VISIBLE); // Mostrar el mensaje de "No hay eventos"
                listaMisEventos.setVisibility(View.GONE); // Ocultar la lista
            }

            // Notificar cambios al adaptador si ya está inicializado
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            // Manejo de errores al cargar los eventos
            Toast.makeText(this, "Error al cargar los eventos", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
