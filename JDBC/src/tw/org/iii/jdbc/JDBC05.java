package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBC05 {

	public static void main(String[] args) {
		String connectionUrl = 
				"jdbc:sqlserver://localhost:1433;"+
				"user=ED43;password=P@ssw0rd;"+
				"databaseName=Northwind;";	
		try{
			Connection conn =
					DriverManager.getConnection(connectionUrl);
			//test Concurrency
			DatabaseMetaData metadata = conn.getMetaData();
			boolean isOK = metadata.supportsResultSetConcurrency(
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			System.out.println(isOK);
			
			
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
