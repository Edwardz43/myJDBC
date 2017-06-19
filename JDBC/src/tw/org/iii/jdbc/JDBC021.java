package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBC021 {

	public static void main(String[] args) {
		// 1. Driver
		try{
			// 2. Connection
			Connection conn = 
					DriverManager.getConnection(
							"jdbc:mysql://localhost/nba",
							"root","root");
			
			// 3. SQL statement
			Statement stmt = conn.createStatement();
			
			// 4. query
			String sql = "select * from players";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				String fname = rs.getString("firstname");
				String lname = rs.getString("lastname");
				System.out.println(fname+" , "+lname);
			}
			conn.close();
			System.out.println("done");
			
		}catch(Exception e){
			System.out.println(e);
		}
		
	}

}





