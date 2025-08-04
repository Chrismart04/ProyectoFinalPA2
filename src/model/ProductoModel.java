package model;

public class ProductoModel {
	
	//Atributos
	int id;
	String nombre;
	String descripcion;
	double precio;
	int stock;
	int id_categoria;
	String nombreCategoria;
	String imagen;
	
	//Constructor
	public ProductoModel(int id, String nombre, String descripcion, double precio, int stock, int id_categoria) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.id_categoria = id_categoria;
		this.nombreCategoria = "";
		this.imagen = "";
	}
	
	//Constructor con imagen
	public ProductoModel(int id, String nombre, String descripcion, double precio, int stock, int id_categoria, String imagen) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.id_categoria = id_categoria;
		this.nombreCategoria = "";
		this.imagen = imagen;
	}

	//Métodos
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getId_categoria() {
		return id_categoria;
	}

	public void setId_categoria(int id_categoria) {
		this.id_categoria = id_categoria;
	}
	
	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}
	
	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	@Override
	public String toString() {
		return "ProductoModel [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion
				+ ", precio=" + precio + ", stock=" + stock + ", id_categoria=" + id_categoria + ", nombreCategoria=" + nombreCategoria + ", imagen=" + imagen + "]";
	}
} 