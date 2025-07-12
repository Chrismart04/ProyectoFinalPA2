package ClinicaDAO;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Conexion {
	protected static Connection con;
	private String driver;
	private String db;
	private String user;
	private String pass;
	private String url;
	
	static Statement st;
	static ResultSet rs;

	public Conexion() {
		super();
		this.driver = "com.mysql.jdbc.Driver";
	
		try {
			//Cargando driver
			System.out.println("Cargando driver de conexion...");
			Class.forName(driver);
			System.out.println("Se cargo el driver correctamente!");
			
			this.db = "clinicadentaldb";
			this.url = "jdbc:mysql://localhost:8889/" + this.db + "?useSSL=false";
			this.user = "root";
			this.pass = "root";
			
			System.out.println("Conectandose a la base de datos...");
			this.con = (Connection) DriverManager.getConnection(this.url, this.user, this.pass);
			System.out.println("Se conecto, yujuuu!");
			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error!");
			e.printStackTrace();
		}
		
		
	}
	public static void main(String args[])
	{
		Conexion c = new Conexion();
		String sentencia = "select * from pacientes";
			try {
				st = (Statement) con.createStatement();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs = st.executeQuery(sentencia);
				while(rs.next())
				{				
					System.out.println( rs.getString("nombre") );
				}
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public Connection getC()
	{
		return con;
	}
	
	
}
