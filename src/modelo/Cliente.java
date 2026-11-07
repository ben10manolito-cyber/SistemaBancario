package modelo;

public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String ciudad;
    private int ncuenta;
    private double saldo;
    private int estado; // 0 = disponible, 1 = ocupado

    public Cliente() {}

    public Cliente(int id, String nombre, String apellido, String telefono, String ciudad, int ncuenta, double saldo, int estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.ciudad = ciudad;
        this.ncuenta = ncuenta;
        this.saldo = saldo;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public int getNcuenta() { return ncuenta; }
    public void setNcuenta(int ncuenta) { this.ncuenta = ncuenta; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
}