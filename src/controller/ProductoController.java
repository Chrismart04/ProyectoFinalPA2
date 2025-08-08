package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class ProductoController implements ActionListener, AbstractPanelController {

    PrincipalView frame;
    ProductoDAO productos;
    ProductoModel productoSeleccionado;

    public ProductoController(PrincipalView frame) {
        super();
        this.frame = frame;
        productos = new ProductoDAO();
        productoSeleccionado = null;
        init();

        this.frame.panelProducto.btnAgregar.addActionListener(this);
        this.frame.panelProducto.btnEditar.addActionListener(this);
        this.frame.panelProducto.btnBorrar.addActionListener(this);
        this.frame.panelProducto.btnBuscar.addActionListener(this);
        this.frame.panelProducto.btnLimpiar.addActionListener(this);
        this.frame.panelProducto.btnExportarProductosPDF.addActionListener(this);
        this.frame.panelProducto.btnSeleccionarImagen.addActionListener(this);
        
        // Configurar permisos por usuario
        configurarPermisos();
        
        // Selección de producto en tabla
        this.frame.panelProducto.tablaProducto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarProducto();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.frame.panelProducto.btnAgregar) {
            agregarProducto();
        } else if (e.getSource() == this.frame.panelProducto.btnEditar) {
            editarProducto();
        } else if (e.getSource() == this.frame.panelProducto.btnBorrar) {
            borrarProducto();
        } else if (e.getSource() == this.frame.panelProducto.btnBuscar) {
            buscarProducto();
        } else if (e.getSource() == this.frame.panelProducto.btnLimpiar) {
            limpiarCampos();
        } else if (e.getSource() == this.frame.panelProducto.btnExportarProductosPDF) {
            exportarProductosPDF();
        } else if (e.getSource() == this.frame.panelProducto.btnSeleccionarImagen) {
            seleccionarImagen();
        }
    }

    private void seleccionarImagen() {
        String rutaImagen = this.frame.panelProducto.seleccionarImagen();
        if (rutaImagen != null) {
            this.frame.panelProducto.textImagen.setText(rutaImagen);
            
            // Si es producto existente, actualizar de inmediato
            String idProducto = this.frame.panelProducto.textID.getText().trim();
            if (!idProducto.isEmpty()) {
                try {
                    int id = Integer.parseInt(idProducto);
                    productos.actualizarImagen(id, rutaImagen);
                    JOptionPane.showMessageDialog(this.frame, 
                        "Imagen seleccionada y guardada exitosamente en la base de datos", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Actualizar tabla
                    actualizarTabla();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this.frame, 
                        "Error al actualizar la imagen en la base de datos", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this.frame, 
                    "Imagen seleccionada y copiada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void agregarProducto() {
        try {
            if (!validarCampos()) {
                return;
            }
            String nombre = this.frame.panelProducto.textNombre.getText().trim();
            String descripcion = this.frame.panelProducto.textDescripcion.getText().trim();
            double precio = Double.parseDouble(this.frame.panelProducto.textPrecio.getText().trim());
            int stock = Integer.parseInt(this.frame.panelProducto.textStock.getText().trim());
            String imagen = this.frame.panelProducto.textImagen.getText().trim();
            
            String categoriaSeleccionada = (String) this.frame.panelProducto.comboCategoria.getSelectedItem();
            int idCategoria = Integer.parseInt(categoriaSeleccionada.split(" - ")[0]);

            ProductoModel nuevoProducto = new ProductoModel(0, nombre, descripcion, precio, stock, idCategoria, imagen);
            productos.insertar(nuevoProducto);
            actualizarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this.frame, "Producto agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this.frame, "El precio y stock deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al agregar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarProducto() {
        try {
            if (productoSeleccionado == null) {
                JOptionPane.showMessageDialog(this.frame, "Debe seleccionar un producto de la tabla", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!validarCampos()) {
                return;
            }
            
            // Actualizar los datos del producto seleccionado
            productoSeleccionado.setNombre(this.frame.panelProducto.textNombre.getText().trim());
            productoSeleccionado.setDescripcion(this.frame.panelProducto.textDescripcion.getText().trim());
            productoSeleccionado.setPrecio(Double.parseDouble(this.frame.panelProducto.textPrecio.getText().trim()));
            productoSeleccionado.setStock(Integer.parseInt(this.frame.panelProducto.textStock.getText().trim()));
            productoSeleccionado.setImagen(this.frame.panelProducto.textImagen.getText().trim());
            
            String categoriaSeleccionada = (String) this.frame.panelProducto.comboCategoria.getSelectedItem();
            int idCategoria = Integer.parseInt(categoriaSeleccionada.split(" - ")[0]);
            productoSeleccionado.setId_categoria(idCategoria);
            
            productos.modificar(productoSeleccionado);
            actualizarTabla();
            limpiarCampos();
            productoSeleccionado = null;
            JOptionPane.showMessageDialog(this.frame, "Producto editado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this.frame, "El precio y stock deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al editar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrarProducto() {
        try {
            // Verificar permisos antes de permitir la eliminación
            if (!SessionController.getInstance().canDeleteProducts()) {
                JOptionPane.showMessageDialog(this.frame, 
                    "No tiene permisos para eliminar productos.\nContacte al administrador.", 
                    "Acceso Denegado", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (productoSeleccionado == null) {
                JOptionPane.showMessageDialog(this.frame, "Debe seleccionar un producto de la tabla", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(
                this.frame,
                "¿Está seguro que desea eliminar el producto: " + productoSeleccionado.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                productos.eliminar(productoSeleccionado);
                actualizarTabla();
                limpiarCampos();
                productoSeleccionado = null;
                JOptionPane.showMessageDialog(this.frame, "Producto eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al eliminar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProducto() {
        try {
            String idTexto = this.frame.panelProducto.textID.getText().trim();
            
            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this.frame, "Debe ingresar el ID del producto a buscar", "Error", JOptionPane.WARNING_MESSAGE);
                this.frame.panelProducto.textID.requestFocus();
                return;
            }
            
            int id = Integer.parseInt(idTexto);
            ProductoModel productoBuscado = new ProductoModel(id, "", "", 0.0, 0, 0);
            productoBuscado = productos.buscar(productoBuscado);
            
            if (productoBuscado.getNombre() != null && !productoBuscado.getNombre().isEmpty()) {
                // Cargar datos del producto encontrado
                this.frame.panelProducto.textNombre.setText(productoBuscado.getNombre());
                this.frame.panelProducto.textDescripcion.setText(productoBuscado.getDescripcion());
                this.frame.panelProducto.textPrecio.setText(String.valueOf(productoBuscado.getPrecio()));
                this.frame.panelProducto.textStock.setText(String.valueOf(productoBuscado.getStock()));
                this.frame.panelProducto.establecerImagen(productoBuscado.getImagen());
                
                // Seleccionar categoría en el combo
                for (int i = 0; i < this.frame.panelProducto.comboCategoria.getItemCount(); i++) {
                    String item = this.frame.panelProducto.comboCategoria.getItemAt(i);
                    if (item.startsWith(productoBuscado.getId_categoria() + " - ")) {
                        this.frame.panelProducto.comboCategoria.setSelectedIndex(i);
                        break;
                    }
                }
                
                productoSeleccionado = productoBuscado;
                JOptionPane.showMessageDialog(this.frame, "Producto encontrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.frame, "No se encontró ningún producto con el ID especificado", "Producto no encontrado", JOptionPane.WARNING_MESSAGE);
                limpiarCampos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this.frame, "El ID debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al buscar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            fileChooser.setSelectedFile(new java.io.File("Productos_" + timestamp + ".pdf"));
            
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
                Paragraph title = new Paragraph("REPORTE DE PRODUCTOS", titleFont);
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
                DefaultTableModel model = (DefaultTableModel) this.frame.panelProducto.tablaProducto.getModel();
                
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        String cellValue = value != null ? value.toString() : "";
                        
                        // Formatear el precio con "L" si es la columna de precio
                        if (j == 3 && value instanceof Double) {
                            cellValue = "L " + String.format("%.2f", (Double) value);
                        }
                        
                        PdfPCell cell = new PdfPCell(new Phrase(cellValue, dataFont));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }
                }
                
                document.add(table);
                
                // Información adicional
                document.add(new Paragraph(" "));
                Paragraph footer = new Paragraph("Total de productos: " + model.getRowCount(), dateFont);
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

    private void seleccionarProducto() {
        int filaSeleccionada = this.frame.panelProducto.tablaProducto.getSelectedRow();
        
        if (filaSeleccionada >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) this.frame.panelProducto.tablaProducto.getModel();
            
            // Datos de la fila seleccionada
            int id = (Integer) modelo.getValueAt(filaSeleccionada, 0);
            String nombre = (String) modelo.getValueAt(filaSeleccionada, 1);
            String descripcion = (String) modelo.getValueAt(filaSeleccionada, 2);
            double precio = (Double) modelo.getValueAt(filaSeleccionada, 3);
            int stock = (Integer) modelo.getValueAt(filaSeleccionada, 4);
            String categoria = (String) modelo.getValueAt(filaSeleccionada, 5);
            
            // Cargar datos en los campos
            this.frame.panelProducto.textID.setText(String.valueOf(id));
            this.frame.panelProducto.textNombre.setText(nombre);
            this.frame.panelProducto.textDescripcion.setText(descripcion);
            this.frame.panelProducto.textPrecio.setText(String.valueOf(precio));
            this.frame.panelProducto.textStock.setText(String.valueOf(stock));
            
            // Seleccionar categoría en el combo
            for (int i = 0; i < this.frame.panelProducto.comboCategoria.getItemCount(); i++) {
                String item = this.frame.panelProducto.comboCategoria.getItemAt(i);
                if (item.endsWith(categoria)) {
                    this.frame.panelProducto.comboCategoria.setSelectedIndex(i);
                    break;
                }
            }
            
            // Crear objeto del producto seleccionado
            productoSeleccionado = new ProductoModel(id, nombre, descripcion, precio, stock, 0);
        }
    }

    private boolean validarCampos() {
        if (this.frame.panelProducto.textNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.frame, "Debe ingresar el nombre del producto", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (this.frame.panelProducto.textPrecio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.frame, "Debe ingresar el precio", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (this.frame.panelProducto.textStock.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.frame, "Debe ingresar el stock", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (this.frame.panelProducto.comboCategoria.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this.frame, "Debe seleccionar una categoría", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) this.frame.panelProducto.tablaProducto.getModel();
        modelo.setRowCount(0);
        for (ProductoModel producto : productos.obtener()) {
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

    private void limpiarCampos() {
        this.frame.panelProducto.textID.setText("");
        this.frame.panelProducto.textNombre.setText("");
        this.frame.panelProducto.textDescripcion.setText("");
        this.frame.panelProducto.textPrecio.setText("");
        this.frame.panelProducto.textStock.setText("");
        this.frame.panelProducto.limpiarImagen();
        this.frame.panelProducto.comboCategoria.setSelectedIndex(0);
        productoSeleccionado = null;
    }

    @Override
    public void init() {
        cargarCategorias();
        actualizarTabla();
        configurarPermisos(); // Reconfigurar permisos al inicializar
    }
    
    private void configurarPermisos() {
        boolean canDelete = SessionController.getInstance().canDeleteProducts();
        
        // Habilitar o deshabilitar borrar según permisos
        this.frame.panelProducto.btnBorrar.setEnabled(canDelete);
        
        // Cambiar la apariencia visual del botón si está deshabilitado
        if (!canDelete) {
            this.frame.panelProducto.btnBorrar.setToolTipText("No tiene permisos para eliminar productos");
            this.frame.panelProducto.btnBorrar.setBackground(new java.awt.Color(150, 150, 150));
        } else {
            this.frame.panelProducto.btnBorrar.setToolTipText("Eliminar producto seleccionado");
            this.frame.panelProducto.btnBorrar.setBackground(null);
        }
    }
    
    private void cargarCategorias() {
        this.frame.panelProducto.comboCategoria.removeAllItems();
        for (String categoria : productos.obtenerCategorias()) {
            this.frame.panelProducto.comboCategoria.addItem(categoria);
        }
    }
} 