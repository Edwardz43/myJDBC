package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaiwanRailwayExample {

	public static void main(String[] args) {
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		
		String strurl ="/home/edlo/20170801.json";
		Long startTime = System.currentTimeMillis();
		String json = getJSONString(strurl);
		
		
		//增加自動關閉功能
		try(Connection conn = 
				DriverManager.getConnection(
						"jdbc:mysql://localhost/traininfos",
						prop)){
			// 3. SQL statement
			String sql = "INSERT INTO traininfo "+
					"(train, type, breastfeed, route, package, overnightstn,"+
					" linedir, line, dinning, cripple, carclass, bike, note, noteeng) "+
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				PreparedStatement pstmt = conn.prepareStatement(sql);
		
				JSONObject root = new JSONObject(json);
				JSONArray array = root.getJSONArray("TrainInfos");
				BufferedWriter writer = new BufferedWriter(new FileWriter("/home/edlo/test.txt"));
				for(int i = 0; i < 1; i++) {					
					JSONObject  row = array.getJSONObject(i);
					//System.out.println(row.toString());
//					String Type = row.getString("Type");
//					String CarClass = row.getString("CarClass");
//					String Cripple = row.getString("Cripple");
//					String BreastFeed = row.getString("BreastFeed");
//					String Package = row.getString("Package");
//					String Train = row.getString("Train");
//					String Route = row.getString("Route");
//					String Bike = row.getString("Bike");
//					String LineDir = row.getString("LineDir");
//					String Line= row.getString("Line");
//					String Note = row.getString("Note");
//					String NoteEng = row.getString("NoteEng");
//					System.out.println(Note);
//					String Dinning = row.getString("Dinning");
//					String OverNightStn = row.getString("OverNightStn");
					
//					pstmt.setString(1, Train);
//					pstmt.setString(2, Type);
//					pstmt.setString(3, BreastFeed);
//					pstmt.setString(4, Route);
//					pstmt.setString(5, Package);
//					pstmt.setString(6, OverNightStn);
//					pstmt.setString(7, LineDir);
//					pstmt.setString(8, Line);
//					pstmt.setString(9, Dinning);
//					pstmt.setString(10, Cripple);
//					pstmt.setString(11, CarClass);
//					pstmt.setString(12, Bike);
//					pstmt.setString(13, Note);
//					pstmt.setString(14, NoteEng);
//					// 4. query
//					pstmt.execute();
					JSONArray timeInfos  = row.getJSONArray("TimeInfos");
					StringBuffer sb = new StringBuffer();
					for(int j = 0; j < timeInfos.length(); j++) {
//						sql = "INSERT INTO timeinfo (station, `[order]`, route, arrTime, depTime, tid)  VALUES (?, ?, ?, ?, ?, ?) ";
//						pstmt = conn.prepareStatement(sql);
//						
						JSONObject infos = timeInfos.getJSONObject(j);
						sb.append(infos.toString());
//						String station = infos.getString("Station");
//						String order = infos.getString("Order");
//						String route = infos.getString("Route");
//						String arrTime = infos.getString("ArrTime");
//						String depTime = infos.getString("DepTime");
////						System.out.println(station+" : "+order+" : "+route+" : "+arrTime+" : "+depTime);
//						
//						pstmt.setString(1, station);
//						pstmt.setString(2, order);
//						pstmt.setString(3, route);
//						pstmt.setString(4, arrTime);
//						pstmt.setString(5, depTime);
//						pstmt.setString(6, ""+(i+1));
//						pstmt.execute();
					}
					writer.write(sb.toString());
					writer.flush();
					writer.close();
				}
				System.out.println("ok");
				System.out.println(System.currentTimeMillis() - startTime);	
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	private static String getJSONString(String string){
		StringBuilder sb = new StringBuilder();
		try {
//			URL url =new URL(string);
//			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//			conn.connect();
			FileInputStream fin = new FileInputStream(new File(string));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fin,"UTF-8"));
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
