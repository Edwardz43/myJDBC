package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.json.JSONStringer;
import org.json.JSONWriter;

public class JDBC06 {

	public static void main(String[] args) {
		//
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try(Connection conn = 
				DriverManager.getConnection(
						"jdbc:mysql://localhost/brad",
						prop)){
			// 3. SQL statement
			String sql = "select * from gift";
			
			JSONWriter jw = new JSONStringer().array();
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				String gid = rs.getString("gid");
				String Name = rs.getString("Name");
				String Feature = rs.getString("Feature");
				String SalePlace = rs.getString("SalePlace");
				
				
				
				jw.object();
				
				jw.key("gid").value(gid);
				jw.key("Name").value(Name);
				jw.key("Feature").value(Feature);
				jw.key("SalePlace").value(SalePlace);
				
				jw.endObject();
			}
			
			jw.endArray();
			System.out.println(jw.toString());
		}catch(Exception e){
			System.out.println(e);
		}
			
		
		
	}
	
	private static String getJSONString(String string){
		StringBuilder sb = new StringBuilder();
		try {
			URL url =new URL(string);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line = null;
			while((line = reader.readLine())!= null){
				sb.append(line.trim());
			}
			reader.close();
			//System.out.println(sb);
		} catch (Exception e) {e.printStackTrace();}
		return sb.toString();
	}

}
