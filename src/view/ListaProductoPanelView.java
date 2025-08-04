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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;

public class ListaProductoPanelView extends JPanel {

	private static final long serialVersionUID = 1L;
	public JTable tablaProductos;
	public JTextField textBuscar;
	public JButton btnBuscar;
	public JButton btnLimpiar;
	public JButton btnExportarPDF;
	public JComboBox<String> comboCategoriaFiltro;
	public JPanel panelCards; // Panel para las tarjetas de productos
	public JScrollPane scrollPaneCards; // ScrollPane para las tarjetas

	/**
	 * Create the panel.
	 */
	public ListaProductoPanelView() {
		setBackground(Color.DARK_GRAY);
		setLayout(null);
		
		JPanel panelListaProductos = new JPanel();
		panelListaProductos.setLayout(null);
		panelListaProductos.setBorder(new TitledBorder(null, "Lista de Productos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelListaProductos.setBackground(Color.WHITE);
		panelListaProductos.setBounds(0, 0, 903, 589);
		add(panelListaProductos);
		
		JPanel panelFiltros = new JPanel();
		panelFiltros.setLayout(null);
		panelFiltros.setBorder(new TitledBorder(null, "Filtros de Búsqueda", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelFiltros.setBackground(Color.WHITE);
		panelFiltros.setBounds(10, 20, 883, 120);
		panelListaProductos.add(panelFiltros);
		
		JLabel lblBuscar = new JLabel("Buscar por nombre:");
		lblBuscar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBuscar.setBounds(21, 30, 150, 30);
		panelFiltros.add(lblBuscar);
		
		textBuscar = new JTextField();
		textBuscar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textBuscar.setBounds(180, 28, 250, 34);
		panelFiltros.add(textBuscar);
		
		JLabel lblCategoria = new JLabel("Filtrar por categoría:");
		lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCategoria.setBounds(21, 70, 150, 30);
		panelFiltros.add(lblCategoria);
		
		comboCategoriaFiltro = new JComboBox<String>();
		comboCategoriaFiltro.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboCategoriaFiltro.setBounds(180, 68, 250, 34);
		comboCategoriaFiltro.addItem("Todas las categorías");
		panelFiltros.add(comboCategoriaFiltro);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setIcon(new ImageIcon(ListaProductoPanelView.class.getResource("/icons/icons8_search_property_32px_1.png")));
		btnBuscar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBuscar.setBounds(450, 28, 120, 35);
		panelFiltros.add(btnBuscar);
		
		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setIcon(new ImageIcon(ListaProductoPanelView.class.getResource("/icons/Close.png")));
		btnLimpiar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnLimpiar.setBounds(580, 28, 120, 35);
		panelFiltros.add(btnLimpiar);
		
		btnExportarPDF = new JButton("Exportar PDF");
		btnExportarPDF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExportarPDF.setBounds(720, 28, 150, 35);
		panelFiltros.add(btnExportarPDF);
		
		JPanel panelDetalle = new JPanel();
		panelDetalle.setLayout(null);
		panelDetalle.setBorder(new TitledBorder(null, "Productos Disponibles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDetalle.setBackground(Color.WHITE);
		panelDetalle.setBounds(10, 150, 883, 428);
		panelListaProductos.add(panelDetalle);
		
		// Panel para las tarjetas con GridLayout
		panelCards = new JPanel();
		panelCards.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columnas, filas dinámicas, gap de 10px
		panelCards.setBackground(Color.WHITE);
		
		// ScrollPane para las tarjetas
		scrollPaneCards = new JScrollPane(panelCards);
		scrollPaneCards.setBounds(10, 26, 862, 391);
		scrollPaneCards.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPaneCards.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelDetalle.add(scrollPaneCards);
		
		// Mantener la tabla para compatibilidad con el controlador existente
		tablaProductos = new JTable();
		tablaProductos.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría"
			}
		));
		tablaProductos.setVisible(false); // Ocultar la tabla
	}
	
	/**
	 * Crea una tarjeta para un producto
	 */
	public JPanel crearTarjetaProducto(int id, String nombre, String descripcion, double precio, int stock, String categoria, String imagen) {
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(new BorderLayout(5, 5));
		tarjeta.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
			BorderFactory.createEmptyBorder(10, 10, 10, 10)
		));
		tarjeta.setBackground(Color.WHITE);
		tarjeta.setPreferredSize(new Dimension(280, 220));
		
		// Header con ID y categoría
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(240, 240, 240));
		header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel lblId = new JLabel("ID: " + id);
		lblId.setFont(new Font("Tahoma", Font.BOLD, 10));
		lblId.setForeground(new Color(100, 100, 100));
		header.add(lblId, BorderLayout.WEST);
		
		JLabel lblCategoria = new JLabel(categoria != null ? categoria : "Sin categoría");
		lblCategoria.setFont(new Font("Tahoma", Font.ITALIC, 10));
		lblCategoria.setForeground(new Color(100, 100, 100));
		header.add(lblCategoria, BorderLayout.EAST);
		
		tarjeta.add(header, BorderLayout.NORTH);
		
		// Panel para imagen y contenido
		JPanel contenidoPrincipal = new JPanel(new BorderLayout(8, 8));
		contenidoPrincipal.setBackground(Color.WHITE);
		
		// Imagen del producto
		JLabel lblImagen = new JLabel();
		lblImagen.setPreferredSize(new Dimension(80, 80));
		lblImagen.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
		lblImagen.setHorizontalAlignment(JLabel.CENTER);
		lblImagen.setVerticalAlignment(JLabel.CENTER);
		
		// Intentar cargar la imagen
		if (imagen != null && !imagen.trim().isEmpty()) {
			try {
				ImageIcon icon = new ImageIcon(getClass().getResource(imagen));
				if (icon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
					// Redimensionar la imagen a 80x80
					java.awt.Image img = icon.getImage();
					java.awt.Image newImg = img.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
					lblImagen.setIcon(new ImageIcon(newImg));
				} else {
					// Si no se puede cargar la imagen, mostrar un placeholder
					lblImagen.setText("Sin imagen");
					lblImagen.setFont(new Font("Tahoma", Font.ITALIC, 10));
					lblImagen.setForeground(new Color(150, 150, 150));
				}
			} catch (Exception e) {
				// Si hay error al cargar la imagen, mostrar un placeholder
				lblImagen.setText("Sin imagen");
				lblImagen.setFont(new Font("Tahoma", Font.ITALIC, 10));
				lblImagen.setForeground(new Color(150, 150, 150));
			}
		} else {
			// Si no hay imagen, mostrar un placeholder
			lblImagen.setText("Sin imagen");
			lblImagen.setFont(new Font("Tahoma", Font.ITALIC, 10));
			lblImagen.setForeground(new Color(150, 150, 150));
		}
		
		contenidoPrincipal.add(lblImagen, BorderLayout.WEST);
		
		// Panel para información del producto
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout(5, 5));
		infoPanel.setBackground(Color.WHITE);
		
		// Nombre del producto
		JLabel lblNombre = new JLabel(nombre);
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNombre.setForeground(new Color(50, 50, 50));
		infoPanel.add(lblNombre, BorderLayout.NORTH);
		
		// Descripción
		JLabel lblDescripcion = new JLabel("<html><body style='width: 150px'>" + descripcion + "</body></html>");
		lblDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblDescripcion.setForeground(new Color(80, 80, 80));
		infoPanel.add(lblDescripcion, BorderLayout.CENTER);
		
		contenidoPrincipal.add(infoPanel, BorderLayout.CENTER);
		tarjeta.add(contenidoPrincipal, BorderLayout.CENTER);
		
		// Footer con precio y stock
		JPanel footer = new JPanel(new BorderLayout());
		footer.setBackground(new Color(250, 250, 250));
		footer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel lblPrecio = new JLabel("L " + String.format("%.2f", precio));
		lblPrecio.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPrecio.setForeground(new Color(0, 150, 0));
		footer.add(lblPrecio, BorderLayout.WEST);
		
		JLabel lblStock = new JLabel("Stock: " + stock);
		lblStock.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblStock.setForeground(stock > 0 ? new Color(0, 100, 0) : new Color(200, 0, 0));
		footer.add(lblStock, BorderLayout.EAST);
		
		tarjeta.add(footer, BorderLayout.SOUTH);
		
		return tarjeta;
	}
	
	/**
	 * Limpia todas las tarjetas del panel
	 */
	public void limpiarTarjetas() {
		panelCards.removeAll();
		panelCards.revalidate();
		panelCards.repaint();
	}
} 