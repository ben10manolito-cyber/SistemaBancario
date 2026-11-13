package modelo;

public class Cajero {
    private int id;
    private int ldiez;
    private int nveinte;
    private int ncincuenta;
    private int ncien;
    private int estado; // 0 = disponible, 1 = ocupado

    public Cajero() {}

    public Cajero(int id, int ldiez, int nveinte, int ncincuenta, int ncien, int estado) {
        this.id = id;
        this.ldiez = ldiez;
        this.nveinte = nveinte;
        this.ncincuenta = ncincuenta;
        this.ncien = ncien;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNdiez() { return ldiez; }
    public void setNdiez(int ldiez) { this.ldiez = ldiez; }

    public int getNveinte() { return nveinte; }
    public void setNveinte(int nveinte) { this.nveinte = nveinte; }

    public int getNcincuenta() { return ncincuenta; }
    public void setNcincuenta(int ncincuenta) { this.ncincuenta = ncincuenta; }

    public int getNcien() { return ncien; }
    public void setNcien(int ncien) { this.ncien = ncien; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    // Método para calcular el dinero total acumulado según los billetes disponibles
    public double getSaldoTotal() {
        return (ldiez * 10000.0) + (nveinte * 20000.0) + (ncincuenta * 50000.0) + (ncien * 100000.0);
    }
}