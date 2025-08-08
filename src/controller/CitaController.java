
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
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
import ClinicaDAO.CitaDAO;
import ClinicaDAO.PacienteDAO;
import model.CitaModel;
import model.ComboModel;
import model.PacienteModel;
import view.PrincipalView;

public class CitaController implements ActionListener, AbstractPanelController {

    PrincipalView frame;
    CitaDAO citas;
    PacienteDAO pacientes;
    CitaModel citaSeleccionada;

    public CitaController(PrincipalView frame) {
        super();
        this.frame = frame;
        citas = new CitaDAO();
        pacientes = new PacienteDAO();
        citaSeleccionada = null;
        init();

        this.frame.panelCita.btnAgregar.addActionListener(this);
        this.frame.panelCita.btnBuscar.addActionListener(this);
        this.frame.panelCita.btnEditar.addActionListener(this);
        this.frame.panelCita.btnBorrar.addActionListener(this);
        this.frame.panelCita.btnLimpiar.addActionListener(this);
        this.frame.panelCita.btnExportarCitaPDF.addActionListener(this);
        
        // Selección de cita en tabla
        this.frame.panelCita.tablaCita.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarCita();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.frame.panelCita.btnAgregar) {
            agregarCita();
        } else if (e.getSource() == this.frame.panelCita.btnBuscar) {
            buscarCita();
        } else if (e.getSource() == this.frame.panelCita.btnEditar) {
            editarCita();
        } else if (e.getSource() == this.frame.panelCita.btnBorrar) {
            borrarCita();
        } else if (e.getSource() == this.frame.panelCita.btnLimpiar) {
            limpiarCampos();
        } else if (e.getSource() == this.frame.panelCita.btnExportarCitaPDF) {
            exportarCitasPDF();
        }
    }

    private void agregarCita() {
        try {
            if (!validarCampos()) {
                return;
            }
            ComboModel pacienteSeleccionado = (ComboModel) this.frame.panelCita.comboPaciente.getSelectedItem();
            String fecha = this.frame.panelCita.textFecha.getText().trim();
            String hora = this.frame.panelCita.textHora.getText().trim();
            String procedimiento = this.frame.panelCita.textProcedimiento.getText().trim();

            CitaModel nuevaCita = new CitaModel(0, pacienteSeleccionado.getId(), fecha, hora, procedimiento);
            citas.insertar(nuevaCita);
            actualizarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this.frame, "Cita agregada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al agregar la cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCita() {
        try {
            String idTexto = this.frame.panelCita.textID.getText().trim();
            
            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this.frame, "Debe ingresar el ID de la cita a buscar", "Error", JOptionPane.WARNING_MESSAGE);
                this.frame.panelCita.textID.requestFocus();
                return;
            }
            
            int id = Integer.parseInt(idTexto);
            CitaModel citaBuscada = new CitaModel(id, 0, "", "", "");
            citaBuscada = citas.buscar(citaBuscada);
            
            if (citaBuscada.getFecha() != null && !citaBuscada.getFecha().isEmpty()) {
                // Cargar datos de la cita encontrada
                this.frame.panelCita.textFecha.setText(citaBuscada.getFecha());
                this.frame.panelCita.textHora.setText(citaBuscada.getHora());
                this.frame.panelCita.textProcedimiento.setText(citaBuscada.getProcedimiento());
                
                // Seleccionar paciente en el combo
                seleccionarPacienteEnCombo(citaBuscada.getId_paciente());
                
                citaSeleccionada = citaBuscada;
                JOptionPane.showMessageDialog(this.frame, "Cita encontrada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.frame, "No se encontró ninguna cita con el ID especificado", "Cita no encontrada", JOptionPane.WARNING_MESSAGE);
                limpiarCampos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this.frame, "El ID debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al buscar la cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarCita() {
        try {
            if (citaSeleccionada == null) {
                JOptionPane.showMessageDialog(this.frame, "Debe seleccionar una cita de la tabla", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!validarCampos()) {
                return;
            }
            
            // Actualizar datos de la cita seleccionada
            ComboModel pacienteSeleccionado = (ComboModel) this.frame.panelCita.comboPaciente.getSelectedItem();
            citaSeleccionada.setId_paciente(pacienteSeleccionado.getId());
            citaSeleccionada.setFecha(this.frame.panelCita.textFecha.getText().trim());
            citaSeleccionada.setHora(this.frame.panelCita.textHora.getText().trim());
            citaSeleccionada.setProcedimiento(this.frame.panelCita.textProcedimiento.getText().trim());
            
            citas.modificar(citaSeleccionada);
            actualizarTabla();
            limpiarCampos();
            citaSeleccionada = null;
            JOptionPane.showMessageDialog(this.frame, "Cita editada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al editar la cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrarCita() {
        try {
            if (citaSeleccionada == null) {
                JOptionPane.showMessageDialog(this.frame, "Debe seleccionar una cita de la tabla", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String nombrePaciente = getNombrePaciente(citaSeleccionada.getId_paciente());
            int confirmacion = JOptionPane.showConfirmDialog(
                this.frame,
                "¿Está seguro que desea eliminar la cita de: " + nombrePaciente + " el " + citaSeleccionada.getFecha() + " a las " + citaSeleccionada.getHora() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                citas.eliminar(citaSeleccionada);
                actualizarTabla();
                limpiarCampos();
                citaSeleccionada = null;
                JOptionPane.showMessageDialog(this.frame, "Cita eliminada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al eliminar la cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarCitasPDF() {
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
            fileChooser.setSelectedFile(new java.io.File("Citas_" + timestamp + ".pdf"));
            
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
                Paragraph title = new Paragraph("REPORTE DE CITAS", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                
                // Fecha de generación
                Font dateFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
                Paragraph date = new Paragraph("Fecha de generación: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), dateFont);
                date.setAlignment(Element.ALIGN_RIGHT);
                document.add(date);
                
                // Espacio
                document.add(new Paragraph(" "));
                
                // Crear tabla con 5 columnas
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);
                
                // Definir anchos de columnas
                float[] columnWidths = {10f, 30f, 20f, 20f, 20f};
                table.setWidths(columnWidths);
                
                // Encabezados de la tabla
                Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
                String[] headers = {"ID", "Paciente", "Fecha", "Hora", "Procedimiento"};
                
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
                    table.addCell(cell);
                }
                
                // Datos de la tabla
                Font dataFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
                DefaultTableModel model = (DefaultTableModel) this.frame.panelCita.tablaCita.getModel();
                
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        PdfPCell cell = new PdfPCell(new Phrase(value != null ? value.toString() : "", dataFont));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }
                }
                
                document.add(table);
                
                // Información adicional
                document.add(new Paragraph(" "));
                Paragraph footer = new Paragraph("Total de citas: " + model.getRowCount(), dateFont);
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

    private void seleccionarCita() {
        int filaSeleccionada = this.frame.panelCita.tablaCita.getSelectedRow();
        
        if (filaSeleccionada >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) this.frame.panelCita.tablaCita.getModel();
            
            // Obtener los datos de la fila seleccionada
            int id = (Integer) modelo.getValueAt(filaSeleccionada, 0);
            String nombrePaciente = (String) modelo.getValueAt(filaSeleccionada, 1);
            String fecha = (String) modelo.getValueAt(filaSeleccionada, 2);
            String hora = (String) modelo.getValueAt(filaSeleccionada, 3);
            String procedimiento = (String) modelo.getValueAt(filaSeleccionada, 4);
            
            // Cargar los datos en los campos de texto
            this.frame.panelCita.textID.setText(String.valueOf(id));
            this.frame.panelCita.textFecha.setText(fecha);
            this.frame.panelCita.textHora.setText(hora);
            this.frame.panelCita.textProcedimiento.setText(procedimiento);
            
            // Buscar el ID del paciente y seleccionarlo en el combo
            int idPaciente = buscarIdPacientePorNombre(nombrePaciente);
            if (idPaciente != -1) {
                seleccionarPacienteEnCombo(idPaciente);
                citaSeleccionada = new CitaModel(id, idPaciente, fecha, hora, procedimiento);
            }
        }
    }

    private int buscarIdPacientePorNombre(String nombreCompleto) {
        for (PacienteModel paciente : pacientes.obtener()) {
            String nombre = paciente.getNombre() + " " + paciente.getApellido();
            if (nombre.equals(nombreCompleto)) {
                return paciente.getId();
            }
        }
        return -1;
    }

    private void seleccionarPacienteEnCombo(int idPaciente) {
        for (int i = 0; i < this.frame.panelCita.comboPaciente.getItemCount(); i++) {
            ComboModel item = (ComboModel) this.frame.panelCita.comboPaciente.getItemAt(i);
            if (item.getId() == idPaciente) {
                this.frame.panelCita.comboPaciente.setSelectedIndex(i);
                break;
            }
        }
    }

    private boolean validarCampos() {
        if (this.frame.panelCita.comboPaciente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this.frame, "Debe seleccionar un paciente", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (this.frame.panelCita.textFecha.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.frame, "Debe ingresar la fecha", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (this.frame.panelCita.textHora.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.frame, "Debe ingresar la hora", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (this.frame.panelCita.textProcedimiento.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.frame, "Debe ingresar el procedimiento", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) this.frame.panelCita.tablaCita.getModel();
        modelo.setRowCount(0);
        for (CitaModel cita : citas.obtener()) {
            Object[] fila = {
                cita.getId(),
                getNombrePaciente(cita.getId_paciente()),
                cita.getFecha(),
                cita.getHora(),
                cita.getProcedimiento()
            };
            modelo.addRow(fila);
        }
    }

    private String getNombrePaciente(int id) {
        for (PacienteModel paciente : pacientes.obtener()) {
            if (paciente.getId() == id) {
                return paciente.getNombre() + " " + paciente.getApellido();
            }
        }
        return "";
    }

    private void limpiarCampos() {
        this.frame.panelCita.textID.setText("");
        this.frame.panelCita.textFecha.setText("");
        this.frame.panelCita.textHora.setText("");
        this.frame.panelCita.textProcedimiento.setText("");
        this.frame.panelCita.comboPaciente.setSelectedIndex(-1);
        citaSeleccionada = null;
    }

    public void llenarCombo(List<PacienteModel> lista, JComboBox<Object> combo) {
        combo.removeAllItems();
        for (PacienteModel paciente : lista) {
            ComboModel comboModel = new ComboModel(paciente.getId(), paciente.getNombre(), paciente.getApellido());
            combo.addItem(comboModel);
        }
    }

    @Override
    public void init() {
        llenarCombo(pacientes.obtener(), this.frame.panelCita.comboPaciente);
        actualizarTabla();
    }
}
