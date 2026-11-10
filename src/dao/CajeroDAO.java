package dao;

import confing.Conexion;
import modelo.Cajero;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CajeroDAO {

   
    public List<Cajero> listarCajeros() {
        List<Cajero> lista = new ArrayList<>();
        String sql = "SELECT * FROM cajeros";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearCajero(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar cajeros: " + e.getMessage());
        }
        return lista;
    }

  
    public boolean agregar(Cajero c) {
        String sql = "INSERT INTO cajeros (ndiez, nveinte, ncincuenta, ncien, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getNdiez());
            ps.setInt(2, c.getNveinte());
            ps.setInt(3, c.getNcincuenta());
            ps.setInt(4, c.getNcien());
            ps.setInt(5, c.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar cajero: " + e.getMessage());
            return false;
        }
    }

    
    public boolean editar(Cajero c) {
        String sql = "UPDATE cajeros SET ndiez=?, nveinte=?, ncincuenta=?, ncien=?, estado=? WHERE id=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getNdiez());
            ps.setInt(2, c.getNveinte());
            ps.setInt(3, c.getNcincuenta());
            ps.setInt(4, c.getNcien());
            ps.setInt(5, c.getEstado());
            ps.setInt(6, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar cajero: " + e.getMessage());
            return false;
        }
    }

   
    public boolean eliminar(int id) {
        String sql = "DELETE FROM cajeros WHERE id=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cajero: " + e.getMessage());
            return false;
        }
    }

    public Cajero buscarPorId(int id) {
        String sql = "SELECT * FROM cajeros WHERE id=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCajero(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cajero por ID: " + e.getMessage());
        }
        return null;
    }

    
    public boolean actualizarBilletes(int id, int ndiez, int nveinte, int ncincuenta, int ncien) {
        String sql = "UPDATE cajeros SET ndiez=?, nveinte=?, ncincuenta=?, ncien=? WHERE id=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ndiez);
            ps.setInt(2, nveinte);
            ps.setInt(3, ncincuenta);
            ps.setInt(4, ncien);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar billetes del cajero: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstado(int id, int estado) {
        String sql = "UPDATE cajeros SET estado=? WHERE id=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del cajero: " + e.getMessage());
            return false;
        }
    }

    private Cajero mapearCajero(ResultSet rs) throws SQLException {
        return new Cajero(
            rs.getInt("id"),
            rs.getInt("ndiez"),
            rs.getInt("nveinte"),
            rs.getInt("ncincuenta"),
            rs.getInt("ncien"),
            rs.getInt("estado")
        );
    }
}