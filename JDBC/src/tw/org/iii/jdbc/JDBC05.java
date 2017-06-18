package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBC05 {

	public static void main(String[] args) {
		String connectionUrl = 
				"jdbc:sqlserver://10.21.200.66:1433;"+
				"user=sa;password=sa;"+
				"databaseName=Northwind;";	
		try{
			Connection conn =
					DriverManager.getConnection(connectionUrl);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Products");
			while(rs.next()){
				String fname = rs.getString("ProductName");
				String lname = rs.getString("Unitprice");
				System.out.println(fname+" , "+lname);
			}
			
			conn.close();
			System.out.println("done");
		}catch(Exception e){
			System.out.println(e);
		}
	}

}
