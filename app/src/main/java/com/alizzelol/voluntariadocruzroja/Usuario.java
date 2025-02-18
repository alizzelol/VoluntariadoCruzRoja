package com.alizzelol.voluntariadocruzroja;

/**
 * Clase Usuario que representa un usuario con los detalles: id, usuario, nombre, apellido, contraseña y rol
 * Incluye métodos para crear, acceder y modificar las propiedades del usuario
 */

public class Usuario {

    //Atributos de la clase Usuario
    private int id;
    private String usuario;
    private String nombre;
    private String apellido;
    private String pass;
    private String rol;

    /**
     * Constructor sin argumentos o por defecto
     * Inicializa un objeto Usuario sin establecer valores para sus atributos
     */
    public Usuario(){

    }

    /**
     * Constructor con argumentos para crear un usuario sin especificar el ID
     * Se utiliza generalmente al añadir nuevos usuario a la BBDD SQLite
     *
     * @param usuario Nombre de usuario del voluntario
     * @param nombre  Nombre del voluntario
     * @param apellido Apellido del voluntario
     * @param pass    Contraseña del voluntario
     * @param rol     Rol asignado al voluntario
     */
    public Usuario(String usuario, String nombre, String apellido, String pass, String rol) {
        this.id = 1;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.pass = pass;
        this.rol = rol;
    }

    /**
     * Constructor con argumentos para crear o actualizar un usuario con un ID específico
     *
     * @param id       Identificador único del usuario
     * @param usuario  Nombre de usuario del usuario
     * @param nombre   Nombre del usuario
     * @param apellido Apellido del usuario
     * @param pass     Contraseña del usuario
     * @param rol      Rol asignado al usuario
     */
    public Usuario(int id, String usuario, String nombre, String apellido, String pass, String rol) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.pass = pass;
        this.rol = rol;
    }

    // Métodos Getters y Setters para acceder y modificar los atributos
    //Obtiene el identificador del usuario, return ID del usuario
    public int getId() {
        return id;
    }

    // Establece el identificador del usuario
    public void setId(int id) {
        this.id = id;
    }

    // Obtiene el nombre de usuario del usuario, return Nombre de usuario
    public String getUsuario() {
        return usuario;
    }

    // Establece el nombre de usuario del usuario
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    // Obtiene el nombre del voluntario, return Nombre del usuario
    public String getNombre() {
        return nombre;
    }

    // Establece el nombre del usuario
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Obtiene el apellido del voluntario, return Apellido del usuario
    public String getApellido() {
        return apellido;
    }

    // Establece el apellido del usuario
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    // Obtiene la contraseña del usuario, return Contraseña del usuario
    public String getPass() {
        return pass;
    }

    // Establece la contraseña del usuario
    public void setPass(String pass) {
        this.pass = pass;
    }

    // Obtiene el rol del voluntario, return Rol del usuario
    public String getRol() {
        return rol;
    }

    // Establece el rol del usuario
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Genera una representación en forma de cadena del objeto Usuario con todos sus datos ingresados.
     * @return una cadena que contiene los valores de todos los atributos del usuario.
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", pass='" + pass + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
