
package ClinicaDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.PacienteModel;

public class PacienteDAO extends Conexion {

    PreparedStatement ps;
    ResultSet rs;

    public PacienteDAO() {
        super();
    }

    public void insertar(PacienteModel paciente) {
        String sentencia = "insert into pacientes(identidad,nombre,apellido,telefono) values(?,?,?,?)";
        try {
            ps = con.prepareStatement(sentencia);
            ps.setString(1, paciente.getIdentidad());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setString(4, paciente.getTelefono());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar: " + e.getMessage());
        }
    }

    public List<PacienteModel> obtener() {
        List<PacienteModel> pacientes = new ArrayList<>();
        String sentencia = "SELECT * FROM pacientes";
        try {
            ps = con.prepareStatement(sentencia);
            rs = ps.executeQuery();
            while (rs.next()) {
                PacienteModel paciente = new PacienteModel(0, "", "", "", "");
                paciente.setId(rs.getInt("id"));
                paciente.setIdentidad(rs.getString("identidad"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setApellido(rs.getString("apellido"));
                paciente.setTelefono(rs.getString("telefono"));
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener: " + e.getMessage());
        }
        return pacientes;
    }

    public void modificar(PacienteModel paciente) {
        String sentencia = "update pacientes set identidad = ?, nombre = ?, apellido = ?, telefono = ? where id = ?";
        try {
            ps = con.prepareStatement(sentencia);
            ps.setString(1, paciente.getIdentidad());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setString(4, paciente.getTelefono());
            ps.setInt(5, paciente.getId());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.getMessage());
        }
    }

    public void eliminar(PacienteModel paciente) {
        String sentencia = "DELETE FROM pacientes WHERE id = ?";
        try {
            ps = con.prepareStatement(sentencia);
            ps.setInt(1, paciente.getId());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        }
    }

    public PacienteModel buscar(PacienteModel cli) {
        String sentencia = "SELECT * FROM pacientes where id="+cli.getId();
        try {
            ps = con.prepareStatement(sentencia);
            rs = ps.executeQuery();
            while (rs.next()) {
                cli.setId(rs.getInt("id"));
                cli.setIdentidad(rs.getString("identidad"));
                cli.setNombre(rs.getString("nombre"));
                cli.setApellido(rs.getString("apellido"));
                cli.setTelefono(rs.getString("telefono"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + e.getMessage());
        }
        return cli;
    }
}
