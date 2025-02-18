package com.alizzelol.voluntariadocruzroja;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase Admin que gestiona las operaciones CRUD sobre
 * en una BBDD SQLite.
 * Permite añadir, borrar y consultar usuarios con diferentes roles.
 */
public class Admin extends AppCompatActivity {

    // Instancia de la base de datos SQLite
    private SQLite BBDD;
    // Campos de entrada de datos para el formulario
    private EditText etId, etUsuario, etNombre, etApellido, etPass, etRol;
    // Botones para realizar las operaciones
    private Button btnAñadir, btnBorrar, btnConsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin);

        // Inicia la BBDD SQLite
        BBDD = new SQLite(this);

        // Inicializa las vistas con sus ID definidos en el layout
        etId = findViewById(R.id.etId);
        etUsuario = findViewById(R.id.etUsuario);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etPass = findViewById(R.id.etPass);
        etRol = findViewById(R.id.etRol);
        btnAñadir = findViewById(R.id.btnAñadir);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnConsultar = findViewById(R.id.btnConsultar);

        // Funcionalidad del Botón Añadir
        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Obtiene los datos introducidos por el usuario
                    String id = etId.getText().toString();
                    String usuario = etUsuario.getText().toString();
                    String nombre = etNombre.getText().toString();
                    String apellido = etApellido.getText().toString();
                    String pass = etPass.getText().toString();
                    String rol = etRol.getText().toString();

                    // Comprueba que los campos no están vacíos
                    if (id.isEmpty() || usuario.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || pass.isEmpty() || rol.isEmpty()) {
                        Toast.makeText(Admin.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Comprueba que la contraseña tiene al menos 8 caracteres, una mayúscula y un símbolo (+, -, *, /, ?)
                    if (pass.length() < 8 || !pass.matches(".*[A-Z].*") || !pass.matches(".*[+*/!?%&=].*")) {
                        Toast.makeText(Admin.this, "La contraseña debe contener al menos 8 caracteres, una letra mayúscula y un símbolo", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Valida el campo 'rol': solo se permiten 'admin' o 'vol'
                    if (!rol.equals("admin") && !rol.equals("vol")) {
                        Toast.makeText(Admin.this, "El rol debe ser 'admin' (administrador) o 'vol' (voluntario)", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Solicita confirmación antes de añadir un usuario
                    AlertDialog.Builder builder = new AlertDialog.Builder(Admin.this);
                    builder.setMessage("¿Desea añadir este usuario?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        // Crear un nuevo objeto Voluntario con los datos ingresados
                                        Usuario nusuario = new Usuario(Integer.parseInt(id), usuario, nombre, apellido, pass, rol);

                                        // Añade un Usuario en la BBDD SQLite
                                        boolean insertado = BBDD.añadir(nusuario);

                                        // Muestra un mensaje de éxito o error
                                        if (insertado) {
                                            Toast.makeText(Admin.this, "Usuario insertado", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Admin.this, "Error al insertar el usuario, compruebe el ID", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(Admin.this, "Error al añadir el usuario", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss(); // Cierra el diálogo
                                }
                            })
                            .show(); // Mostrar el diálogo
                } catch (Exception e) {
                    Toast.makeText(Admin.this, "Error en la entrada de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Funcionalidad del Botón Borrar
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // //Obtiene los datos introducidos por el usuario
                String ID = etId.getText().toString();
                String usuario = etUsuario.getText().toString();
                String nombre = etNombre.getText().toString();
                String apellido = etApellido.getText().toString();
                String pass = etPass.getText().toString();
                String rol = etRol.getText().toString();

                // Validar que los campos no están vacíos
                if (ID.isEmpty() || usuario.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || pass.isEmpty() || rol.isEmpty()) {
                    Toast.makeText(Admin.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    int id = Integer.parseInt(etId.getText().toString());

                    //Solicita confirmación antes de eliminar el usuario
                    AlertDialog.Builder builder = new AlertDialog.Builder(Admin.this);
                    builder.setMessage("¿Desea borrar este usuario?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        //Eliminar el usuario de la BBDD SQLite
                                        boolean borrar = BBDD.borrar(id);
                                        if (borrar) {
                                            Toast.makeText(Admin.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Admin.this, "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(Admin.this, "Error al eliminar el usuario, compruebe el ID", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(Admin.this, "Introduce un ID válido", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(Admin.this, "Error en la eliminación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Funcionalidad del Botón Consultar
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lanza una nueva actividad para mostrar la lista de Voluntarios
                Intent i = new Intent(Admin.this, ListaUsuarios.class);
                startActivity(i);
            }
        });
    }
}
