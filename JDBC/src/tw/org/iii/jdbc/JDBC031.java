package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBC031 {

	public static void main(String[] args) {
		// 1. Driver
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("OK");
		}catch(ClassNotFoundException ee){
			System.out.println(ee);
			System.exit(-1);
		}
		
		try{
			
			Properties prop = new Properties();
			prop.setProperty("user", "root");
			prop.setProperty("password", "");
			
			// 2. Connection
			Connection conn = 
					DriverManager.getConnection(
							"jdbc:mysql://localhost/test",
							prop);
			
			// 3. SQL statement
			Statement stmt = conn.createStatement();
			
			// 4. query
			String sql = "select * from cust";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String id = rs.getString("id");
				String cname = rs.getString("cname");
				String tel = rs.getString("tel");
				String birthday = rs.getString("birthday");
				String temp = "id :" +id+" , cname :"+cname+" , tel :"+tel
						+" , birthday :"+birthday;
				System.out.println(temp);
				
			}
			boolean isQueryOK = stmt.execute(sql);
			if (isQueryOK){
				System.out.println("OK");
			}else{
				System.out.println(sql);
			}
		}catch(Exception e){
			System.out.println(e);
		}
		
	}

}
