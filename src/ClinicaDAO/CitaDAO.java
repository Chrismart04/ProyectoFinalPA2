
package ClinicaDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.CitaModel;

public class CitaDAO extends Conexion {

    PreparedStatement ps;
    ResultSet rs;

    public CitaDAO() {
        super();
    }

    public void insertar(CitaModel cita) {
        String sentencia = "insert into citas(id_paciente,fecha,hora,procedimiento) values(?,?,?,?)";
        try {
            ps = con.prepareStatement(sentencia);
            ps.setInt(1, cita.getId_paciente());
            ps.setString(2, cita.getFecha());
            ps.setString(3, cita.getHora());
            ps.setString(4, cita.getProcedimiento());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar: " + e.getMessage());
        }
    }

    public List<CitaModel> obtener() {
        List<CitaModel> citas = new ArrayList<>();
        String sentencia = "SELECT * FROM citas";
        try {
            ps = con.prepareStatement(sentencia);
            rs = ps.executeQuery();
            while (rs.next()) {
                CitaModel cita = new CitaModel(0, 0, "", "", "");
                cita.setId(rs.getInt("id"));
                cita.setId_paciente(rs.getInt("id_paciente"));
                cita.setFecha(rs.getString("fecha"));
                cita.setHora(rs.getString("hora"));
                cita.setProcedimiento(rs.getString("procedimiento"));
                citas.add(cita);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener: " + e.getMessage());
        }
        return citas;
    }

    public void modificar(CitaModel cita) {
        String sentencia = "update citas set id_paciente = ?, fecha = ?, hora = ?, procedimiento = ? where id = ?";
        try {
            ps = con.prepareStatement(sentencia);
            ps.setInt(1, cita.getId_paciente());
            ps.setString(2, cita.getFecha());
            ps.setString(3, cita.getHora());
            ps.setString(4, cita.getProcedimiento());
            ps.setInt(5, cita.getId());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.getMessage());
        }
    }

    public void eliminar(CitaModel cita) {
        String sentencia = "DELETE FROM citas WHERE id = ?";
        try {
            ps = con.prepareStatement(sentencia);
            ps.setInt(1, cita.getId());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        }
    }

    public CitaModel buscar(CitaModel cita) {
        String sentencia = "SELECT * FROM citas where id=" + cita.getId();
        try {
            ps = con.prepareStatement(sentencia);
            rs = ps.executeQuery();
            while (rs.next()) {
                cita.setId(rs.getInt("id"));
                cita.setId_paciente(rs.getInt("id_paciente"));
                cita.setFecha(rs.getString("fecha"));
                cita.setHora(rs.getString("hora"));
                cita.setProcedimiento(rs.getString("procedimiento"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + e.getMessage());
        }
        return cita;
    }
}
