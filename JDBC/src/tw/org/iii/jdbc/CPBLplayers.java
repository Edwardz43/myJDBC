package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class CPBLplayers {
	
	public  static  void main(String[] args) {
			// Brother=> E02, Lions=> L01, Lamigo=>A02, Fubon=>B04
			String[] teamURL = {"E02", "L01", "A02", "B04", "EE2", "L02", "AA1", "BA3"}; 
			Connection conn;
			try {
				conn = DriverManager.getConnection(
						"jdbc:mysql://localhost/cpbl","root","root");
				String sql ="insert into players (number, name, pos, BT, height, weight, DOB, teamID) values (?, ?, ?, ?, ?, ?, ?, ?) ";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				for(int i = 0; i < teamURL.length; i++) {
					int n = 0;
					ArrayList<HashMap<String, String>> players = getRows(teamURL[i], n = (i > 3 ? 4 : 1));
					for(HashMap<String, String> player : players) {
						String number = player.get("number");
						String name = player.get("name");
						String pos = player.get("pos");
						String BT = player.get("B/T");
						String HW = player.get("H/W");
						String height = (HW.substring(0, HW.indexOf("/") ));
						String weight = (HW.substring(HW.indexOf("/") + 1));
						String DOB = player.get("DOB");
						String teamID="";
						switch(teamURL[i]) {
							case "E02": case "EE2":
								teamID="1";
								break;
							case "L01": case "L02":
								teamID="2";
								break;
							 case "A02": case "AA1":
								teamID="3";
								break;
							 case "B04": case "BA3":
								teamID="4";
								break;
						}
						System.out.println(number + ":"+name+ ":"+pos+ ":"+BT+ ":"+HW+ ":"+DOB+":"+teamID);
//						System.out.println(HW.substring(0, HW.indexOf("/") ));
//						System.out.println(HW.substring(HW.indexOf("/") + 1));
						pstmt.setString(1, number);
						pstmt.setString(2, name);
						pstmt.setString(3, pos);
						pstmt.setString(4, BT);
						pstmt.setString(5, height);
						pstmt.setString(6, weight);
						pstmt.setString(7, DOB);
						pstmt.setString(8, teamID);
						pstmt.addBatch();
					}
				}
				pstmt.executeBatch();	
				conn.close();
			} catch (Exception e) {e.printStackTrace();}
		}
		
		public static ArrayList<HashMap<String,  String>> getRows(String url, int i){
			ArrayList<HashMap<String, String>> players = new ArrayList<>();
			String[] infos = new String[6];
			try {
				Document doc = Jsoup.connect("http://www.cpbl.com.tw/web/team_player.php?&team="+url+"&gameno=0"+i).get();
				Elements elements = doc.getElementsByTag("tr");	
				Elements e = elements.select("td");
				BufferedReader br = new BufferedReader(new StringReader(e.toString()));
				String line;
				int n = 0;
				while((line = br.readLine()) != null) {
					String temp;
					switch(n%6) {
						//背號
						case 0:
							temp = line.substring(line.indexOf("center")+8, line.indexOf("/td")-1);
							infos[0] = temp;
							break;
						//姓名
						case 1:
							temp = line.substring(line.indexOf(url)+5, line.indexOf("/a")-1);
							infos[1] = temp;
							break;
						//守備位置
						case 2:
							temp = line.substring(line.indexOf("\">")+2, line.indexOf("</t"));
							infos[2] = temp;
							break;
						//投/打
						case 3:
							temp = line.substring(line.indexOf("\">")+2, line.indexOf("</t"));
							infos[3] = temp;
							break;
						//身高/體重
						case 4:
							temp = line.substring(line.indexOf("\">")+2, line.indexOf("</t"));
							infos[4] = temp;
							break;
						//生日
						case 5:
							temp = line.substring(line.indexOf("\">")+2, line.indexOf("</t"));
							infos[5] = temp;
							break;
					}
					if(n % 6 == 5) {
						HashMap<String, String> result = new HashMap<>();
						result.put("number", infos[0]);
						result.put("name", infos[1]);
						result.put("pos", infos[2]);
						result.put("B/T", infos[3]);
						result.put("H/W", infos[4]);
						result.put("DOB", infos[5]);
						players.add(result);
					}
					n++;
				}
				//System.out.println(players.size());
				
			} catch (Exception e) {e.printStackTrace();}
			return players;
		}
}
