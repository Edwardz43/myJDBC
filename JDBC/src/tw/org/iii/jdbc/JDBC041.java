package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class JDBC041 {

	public static void main(String[] args) {
		//preparedstatement ->避免隱碼攻擊
		//--------------
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		//增加自動關閉功能
		try(Connection conn = 
				DriverManager.getConnection(
						"jdbc:mysql://localhost/brad",
						prop)){
			// 3. SQL statement
			String sql = "insert into cust (cname ,tel ,birthday) values (?,?,?)";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Peter");
			pstmt.setString(2, "321");
			pstmt.setString(3, "1998-05-09");
			
			// 4. query
			pstmt.execute();
			
			
		}catch(Exception e){
			System.out.println(e);
		}
		
	}

}
