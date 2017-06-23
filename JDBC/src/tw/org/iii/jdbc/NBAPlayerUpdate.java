package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NBAPlayerUpdate {

	public static void main(String[] args) {
		try {
			Connection conn = 
					DriverManager.getConnection(
							"jdbc:mysql://localhost/nba",
							"root","root");
			
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("select firstname, lastname, website from players");
			
			int playerID =  1;
			while(rs.next()){
				String src = rs.getString("website");
				Document doc = Jsoup.connect(src).get();
				Elements test = doc.select("td");
				StringBuffer temp = new StringBuffer();
				temp.append(test.toString());
				while(temp.indexOf("<td>") != -1){
					temp.replace(temp.indexOf("<td>"), temp.indexOf("<td>") + 4, "");
				}
				while(temp.indexOf("</td>") != -1){
					temp.replace(temp.indexOf("</td>"), temp.indexOf("</td>") + 5, "");
				}
				StringReader sr = new StringReader(temp.toString());
				BufferedReader br = new BufferedReader(sr);
				
				String line; int i = 0;
				float[] num = new float[8];
				while((line = br.readLine()) != null && i < 8){
					System.out.println(line + i);
					num[i] = Float.parseFloat(line);
					i++;
				}
				br.close();
				
				String sql = "UPDATE players "
						+ "SET MPG = '"+num[0]+"', `FG%` = '"+num[1]+"', `3P%` ='"+num[2]+"',"
						+ " `FT%` ='"+num[3]+"', PPG = '"+num[4]+"', RPG = '"+num[5]+"',"
						+ " APG = '"+num[6]+"', BPG = '"+num[7]
						+ "' WHERE playerID = " +playerID+";";
				Statement stmt2 = conn.createStatement();
				stmt2.execute(sql);
				
				playerID++;
			}
		} catch (Exception e) {e.printStackTrace();}
	}
}

