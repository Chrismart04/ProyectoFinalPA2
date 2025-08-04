package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import ClinicaDAO.ProductoDAO;
import model.ProductoModel;
import view.PrincipalView;

public class ListaProductoController implements ActionListener, AbstractPanelController {

    PrincipalView frame;
    ProductoDAO productos;
    List<ProductoModel> productosFiltrados;

    public ListaProductoController(PrincipalView frame) {
        super();
        this.frame = frame;
        productos = new ProductoDAO();
        productosFiltrados = null;
        init();

        this.frame.panelListaProducto.btnBuscar.addActionListener(this);
        this.frame.panelListaProducto.btnLimpiar.addActionListener(this);
        this.frame.panelListaProducto.btnExportarPDF.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.frame.panelListaProducto.btnBuscar) {
            buscarProductos();
        } else if (e.getSource() == this.frame.panelListaProducto.btnLimpiar) {
            limpiarFiltros();
        } else if (e.getSource() == this.frame.panelListaProducto.btnExportarPDF) {
            exportarProductosPDF();
        }
    }

    private void buscarProductos() {
        String nombreBuscar = this.frame.panelListaProducto.textBuscar.getText().trim();
        String categoriaFiltro = (String) this.frame.panelListaProducto.comboCategoriaFiltro.getSelectedItem();
        
        // Filtrar productos
        productosFiltrados = productos.obtener();
        
        if (!nombreBuscar.isEmpty()) {
            productosFiltrados = productosFiltrados.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombreBuscar.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
        }
        
        if (categoriaFiltro != null && !categoriaFiltro.equals("Todas las categorías")) {
            int idCategoria = Integer.parseInt(categoriaFiltro.split(" - ")[0]);
            productosFiltrados = productosFiltrados.stream()
                .filter(p -> p.getId_categoria() == idCategoria)
                .collect(java.util.stream.Collectors.toList());
        }
        
        actualizarTarjetas();
    }

    private void limpiarFiltros() {
        this.frame.panelListaProducto.textBuscar.setText("");
        this.frame.panelListaProducto.comboCategoriaFiltro.setSelectedIndex(0);
        productosFiltrados = null;
        actualizarTarjetas();
    }

    private void exportarProductosPDF() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar archivo PDF");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            
            // Filtro para archivos PDF
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos PDF", "pdf");
            fileChooser.setFileFilter(filter);
            
            // Nombre sugerido del archivo
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = dateFormat.format(new Date());
            fileChooser.setSelectedFile(new java.io.File("Lista_Productos_" + timestamp + ".pdf"));
            
            int userSelection = fileChooser.showSaveDialog(this.frame);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                
                // Asegurar que el archivo tenga extensión .pdf
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }
                
                // Crear el documento PDF
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                
                document.open();
                
                // Título del documento
                Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
                Paragraph title = new Paragraph("LISTA DE PRODUCTOS", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                
                // Fecha de generación
                Font dateFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
                Paragraph date = new Paragraph("Fecha de generación: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), dateFont);
                date.setAlignment(Element.ALIGN_RIGHT);
                document.add(date);
                
                // Espacio
                document.add(new Paragraph(" "));
                
                // Crear tabla con 6 columnas
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);
                
                // Definir anchos de columnas
                float[] columnWidths = {8f, 20f, 25f, 12f, 10f, 25f};
                table.setWidths(columnWidths);
                
                // Encabezados de la tabla
                Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
                String[] headers = {"ID", "Nombre", "Descripción", "Precio (L)", "Stock", "Categoría"};
                
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
                    table.addCell(cell);
                }
                
                // Datos de la tabla
                Font dataFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
                List<ProductoModel> productosAMostrar = productosFiltrados != null ? productosFiltrados : productos.obtener();
                
                for (ProductoModel producto : productosAMostrar) {
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(producto.getId()), dataFont)));
                    table.addCell(new PdfPCell(new Phrase(producto.getNombre(), dataFont)));
                    table.addCell(new PdfPCell(new Phrase(producto.getDescripcion(), dataFont)));
                    table.addCell(new PdfPCell(new Phrase("L " + String.format("%.2f", producto.getPrecio()), dataFont)));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(producto.getStock()), dataFont)));
                    table.addCell(new PdfPCell(new Phrase(producto.getNombreCategoria() != null ? producto.getNombreCategoria() : "Sin categoría", dataFont)));
                }
                
                document.add(table);
                
                // Información adicional
                document.add(new Paragraph(" "));
                Paragraph footer = new Paragraph("Total de productos: " + productosAMostrar.size(), dateFont);
                footer.setAlignment(Element.ALIGN_LEFT);
                document.add(footer);
                
                document.close();
                
                JOptionPane.showMessageDialog(this.frame, 
                    "PDF exportado exitosamente en:\n" + filePath, 
                    "Exportación exitosa", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, 
                "Error al exportar PDF: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) this.frame.panelListaProducto.tablaProductos.getModel();
        modelo.setRowCount(0);
        
        List<ProductoModel> productosAMostrar = productosFiltrados != null ? productosFiltrados : productos.obtener();
        
        for (ProductoModel producto : productosAMostrar) {
            Object[] fila = {
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getNombreCategoria() != null ? producto.getNombreCategoria() : "Sin categoría"
            };
            modelo.addRow(fila);
        }
    }
    
    private void actualizarTarjetas() {
        // Limpiar tarjetas existentes
        this.frame.panelListaProducto.limpiarTarjetas();
        
        List<ProductoModel> productosAMostrar = productosFiltrados != null ? productosFiltrados : productos.obtener();
        
        for (ProductoModel producto : productosAMostrar) {
            // Crear tarjeta para cada producto
            javax.swing.JPanel tarjeta = this.frame.panelListaProducto.crearTarjetaProducto(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getNombreCategoria(),
                producto.getImagen()
            );
            
            // Agregar la tarjeta al panel
            this.frame.panelListaProducto.panelCards.add(tarjeta);
        }
        
        // Actualizar la vista
        this.frame.panelListaProducto.panelCards.revalidate();
        this.frame.panelListaProducto.panelCards.repaint();
    }

    @Override
    public void init() {
        cargarCategorias();
        actualizarTarjetas();
    }
    
    private void cargarCategorias() {
        this.frame.panelListaProducto.comboCategoriaFiltro.removeAllItems();
        this.frame.panelListaProducto.comboCategoriaFiltro.addItem("Todas las categorías");
        for (String categoria : productos.obtenerCategorias()) {
            this.frame.panelListaProducto.comboCategoriaFiltro.addItem(categoria);
        }
    }
} 