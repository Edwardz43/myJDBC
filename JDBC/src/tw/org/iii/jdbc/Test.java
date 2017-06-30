package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Test {

	
	public static void main(String[] args) {
		LinkedList<HashMap<String, String>> careerSets = new LinkedList<HashMap<String, String>>();
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/nba","root","root");
			StringBuffer sql = new StringBuffer(""
					+ "select playerID, firstname, lastname from players where firstname = '"+"Vince"+"' and lastname = '"+"Carter"+"' ");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
			rs.next();
			int playerID = rs.getInt(1);
			System.out.println(playerID);
			Document doc = Jsoup.connect("http://www.nba.com/players/vince/carter/1713").get();
			Elements es = doc.select(".nba-player-career-snapshot .scroll");
			String[] temp = es.text().split(" ");
			HashMap<String, String> careerSet = new HashMap<String, String>();
			for(int i = 24; i < temp.length; i++){
				System.out.println(temp[i] +" : "+i);
				switch (i % 24) {
				case 0:
					careerSet.put("year", temp[i]);
//					System.out.println(careerSet.get("year") + " : " +i);
					break;
				case 1:
					careerSet.put("team", temp[i]);
//					System.out.println(careerSet.get("team")+ " : " +i);
					break;
				case 2:
					careerSet.put("gp", temp[i]);
//					System.out.println(careerSet.get("gp")+ " : " +i);
					break;
				case 3:
					careerSet.put("gs", temp[i]);
//					System.out.println(careerSet.get("gs")+ " : " +i);
					break;
				case 4:
					careerSet.put("min", temp[i]);
					break;
				case 5:
					careerSet.put("pts", temp[i]);
					break;
				case 6:
					careerSet.put("fgm", temp[i]);
					break;
					
				case 7:
					careerSet.put("fga", temp[i]);
					break;
				case 8:
					careerSet.put("fg%", temp[i]);
					break;
				case 9:
					careerSet.put("3pm", temp[i]);
					break;
				case 10:
					careerSet.put("3pa", temp[i]);
					break;
				case 11:
					careerSet.put("3p%", temp[i]);
					break;
				case 12:
					careerSet.put("ftm", temp[i]);
					break;
				case 13:
					careerSet.put("fta", temp[i]);
					break;
				case 14:
					careerSet.put("ft%", temp[i]);
					break;
					
				case 15:
					careerSet.put("oreb", temp[i]);
					break;
				case 16:
					careerSet.put("dreb", temp[i]);
					break;
				case 17:
					careerSet.put("reb", temp[i]);
					break;
				case 18:
					careerSet.put("ast", temp[i]);
					break;
					
				case 19:
					careerSet.put("stl", temp[i]);
					break;
				case 20:
					careerSet.put("blk", temp[i]);
					break;
				case 21:
					careerSet.put("tov", temp[i]);
					break;
				case 22:
					careerSet.put("pf", temp[i]);
					break;
				case 23:
					careerSet.put("+/-", temp[i]);
					break;
				}
				if(i % 23 == 0) {
//					System.out.println(careerSet);
					HashMap<String, String> player = new HashMap<>();
					player.put("year", careerSet.get("year"));
					player.put("team", careerSet.get("team"));
					player.put("gp", careerSet.get("gp"));
					player.put("gs", careerSet.get("gs"));
					player.put("min", careerSet.get("min"));
					player.put("pts", careerSet.get("pts"));
					player.put("fgm", careerSet.get("fgm"));
					player.put("fga", careerSet.get("fga"));
					player.put("fg%", careerSet.get("fg%"));
					player.put("3pm", careerSet.get("3pm"));
					player.put("3pa", careerSet.get("3pa"));
					player.put("3p%", careerSet.get("3p%"));
					player.put("ftm", careerSet.get("ftm"));
					player.put("fta", careerSet.get("fta"));
					player.put("ft%", careerSet.get("ft%"));
					player.put("oreb", careerSet.get("oreb"));
					player.put("dreb", careerSet.get("dreb"));
					player.put("ast", careerSet.get("ast"));
					player.put("stl", careerSet.get("stl"));
					player.put("blk", careerSet.get("blk"));
					player.put("tov", careerSet.get("tov"));
					player.put("pf", careerSet.get("pf"));
					player.put("+/-", careerSet.get("+/-"));
					
					careerSets.add(player);
//					careerSet.clear();
					System.out.println(careerSets.size());
				}
			}
			
			for(int i = 0; i < careerSets.size(); i++){
				System.out.println(careerSets.get(i) +" : "+ i);
			}
			sql.delete(0, sql.length()-1);
			sql = sql.append("INSERT INTO nba.career "
					+ "(year, team, gp, gs, `min`, pts,"
					+ " fgm, fga, `fg%`, 3pm, 3pa, `3p%`, ftm,"
					+ " fta, `ft%`, `oreb`, `dreb`, ast, stl, blk, tov, "
					+ "pf, `+/-`, playerID ) VALUES"
					+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?,?,?,?) ");
			
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			
			for(int i = 0; i < careerSets.size(); i++){
				HashMap<String, String> player = careerSets.get(i);
				String year = player.get("year");
				String team = player.get("team");
				String gp = player.get("gp");
				String gs = player.get("gs");
				String min = player.get("min");
				String pts = player.get("pts");
				String fgm = player.get("fgm");
				String fga = player.get("fga");
				String fgp = player.get("fg%");
				String threePm = player.get("3pm");
				String threePa = player.get("3pa");
				String threePp = player.get("3p%");
				String ftm = player.get("ftm");
				String fta = player.get("fta");
				String ftp = player.get("ft%");
				String oreb = player.get("oreb");
				String dreb = player.get("dreb");
				String ast = player.get("ast");
				String stl = player.get("stl");
				String blk = player.get("blk");
				String tov = player.get("tov");
				String pf = player.get("pf");
				String PM = player.get("+/-");
//				
				pstmt.setString(1, year);pstmt.setString(2, team);
				pstmt.setString(3, gp);pstmt.setString(4, gs);
				pstmt.setString(5, min);pstmt.setString(6, pts);
				pstmt.setString(7, fgm);pstmt.setString(8, fga);
				pstmt.setString(9, fgp);pstmt.setString(10, threePm);
				pstmt.setString(11, threePa);pstmt.setString(12, threePp);
				pstmt.setString(13, ftm);pstmt.setString(14, fta);
				pstmt.setString(15, ftp);pstmt.setString(16, oreb);
				pstmt.setString(17, dreb);pstmt.setString(18, ast);
				pstmt.setString(19, stl);pstmt.setString(20, blk);
				pstmt.setString(21, tov);pstmt.setString(22, pf);
				pstmt.setString(23, PM);pstmt.setString(24, ""+playerID);
				pstmt.addBatch();
				
			}
			pstmt.executeBatch();	
				System.out.println("DONE");	

		} catch (Exception e) {e.printStackTrace();}
	}

}

