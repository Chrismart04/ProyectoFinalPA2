package ClinicaDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatContextDAO extends Conexion {
    
    public ChatContextDAO() {
        super();
    }
    
    /**
     * Obtiene información resumida de pacientes para contexto del chat
     */
    public String getPacientesContext() {
        StringBuilder context = new StringBuilder();
        
        try {
            // Total de pacientes
            String sql = "SELECT COUNT(*) as total_pacientes FROM pacientes";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                context.append("Total de pacientes registrados: ").append(rs.getInt("total_pacientes")).append("\n");
            }
            
            // Estadísticas por edad (si existe campo edad)
            try {
                sql = "SELECT AVG(YEAR(CURDATE()) - YEAR(STR_TO_DATE(fecha_nacimiento, '%Y-%m-%d'))) as edad_promedio FROM pacientes WHERE fecha_nacimiento IS NOT NULL";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    double edadPromedio = rs.getDouble("edad_promedio");
                    if (edadPromedio > 0) {
                        context.append("Edad promedio de pacientes: ").append(String.format("%.1f", edadPromedio)).append(" años\n");
                    }
                }
            } catch (SQLException e) {
                // Campo fecha_nacimiento puede no existir
            }
            
            // Obtener algunos pacientes recientes
            sql = "SELECT nombre, apellido, telefono, identidad FROM pacientes ORDER BY id DESC LIMIT 5";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            context.append("Últimos 5 pacientes registrados:\n");
            while (rs.next()) {
                context.append("- ").append(rs.getString("nombre"))
                       .append(" ").append(rs.getString("apellido"))
                       .append(" (ID: ").append(rs.getString("identidad"))
                       .append(", Tel: ").append(rs.getString("telefono")).append(")\n");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            context.append("Error al cargar información de pacientes: ").append(e.getMessage()).append("\n");
        }
        
        return context.toString();
    }
    
    /**
     * Obtiene información de citas para contexto del chat
     */
    public String getCitasContext() {
        StringBuilder context = new StringBuilder();
        
        try {
            // Total de citas
            String sql = "SELECT COUNT(*) as total_citas FROM citas";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                context.append("Total de citas en el sistema: ").append(rs.getInt("total_citas")).append("\n");
            }
            
            // Citas de hoy
            sql = "SELECT COUNT(*) as citas_hoy FROM citas WHERE DATE(fecha) = CURDATE()";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                context.append("Citas programadas para hoy: ").append(rs.getInt("citas_hoy")).append("\n");
            }
            
            // Citas de esta semana
            sql = "SELECT COUNT(*) as citas_semana FROM citas WHERE WEEK(fecha) = WEEK(CURDATE()) AND YEAR(fecha) = YEAR(CURDATE())";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                context.append("Citas esta semana: ").append(rs.getInt("citas_semana")).append("\n");
            }
            
            // Procedimientos más comunes
            sql = "SELECT procedimiento, COUNT(*) as cantidad FROM citas GROUP BY procedimiento ORDER BY cantidad DESC LIMIT 5";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            context.append("\n📋 PROCEDIMIENTOS MÁS FRECUENTES:\n");
            while (rs.next()) {
                context.append("- ").append(rs.getString("procedimiento"))
                       .append(" (").append(rs.getInt("cantidad")).append(" citas)\n");
            }
            
            // Próximas citas
            sql = "SELECT c.fecha, c.hora, c.procedimiento, p.nombre, p.apellido " +
                  "FROM citas c JOIN pacientes p ON c.id_paciente = p.id " +
                  "WHERE c.fecha >= CURDATE() " +
                  "ORDER BY c.fecha ASC, c.hora ASC LIMIT 5";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            context.append("\n📅 PRÓXIMAS 5 CITAS:\n");
            while (rs.next()) {
                context.append("- ").append(rs.getString("fecha"))
                       .append(" ").append(rs.getString("hora"))
                       .append(" - ").append(rs.getString("procedimiento"))
                       .append(" (").append(rs.getString("nombre"))
                       .append(" ").append(rs.getString("apellido")).append(")\n");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            context.append("Error al cargar información de citas: ").append(e.getMessage()).append("\n");
        }
        
        return context.toString();
    }
    
    /**
     * Obtiene información de productos para contexto del chat con formato optimizado para AI
     */
    public String getProductosContext() {
        StringBuilder context = new StringBuilder();
        
        try {
            // Total de productos
            String sql = "SELECT COUNT(*) as total_productos, SUM(stock) as total_stock, AVG(precio) as precio_promedio FROM productos";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                context.append("📊 RESUMEN GENERAL:\n");
                context.append("• Total de productos en catálogo: ").append(rs.getInt("total_productos")).append("\n");
                context.append("• Stock total disponible: ").append(rs.getInt("total_stock")).append(" unidades\n");
                context.append("• Precio promedio: $").append(String.format("%.2f", rs.getDouble("precio_promedio"))).append("\n\n");
            }
            
            // IMPORTANTE: Productos con stock crítico (ordenados por stock ascendente)
            // El primer producto de esta lista es el que tiene MENOS INVENTARIO
            sql = "SELECT nombre, stock, precio FROM productos WHERE stock < 5 ORDER BY stock ASC LIMIT 5";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            context.append("🚨 PRODUCTOS CON STOCK CRÍTICO (menos de 5 unidades) - ORDENADOS POR MENOR STOCK:\n");
            context.append("⚠️ NOTA: El PRIMER producto de esta lista es el que tiene MENOS INVENTARIO de toda la clínica\n");
            boolean hasLowStock = false;
            int productIndex = 1;
            while (rs.next()) {
                String indicator = productIndex == 1 ? "🔴 MENOR STOCK → " : "⚠️ ";
                context.append(indicator).append(rs.getString("nombre"))
                       .append(" → ").append(rs.getInt("stock")).append(" unidades")
                       .append(" (Precio: $").append(String.format("%.2f", rs.getDouble("precio"))).append(")\n");
                hasLowStock = true;
                productIndex++;
            }
            if (!hasLowStock) {
                context.append("✅ No hay productos con stock crítico (todos tienen 5+ unidades)\n");
            }
            
            // Productos con stock bajo (advertencia)
            sql = "SELECT nombre, stock, precio FROM productos WHERE stock >= 5 AND stock < 10 ORDER BY stock ASC LIMIT 5";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            context.append("\n⚡ PRODUCTOS CON STOCK BAJO (5-9 unidades) - NECESITAN REPOSICIÓN PRONTO:\n");
            boolean hasLowMediumStock = false;
            while (rs.next()) {
                context.append("⚡ ").append(rs.getString("nombre"))
                       .append(" → ").append(rs.getInt("stock")).append(" unidades")
                       .append(" (Precio: $").append(String.format("%.2f", rs.getDouble("precio"))).append(")\n");
                hasLowMediumStock = true;
            }
            if (!hasLowMediumStock) {
                context.append("✅ No hay productos con stock bajo en este rango\n");
            }
            
            // Productos con stock normal para referencia
            sql = "SELECT nombre, stock, precio FROM productos WHERE stock >= 10 ORDER BY stock ASC LIMIT 3";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            context.append("\n✅ PRODUCTOS CON STOCK NORMAL (10+ unidades) - ALGUNOS EJEMPLOS:\n");
            boolean hasNormalStock = false;
            while (rs.next()) {
                context.append("✅ ").append(rs.getString("nombre"))
                       .append(" → ").append(rs.getInt("stock")).append(" unidades")
                       .append(" (Precio: $").append(String.format("%.2f", rs.getDouble("precio"))).append(")\n");
                hasNormalStock = true;
            }
            if (!hasNormalStock) {
                context.append("⚠️ No hay productos con stock normal - REVISAR INVENTARIO URGENTE\n");
            }
            
            // Productos más caros para contexto adicional
            sql = "SELECT nombre, stock, precio FROM productos ORDER BY precio DESC LIMIT 3";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            context.append("\n💎 PRODUCTOS DE MAYOR VALOR:\n");
            while (rs.next()) {
                context.append("💎 ").append(rs.getString("nombre"))
                       .append(" → ").append(rs.getInt("stock")).append(" unidades")
                       .append(" (Precio: $").append(String.format("%.2f", rs.getDouble("precio"))).append(")\n");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            context.append("❌ Error al cargar información de productos: ").append(e.getMessage()).append("\n");
        }
        
        return context.toString();
    }
    
    /**
     * Busca paciente por nombre o identidad
     */
    public String buscarPaciente(String query) {
        StringBuilder result = new StringBuilder();
        String sql = "SELECT * FROM pacientes WHERE nombre LIKE ? OR apellido LIKE ? OR identidad LIKE ?";
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            String searchTerm = "%" + query + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);
            
            ResultSet rs = ps.executeQuery();
            
            result.append("Resultados de búsqueda para '").append(query).append("':\n");
            while (rs.next()) {
                result.append("- ID: ").append(rs.getInt("id"))
                      .append(", Identidad: ").append(rs.getString("identidad"))
                      .append(", Nombre: ").append(rs.getString("nombre"))
                      .append(" ").append(rs.getString("apellido"))
                      .append(", Teléfono: ").append(rs.getString("telefono")).append("\n");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            result.append("Error al buscar paciente: ").append(e.getMessage());
        }
        
        return result.toString();
    }
    
    /**
     * Obtiene un contexto completo para el asistente de IA
     */
    public String getFullContext() {
        StringBuilder fullContext = new StringBuilder();
        fullContext.append("=== CONTEXTO DE LA CLÍNICA DENTAL ===\n\n");
        fullContext.append("INFORMACIÓN DE PACIENTES:\n");
        fullContext.append(getPacientesContext()).append("\n");
        fullContext.append("INFORMACIÓN DE CITAS:\n");
        fullContext.append(getCitasContext()).append("\n");
        fullContext.append("INFORMACIÓN DE PRODUCTOS:\n");
        fullContext.append(getProductosContext()).append("\n");
        fullContext.append("=====================================\n");
        
        return fullContext.toString();
    }
}
