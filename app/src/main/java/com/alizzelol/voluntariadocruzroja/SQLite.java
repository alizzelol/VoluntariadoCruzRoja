package com.alizzelol.voluntariadocruzroja;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que maneja las operaciones de la BBDD SQlite relacionadas con los usuarios.
 * Extiende SQLiteOpenHelper para facilitar la creación, actualización y acceso a la BBDD SQLite.
 */
public class SQLite extends SQLiteOpenHelper {

    //Nombre de la BBDD, tabla y versión
    private static final String dbName = "usuarios.db";
    private static final int dbVersion = 1;
    private static final String dbTable = "Usuarios";

    //Datos de las Columnas de la Tabla
    private static final String Cid = "id";
    private static final String CUsuario = "usuario";
    private static final String CNombre = "nombre";
    private static final String CApellido = "apellido";
    private static final String CPass = "pass";
    private static final String CRol = "rol";

    // Constructor de la clase SQLite que inicializa la BBDD SQLite
    public SQLite(Context context) {
        super(context, dbName, null, dbVersion);
    }

    /**
     * Método llamado al crear la BBDD. Aquí se crea la tabla de Usuarios.
     * @param db La BBDD SQLite donde se creará la tabla.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crea la tabla Voluntarios
        String createTable = "create table " + dbTable + " (" +
                Cid + " INTEGER PRIMARY KEY, " +
                CUsuario + " TEXT, " +
                CNombre + " TEXT," +
                CApellido + " TEXT, " +
                CPass + " TEXT, " +
                CRol + " TEXT)";

        db.execSQL(createTable);
    }

    /**
     * Método llamado cuando se realiza una actualización en la BBDD SQLite
     * @param db         La base de datos SQLite a actualizar
     * @param oldVersion Versión antigua de la BBDD SQLite
     * @param newVersion Nueva versión de la BBDD SQLite
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + dbTable); // Eliminar la tabla si existe
        onCreate(db); // Crear una nueva tabla
    }

    /**
     * Añadir un nuevo usuario a la BBDD SQLite
     * @param usuario El objeto Usuario que contiene los datos a agregar
     * @return Devuelve true si el usuario fue añadido exitosamente, false si ya existe
     */
    public boolean añadir(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Insertar los datos del usuario en el objeto ContentValues
        values.put(Cid, usuario.getId());
        values.put(CUsuario, usuario.getUsuario());
        values.put(CNombre, usuario.getNombre());
        values.put(CApellido, usuario.getApellido());
        values.put(CPass, usuario.getPass());
        values.put(CRol, usuario.getRol());

        try {
            // Verificar si el ID ya existe en la BBDD SQLite
            Cursor cursor = db.query(dbTable, new String[]{Cid}, Cid + "=?",
                    new String[]{String.valueOf(usuario.getId())}, null, null, null);

            boolean idExists = cursor != null && cursor.moveToFirst();
            cursor.close(); // Cerrar el cursor

            if (idExists) {
                // El ID ya existe en la BBDD SQLite, devolver false
                return false; // Retorna false si el ID ya está en uso
            } else {
                // Insertar nuevo evento
                long done = db.insert(dbTable, null, values);
                return done != -1; // Devuelve true si la inserción fue exitosa
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en caso de excepción
            return false; // Retorna false en caso de error
        } finally {
            db.close(); // Cerrar la BBDD SQLite
        }
    }

    /**
     * Eliminar un usuario de la BBDD SQLite por su ID.
     * @param id El ID del usuario a eliminar.
     * @return Devuelve true si el usuario fue eliminado correctamente, false si no se encontró.
     */
    public boolean borrar(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Eliminar el voluntario con el ID especificado
            int result = db.delete(dbTable, Cid + " =?", new String[]{String.valueOf(id)});
            return result > 0; // Devuelve true si se eliminó al menos un usuario
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en caso de excepción
            return false; // Retorna false en caso de error
        } finally {
            db.close(); // Cerrar la BBDD SQLite
        }
    }

    /**
     * Consultar un usuario de la BBDD SQLite por su ID.
     * @param id El ID del usuario a consultar.
     * @return Devuelve el objeto Usuario correspondiente al ID o null si no se encuentra.
     */
    public Usuario obtener(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Consultar el voluntario por su ID
            cursor = db.query(dbTable, null, Cid + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // Recuperar los datos del usuario
                String usuario = cursor.getString(cursor.getColumnIndexOrThrow(CUsuario));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(CNombre));
                String apellido = cursor.getString(cursor.getColumnIndexOrThrow(CApellido));
                String pass = cursor.getString(cursor.getColumnIndexOrThrow(CPass));
                String rol = cursor.getString(cursor.getColumnIndexOrThrow(CRol));
                return new Usuario(usuario, nombre, apellido, pass, rol);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir error en caso de excepción
        } finally {
            if (cursor != null) {
                cursor.close(); // Cerrar el cursor si no es nulo
            }
            db.close(); // Cerrar la BBDD SQLite
        }
        return null;
    }

    /**
     * Obtener una lista de todos los voluntarios de la BBDD SQLite
     * @return Devuelve una lista de objetos Voluntario
     */
    public List<Usuario> obtenerLista() {
        List<Usuario> listaUsuario = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Consultar todos los voluntarios
            cursor = db.query("Usuarios", new String[]{"id", "usuario", "nombre", "apellido", "pass", "rol"},
                    null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    // Recorrer todos los resultados y agregar cada voluntario a la lista
                    Usuario usuario = new Usuario();
                    usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Cid)));
                    usuario.setUsuario(cursor.getString(cursor.getColumnIndexOrThrow(CUsuario)));
                    usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(CNombre)));
                    usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(CApellido)));
                    usuario.setPass(cursor.getString(cursor.getColumnIndexOrThrow(CPass)));
                    usuario.setRol(cursor.getString(cursor.getColumnIndexOrThrow(CRol)));
                    listaUsuario.add(usuario);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en caso de excepción
        } finally {
            if (cursor != null) {
                cursor.close(); // Cerrar el cursor si no es nulo
            }
            db.close(); // Cerrar la BBDD SQLite
        }
        return listaUsuario; // Retornar la lista de voluntarios
    }
}


