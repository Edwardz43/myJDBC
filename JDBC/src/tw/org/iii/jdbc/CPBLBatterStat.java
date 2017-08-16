package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class CPBLBatterStat {
	
	public  static  void main(String[] args) {
		// Brother=> E02, Lions=> L01, Lamigo=>A02, Fubon=>B04
		String[] teamURL = {"E02", "L01", "A02", "B04", "EE2", "L02", "AA1", "BA3"};
		//System.out.println(batters.size());
		Connection conn;
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/cpbl","root","root");
			String sql ="insert into batterstat (playerID, games, pa, ab, rbi, r, hits, 1b, 2b, 3b, hr, tb, so, sb, bb) "
									+ "	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			for(int i = 0; i < teamURL.length; i++) {
				int n = 0;
				ArrayList<HashMap<String, String>> batters = getRows(teamURL[i], n = (i > 3 ? 4 : 1));
				for(HashMap<String, String> batter : batters) {
					String name = batter.get("name");
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("select playerID, name from players where name='"+name+"'");
					System.out.println("select playerID, name from players where name='"+name+"'");
					if(rs.next()) {
						String playerID = rs.getString("playerID");
						System.out.println(playerID);
						String games = batter.get("games");
						String pa = batter.get("pa");
						String ab = batter.get("ab");
						String rbi = batter.get("rbi");
						String r = batter.get("r");
						String hits = batter.get("hits");
						String b1 = batter.get("1b");
						String  b2= batter.get("2b");
						String b3 = batter.get("3b");
						String hr = batter.get("hr");
						String tb = batter.get("tb");
						String so = batter.get("so");
						String sb = batter.get("sb");
						String bb = batter.get("bb");
						//System.out.println(name + ":"+games+ ":"+pa+ ":"+ab+ ":"+rbi+ ":"+r+":"+hits);
						pstmt.setString(1, playerID);
						pstmt.setString(2, games);
						pstmt.setString(3, pa);
						pstmt.setString(4, ab);
						pstmt.setString(5, rbi);
						pstmt.setString(6, r);
						pstmt.setString(7, hits);
						pstmt.setString(8, b1);
						pstmt.setString(9, b2);
						pstmt.setString(10, b3);
						pstmt.setString(11, hr);
						pstmt.setString(12, tb);
						pstmt.setString(13, so);
						pstmt.setString(14, sb);
						pstmt.setString(15, bb);
						pstmt.addBatch();
					}
				}
			}
			pstmt.executeBatch();	
			conn.close();
		} catch (Exception e) {e.printStackTrace();}
		}

		public static ArrayList<HashMap<String,  String>> getRows(String url, int i){
		ArrayList<HashMap<String, String>> batters = new ArrayList<>();
		String[] infos = new String[15];
		try {
			Document doc = Jsoup.connect("http://www.cpbl.com.tw/web/team_playergrade.php?&team="+url+"&gameno=0"+i).get();
			Elements elements = doc.getElementsByTag("tr");	
			Elements es = elements.select("td");
			int n = 0;
			for(Element e : es) {
				switch(n % 31) {
					case 0: infos[0] = e.text(); break;//
					case 2: infos[1] = e.text();break;//出場
					case 3: infos[2] = e.text();break;//打席
					case 4: infos[3] = e.text();break;//打數
					case 5: infos[4] = e.text();break;//打點
					case 6: infos[5] = e.text();break;//得分
					case 7: infos[6] = e.text();break;//安打
					case 8: infos[7] = e.text();break;//一安
					case 9: infos[8] = e.text();break;//二安
					case 10: infos[9] = e.text();break;//三安
					case 11: infos[10] = e.text();break;//HR
					case 12: infos[11] = e.text();break;//壘打數
					case 13: infos[12] = e.text(); break;//三振
					case 14: infos[13] = e.text();break;//盜壘
					case 21: infos[14] = e.text();break;//保送
				}
				if(n % 31 == 30) {
					HashMap<String, String> result = new HashMap<>();
					result.put("name", infos[0]);
					result.put("games", infos[1]);
					result.put("pa", infos[2]);
					result.put("ab", infos[3]);
					result.put("rbi", infos[4]);
					result.put("r", infos[5]);
					result.put("hits", infos[6]);
					result.put("1b", infos[7]);
					result.put("2b", infos[8]);
					result.put("3b", infos[9]);
					result.put("hr", infos[10]);
					result.put("tb", infos[11]);
					result.put("so", infos[12]);
					result.put("sb", infos[13]);
					result.put("bb", infos[14]);
					batters.add(result);
				}
				n++;
				//System.out.println(e.text()+":"+n);
			}			
		} catch (Exception e) {e.printStackTrace();}
		return batters;
	}
}
