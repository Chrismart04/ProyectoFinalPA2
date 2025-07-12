
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
import ClinicaDAO.PacienteDAO;
import model.PacienteModel;
import view.PrincipalView;

public class PacienteController implements ActionListener, AbstractPanelController {

    PrincipalView frame;
    PacienteDAO pacientes;
    PacienteModel pacienteSeleccionado;

    public PacienteController(PrincipalView frame) {
        super();
        this.frame = frame;
        pacientes = new PacienteDAO();
        pacienteSeleccionado = null;
        init();

        this.frame.panelPaciente.btnAgregar.addActionListener(this);
        this.frame.panelPaciente.btnEditar.addActionListener(this);
        this.frame.panelPaciente.btnBorrar.addActionListener(this);
        this.frame.panelPaciente.btnBuscar.addActionListener(this);
        this.frame.panelPaciente.btnLimpiar.addActionListener(this);
        this.frame.panelPaciente.btnExportarPacientesPDF.addActionListener(this);
        
        // Agregar MouseListener a la tabla para seleccionar pacientes
        this.frame.panelPaciente.tablaPaciente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarPaciente();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.frame.panelPaciente.btnAgregar) {
            agregarPaciente();
        } else if (e.getSource() == this.frame.panelPaciente.btnEditar) {
            editarPaciente();
        } else if (e.getSource() == this.frame.panelPaciente.btnBorrar) {
            borrarPaciente();
        } else if (e.getSource() == this.frame.panelPaciente.btnBuscar) {
            buscarPaciente();
        } else if (e.getSource() == this.frame.panelPaciente.btnLimpiar) {
            limpiarCampos();
        } else if (e.getSource() == this.frame.panelPaciente.btnExportarPacientesPDF) {
            exportarPacientesPDF();
        }
    }

    private void agregarPaciente() {
        try {
            if (!validarCampos()) {
                return;
            }
            String identidad = this.frame.panelPaciente.textIdentidad.getText().trim();
            String nombre = this.frame.panelPaciente.textNombre.getText().trim();
            String apellido = this.frame.panelPaciente.textApellido.getText().trim();
            String telefono = this.frame.panelPaciente.textTelefono.getText().trim();

            PacienteModel nuevoPaciente = new PacienteModel(0, identidad, nombre, apellido, telefono);
            pacientes.insertar(nuevoPaciente);
            actualizarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this.frame, "Paciente agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al agregar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarPaciente() {
        try {
            if (pacienteSeleccionado == null) {
                JOptionPane.showMessageDialog(this.frame, "Debe seleccionar un paciente de la tabla", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!validarCampos()) {
                return;
            }
            
            // Actualizar los datos del paciente seleccionado
            pacienteSeleccionado.setIdentidad(this.frame.panelPaciente.textIdentidad.getText().trim());
            pacienteSeleccionado.setNombre(this.frame.panelPaciente.textNombre.getText().trim());
            pacienteSeleccionado.setApellido(this.frame.panelPaciente.textApellido.getText().trim());
            pacienteSeleccionado.setTelefono(this.frame.panelPaciente.textTelefono.getText().trim());
            
            pacientes.modificar(pacienteSeleccionado);
            actualizarTabla();
            limpiarCampos();
            pacienteSeleccionado = null;
            JOptionPane.showMessageDialog(this.frame, "Paciente editado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al editar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrarPaciente() {
        try {
            if (pacienteSeleccionado == null) {
                JOptionPane.showMessageDialog(this.frame, "Debe seleccionar un paciente de la tabla", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(
                this.frame,
                "¿Está seguro que desea eliminar el paciente: " + pacienteSeleccionado.getNombre() + " " + pacienteSeleccionado.getApellido() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                pacientes.eliminar(pacienteSeleccionado);
                actualizarTabla();
                limpiarCampos();
                pacienteSeleccionado = null;
                JOptionPane.showMessageDialog(this.frame, "Paciente eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al eliminar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPaciente() {
        try {
            String idTexto = this.frame.panelPaciente.textID.getText().trim();
            
            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this.frame, "Debe ingresar el ID del paciente a buscar", "Error", JOptionPane.WARNING_MESSAGE);
                this.frame.panelPaciente.textID.requestFocus();
                return;
            }
            
            int id = Integer.parseInt(idTexto);
            PacienteModel pacienteBuscado = new PacienteModel(id, "", "", "", "");
            pacienteBuscado = pacientes.buscar(pacienteBuscado);
            
            if (pacienteBuscado.getNombre() != null && !pacienteBuscado.getNombre().isEmpty()) {
                // Cargar los datos del paciente encontrado en los campos
                this.frame.panelPaciente.textIdentidad.setText(pacienteBuscado.getIdentidad());
                this.frame.panelPaciente.textNombre.setText(pacienteBuscado.getNombre());
                this.frame.panelPaciente.textApellido.setText(pacienteBuscado.getApellido());
                this.frame.panelPaciente.textTelefono.setText(pacienteBuscado.getTelefono());
                
                pacienteSeleccionado = pacienteBuscado;
                JOptionPane.showMessageDialog(this.frame, "Paciente encontrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.frame, "No se encontró ningún paciente con el ID especificado", "Paciente no encontrado", JOptionPane.WARNING_MESSAGE);
                limpiarCampos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this.frame, "El ID debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, "Error al buscar el paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarPacientesPDF() {
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
            fileChooser.setSelectedFile(new java.io.File("Pacientes_" + timestamp + ".pdf"));
            
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
                Paragraph title = new Paragraph("REPORTE DE PACIENTES", titleFont);
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
                float[] columnWidths = {10f, 25f, 20f, 20f, 25f};
                table.setWidths(columnWidths);
                
                // Encabezados de la tabla
                Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
                String[] headers = {"ID", "Identidad", "Nombre", "Apellido", "Teléfono"};
                
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
                    table.addCell(cell);
                }
                
                // Datos de la tabla
                Font dataFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
                DefaultTableModel model = (DefaultTableModel) this.frame.panelPaciente.tablaPaciente.getModel();
                
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
                Paragraph footer = new Paragraph("Total de pacientes: " + model.getRowCount(), dateFont);
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

    private void seleccionarPaciente() {
        int filaSeleccionada = this.frame.panelPaciente.tablaPaciente.getSelectedRow();
        
        if (filaSeleccionada >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) this.frame.panelPaciente.tablaPaciente.getModel();
            
            // Obtener los datos de la fila seleccionada
            int id = (Integer) modelo.getValueAt(filaSeleccionada, 0);
            String identidad = (String) modelo.getValueAt(filaSeleccionada, 1);
            String nombre = (String) modelo.getValueAt(filaSeleccionada, 2);
            String apellido = (String) modelo.getValueAt(filaSeleccionada, 3);
            String telefono = (String) modelo.getValueAt(filaSeleccionada, 4);
            
            // Cargar los datos en los campos de texto
            this.frame.panelPaciente.textID.setText(String.valueOf(id));
            this.frame.panelPaciente.textIdentidad.setText(identidad);
            this.frame.panelPaciente.textNombre.setText(nombre);
            this.frame.panelPaciente.textApellido.setText(apellido);
            this.frame.panelPaciente.textTelefono.setText(telefono);
            
            // Crear el objeto del paciente seleccionado
            pacienteSeleccionado = new PacienteModel(id, identidad, nombre, apellido, telefono);
        }
    }

    private boolean validarCampos() {
        if (this.frame.panelPaciente.textIdentidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.frame, "Debe ingresar la identidad", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (this.frame.panelPaciente.textNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.frame, "Debe ingresar el nombre", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (this.frame.panelPaciente.textApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.frame, "Debe ingresar el apellido", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (this.frame.panelPaciente.textTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.frame, "Debe ingresar el telefono", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) this.frame.panelPaciente.tablaPaciente.getModel();
        modelo.setRowCount(0);
        for (PacienteModel paciente : pacientes.obtener()) {
            Object[] fila = {
                paciente.getId(),
                paciente.getIdentidad(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getTelefono()
            };
            modelo.addRow(fila);
        }
    }

    private void limpiarCampos() {
        this.frame.panelPaciente.textID.setText("");
        this.frame.panelPaciente.textIdentidad.setText("");
        this.frame.panelPaciente.textNombre.setText("");
        this.frame.panelPaciente.textApellido.setText("");
        this.frame.panelPaciente.textTelefono.setText("");
        pacienteSeleccionado = null;
    }

    @Override
    public void init() {
        actualizarTabla();
    }
}
