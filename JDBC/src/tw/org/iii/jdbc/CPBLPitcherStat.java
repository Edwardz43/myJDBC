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


public class CPBLPitcherStat {
	
	public  static  void main(String[] args) {
		// Brother=> E02, Lions=> L01, Lamigo=>A02, Fubon=>B04
		String[] teamURL = {"E02", "L01", "A02", "B04", "EE2", "L02", "AA1", "BA3"};
//		System.out.println(batters.size());
		Connection conn;
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/cpbl","root","root");
			String sql ="insert into pitcherstat (playerID, g, gs, gr, cg, sho, nbb, w, l, sv, bs, hld, ip, bf, np"
									+ ",h, hr, bb, ibb, hbp, so, wp, bk, r, er, go, ao) "
									+ "	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			for(int i = 0; i < teamURL.length; i++) {
				int n = 0;
				ArrayList<HashMap<String, String>> batters = getRows(teamURL[i], n = (i > 3 ? 4 : 1));
				for(HashMap<String, String> batter : batters) {
					String name = batter.get("name");
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("select playerID, name from players where name='"+name+"'");
					//System.out.println("select playerID, name from players where name='"+name+"'");
					if(rs.next()) {
						String playerID = rs.getString("playerID");
						System.out.println(playerID);
						String g = batter.get("g");
						String gs = batter.get("gs");
						String gr = batter.get("gr");
						String cg = batter.get("cg");
						String sho = batter.get("sho");
						String nbb = batter.get("nbb");
						String w = batter.get("w");
						String  l= batter.get("l");
						String sv = batter.get("sv");
						String bs = batter.get("bs");
						String hld = batter.get("hld");
						String ip = batter.get("ip");
						String bf = batter.get("bf");
						String np = batter.get("np");
						String h = batter.get("h");
						String hr = batter.get("hr");
						String bb = batter.get("bb");
						String ibb = batter.get("ibb");
						String hbp = batter.get("hbp");
						String so= batter.get("so");
						String wp = batter.get("wp");
						String bk = batter.get("bk");
						String r = batter.get("r");
						String er = batter.get("er");
						String go = batter.get("go");
						String ao = batter.get("ao");
						System.out.println(name + ":"+np+ ":"+ip+ ":"+so+ ":"+er+ ":"+go+":"+ao);
						pstmt.setString(1, playerID); pstmt.setString(2, g);
						pstmt.setString(3, gs);pstmt.setString(4, gr);
						pstmt.setString(5, cg);pstmt.setString(6, sho);
						pstmt.setString(7, nbb);pstmt.setString(8, w);
						pstmt.setString(9, l);pstmt.setString(10, sv);
						pstmt.setString(11, bs);pstmt.setString(12, hld);
						pstmt.setString(13, ip);pstmt.setString(14, bf);
						pstmt.setString(15, np);pstmt.setString(16, h);
						pstmt.setString(17, hr);pstmt.setString(18, bb);
						pstmt.setString(19, ibb);pstmt.setString(20, hbp);
						pstmt.setString(21, so);pstmt.setString(22, wp);
						pstmt.setString(23, bk);pstmt.setString(24, r);
						pstmt.setString(25, er);pstmt.setString(26, go);
						pstmt.setString(27, ao);
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
		String[] infos = new String[27];
		try {
			
			Document doc = Jsoup.connect(
					"http://www.cpbl.com.tw/web/team_playergrade.php?&gameno=0"+i+"&team="+url+"&year=2017&grade=2&syear=2017").get();
			Elements elements = doc.getElementsByTag("tr");	
			Elements es = elements.select("td");
			int n = 0;
			for(Element e : es) {
				//System.out.println(e.text() + " : " + n);
				switch(n % 31) {
					case 0: infos[0] = e.text(); break;//姓名
					case 2: infos[1] = e.text();break;//出場
					case 3: infos[2] = e.text();break;//先發
					case 4: infos[3] = e.text();break;//後援
					case 5: infos[4] = e.text();break;//完投
					case 6: infos[5] = e.text();break;//完封
					case 7: infos[6] = e.text();break;//無四球
					case 8: infos[7] = e.text();break;//勝
					case 9: infos[8] = e.text();break;//敗
					case 10: infos[9] = e.text();break;//救援
					case 11: infos[10] = e.text();break;//救援失敗
					case 12: infos[11] = e.text();break;//中繼點
					case 13: infos[12] = e.text(); break;//局數
					case 16: infos[13] = e.text();break;//面對打席
					case 17: infos[14] = e.text();break;//投球數
					case 18: infos[15] = e.text(); break;//安打
					case 19: infos[16] = e.text();break;//hr
					case 20: infos[17] = e.text();break;//四壞球
					case 21: infos[18] = e.text();break;//故意四壞
					case 22: infos[19] = e.text();break;//死球
					case 23: infos[20] = e.text();break;//三振
					case 24: infos[21] = e.text();break;//暴投
					case 25: infos[22] = e.text();break;//投手犯規
					case 26: infos[23] = e.text();break;//失分
					case 27: infos[24] = e.text();break;//責分
					case 28: infos[25] = e.text();break;//滾地出局
					case 29: infos[26] = e.text();break;//飛球出局
				}
				if(n % 31 == 30) {
					HashMap<String, String> result = new HashMap<>();
					result.put("name", infos[0]);result.put("g", infos[1]);
					result.put("gs", infos[2]);result.put("gr", infos[3]);
					result.put("cg", infos[4]);result.put("sho", infos[5]);
					result.put("nbb", infos[6]);result.put("w", infos[7]);
					result.put("l", infos[8]);result.put("sv", infos[9]);
					result.put("bs", infos[10]);result.put("hld", infos[11]);
					result.put("ip", infos[12]);result.put("bf", infos[13]);
					result.put("np", infos[14]);result.put("h", infos[15]);
					result.put("hr", infos[16]);result.put("bb", infos[17]);
					result.put("ibb", infos[18]);result.put("hbp", infos[19]);
					result.put("so", infos[20]);result.put("wp", infos[21]);
					result.put("bk", infos[22]);result.put("r", infos[23]);
					result.put("er", infos[24]);result.put("go", infos[25]);
					result.put("ao", infos[26]);
					batters.add(result);
				}
				n++;
				//System.out.println(e.text()+":"+n);
			}			
		} catch (Exception e) {e.printStackTrace();}
		return batters;
	}
}
