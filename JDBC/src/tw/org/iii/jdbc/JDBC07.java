package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.json.JSONStringer;
import org.json.JSONWriter;

public class JDBC07 {

	public static void main(String[] args) {
		//
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try(Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/brad",prop)){
			
			Statement stmt = conn.createStatement();
			String sql = "insert into member (account, pw, realname)"+
						" values ('brad','123','brad chao')";
			int n = stmt.executeUpdate(sql);
			System.out.println(n);
			
		}catch(Exception e){
			System.out.println(e);
		}	
	}
}
