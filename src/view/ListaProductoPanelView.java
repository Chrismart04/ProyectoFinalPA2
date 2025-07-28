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

public class ListaProductoPanelView extends JPanel {

	private static final long serialVersionUID = 1L;
	public JTable tablaProductos;
	public JTextField textBuscar;
	public JButton btnBuscar;
	public JButton btnLimpiar;
	public JButton btnExportarPDF;
	public JComboBox<String> comboCategoriaFiltro;

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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 26, 862, 391);
		panelDetalle.add(scrollPane);
		
		tablaProductos = new JTable();
		tablaProductos.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría"
			}
		));
		scrollPane.setViewportView(tablaProductos);
	}
} 