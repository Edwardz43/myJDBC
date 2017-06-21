package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupTest {

	public static void main(String[] args) {
		try {
			Connection conn = 
					DriverManager.getConnection(
							"jdbc:mysql://localhost/nba",
							"root","root");
			
			Statement stmt = conn.createStatement();
			
			LinkedList playerDetail = new LinkedList();
			ResultSet rs = stmt.executeQuery("select firstname, lastname, website from players");
			while(rs.next()){
				String player = "\n"+ rs.getString("firstname") + rs.getString("lastname") + "\n";
				String src = rs.getString("website");
				Document doc = Jsoup.connect(src).get();
				Elements test = doc.select("td");
				playerDetail.add(player);
				playerDetail.add(test);
			}
//			System.out.println("------------"+player+"----------------");
			System.out.println(playerDetail.size());
			FileManager fm = new FileManager();
			StringBuffer temp = new StringBuffer();
			for(int i = 0; i < playerDetail.size(); i++){
				temp.append(playerDetail.get(i));
			}
			while(temp.indexOf("<td>") != -1){
				temp.replace(temp.indexOf("<td>"), temp.indexOf("<td>") + 4, "");
			}
			while(temp.indexOf("</td>") != -1){
				temp.replace(temp.indexOf("</td>"), temp.indexOf("</td>") + 5, "");
			}
			fm.openFile(temp.toString());
			
//			System.out.println(rs.getString("website"));
		} catch (Exception e) {e.printStackTrace();}
		
		
	}

}
