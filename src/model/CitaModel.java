
package model;

public class CitaModel {

    //Atributos
    int id;
    int id_paciente;
    String fecha;
    String hora;
    String procedimiento;

    //Constructor
    public CitaModel(int id, int id_paciente, String fecha, String hora, String procedimiento) {
        this.id = id;
        this.id_paciente = id_paciente;
        this.fecha = fecha;
        this.hora = hora;
        this.procedimiento = procedimiento;
    }

    //Métodos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    @Override
    public String toString() {
        return "CitaModel [id=" + id + ", id_paciente=" + id_paciente + ", fecha=" + fecha + ", hora=" + hora + ", procedimiento=" + procedimiento + "]";
    }
}
