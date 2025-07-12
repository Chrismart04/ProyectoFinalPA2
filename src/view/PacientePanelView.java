
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

public class PacientePanelView extends JPanel {

	private static final long serialVersionUID = 1L;
	public JTextField textNombre;
	public JTextField textApellido;
	public JTextField textTelefono;
	public JTable tablaPaciente;
	public JTextField textIdentidad;
	public JButton btnEditar;
	public JButton btnBorrar;
	public JButton btnBuscar;
	public JTextField textID;
	public JButton btnAgregar;
	public JButton btnLimpiar;
	public JButton btnExportarPacientesPDF;

	/**
	 * Create the panel.
	 */
	public PacientePanelView() {
		setBackground(Color.DARK_GRAY);
		setLayout(null);
		
		JPanel panelPaciente = new JPanel();
		panelPaciente.setLayout(null);
		panelPaciente.setBorder(new TitledBorder(null, "Paciente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPaciente.setBackground(Color.WHITE);
		panelPaciente.setBounds(0, 0, 903, 589);
		add(panelPaciente);
		
		JPanel panelDatosGenerales = new JPanel();
		panelDatosGenerales.setLayout(null);
		panelDatosGenerales.setBorder(new TitledBorder(null, "Datos Generales", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDatosGenerales.setBackground(Color.WHITE);
		panelDatosGenerales.setBounds(10, 20, 883, 260);
		panelPaciente.add(panelDatosGenerales);
		
		JLabel lblNewLabel_1 = new JLabel("Identidad:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(21, 49, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1);
		
		textIdentidad = new JTextField();
		textIdentidad.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textIdentidad.setBounds(115, 47, 425, 34);
		panelDatosGenerales.add(textIdentidad);
		
		JLabel lblNewLabel_1_1 = new JLabel("Nombre:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(21, 92, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_1);
		
		textNombre = new JTextField();
		textNombre.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textNombre.setBounds(115, 90, 425, 34);
		panelDatosGenerales.add(textNombre);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Apellido:");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_1.setBounds(21, 139, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_1_1);
		
		textApellido = new JTextField();
		textApellido.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textApellido.setBounds(115, 135, 425, 34);
		panelDatosGenerales.add(textApellido);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Telefono:");
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_1_1.setBounds(21, 184, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_1_1_1);
		
		textTelefono = new JTextField();
		textTelefono.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textTelefono.setBounds(115, 180, 425, 34);
		panelDatosGenerales.add(textTelefono);
		
		JLabel lblNewLabel_1_2 = new JLabel("ID:");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_2.setBounds(21, 13, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_2);
		
		textID = new JTextField();
		textID.setEditable(false);
		textID.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textID.setBounds(115, 11, 425, 34);
		panelDatosGenerales.add(textID);
		
		// Panel para los botones en el lado derecho
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(null);
		panelBotones.setBorder(new TitledBorder(null, "Acciones", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBotones.setBackground(Color.WHITE);
		panelBotones.setBounds(560, 20, 180, 230);
		panelDatosGenerales.add(panelBotones);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setIcon(new ImageIcon(PacientePanelView.class.getResource("/icons/icons8_save_32px_1.png")));
		btnAgregar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAgregar.setBounds(10, 25, 160, 35);
		panelBotones.add(btnAgregar);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setIcon(new ImageIcon(PacientePanelView.class.getResource("/icons/icons8_search_property_32px_1.png")));
		btnBuscar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBuscar.setBounds(10, 70, 160, 35);
		panelBotones.add(btnBuscar);
		
		btnEditar = new JButton("Editar");
		btnEditar.setIcon(new ImageIcon(PacientePanelView.class.getResource("/icons/icons8_edit_file_32px_1.png")));
		btnEditar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEditar.setBounds(10, 115, 160, 35);
		panelBotones.add(btnEditar);
		
		btnBorrar = new JButton("Borrar");
		btnBorrar.setIcon(new ImageIcon(PacientePanelView.class.getResource("/icons/icons8_delete_database_32px.png")));
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
		btnLimpiar.setIcon(new ImageIcon(PacientePanelView.class.getResource("/icons/Close.png")));
		btnLimpiar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLimpiar.setBounds(10, 25, 100, 35);
		panelBotonesExtra.add(btnLimpiar);
		
		btnExportarPacientesPDF = new JButton("PDF");
		btnExportarPacientesPDF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExportarPacientesPDF.setBounds(10, 70, 100, 35);
		panelBotonesExtra.add(btnExportarPacientesPDF);
		
		JPanel panelDetalle = new JPanel();
		panelDetalle.setLayout(null);
		panelDetalle.setBorder(new TitledBorder(null, "Detalle de Pacientes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDetalle.setBackground(Color.WHITE);
		panelDetalle.setBounds(10, 290, 883, 288);
		panelPaciente.add(panelDetalle);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 26, 862, 251);
		panelDetalle.add(scrollPane);
		
		tablaPaciente = new JTable();
		tablaPaciente.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Identidad", "Nombre", "Apellido", "Telefono"
			}
		));
		scrollPane.setViewportView(tablaPaciente);
	}
}
