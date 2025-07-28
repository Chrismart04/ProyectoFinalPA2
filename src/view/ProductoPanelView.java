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
		panelDatosGenerales.setBounds(10, 20, 883, 260);
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
		comboCategoria.setBounds(115, 203, 425, 34);
		panelDatosGenerales.add(comboCategoria);
		
		// Panel para los botones en el lado derecho
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(null);
		panelBotones.setBorder(new TitledBorder(null, "Acciones", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBotones.setBackground(Color.WHITE);
		panelBotones.setBounds(560, 20, 180, 230);
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
		panelDetalle.setBounds(10, 290, 883, 288);
		panelProducto.add(panelDetalle);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 26, 862, 251);
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
} 