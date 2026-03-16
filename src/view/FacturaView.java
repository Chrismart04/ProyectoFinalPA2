package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.reportes.FacturaGenerator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacturaView extends JPanel {

    // Campos del formulario
    private JTextField txtClienteNombre, txtClienteRtn, txtClienteTelefono, txtClienteDireccion;
    private JTextField txtProductoNombre, txtCantidad, txtPrecioUnitario, txtDescuento, txtIsvPorcentaje;
    private JTextField txtFacturaNumero;
    private JLabel lblFechaEmision, lblSubtotal, lblDescuentoTotal, lblIsvTotal, lblTotal;
    
    // Tabla de productos
    private JTable productosTable;
    private DefaultTableModel tableModel;
    
    // Panel de facturas generadas
    private JPanel panelFacturasGrid;
    private JScrollPane scrollFacturas;
    
    // Botones
    private JButton btnAgregarProducto, btnEliminarProducto, btnLimpiarFormulario, btnGenerarFactura;
    private JButton btnRefrescarFacturas;
    
    // Lista para almacenar información de facturas generadas
    private List<FacturaInfo> facturasGeneradas;
    
    // Clase interna para almacenar información de facturas
    private static class FacturaInfo {
        private String numeroFactura;
        private String clienteNombre;
        private String fechaEmision;
        private String total;
        private String rutaArchivo;
        
        public FacturaInfo(String numeroFactura, String clienteNombre, String fechaEmision, String total, String rutaArchivo) {
            this.numeroFactura = numeroFactura;
            this.clienteNombre = clienteNombre;
            this.fechaEmision = fechaEmision;
            this.total = total;
            this.rutaArchivo = rutaArchivo;
        }
        
        // Getters
        public String getNumeroFactura() { return numeroFactura; }
        public String getClienteNombre() { return clienteNombre; }
        public String getFechaEmision() { return fechaEmision; }
        public String getTotal() { return total; }
        public String getRutaArchivo() { return rutaArchivo; }
    }

    public FacturaView() {
        facturasGeneradas = new ArrayList<>();
        initComponents();
        setupEventListeners();
        actualizarTotales();
        cargarFacturasExistentes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Facturación"));
        setBackground(new Color(245, 245, 245));

        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Pestaña de datos de factura
        JPanel panelFactura = createFacturaPanel();
        tabbedPane.addTab("Datos de Factura", panelFactura);
        
        // Pestaña de productos
        JPanel panelProductos = createProductosPanel();
        tabbedPane.addTab("Productos", panelProductos);
        
        // Pestaña de facturas generadas
        JPanel panelFacturasGeneradas = createFacturasGeneradasPanel();
        tabbedPane.addTab("Facturas Generadas", panelFacturasGeneradas);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Panel de totales y botones
        JPanel panelBottom = createBottomPanel();
        add(panelBottom, BorderLayout.SOUTH);
    }
    
    private JPanel createFacturaPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Información de la factura
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("No. Factura:"), gbc);
        gbc.gridx = 1;
        txtFacturaNumero = new JTextField("001-001-01-" + String.format("%08d", System.currentTimeMillis() % 100000000), 20);
        panel.add(txtFacturaNumero, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Fecha Emisión:"), gbc);
        gbc.gridx = 1;
        lblFechaEmision = new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        lblFechaEmision.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblFechaEmision, gbc);
        
        // Información del cliente
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel lblClienteTitle = new JLabel("Información del Cliente");
        lblClienteTitle.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblClienteTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Nombre del Cliente:"), gbc);
        gbc.gridx = 1;
        txtClienteNombre = new JTextField(25);
        panel.add(txtClienteNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("RTN:"), gbc);
        gbc.gridx = 1;
        txtClienteRtn = new JTextField(20);
        panel.add(txtClienteRtn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtClienteTelefono = new JTextField(15);
        panel.add(txtClienteTelefono, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtClienteDireccion = new JTextField(30);
        panel.add(txtClienteDireccion, gbc);
        
        return panel;
    }
    
    private JPanel createProductosPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 245, 245));
        
        // Panel para agregar productos
        JPanel panelAgregar = new JPanel(new GridBagLayout());
        panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar Producto"));
        panelAgregar.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelAgregar.add(new JLabel("Producto:"), gbc);
        gbc.gridx = 1;
        txtProductoNombre = new JTextField(20);
        panelAgregar.add(txtProductoNombre, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        panelAgregar.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 3;
        txtCantidad = new JTextField("1", 8);
        panelAgregar.add(txtCantidad, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelAgregar.add(new JLabel("Precio Unitario:"), gbc);
        gbc.gridx = 1;
        txtPrecioUnitario = new JTextField("0.00", 10);
        panelAgregar.add(txtPrecioUnitario, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        panelAgregar.add(new JLabel("Descuento:"), gbc);
        gbc.gridx = 3;
        txtDescuento = new JTextField("0.00", 8);
        panelAgregar.add(txtDescuento, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panelAgregar.add(new JLabel("ISV (%):"), gbc);
        gbc.gridx = 1;
        txtIsvPorcentaje = new JTextField("15.00", 8);
        panelAgregar.add(txtIsvPorcentaje, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(245, 245, 245));
        btnAgregarProducto = new JButton("Agregar");
        btnAgregarProducto.setBackground(new Color(60, 63, 65));
        btnAgregarProducto.setForeground(Color.WHITE);
        btnEliminarProducto = new JButton("Eliminar");
        btnEliminarProducto.setBackground(new Color(200, 50, 50));
        btnEliminarProducto.setForeground(Color.WHITE);
        panelBotones.add(btnAgregarProducto);
        panelBotones.add(btnEliminarProducto);
        panelAgregar.add(panelBotones, gbc);
        
        panel.add(panelAgregar, BorderLayout.NORTH);
        
        // Tabla de productos
        String[] columnNames = {"Producto", "Cantidad", "Precio Unit.", "Subtotal", "Descuento", "ISV", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productosTable = new JTable(tableModel);
        productosTable.setRowHeight(25);
        productosTable.setGridColor(new Color(200, 200, 200));
        productosTable.setSelectionBackground(new Color(180, 180, 180));
        
        JScrollPane scrollPane = new JScrollPane(productosTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Productos en la Factura"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFacturasGeneradasPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 245, 245));
        
        // Panel superior con título y botón de refrescar
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(245, 245, 245));
        
        JLabel lblTitulo = new JLabel("Facturas Generadas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(60, 63, 65));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        btnRefrescarFacturas = new JButton("Refrescar");
        btnRefrescarFacturas.setBackground(new Color(60, 63, 65));
        btnRefrescarFacturas.setForeground(Color.WHITE);
        btnRefrescarFacturas.setPreferredSize(new Dimension(100, 30));
        panelSuperior.add(btnRefrescarFacturas, BorderLayout.EAST);
        
        panel.add(panelSuperior, BorderLayout.NORTH);
        
        // Panel para las tarjetas de facturas
        panelFacturasGrid = new JPanel();
        panelFacturasGrid.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columnas
        panelFacturasGrid.setBackground(new Color(245, 245, 245));
        
        scrollFacturas = new JScrollPane(panelFacturasGrid);
        scrollFacturas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollFacturas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollFacturas.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollFacturas, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 245, 245));
        
        // Panel de totales
        JPanel panelTotales = new JPanel(new GridLayout(4, 2, 5, 5));
        panelTotales.setBorder(BorderFactory.createTitledBorder("Totales"));
        panelTotales.setBackground(new Color(245, 245, 245));
        
        panelTotales.add(new JLabel("Subtotal:"));
        lblSubtotal = new JLabel("L. 0.00");
        lblSubtotal.setFont(new Font("Arial", Font.BOLD, 14));
        panelTotales.add(lblSubtotal);
        
        panelTotales.add(new JLabel("Descuento Total:"));
        lblDescuentoTotal = new JLabel("L. 0.00");
        lblDescuentoTotal.setFont(new Font("Arial", Font.BOLD, 14));
        panelTotales.add(lblDescuentoTotal);
        
        panelTotales.add(new JLabel("ISV Total:"));
        lblIsvTotal = new JLabel("L. 0.00");
        lblIsvTotal.setFont(new Font("Arial", Font.BOLD, 14));
        panelTotales.add(lblIsvTotal);
        
        panelTotales.add(new JLabel("TOTAL:"));
        lblTotal = new JLabel("L. 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(new Color(0, 120, 0));
        panelTotales.add(lblTotal);
        
        panel.add(panelTotales, BorderLayout.EAST);
        
        // Panel de botones principales
        JPanel panelBotonesPrincipales = new JPanel(new FlowLayout());
        panelBotonesPrincipales.setBackground(new Color(245, 245, 245));
        
        btnLimpiarFormulario = new JButton("Limpiar Formulario");
        btnLimpiarFormulario.setFont(new Font("Arial", Font.PLAIN, 14));
        btnLimpiarFormulario.setBackground(new Color(100, 100, 100));
        btnLimpiarFormulario.setForeground(Color.WHITE);
        btnLimpiarFormulario.setPreferredSize(new Dimension(150, 35));
        
        btnGenerarFactura = new JButton("Generar Factura PDF");
        btnGenerarFactura.setFont(new Font("Arial", Font.BOLD, 14));
        btnGenerarFactura.setBackground(new Color(0, 120, 0));
        btnGenerarFactura.setForeground(Color.WHITE);
        btnGenerarFactura.setPreferredSize(new Dimension(200, 35));
        
        panelBotonesPrincipales.add(btnLimpiarFormulario);
        panelBotonesPrincipales.add(btnGenerarFactura);
        
        panel.add(panelBotonesPrincipales, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupEventListeners() {
        btnAgregarProducto.addActionListener(e -> agregarProducto());
        btnEliminarProducto.addActionListener(e -> eliminarProducto());
        btnLimpiarFormulario.addActionListener(e -> limpiarFormulario());
        btnGenerarFactura.addActionListener(e -> generarFactura());
        btnRefrescarFacturas.addActionListener(e -> cargarFacturasExistentes());
    }
    
    private void cargarFacturasExistentes() {
        facturasGeneradas.clear();
        panelFacturasGrid.removeAll();
        
        // Buscar archivos PDF en la carpeta reportes
        File carpetaReportes = new File("reportes");
        if (carpetaReportes.exists() && carpetaReportes.isDirectory()) {
            File[] archivos = carpetaReportes.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
            if (archivos != null) {
                for (File archivo : archivos) {
                    String nombreArchivo = archivo.getName();
                    if (nombreArchivo.startsWith("factura_")) {
                        // Extraer información del nombre del archivo
                        String timestamp = nombreArchivo.replace("factura_", "").replace(".pdf", "");
                        Date fecha = new Date(Long.parseLong(timestamp));
                        String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fecha);
                        
                        FacturaInfo facturaInfo = new FacturaInfo(
                            "001-001-01-" + timestamp.substring(timestamp.length() - 8),
                            "Cliente",
                            fechaFormateada,
                            "L. 0.00",
                            archivo.getAbsolutePath()
                        );
                        
                        facturasGeneradas.add(facturaInfo);
                        crearTarjetaFactura(facturaInfo);
                    }
                }
            }
        }
        
        if (facturasGeneradas.isEmpty()) {
            JLabel lblSinFacturas = new JLabel("No hay facturas generadas", SwingConstants.CENTER);
            lblSinFacturas.setFont(new Font("Arial", Font.ITALIC, 14));
            lblSinFacturas.setForeground(Color.GRAY);
            panelFacturasGrid.add(lblSinFacturas);
        }
        
        panelFacturasGrid.revalidate();
        panelFacturasGrid.repaint();
    }
    
    private void crearTarjetaFactura(FacturaInfo facturaInfo) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(250, 120));
        
        // Panel de información
        JPanel panelInfo = new JPanel(new GridLayout(4, 1, 2, 2));
        panelInfo.setBackground(Color.WHITE);
        
        JLabel lblNumero = new JLabel("No: " + facturaInfo.getNumeroFactura());
        lblNumero.setFont(new Font("Arial", Font.BOLD, 12));
        lblNumero.setForeground(new Color(60, 63, 65));
        
        JLabel lblCliente = new JLabel("Cliente: " + facturaInfo.getClienteNombre());
        lblCliente.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JLabel lblFecha = new JLabel("Fecha: " + facturaInfo.getFechaEmision());
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 11));
        
        JLabel lblTotal = new JLabel("Total: " + facturaInfo.getTotal());
        lblTotal.setFont(new Font("Arial", Font.BOLD, 12));
        lblTotal.setForeground(new Color(0, 120, 0));
        
        panelInfo.add(lblNumero);
        panelInfo.add(lblCliente);
        panelInfo.add(lblFecha);
        panelInfo.add(lblTotal);
        
        tarjeta.add(panelInfo, BorderLayout.CENTER);
        
        // Agregar funcionalidad de clic
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirFactura(facturaInfo);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBackground(new Color(240, 248, 255));
                tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBackground(Color.WHITE);
                tarjeta.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        panelFacturasGrid.add(tarjeta);
    }
    
    private void abrirFactura(FacturaInfo facturaInfo) {
        try {
            File archivo = new File(facturaInfo.getRutaArchivo());
            if (archivo.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(archivo);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No se puede abrir el archivo automáticamente.\nUbicación: " + facturaInfo.getRutaArchivo(),
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "El archivo de factura no existe: " + facturaInfo.getRutaArchivo(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al abrir la factura: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarProducto() {
        try {
            String producto = txtProductoNombre.getText().trim();
            String cantidadText = txtCantidad.getText().trim();
            String precioText = txtPrecioUnitario.getText().trim();
            String descuentoText = txtDescuento.getText().trim();
            String isvPorcentajeText = txtIsvPorcentaje.getText().trim();

            if (producto.isEmpty() || cantidadText.isEmpty() || precioText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.", 
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int cantidad = Integer.parseInt(cantidadText);
            BigDecimal precio = new BigDecimal(precioText);
            BigDecimal descuento = new BigDecimal(descuentoText.isEmpty() ? "0" : descuentoText);
            BigDecimal isvPorcentaje = new BigDecimal(isvPorcentajeText.isEmpty() ? "15" : isvPorcentajeText);

            if (cantidad <= 0 || precio.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser valores positivos.", 
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cálculos
            BigDecimal subtotal = precio.multiply(new BigDecimal(cantidad));
            BigDecimal baseImponible = subtotal.subtract(descuento);
            BigDecimal isvMonto = baseImponible.multiply(isvPorcentaje.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
            BigDecimal total = baseImponible.add(isvMonto);

            // Agregar a la tabla
            Object[] fila = {producto, cantidad, precio, subtotal, descuento, isvMonto, total};
            tableModel.addRow(fila);

            // Limpiar campos de producto
            txtProductoNombre.setText("");
            txtCantidad.setText("1");
            txtPrecioUnitario.setText("0.00");
            txtDescuento.setText("0.00");
            txtIsvPorcentaje.setText("15.00");

            // Actualizar totales
            actualizarTotales();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese números válidos en los campos numéricos.", 
                "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        int selectedRow = productosTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
            actualizarTotales();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.", 
                "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        // Limpiar campos del cliente
        txtClienteNombre.setText("");
        txtClienteRtn.setText("");
        txtClienteTelefono.setText("");
        txtClienteDireccion.setText("");
        
        // Limpiar campos de producto
        txtProductoNombre.setText("");
        txtCantidad.setText("1");
        txtPrecioUnitario.setText("0.00");
        txtDescuento.setText("0.00");
        txtIsvPorcentaje.setText("15.00");
        
        // Generar nuevo número de factura
        txtFacturaNumero.setText("001-001-01-" + String.format("%08d", System.currentTimeMillis() % 100000000));
        
        // Limpiar tabla
        tableModel.setRowCount(0);
        
        // Actualizar totales
        actualizarTotales();
        
        JOptionPane.showMessageDialog(this, "Formulario limpiado correctamente.", 
            "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarTotales() {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal descuentoTotal = BigDecimal.ZERO;
        BigDecimal isvTotal = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            subtotal = subtotal.add(new BigDecimal(tableModel.getValueAt(i, 3).toString()));
            descuentoTotal = descuentoTotal.add(new BigDecimal(tableModel.getValueAt(i, 4).toString()));
            isvTotal = isvTotal.add(new BigDecimal(tableModel.getValueAt(i, 5).toString()));
            total = total.add(new BigDecimal(tableModel.getValueAt(i, 6).toString()));
        }

        lblSubtotal.setText(String.format("L. %,.2f", subtotal));
        lblDescuentoTotal.setText(String.format("L. %,.2f", descuentoTotal));
        lblIsvTotal.setText(String.format("L. %,.2f", isvTotal));
        lblTotal.setText(String.format("L. %,.2f", total));
    }

    private void generarFactura() {
        try {
            // Validaciones
            String clienteNombre = txtClienteNombre.getText().trim();
            String clienteRtn = txtClienteRtn.getText().trim();

            if (clienteNombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del cliente es obligatorio.", 
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto a la factura.", 
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear parámetros para el reporte
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CLIENTE_NOMBRE", clienteNombre);
            parameters.put("CLIENTE_RTN", clienteRtn);
            parameters.put("CLIENTE_TELEFONO", txtClienteTelefono.getText().trim());
            parameters.put("CLIENTE_DIRECCION", txtClienteDireccion.getText().trim());
            parameters.put("FACTURA_NUMERO", txtFacturaNumero.getText());
            parameters.put("FECHA_EMISION", lblFechaEmision.getText());
            parameters.put("FECHA_VENCE", "N/A");

            // Recolectar productos de la tabla
            List<FacturaGenerator.ProductoFactura> productos = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String nombre = (String) tableModel.getValueAt(i, 0);
                int cantidad = (Integer) tableModel.getValueAt(i, 1);
                BigDecimal precio = (BigDecimal) tableModel.getValueAt(i, 2);
                BigDecimal descuento = (BigDecimal) tableModel.getValueAt(i, 4);
                BigDecimal isv = (BigDecimal) tableModel.getValueAt(i, 5);
                productos.add(new FacturaGenerator.ProductoFactura(nombre, cantidad, precio, descuento, isv));
            }

            // Generar la factura
            FacturaGenerator generator = new FacturaGenerator();
            String rutaArchivo = generator.generarFactura(parameters, productos);

            // Agregar la factura a la lista si se generó correctamente
            if (rutaArchivo != null && !rutaArchivo.isEmpty()) {
                String timestamp = String.valueOf(System.currentTimeMillis());
                FacturaInfo nuevaFactura = new FacturaInfo(
                    txtFacturaNumero.getText(),
                    clienteNombre,
                    lblFechaEmision.getText(),
                    lblTotal.getText(),
                    "reportes/factura_" + timestamp + ".pdf"
                );
                
                facturasGeneradas.add(nuevaFactura);
                crearTarjetaFactura(nuevaFactura);
                panelFacturasGrid.revalidate();
                panelFacturasGrid.repaint();
            }

            JOptionPane.showMessageDialog(this, "Factura generada exitosamente!", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar la factura: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirFactura(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            if (archivo.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(archivo);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "No se puede abrir el archivo automáticamente.\nRuta: " + archivo.getAbsolutePath(),
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "El archivo no existe: " + rutaArchivo,
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al abrir el archivo: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
