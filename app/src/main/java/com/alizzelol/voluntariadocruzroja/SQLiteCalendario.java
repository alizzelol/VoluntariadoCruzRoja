package com.alizzelol.voluntariadocruzroja;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase SQLiteCalendario para gestionar la BBDD del calendario: tablas "Evento" y "MisEventos"
 * Esta clase extiende SQLiteOpenHelper para interactuar con la BBDD y realizar operaciones CRUD
 */
public class SQLiteCalendario extends SQLiteOpenHelper {

    //  //Nombre de la BBDD y versión
    private static final String dbName = "calendario.db";
    private static final int dbVersion = 1;

    // Nombres de las tablas de la BBDD SQLiteCalendario
    private static final String tablaEventos = "Eventos";
    private static final String tablaMisEventos = "MisEventos";

    // Columnas de la Tabla Eventos
    private static final String Cid = "id";
    private static final String CTitulo = "titulo";
    private static final String CDescripcion = "descripcion";
    private static final String CFecha = "fecha";
    private static final String CHora = "hora";
    private static final String CUbicacion = "ubicacion";

    public SQLiteCalendario(Context context) {
        super(context, dbName, null, dbVersion);
    }

    /**
     * Crea las tablas "Eventos" y "MisEventos" en la BBDD SQLiteCalendario
     * @param db Instancia de la BBDD
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crea la tabla de Eventos
        String createTableEventos = "CREATE TABLE " + tablaEventos + " (" +
                Cid + " INTEGER PRIMARY KEY, " +
                CTitulo + " TEXT, " +
                CDescripcion + " TEXT, " +
                CFecha + " TEXT, " +
                CHora + " TEXT, " +
                CUbicacion + " TEXT" + ")";
        db.execSQL(createTableEventos);

        // Crea la tabla de Mis Eventos
        String createTableMisEventos = "CREATE TABLE " + tablaMisEventos + " (" +
                Cid + " INTEGER PRIMARY KEY, " +
                CTitulo + " TEXT, " +
                CDescripcion + " TEXT, " +
                CFecha + " TEXT, " +
                CHora + " TEXT, " +
                CUbicacion + " TEXT" + ")";
        db.execSQL(createTableMisEventos);
    }

    /**
     * Actualiza la BBDD SQLiteCalendario cuando se realiza una nueva versión.
     * @param db Instancia de la BBDD SQLiteCalendario
     * @param oldVersion La versión antigua de la BBDD SQLiteCalendario
     * @param newVersion La nueva versión de la BBDD SQLiteCalendario
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tablaEventos);
        db.execSQL("DROP TABLE IF EXISTS " + tablaMisEventos);
        onCreate(db); // Crear una nueva tabla
    }

    /**
     * Añade un nuevo evento a la BBDD SQLiteCalendario
     * @param evento El objeto Evento a insertar.
     * @return true si el evento se añade correctamente, false si ya existe.
     */
    public boolean añadirEvento(Evento evento) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            // Insertar los datos del evento en el objeto ContentValues
            values.put(Cid, evento.getId());
            values.put(CTitulo, evento.getTitulo());
            values.put(CDescripcion, evento.getDescripcion());
            values.put(CFecha, evento.getFecha());
            values.put(CHora, evento.getHora());
            values.put(CUbicacion, evento.getUbicacion());

            // Verificar si el evento ya existe por su ID en la BBDD SQLiteCalendario
            Cursor cursor = db.query(tablaEventos, new String[]{Cid}, Cid + "=?",
                    new String[]{String.valueOf(evento.getId())}, null, null, null);

            boolean idExists = cursor != null && cursor.moveToFirst();
            cursor.close(); // Cerrar el cursor

            if (idExists) {
                // El ID ya existe en la BBDD SQLiteCalendario, devolver false
                return false; // Retorna false si el ID ya está en uso
            } else {
                long done = db.insert(tablaEventos, null, values);
                return done != -1;  // Retorna true si la inserción fue exitosa
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en caso de excepción
            return false; // Retorna false en caso de error
        } finally {
            db.close(); //Cerrar la BBDD SQLiteCalendario
        }
    }

    /**
     * Añade un evento a la tabla de "MisEventos" del usuario.
     * @param evento El objeto Evento a insertar.
     * @return true si el evento se añade correctamente, false si ya existe.
     */
    public boolean añadirMisEventos(Evento evento) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            // Insertar los datos del voluntario en el objeto ContentValues
            values.put(Cid, evento.getId());
            values.put(CTitulo, evento.getTitulo());
            values.put(CDescripcion, evento.getDescripcion());
            values.put(CFecha, evento.getFecha());
            values.put(CHora, evento.getHora());
            values.put(CUbicacion, evento.getUbicacion());

            // Verificar si el evento ya existe por su ID en la BBDD SQLiteCalendario
            Cursor cursor = db.query(tablaMisEventos, new String[]{Cid}, Cid + "=?",
                    new String[]{String.valueOf(evento.getId())}, null, null, null);

            boolean idExists = cursor != null && cursor.moveToFirst();
            cursor.close(); //Cerrar el cursor

            if (idExists) {
                // El ID ya existe en la BBDD SQLiteCalendario, devolver false
                return false; // Retorna false si el ID ya está en uso
            } else {
                long done = db.insert(tablaMisEventos, null, values);
                return done != -1; // Retorna true si la inserción fue exitosa
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en caso de excepción
            return false; // Retorna false en caso de error
        } finally {
            db.close(); //Cerrar la BBDD SQLiteCalendario
        }
    }

    /**
     * Borra un evento de la tabla "Eventos".
     * @param id El ID del evento a borrar.
     * @return true si el evento se borra correctamente, false si no se pudo borrar.
     */
    public boolean borrarEvento(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Eliminar el evento con el ID especificado
            int result = db.delete(tablaEventos, Cid + "=?", new String[]{String.valueOf(id)});
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en caso de excepción
            return false; // Retorna false en caso de error
        } finally {
            db.close(); //Cerrar la BBDD SQLiteCalendario
        }
    }

    /**
     * Borra un evento de la tabla "MisEventos".
     * @param id El ID del evento a borrar.
     * @return true si el evento se borra correctamente, false si no se pudo borrar.
     */
    public boolean borrarMisEventos(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Eliminar el evento con el ID especificado
            int result = db.delete(tablaMisEventos, Cid + "=?", new String[]{String.valueOf(id)});
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en caso de excepción
            return false; // Retorna false en caso de error
        } finally {
            db.close(); //Cerrar la BBDD SQLiteCalendario
        }
    }

    /**
     * Consulta un evento por su ID en la tabla "Eventos".
     * @param id El ID del evento que se desea consultar.
     * @return El objeto Evento si se encuentra, null si no se encuentra.
     */
    public Evento consultar(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Consultar el evento por su ID
            cursor = db.query(tablaEventos, null, Cid + "=?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // Recuperar los datos del evento
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(CTitulo));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(CDescripcion));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(CFecha));
                String hora = cursor.getString(cursor.getColumnIndexOrThrow(CHora));
                String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(CUbicacion));
                return new Evento(id, titulo, descripcion, fecha, hora, ubicacion);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir error en caso de excepción
        } finally {
            if (cursor != null) cursor.close(); // Cerrar el cursor si no es nulo
            db.close(); // Cerrar la BBDD SQLiteCalendario
        }
        return null;
    }

    /**
     * Obtiene la lista de todos los eventos en la tabla "Eventos"
     * @return Una lista de Eventos
     */
    public List<Evento> obtenerListaEventos() {
        List<Evento> listaEventos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Consultar todos los eventos
            cursor = db.query(tablaEventos, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    // Recorrer todos los resultados y agregar cada evento a la lista
                    Evento evento = new Evento();
                    evento.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Cid)));
                    evento.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(CTitulo)));
                    evento.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(CDescripcion)));
                    evento.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(CFecha)));
                    evento.setHora(cursor.getString(cursor.getColumnIndexOrThrow(CHora)));
                    evento.setUbicacion(cursor.getString(cursor.getColumnIndexOrThrow(CUbicacion)));
                    listaEventos.add(evento);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en caso de excepción
        } finally {
            if (cursor != null) cursor.close(); // Cerrar el cursor si no es nulo
            db.close(); // Cerrar la BBDD SQLiteCalendario
        }
        return listaEventos; // Retornar la lista de eventos
    }

    /**
     * Obtiene la lista de todos los eventos en la tabla "MisEventos"
     * @return Una lista de Eventos
     */
    public List<Evento> obtenerListaMisEventos() {
        List<Evento> listaMisEventos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Consultar todos los eventos
            cursor = db.query(tablaMisEventos, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    // Recorrer todos los resultados y agregar cada evento a la lista
                    Evento evento = new Evento();
                    evento.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Cid)));
                    evento.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(CTitulo)));
                    evento.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(CDescripcion)));
                    evento.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(CFecha)));
                    evento.setHora(cursor.getString(cursor.getColumnIndexOrThrow(CHora)));
                    evento.setUbicacion(cursor.getString(cursor.getColumnIndexOrThrow(CUbicacion)));
                    listaMisEventos.add(evento);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en caso de excepción
        } finally {
            if (cursor != null) cursor.close(); // Cerrar el cursor si no es nulo
            db.close(); // Cerrar la BBDD SQLiteCalendario
        }
        return listaMisEventos; // Retornar la lista de eventos
    }
}
