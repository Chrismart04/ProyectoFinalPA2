
package view;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;

public class CitaPanelView extends JPanel {

	private static final long serialVersionUID = 1L;
	public JComboBox<Object> comboPaciente;
	public JTextField textFecha;
	public JTextField textHora;
	public JTextField textProcedimiento;
	public JTextField textID;
	public JTable tablaCita;
	public JButton btnAgregar;
	public JButton btnBuscar;
	public JButton btnEditar;
	public JButton btnBorrar;
	public JButton btnLimpiar;
	public JButton btnExportarCitaPDF;

	/**
	 * Create the panel.
	 */
	public CitaPanelView() {
		setBackground(Color.DARK_GRAY);
		setLayout(null);
		
		JPanel panelCita = new JPanel();
		panelCita.setLayout(null);
		panelCita.setBorder(new TitledBorder(null, "Cita", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCita.setBackground(Color.WHITE);
		panelCita.setBounds(0, 0, 903, 589);
		add(panelCita);
		
		JPanel panelDatosGenerales = new JPanel();
		panelDatosGenerales.setLayout(null);
		panelDatosGenerales.setBorder(new TitledBorder(null, "Datos Generales", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDatosGenerales.setBackground(Color.WHITE);
		panelDatosGenerales.setBounds(10, 20, 883, 255);
		panelCita.add(panelDatosGenerales);
		
		JLabel lblNewLabel_1_2 = new JLabel("ID:");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_2.setBounds(21, 23, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_2);
		
		textID = new JTextField();
		textID.setEditable(false);
		textID.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textID.setBounds(115, 21, 425, 34);
		panelDatosGenerales.add(textID);
		
		JLabel lblNewLabel_1 = new JLabel("Paciente:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(21, 66, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1);
		
		comboPaciente = new JComboBox<Object>();
		comboPaciente.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboPaciente.setBounds(115, 64, 425, 34);
		panelDatosGenerales.add(comboPaciente);
		
		JLabel lblNewLabel_1_1 = new JLabel("Fecha:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(21, 109, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_1);
		
		textFecha = new JTextField();
		textFecha.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFecha.setBounds(115, 107, 425, 34);
		panelDatosGenerales.add(textFecha);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Hora:");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_1.setBounds(21, 152, 93, 30);
		panelDatosGenerales.add(lblNewLabel_1_1_1);
		
		textHora = new JTextField();
		textHora.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textHora.setBounds(115, 150, 425, 34);
		panelDatosGenerales.add(textHora);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Procedimiento:");
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_1_1.setBounds(21, 195, 120, 30);
		panelDatosGenerales.add(lblNewLabel_1_1_1_1);
		
		textProcedimiento = new JTextField();
		textProcedimiento.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textProcedimiento.setBounds(150, 193, 390, 34);
		panelDatosGenerales.add(textProcedimiento);
		
		// Panel para los botones en el lado derecho
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(null);
		panelBotones.setBorder(new TitledBorder(null, "Acciones", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBotones.setBackground(Color.WHITE);
		panelBotones.setBounds(560, 20, 180, 180);
		panelDatosGenerales.add(panelBotones);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setIcon(new ImageIcon(CitaPanelView.class.getResource("/icons/icons8_save_32px_1.png")));
		btnAgregar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAgregar.setBounds(10, 25, 160, 35);
		panelBotones.add(btnAgregar);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setIcon(new ImageIcon(CitaPanelView.class.getResource("/icons/icons8_search_property_32px_1.png")));
		btnBuscar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBuscar.setBounds(10, 70, 160, 35);
		panelBotones.add(btnBuscar);
		
		btnEditar = new JButton("Editar");
		btnEditar.setIcon(new ImageIcon(CitaPanelView.class.getResource("/icons/icons8_edit_file_32px_1.png")));
		btnEditar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEditar.setBounds(10, 115, 160, 35);
		panelBotones.add(btnEditar);
		
		// Panel para botones adicionales
		JPanel panelBotonesExtra = new JPanel();
		panelBotonesExtra.setLayout(null);
		panelBotonesExtra.setBorder(new TitledBorder(null, "Herramientas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBotonesExtra.setBackground(Color.WHITE);
		panelBotonesExtra.setBounds(750, 20, 120, 180);
		panelDatosGenerales.add(panelBotonesExtra);
		
		btnBorrar = new JButton("Borrar");
		btnBorrar.setIcon(new ImageIcon(CitaPanelView.class.getResource("/icons/icons8_delete_database_32px.png")));
		btnBorrar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBorrar.setBounds(10, 25, 100, 35);
		panelBotonesExtra.add(btnBorrar);
		
		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setIcon(new ImageIcon(CitaPanelView.class.getResource("/icons/Close.png")));
		btnLimpiar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLimpiar.setBounds(10, 70, 100, 35);
		panelBotonesExtra.add(btnLimpiar);
		
		btnExportarCitaPDF = new JButton("PDF");
		btnExportarCitaPDF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExportarCitaPDF.setBounds(10, 115, 100, 35);
		panelBotonesExtra.add(btnExportarCitaPDF);
		
		JPanel panelDetalle = new JPanel();
		panelDetalle.setLayout(null);
		panelDetalle.setBorder(new TitledBorder(null, "Detalle de Citas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDetalle.setBackground(Color.WHITE);
		panelDetalle.setBounds(10, 285, 883, 293);
		panelCita.add(panelDetalle);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 26, 862, 256);
		panelDetalle.add(scrollPane);
		
		tablaCita = new JTable();
		tablaCita.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Paciente", "Fecha", "Hora", "Procedimiento"
			}
		));
		scrollPane.setViewportView(tablaCita);
	}
}
