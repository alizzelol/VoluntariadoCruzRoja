package com.alizzelol.voluntariadocruzroja;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad de inicio de sesión que valida las credenciales del usuario
 * y redirige a diferentes pantallas según el rol del usuario.
 */
public class MainLogin extends AppCompatActivity {

    // Declaración de variables para la BBDD SQLite y las vistas
    private SQLite BBDD; // Instancia para gestionar la base de datos
    private EditText etUsuario, etPass; // Campos de texto para el usuario y la contraseña
    private Button btnIniciar; // Botón para iniciar sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.iniciolog);

        // Inicializar la BBDD SQLite
        BBDD = new SQLite(this);
        SQLiteDatabase db = null;
        try {
            db = BBDD.getWritableDatabase(); // Crear o abrir la BBDD SQLite

            // Verificar que la BBDD está creada correctamente
            if (db != null) {
                Toast.makeText(this, "Base de Datos creada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al crear la BBDD", Toast.LENGTH_SHORT).show();
            }

            // Crear un usuario administrador
            Usuario Nusuario = new Usuario("Will", "Guillermo", "Piñero", "A12345678+", "admin");
            boolean insertado = BBDD.añadir(Nusuario);

        } catch (Exception e) {
            // Manejo de excepciones en la inicialización de la BBDD SQLite
            Toast.makeText(this, "Error al inicializar la BBDD: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close(); // Cierra la base de datos
            }
        }

        // Iniciar las vistas
        etUsuario = findViewById(R.id.etUsuario); // Campo para ingresar el usuario
        etPass = findViewById(R.id.etPass); // Campo para ingresar la contraseña
        btnIniciar = findViewById(R.id.btnIniciar); // Botón para iniciar sesión

        // Funcionalidad del Botón Iniciar Sesión
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString(); // Obtener el texto ingresado como usuario
                String pass = etPass.getText().toString(); // Obtener el texto ingresado como contraseña
                validarCredenciales(usuario, pass); // Llamar a la función para validar las credenciales
            }
        });
    }

    /**
     * Función para validar las credenciales del usuario.
     * @param usuario El nombre de usuario ingresado
     * @param pass La contraseña ingresada
     */
    private void validarCredenciales(String usuario, String pass) {
        String rol = ValidarRol(usuario, pass);  // Llama a la función para obtener el rol
        if (rol != null) {
            obtenerRol(rol);  // Llama a la función para redirigir según el rol
        } else {
            // Si las credenciales no son válidas, mostrar este mensaje
            Toast.makeText(MainLogin.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para validar el rol del usuario a partir de las credenciales ingresadas.
     * @param usuario El nombre de usuario ingresado
     * @param pass La contraseña ingresada
     * @return El rol del usuario si es válido, o null si las credenciales no son correctas.
     */
    private String ValidarRol(String usuario, String pass) {
        SQLiteDatabase db = BBDD.getReadableDatabase();
        String[] columnas = {"rol"};  // Seleccionamos solo la columna del rol
        String validar = "usuario = ? AND pass = ?";  // Condiciones para la consulta
        String[] verificar = {usuario, pass};  // Valores a verificar
        Cursor cursor = null;
        String rol = null;  // Inicializamos rol como null

        try {
            cursor = db.query(
                    "Voluntarios",   // Nombre de la tabla
                    columnas,      // Columnas a seleccionar
                    validar,    // Condición del WHERE
                    verificar,// Valores de la condición
                    null,         // No necesitamos agrupar los resultados
                    null,         // No necesitamos filtros adicionales
                    null          // No necesitamos orden
            );

            if (cursor != null && cursor.moveToFirst()) {
                rol = cursor.getString(cursor.getColumnIndexOrThrow("rol"));  // Obtenemos el rol
            }
        } catch (Exception e) {
            // En caso de error al validar el rol
            Toast.makeText(this, "Error al validar rol: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();  // Cierra el cursor
            }
            db.close();  // Cierra la base de datos
        }

        return rol;  // Retorna el rol obtenido
    }

    private void obtenerRol(String rol) {
        Intent intent;
        switch (rol) {
            case "admin":
                intent = new Intent(MainLogin.this, MainCalendario.class); // Redirigir a la actividad de administrador
                startActivity(intent);
                break;
            case "vol":
                intent = new Intent(MainLogin.this, VolCalendario.class); // Redirigir a la actividad de voluntario
                startActivity(intent);
                break;
            default:
                // Si el rol no está reconocido
                Toast.makeText(MainLogin.this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
