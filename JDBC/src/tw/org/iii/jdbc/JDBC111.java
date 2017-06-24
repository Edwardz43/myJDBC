package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;



public class JDBC111 {

	public static void main(String[] args) {
		//
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try(Connection conn = DriverManager.getConnection(
					"jdbc:mssql://localhost/brad",prop)){
			
			DatabaseMetaData metadata = conn.getMetaData();
			boolean isOK = metadata.supportsResultSetConcurrency(
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			System.out.println(isOK);
			
			
			Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			
			
			String sql = "select * from member where id = 2";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			System.out.println(rs.getString("account"));
			
			rs.updateString("account", "ED43");
			rs.updateString("pw", "7788");
			rs.updateRow();
			
			
			
		}catch(Exception e){
			System.out.println(e);
		}	
	}
	
}

