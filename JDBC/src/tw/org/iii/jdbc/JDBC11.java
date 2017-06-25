package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBC11 {

	public static void main(String[] args) {
		//
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try(Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/brad",prop)){
			
//			DatabaseMetaData metadata = conn.getMetaData();
//			boolean isOK = metadata.supportsResultSetConcurrency(
//					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//			System.out.println(isOK);
			
			
			Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			
			
			String sql = "select * from member where id = 2";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			System.out.println(rs.getString("account"));
			
			rs.updateString("account", "ED43");
			rs.updateString("pw", "7788");
			rs.updateRow();
			
			PreparedStatement pstmt = 
					conn.prepareStatement(
					"select * from member ", 
					ResultSet.TYPE_FORWARD_ONLY, 
					ResultSet.CONCUR_UPDATABLE);
			
			ResultSet rs2 = pstmt.executeQuery();
			while(rs2.next()){
				rs2.updateString("pw", "111111");
				rs2.updateRow();
			}
			
			rs2.moveToInsertRow();
			rs2.updateString("account", "tony");
			rs2.updateString("pw", "4466");
			rs2.updateString("realname", "tony cha");
			rs2.insertRow();
			
		}catch(Exception e){
			System.out.println(e);
		}	
	}
	
}

