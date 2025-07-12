
package model;

public class PacienteModel {
	
	//Atributos
	int id;
	String identidad;
	String nombre;
	String apellido;
	String telefono;
	
	//Constructor
	public PacienteModel(int id, String identidad, String nombre, String apellido, String telefono) {
		super();
		this.id = id;
		this.identidad = identidad;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
	}

	//Métodos
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdentidad() {
		return identidad;
	}

	public void setIdentidad(String identidad) {
		this.identidad = identidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@Override
	public String toString() {
		return "PacienteModel [id=" + id + ", identidad=" + identidad + ", nombre=" + nombre + ", apellido=" + apellido
				+ ", telefono=" + telefono + "]";
	}
	

}
