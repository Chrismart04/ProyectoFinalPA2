package controller.reportes;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

public class FacturaGenerator {

    // A simple class to represent a product line in the invoice
    public static class ProductoFactura {
        private String producto;
        private Integer cantidad;
        private BigDecimal precio;
        private BigDecimal subtotal;
        private BigDecimal descuento;
        private BigDecimal isv;
        private BigDecimal total;

        public ProductoFactura(String producto, Integer cantidad, BigDecimal precio, BigDecimal descuento, BigDecimal isv) {
            this.producto = producto;
            this.cantidad = cantidad;
            this.precio = precio;
            this.descuento = descuento;
            this.isv = isv;
            this.subtotal = precio.multiply(new BigDecimal(cantidad));
            this.total = this.subtotal.subtract(descuento).add(isv);
        }

        // Getters are required by JasperReports to access the fields
        public String getProducto() { return producto; }
        public Integer getCantidad() { return cantidad; }
        public BigDecimal getPrecio() { return precio; }
        public BigDecimal getSubtotal() { return subtotal; }
        public BigDecimal getDescuento() { return descuento; }
        public BigDecimal getIsv() { return isv; }
        public BigDecimal getTotal() { return total; }
    }

    public String generarFactura(Map<String, Object> parameters, List<ProductoFactura> productos) {
        try {
            System.out.println("Iniciando generación de factura...");
            
            // 1. Load the report template (.jrxml)
            InputStream reportStream = getClass().getResourceAsStream("/reports/factura.jrxml");
            if (reportStream == null) {
                // Intentar cargar desde la ruta alternativa
                reportStream = getClass().getClassLoader().getResourceAsStream("reports/factura.jrxml");
                if (reportStream == null) {
                    throw new RuntimeException("No se pudo encontrar el archivo factura.jrxml en /reports/ o reports/");
                }
            }
            
            System.out.println("Archivo .jrxml cargado correctamente");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            System.out.println("Reporte compilado exitosamente");

            // 2. Calculate totals and add to parameters
            BigDecimal totalGeneral = productos.stream()
                .map(ProductoFactura::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal subtotalGeneral = productos.stream()
                .map(ProductoFactura::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal descuentoGeneral = productos.stream()
                .map(ProductoFactura::getDescuento)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal isvGeneral = productos.stream()
                .map(ProductoFactura::getIsv)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            parameters.put("TOTAL_GENERAL", String.format("L. %,.2f", totalGeneral));
            parameters.put("SUBTOTAL_GENERAL", String.format("L. %,.2f", subtotalGeneral));
            parameters.put("DESCUENTO_GENERAL", String.format("L. %,.2f", descuentoGeneral));
            parameters.put("ISV_GENERAL", String.format("L. %,.2f", isvGeneral));
            
            // 3. Create a JRBeanCollectionDataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productos);
            System.out.println("DataSource creado con " + productos.size() + " productos");

            // 4. Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            System.out.println("Reporte rellenado exitosamente");

            // 5. Create output directory and export to PDF
            File reportesDir = new File("reportes");
            if (!reportesDir.exists()) {
                boolean created = reportesDir.mkdirs();
                System.out.println("Directorio reportes creado: " + created);
            }
            
            String timestamp = String.valueOf(System.currentTimeMillis());
            String outputFile = "reportes/factura_" + timestamp + ".pdf";
            
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);
            System.out.println("PDF exportado exitosamente en: " + outputFile);

            // 6. Try to open the generated PDF
            File pdfFile = new File(outputFile);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(pdfFile);
                    } catch (Exception e) {
                        System.err.println("No se pudo abrir el PDF automáticamente: " + e.getMessage());
                    }
                }
                return outputFile; // Retornar la ruta del archivo generado
            } else {
                throw new RuntimeException("El archivo PDF no se creó correctamente");
            }

        } catch (JRException e) {
            e.printStackTrace();
            String errorMsg = "Error de JasperReports: " + e.getMessage();
            System.err.println(errorMsg);
            JOptionPane.showMessageDialog(null, errorMsg, "Error de JasperReports", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "Error inesperado: " + e.getMessage();
            System.err.println(errorMsg);
            JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
