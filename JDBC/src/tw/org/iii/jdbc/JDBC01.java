package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class JDBC01 {

	public static void main(String[] args) {
		// 1. Driver
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("OK");
		}catch(ClassNotFoundException ee){
			System.out.println(ee);
			System.exit(0);
		}
		
		try{
			// 2. Connection
			Connection conn = 
					DriverManager.getConnection(
					"jdbc:mysql://10.21.200.66/brad?" +
                    "user=root&password=root");
			
			// 3. SQL statement
			Statement stmt = conn.createStatement();
			
			// 4. query
			String sql = "INSERT INTO cust (cname,tel,birthday)" + 
					" VALUES ('Brad','123','1999-09-08')";
			boolean isQueryOK = stmt.execute(sql);
			if (isQueryOK){
				System.out.println("OK");
			}else{
				System.out.println("sql");
			}
		}catch(Exception e){
			System.out.println(e);
		}
		
	}

}
