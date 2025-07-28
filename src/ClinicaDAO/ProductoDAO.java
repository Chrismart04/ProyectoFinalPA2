package ClinicaDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.ProductoModel;

public class ProductoDAO extends Conexion {

    PreparedStatement ps;
    ResultSet rs;

    public ProductoDAO() {
        super();
    }

    public void insertar(ProductoModel producto) {
        String sentencia = "insert into productos(nombre,descripcion,precio,stock,id_categoria) values(?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sentencia);
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getId_categoria());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar: " + e.getMessage());
        }
    }

    public List<ProductoModel> obtener() {
        List<ProductoModel> productos = new ArrayList<>();
        String sentencia = "SELECT p.*, c.nombre as nombre_categoria FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id";
        try {
            ps = con.prepareStatement(sentencia);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductoModel producto = new ProductoModel(0, "", "", 0.0, 0, 0);
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setId_categoria(rs.getInt("id_categoria"));
                producto.setNombreCategoria(rs.getString("nombre_categoria"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener: " + e.getMessage());
        }
        return productos;
    }

    public void modificar(ProductoModel producto) {
        String sentencia = "update productos set nombre = ?, descripcion = ?, precio = ?, stock = ?, id_categoria = ? where id = ?";
        try {
            ps = con.prepareStatement(sentencia);
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getId_categoria());
            ps.setInt(6, producto.getId());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.getMessage());
        }
    }

    public void eliminar(ProductoModel producto) {
        String sentencia = "DELETE FROM productos WHERE id = ?";
        try {
            ps = con.prepareStatement(sentencia);
            ps.setInt(1, producto.getId());
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        }
    }

    public ProductoModel buscar(ProductoModel producto) {
        String sentencia = "SELECT * FROM productos where id="+producto.getId();
        try {
            ps = con.prepareStatement(sentencia);
            rs = ps.executeQuery();
            while (rs.next()) {
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setId_categoria(rs.getInt("id_categoria"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + e.getMessage());
        }
        return producto;
    }
    
    public List<String> obtenerCategorias() {
        List<String> categorias = new ArrayList<>();
        String sentencia = "SELECT id, nombre FROM categorias";
        try {
            ps = con.prepareStatement(sentencia);
            rs = ps.executeQuery();
            while (rs.next()) {
                categorias.add(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener categorías: " + e.getMessage());
        }
        return categorias;
    }
} 