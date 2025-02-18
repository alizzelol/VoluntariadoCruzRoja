package com.alizzelol.voluntariadocruzroja;

/**
 * Clase Evento que representa un evento con los detalles: id, título, descripción, fecha, hora y ubicación
 * Incluye métodos para crear, acceder y modificar las propiedades del evento
 */

public class Evento {
    //Atributos de la clase Evento
    private int id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String ubicacion;

    /**
     * Constructor sin argumentos o por defecto.
     * Inicializa un objeto Evento sin establecer valores para sus atributos.
     */
    public Evento(){

    }

    /**
     * Constructor con argumentos para crear un evento sin especificar el ID
     * Se utiliza generalmente al añadir nuevos eventos a la BBDD SQLiteCalendario
     *
     * @param titulo      título del evento
     * @param descripcion descripción del evento
     * @param fecha       fecha del evento
     * @param hora        hora del evento
     * @param ubicacion   ubicación del evento
     */
    public Evento(String titulo, String descripcion, String fecha, String hora, String ubicacion) {
        this.id = 1;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.ubicacion = ubicacion;
    }

    /**
     * Constructor con argumentos para crear o actualizar un evento con un ID específico
     *
     * @param id          identificador único del evento
     * @param titulo      título del evento
     * @param descripcion descripción del evento
     * @param fecha       fecha del evento
     * @param hora        hora del evento
     * @param ubicacion   ubicación del evento
     */
    public Evento(int id, String titulo, String descripcion, String fecha, String hora, String ubicacion) {
        this.id=id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.ubicacion = ubicacion;
    }

    // Métodos Getters y Setters para acceder y modificar los atributos
    // Obtiene el ID del evento, return id del evento.
    public int getId() {
        return id;
    }

    // Establece un nuevo ID para el evento.
    public void setId(int id) {
        this.id = id;
    }

    // Obtiene el título del evento, return título del evento.
    public String getTitulo() {
        return titulo;
    }

    // Establece un nuevo título para el evento.
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Obtiene la descripción del evento, return descripción del evento.
    public String getDescripcion() {
        return descripcion;
    }

    // Establece una nueva descripción para el evento.
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Obtiene la fecha del evento, return fecha del evento.
    public String getFecha() {
        return fecha;
    }

    // Establece una nueva fecha para el evento.
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    // Obtiene la hora del evento, return hora del evento.
    public String getHora() {
        return hora;
    }

    // Establece una nueva hora para el evento.
    public void setHora(String hora) {
        this.hora = hora;
    }

    // Obtiene la ubicación del evento, return ubicación del evento.
    public String getUbicacion() {
        return ubicacion;
    }

    // Establece una nueva ubicación para el evento.
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Genera una representación en forma de cadena del objeto Evento con todos sus datos ingresados.
     * @return una cadena que contiene los valores de todos los atributos del evento.
     */
    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}

