package com.alizzelol.voluntariadocruzroja;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

/**
 * Clase ListaUsuarios que muestra una lista de usuarios obtenidos de la BBDD SQLite
 */
public class ListaUsuarios extends AppCompatActivity {

    private ListView listaEv; // ListView para mostrar la lista de voluntarios
    private ArrayAdapter<String> adapter; // Adaptador para gestionar y mostrar los datos en el ListView
    private ArrayList<String> ListUsuarios = new ArrayList<>(); // Lista que almacena los datos de los voluntarios

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.listausuarios);

        // Vincular las vistas del diseño del layout con las variables de la clase
        listaEv = findViewById(R.id.listaEv);
        ListUsuarios = new ArrayList<>(); // Iniciar la lista de voluntarios

        // Inicializa Adaptador
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListUsuarios);
        listaEv.setAdapter(adapter);

        // Cargar las voluntarios desde la BBDD SQLite
        cargarUsuarios();
    }

    /**
     * Método que consulta los datos de los usuarios en la BBDD SQLite
     * y los carga en la lista para mostrarlos en el ListView.
     */
    private void cargarUsuarios() {
        // Limpiar la lista de voluntarios antes de cargar datos
        ListUsuarios.clear();

        // Crear una instancia de la BBDD SQLite
        SQLite BBDD = new SQLite(this);
        SQLiteDatabase db = BBDD.getReadableDatabase();

        // Definir las columnas que se quieren recuperar
        String[] columnas = {"id", "usuario", "nombre", "apellido", "pass", "rol"};

        try {
            // Consultar la BBDD SQLite, tabla "Voluntarios"
            Cursor cursor = db.query("Usuarios", columnas, null, null,
                    null, null, null);

            // Verificar si la consulta tiene resultados
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Obtener los datos de cada columna de la tabla "Usuarios"
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                    String usuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario"));
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                    String apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"));
                    String pass = cursor.getString(cursor.getColumnIndexOrThrow("pass"));
                    String rol = cursor.getString(cursor.getColumnIndexOrThrow("rol"));

                    // Agregar los datos a la lista de voluntarios
                    ListUsuarios.add(id + "-" + usuario + "-" + nombre + "-" + apellido + "-" + pass + "-" + rol);
                } while (cursor.moveToNext());
            }

            // Cerrar cursor y BBDD SQLite
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            // Manejo de errores al realizar la consulta o al procesar los datos
            Toast.makeText(this, "Error al cargar los usuarios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close(); // Asegurarse de cerrar la BBDD SQLite
        }

        // Notificar al adaptador que los datos han cambiado para refrescar el ListView
        adapter.notifyDataSetChanged();

    }
}
