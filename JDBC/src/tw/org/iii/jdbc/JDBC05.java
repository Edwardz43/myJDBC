package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBC05 {

	public static void main(String[] args) {
		String connectionUrl = 
				"jdbc:sqlserver://localhost:1433;"+
				"user=Ed43;password=P@ssw0rd;"+
				"databaseName=NBA;";	
		try{
			Connection conn =
					DriverManager.getConnection(connectionUrl);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Players");
			while(rs.next()){
				String fname = rs.getString("firstname");
				String lname = rs.getString("lastname");
				System.out.println(fname+" "+lname);
			}
			
			conn.close();
			System.out.println("done");
		}catch(Exception e){
			System.out.println(e);
		}
	}

}
