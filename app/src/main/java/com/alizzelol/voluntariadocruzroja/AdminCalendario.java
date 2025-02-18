package com.alizzelol.voluntariadocruzroja;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase AdminCalendario que permite gestionar eventos en una BBDD SQLite.
 * Incluye operaciones para añadir, borrar y consultar eventos.
 * Los eventos contienen información como título, descripción, fecha, hora y ubicación.
 */

public class AdminCalendario extends AppCompatActivity {

    // Instancia de la BBDD SQLite para gestionar los eventos
    private SQLiteCalendario BBDD;
    // Campos de entrada para los datos del evento
    private EditText etId, etTitulo, etDescripcion, etFecha, etHora, etUbicacion;
    // Botones para realizar operaciones CRUD
    private Button btnAñadir, btnBorrar, btnConsultar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admincalendario);

        // Inicializa la BBDD SQLiteCalendario
        BBDD = new SQLiteCalendario(this);

        // Inicializa las vistas con sus respectivos IDs del layout
        etId = findViewById(R.id.etId);
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        etUbicacion = findViewById(R.id.etUbicacion);
        btnAñadir = findViewById(R.id.btnAñadir);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnConsultar = findViewById(R.id.btnConsultar);

        // Funcionalidad del Botón Añadir
        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene los valores ingresados por el usuario
                String id = etId.getText().toString();
                String titulo = etTitulo.getText().toString();
                String descripcion = etDescripcion.getText().toString();
                String fecha = etFecha.getText().toString();
                String hora = etHora.getText().toString();
                String ubicacion = etUbicacion.getText().toString();

                // Comprueba que los campos no están vacíos
                if (titulo.isEmpty() || descripcion.isEmpty() || fecha.isEmpty() || hora.isEmpty() || ubicacion.isEmpty()) {
                    Toast.makeText(AdminCalendario.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crea un nuevo objeto Evento con los datos proporcionados
                Evento nevento = new Evento(titulo, descripcion, fecha, hora, ubicacion);

                // Si el ID no está vacío, actualizar el ID de String a número
                if (!id.isEmpty()) { // true si id no está vacío
                    try {
                        nevento.setId(Integer.parseInt(id));  // Convertir el String a int
                    } catch (NumberFormatException e) { // Excepción numérica
                        Toast.makeText(AdminCalendario.this, "El ID debe ser un número válido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Crear AlertDialog para confirmar la adición del evento
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCalendario.this);
                builder.setMessage("¿Desea añadir un evento?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    // Añadir el evento en la BBDD SQLiteCalendario
                                    boolean añadir = BBDD.añadirEvento(nevento);
                                    // Mostrar mensaje de éxito o error
                                    if (añadir) {
                                        Toast.makeText(AdminCalendario.this, "Evento insertado", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AdminCalendario.this, "Error al insertar evento, compruebe el ID", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(AdminCalendario.this, "Ocurrió un error" , Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        // Funcionalidad del Botón borrar
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene los datos ingresados por el usuario
                String ID = etId.getText().toString();
                String titulo = etTitulo.getText().toString();
                String descripcion = etDescripcion.getText().toString();
                String fecha = etFecha.getText().toString();
                String hora = etHora.getText().toString();
                String ubicacion = etUbicacion.getText().toString();

                // Validar que los campos no están vacíos
                if (ID.isEmpty() || titulo.isEmpty() || descripcion.isEmpty() || fecha.isEmpty() || hora.isEmpty() || ubicacion.isEmpty()) {
                    Toast.makeText(AdminCalendario.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int id = Integer.parseInt(etId.getText().toString());
                    // Crear AlertDialog para confirmar la eliminación del evento
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminCalendario.this);
                    builder.setMessage("¿Desea eliminar este evento?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        //Elimina el evento de la BBDD SQLiteCalendario
                                        boolean borrar = BBDD.borrarEvento(id);
                                        if (borrar) {
                                            Toast.makeText(AdminCalendario.this, "Evento eliminado", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(AdminCalendario.this, "Error al eliminar evento, compruebe el ID", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(AdminCalendario.this, "Ocurrió un error" , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                } catch (NumberFormatException e) {
                    Toast.makeText(AdminCalendario.this, "Introduce un ID válido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Funcionalidad del Botón Consultar
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia una nueva actividad para mostrar la lista de eventos
                Intent i = new Intent(AdminCalendario.this, ListaEventosAdm.class);
                startActivity(i);
            }
        });
    }
}
