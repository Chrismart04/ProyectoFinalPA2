package view;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ProductoPanelView extends JPanel {

	private static final long serialVersionUID = 1L;
	public JTextField textNombre;
	public JTextField textPrecio;
	public JTextField textStock;
	public JTable tablaProducto;
	public JTextField textID;
	public JButton btnEditar;
	public JButton btnBorrar;
	public JButton btnBuscar;
	public JButton btnAgregar;
	public JButton btnLimpiar;
	public JButton btnExportarProductosPDF;
	public JTextArea textDescripcion;
	public JComboBox<String> comboCategoria;
	public JTextField textImagen;
	public JButton btnSeleccionarImagen;
	public JLabel lblImagenSeleccionada;

	/**
	 * Create the panel.
	 */
	public ProductoPanelView() {
		setBackground(Color.DARK_GRAY);
		setLayout(null);
		
		JPanel panelProducto = new JPanel();
		panelProducto.setLayout(null);
		panelProducto.setBorder(new TitledBorder(null, "Producto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelProducto.setBackground(Color.WHITE);
		panelProducto.setBounds(0, 0, 903, 589);
		add(panelProducto);
		
		JPanel panelDatosGenerales = new JPanel();
		panelDatosGenerales.setLayout(null);
		panelDatosGenerales.setBorder(new TitledBorder(null, "Datos Generales", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDatosGenerales.setBackground(Color.WHITE);
		panelDatosGenerales.setBounds(10, 20, 883, 280);
		panelProducto.add(panelDatosGenerales);
		
		JLabel lblNewLabel_1 = new JLabel("ID:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(21, 13, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1);
		
		textID = new JTextField();
		textID.setEditable(false);
		textID.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textID.setBounds(115, 11, 425, 34);
		panelDatosGenerales.add(textID);
		
		JLabel lblNewLabel_1_1 = new JLabel("Nombre:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(21, 49, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_1);
		
		textNombre = new JTextField();
		textNombre.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textNombre.setBounds(115, 47, 425, 34);
		panelDatosGenerales.add(textNombre);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Descripción:");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_1.setBounds(21, 92, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_1_1);
		
		textDescripcion = new JTextArea();
		textDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textDescripcion.setBounds(115, 90, 425, 60);
		textDescripcion.setLineWrap(true);
		textDescripcion.setWrapStyleWord(true);
		panelDatosGenerales.add(textDescripcion);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Precio:");
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_1_1.setBounds(21, 160, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_1_1_1);
		
		textPrecio = new JTextField();
		textPrecio.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textPrecio.setBounds(115, 158, 200, 34);
		panelDatosGenerales.add(textPrecio);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Stock:");
		lblNewLabel_1_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_1_1_1.setBounds(340, 160, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_1_1_1_1);
		
		textStock = new JTextField();
		textStock.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textStock.setBounds(434, 158, 106, 34);
		panelDatosGenerales.add(textStock);
		
		JLabel lblNewLabel_1_1_1_1_1_1 = new JLabel("Categoría:");
		lblNewLabel_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_1_1_1_1.setBounds(21, 205, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_1_1_1_1_1);
		
		comboCategoria = new JComboBox<String>();
		comboCategoria.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboCategoria.setBounds(115, 203, 200, 34);
		panelDatosGenerales.add(comboCategoria);
		
		JLabel lblImagen = new JLabel("Imagen:");
		lblImagen.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblImagen.setBounds(340, 205, 93, 30);
		panelDatosGenerales.add(lblImagen);
		
		btnSeleccionarImagen = new JButton("Imagen");
		btnSeleccionarImagen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSeleccionarImagen.setBounds(434, 203, 120, 34);
		btnSeleccionarImagen.setIcon(new ImageIcon(ProductoPanelView.class.getResource("/icons/icons8_save_32px_1.png")));
		btnSeleccionarImagen.setBackground(new Color(60, 63, 65));
		btnSeleccionarImagen.setForeground(Color.WHITE);
		btnSeleccionarImagen.setBorderPainted(false);
		btnSeleccionarImagen.setFocusPainted(false);
		panelDatosGenerales.add(btnSeleccionarImagen);
		
		// Campo oculto para almacenar la ruta de la imagen
		textImagen = new JTextField();
		textImagen.setVisible(false);
		panelDatosGenerales.add(textImagen);
		
		// Label para mostrar la imagen seleccionada
		lblImagenSeleccionada = new JLabel("Sin imagen seleccionada");
		lblImagenSeleccionada.setFont(new Font("Tahoma", Font.ITALIC, 12));
		lblImagenSeleccionada.setForeground(new Color(100, 100, 100));
		lblImagenSeleccionada.setBounds(560, 205, 200, 30);
		panelDatosGenerales.add(lblImagenSeleccionada);
		
		// Panel para los botones en el lado derecho
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(null);
		panelBotones.setBorder(new TitledBorder(null, "Acciones", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBotones.setBackground(Color.WHITE);
		panelBotones.setBounds(560, 20, 180, 240);
		panelDatosGenerales.add(panelBotones);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setIcon(new ImageIcon(ProductoPanelView.class.getResource("/icons/icons8_save_32px_1.png")));
		btnAgregar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAgregar.setBounds(10, 25, 160, 35);
		panelBotones.add(btnAgregar);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setIcon(new ImageIcon(ProductoPanelView.class.getResource("/icons/icons8_search_property_32px_1.png")));
		btnBuscar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBuscar.setBounds(10, 70, 160, 35);
		panelBotones.add(btnBuscar);
		
		btnEditar = new JButton("Editar");
		btnEditar.setIcon(new ImageIcon(ProductoPanelView.class.getResource("/icons/icons8_edit_file_32px_1.png")));
		btnEditar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEditar.setBounds(10, 115, 160, 35);
		panelBotones.add(btnEditar);
		
		btnBorrar = new JButton("Borrar");
		btnBorrar.setIcon(new ImageIcon(ProductoPanelView.class.getResource("/icons/icons8_delete_database_32px.png")));
		btnBorrar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBorrar.setBounds(10, 160, 160, 35);
		panelBotones.add(btnBorrar);
		
		// Panel para botones adicionales
		JPanel panelBotonesExtra = new JPanel();
		panelBotonesExtra.setLayout(null);
		panelBotonesExtra.setBorder(new TitledBorder(null, "Herramientas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBotonesExtra.setBackground(Color.WHITE);
		panelBotonesExtra.setBounds(750, 20, 120, 160);
		panelDatosGenerales.add(panelBotonesExtra);
		
		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setIcon(new ImageIcon(ProductoPanelView.class.getResource("/icons/Close.png")));
		btnLimpiar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLimpiar.setBounds(10, 25, 100, 35);
		panelBotonesExtra.add(btnLimpiar);
		
		btnExportarProductosPDF = new JButton("PDF");
		btnExportarProductosPDF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExportarProductosPDF.setBounds(10, 70, 100, 35);
		panelBotonesExtra.add(btnExportarProductosPDF);
		
		JPanel panelDetalle = new JPanel();
		panelDetalle.setLayout(null);
		panelDetalle.setBorder(new TitledBorder(null, "Detalle de Productos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDetalle.setBackground(Color.WHITE);
		panelDetalle.setBounds(10, 310, 883, 268);
		panelProducto.add(panelDetalle);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 26, 862, 231);
		panelDetalle.add(scrollPane);
		
		tablaProducto = new JTable();
		tablaProducto.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría"
			}
		));
		scrollPane.setViewportView(tablaProducto);
	}
	
	/**
	 * Método para seleccionar una imagen del sistema de archivos
	 * Funciona tanto para productos nuevos como para actualizar productos existentes
	 */
	public String seleccionarImagen() {
		// Verificar que el nombre del producto esté ingresado
		String nombreProducto = textNombre.getText().trim();
		if (nombreProducto.isEmpty()) {
			javax.swing.JOptionPane.showMessageDialog(this, 
				"Debe ingresar el nombre del producto antes de seleccionar una imagen", 
				"Error", 
				javax.swing.JOptionPane.WARNING_MESSAGE);
			textNombre.requestFocus();
			return null;
		}
		
		// Determinar si es un producto nuevo o existente
		String idProducto = textID.getText().trim();
		boolean esProductoExistente = !idProducto.isEmpty();
		
		String tituloDialogo = esProductoExistente ? 
			"Seleccionar imagen para: " + nombreProducto :
			"Seleccionar imagen del producto";
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(tituloDialogo);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		// Filtro para archivos de imagen
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"Archivos de imagen", "jpg", "jpeg", "png", "gif", "bmp"
		);
		fileChooser.setFileFilter(filter);
		
		int userSelection = fileChooser.showOpenDialog(this);
		
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			
			try {
				// Crear directorio de productos si no existe
				Path productosDir = Paths.get("src/img2/productos");
				if (!Files.exists(productosDir)) {
					Files.createDirectories(productosDir);
				}
				
				// Obtener la extensión del archivo original
				String originalFileName = selectedFile.getName();
				String extension = "";
				int lastDot = originalFileName.lastIndexOf('.');
				if (lastDot > 0) {
					extension = originalFileName.substring(lastDot).toLowerCase();
				}
				
				// Crear nombre del archivo basado en el nombre del producto
				String nombreArchivo = limpiarNombreArchivo(nombreProducto) + extension;
				Path destinationPath = productosDir.resolve(nombreArchivo);
				
				// Si el archivo ya existe, agregar un número
				int contador = 1;
				while (Files.exists(destinationPath)) {
					nombreArchivo = limpiarNombreArchivo(nombreProducto) + "_" + contador + extension;
					destinationPath = productosDir.resolve(nombreArchivo);
					contador++;
				}
				
				// Copiar el archivo a la carpeta de productos
				Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
				
				// Retornar la ruta relativa para la base de datos
				String relativePath = "/img2/productos/" + nombreArchivo;
				
				// Actualizar el label para mostrar la imagen seleccionada
				lblImagenSeleccionada.setText("Imagen: " + nombreArchivo);
				lblImagenSeleccionada.setForeground(new Color(0, 150, 0));
				
				return relativePath;
				
			} catch (Exception e) {
				javax.swing.JOptionPane.showMessageDialog(this, 
					"Error al copiar la imagen: " + e.getMessage(), 
					"Error", 
					javax.swing.JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		
		return null;
	}
	
	/**
	 * Método para limpiar el nombre del archivo (remover caracteres especiales)
	 */
	private String limpiarNombreArchivo(String nombre) {
		// Reemplazar espacios con guiones bajos
		String limpio = nombre.replaceAll("\\s+", "_");
		// Remover caracteres especiales excepto guiones bajos
		limpio = limpio.replaceAll("[^a-zA-Z0-9_]", "");
		// Convertir a minúsculas
		limpio = limpio.toLowerCase();
		// Limitar la longitud
		if (limpio.length() > 50) {
			limpio = limpio.substring(0, 50);
		}
		return limpio;
	}
	
	/**
	 * Método para limpiar la imagen seleccionada
	 */
	public void limpiarImagen() {
		textImagen.setText("");
		lblImagenSeleccionada.setText("Sin imagen seleccionada");
		lblImagenSeleccionada.setForeground(new Color(100, 100, 100));
	}
	
	/**
	 * Método para establecer una imagen existente
	 */
	public void establecerImagen(String rutaImagen) {
		if (rutaImagen != null && !rutaImagen.trim().isEmpty()) {
			textImagen.setText(rutaImagen);
			// Extraer solo el nombre del archivo para mostrar
			String fileName = rutaImagen.substring(rutaImagen.lastIndexOf('/') + 1);
			lblImagenSeleccionada.setText("Imagen: " + fileName);
			lblImagenSeleccionada.setForeground(new Color(0, 150, 0));
		} else {
			limpiarImagen();
		}
	}
} 