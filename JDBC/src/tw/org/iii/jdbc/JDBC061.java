package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.json.JSONStringer;
import org.json.JSONWriter;

public class JDBC061 {

	public static void main(String[] args) {
		//
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try(Connection conn = 
				DriverManager.getConnection(
						"jdbc:mysql://localhost/nba",
						prop)){
			// 3. SQL statement
			String sql = "select * from players";
			
			JSONWriter jw = new JSONStringer().array();
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				String firstname = rs.getString("firstname");
				String lastname = rs.getString("lastname");
				String pos = rs.getString("pos");
				String number = rs.getString("number");
					
				jw.object();
				
				jw.key("firstname").value(firstname);
				jw.key("lastname").value(lastname);
				jw.key("pos").value("#"+pos);
				jw.key("number").value(number);
				
				jw.endObject();
			}
			jw.endArray();
			//之前的java作業
			FileManager fm = new FileManager();
			fm.openFile(jw.toString());
//			System.out.println(jw.toString());
		}catch(Exception e){
			System.out.println(e);
		}	
	}
}
