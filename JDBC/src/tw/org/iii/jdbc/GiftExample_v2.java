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

public class GiftExample_v2 {

	public static void main(String[] args) {
		
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		
		String strurl ="http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAgriculturalProduce.aspx";
		String json = getJSONString(strurl);
		Long startTime = System.currentTimeMillis();
		//增加自動關閉功能
		try(Connection conn = 
				DriverManager.getConnection(
						"jdbc:mysql://localhost/brad",
						prop)){
			// 3. SQL statement
			String sql = "INSERT INTO gift "+
					"(gid, Name, Feature, SalePlace,"+
					" ProduceOrg, SpecAndPrice, OrderUrl, ContactTel, Column1) "+
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			JSONArray root = new JSONArray(json);
			for(int i = 0; i < root.length(); i++){
				JSONObject row = root.getJSONObject(i);
				String gid = row.getString("ID");
				String name = row.getString("Name");
				String feature = row.getString("Feature");
				String salePlace = row.getString("SalePlace");
				String produceOrg = row.getString("ProduceOrg");
				String specAndPrice = row.getString("SpecAndPrice");
				String orderUrl = row.getString("OrderUrl");
				String contactTel = row.getString("ContactTel");
				String column1 = row.getString("Column1");
				
				pstmt.setString(1, gid);
				pstmt.setString(2, name);
				pstmt.setString(3, feature);
				pstmt.setString(4, salePlace);
				pstmt.setString(5, produceOrg);
				pstmt.setString(6, specAndPrice);
				pstmt.setString(7, orderUrl);
				pstmt.setString(8, contactTel);
				pstmt.setString(9, column1);
				
				//batch
				pstmt.addBatch();
			}
			// 4. query
			pstmt.executeBatch();
			
			System.out.println("ok");
			System.out.println(System.currentTimeMillis() - startTime);
			
			
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
