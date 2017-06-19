package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

public class AgriExample {

	public static void main(String[] args) {
		
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		
		String strurl ="http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvPermitAgri.aspx";
		String json = getJSONString(strurl);
		
		
		
		
		//增加自動關閉功能
		try(Connection conn = 
				DriverManager.getConnection(
						"jdbc:mysql://localhost/brad",
						prop)){
			// 3. SQL statement
			String sql = "INSERT INTO agri "+
					"(AgriMainName, CountyName, TownshipName, AgriMainAdrs,"+
					" Longitude, Latitude, AgriTel, AgriURL, PermitNo, Photo) "+
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? ,?);";
			
			
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			JSONArray root = new JSONArray(json);
			for(int i = 0; i < root.length(); i++){
				JSONObject row = root.getJSONObject(i);
				String agriMainName = row.getString("AgriMainName");
				String countyName = row.getString("CountyName");
				String townshipName = row.getString("TownshipName");
				String agriMainAdrs = row.getString("AgriMainAdrs");
				String longitude = row.getString("Longitude");
				String latitude = row.getString("Latitude");
				String agriTel = row.getString("AgriTel");
				String agriURL = row.getString("AgriURL");
				String permitNo = row.getString("PermitNo");
				String photo = row.getString("Photo");
				
				pstmt.setString(1, agriMainName);
				pstmt.setString(2, countyName);
				pstmt.setString(3, townshipName);
				pstmt.setString(4, agriMainAdrs);
				pstmt.setString(5, longitude);
				pstmt.setString(6, latitude);
				pstmt.setString(7, agriTel);
				pstmt.setString(8, agriURL);
				pstmt.setString(9, permitNo);
				pstmt.setString(10, photo);
				
				// 4. query
				pstmt.execute();
			}
			
			
			
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
			System.out.println(sb);
		} catch (Exception e) {e.printStackTrace();}
		return sb.toString();
	}

}
