package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NBAPlayerUpdate_v3 {

	public static void main(String[] args) {
		LinkedList<HashMap<String, String>> players = new LinkedList<HashMap<String, String>>();
		ArrayList<String> urls = DataCatcher.get("link");
		Integer teamIDS = 1;
		try {
			for(int i = 0; i < urls.size(); i ++){
				Document doc;
				String url = urls.get(i);
				doc = Jsoup.connect(url).get();
				Elements html = doc.select(".nba-player-index__trending-item > a[href]");
				try (BufferedReader br = new BufferedReader(new StringReader(html.toString()));){
					String line;
					
					while((line = br.readLine())!=null){
						HashMap<String, String> player = new HashMap();
						if(line.contains("title")){
							
							//teamID
							player.put("teamID", teamIDS.toString());
	//						System.out.println(player.get("teamID"));
							
							//website
							String debug = "http://www.nba.com"+line.substring(line.indexOf("/"), line.indexOf(">") - 1);
							if(debug.contains("'")) player.put("website", debug.replace("'", "''"));
							else player.put("website", debug);
	//						System.out.println(player.get("website"));
							
							// firstname & last name
							doc = Jsoup.connect(debug).get();
							Elements test = doc.select(".nba-player-header__details-bottom");
							BufferedReader br2 = new BufferedReader(new StringReader(test.toString()));
							String line2;String temp = "";
							while((line2 = br2.readLine())!= null){
								if(line2.contains("first-name")){
									temp = line2.substring(line2.indexOf(">")+2, line2.indexOf("/")-2);
									if(temp.contains("'")) {
										temp = temp.replace("'", "''");
	//									System.out.println(temp);	
									}
									player.put("firstname", temp);
	//								System.out.println(player.get("firstname"));
								}
								if(line2.contains("last-name")){
									temp = line2.substring(line2.indexOf(">")+2, line2.indexOf("/")-2);
									if(temp.contains("'")) {
										temp = temp.replace("'", "''");
	//									System.out.println(temp);	
									}
									player.put("lastname", temp);
	//								System.out.println(player.get("lastname"));
								}
							}
							
	//						number &pos
							test = doc.select(".nba-player-header__details-top >span");
							br2 = new BufferedReader(new StringReader(test.toString()));
							while((line2 = br2.readLine())!= null){
								if(line2.contains("jersey-number")){
									temp = line2.substring(line2.indexOf("#")+1, line2.indexOf("/")-1);
									if(temp.length()==0) temp = "'Null'";
									player.put("number", temp);
	//								System.out.println(number.get("number"));
								}
								if(line2.contains("Forward Center")){
									temp = line2.substring(line2.indexOf(">")+1, line2.indexOf("/")-1);
									player.put("pos", temp);
	//								System.out.println(pos.get(pos));
								}
								
							}
							
							//height & weight
							test = doc.select(".nba-player-vitals__top-info-imperial > span");
							br2 = new BufferedReader(new StringReader(test.toString()));
							String temp2 = "";String temp3 = "";
							int n = 0;
							while((line2 = br2.readLine())!= null){
								if(line2.contains("feet")){
									temp = line2.substring(line2.indexOf("span")+5, line2.indexOf("abbr")-2) + "''";
								}
								if(line2.contains("inches")){
									temp2 = line2.substring(line2.indexOf("span")+5, line2.indexOf("abbr")-2) + "''''";
								}
								if(n == 2){
									player.put("height", temp+temp2);
	//								System.out.println(height.get("height"));
								}
								if(line2.contains("pounds")){
									temp3 = line2.substring(line2.indexOf("span")+5, line2.indexOf("abbr")-2);
									player.put("weight", temp3);
	//								System.out.println(player.get("weight"));
								}
								n++;
							}
							
							
							//born age from debut 
							test = doc.select(".nba-player-vitals__bottom ");
							br2 = new BufferedReader(new StringReader(test.toString()));
	//						n = 0;
							while((line2 = br2.readLine())!= null){
								if(line2.contains("BORN")){
									temp = line2.substring(line2.lastIndexOf("/") - 19, line2.lastIndexOf("/") - 9);
									player.put("born", temp);
	//								System.out.println(born.get("born"));
								}
								if(line2.contains("AGE")){
									temp = line2.substring(line2.lastIndexOf("span") - 11, line2.lastIndexOf("span") - 9);
									player.put("age", temp);
	//								System.out.println(age.get("age"));
								}
								if(line2.contains("FROM")){
									temp = line2.substring(line2.lastIndexOf("bottom-info") + 14, line2.lastIndexOf("span") - 3);
									if(temp.contains("'")) {
										temp = temp.replace("'", "");
	//									System.out.println(temp);	
									}
									player.put("from", temp);
	//								System.out.println(from.get("from"));
								}
								if(line2.contains("DEBUT")){
									temp = line2.substring(line2.lastIndexOf("bottom-info") + 14, line2.lastIndexOf("span") - 3);
									if(temp.contains("—")) {
										temp = "Null";
	//									System.out.println(temp);	
									}
									player.put("debut", temp);
	//								System.out.println("debut");
								}
	//							System.out.println(line2);
							}
							
							//picture
							test = doc.select("img");
							br2 = new BufferedReader(new StringReader(test.toString()));
							line2 = br2.readLine();
							temp = line2.substring(line2.indexOf("src")+7,line2.length()-2);
							player.put("picture", temp);
	//						System.out.println(picture.get("picture"));
								
							
							// state
							test = doc.select("td");
							br2 = new BufferedReader(new StringReader(test.toString()));
							n=0;
							while((line2 = br2.readLine())!= null && n < 8){
								temp = line2.substring(line2.indexOf("td") + 4, line2.indexOf("/") - 2);
								switch (n) {
								case 0:
									if(temp.contains("—")) {
										temp = "Null";
	//									System.out.println(temp);	
									}
									player.put("mpg", temp);
	//								System.out.println("mpg :"+mpg.get("mpg"));
									break;
								case 1:
									if(temp.contains("—")) {
										temp = "Null";
	//									System.out.println(temp);	
									}
									player.put("fg", temp);
	//								System.out.println("fg :"+ fg.get("fg"));
									break;
								case 2:
									if(temp.contains("—")) {
										temp = "Null";
	//									System.out.println(temp);	
									}
									player.put("threeP", temp);
	//								System.out.println("threeP :"+threeP.get("threeP"));
									break;
								case 3:
									if(temp.contains("—")) {
										temp = "Null";
	//									System.out.println(temp);	
									}
									player.put("ft", temp);
	//								System.out.println("ft :"+ ft.get("ft"));
									break;
								case 4:
									if(temp.contains("—")) {
										temp = "Null";
	//									System.out.println(temp);	
									}
									player.put("ppg", temp);
	//								System.out.println("ppg :"+ ppg.get("ppg"));
									break;
								case 5:
									if(temp.contains("—")) {
										temp = "Null";
	//									System.out.println(temp);	
									}
									player.put("rpg", temp);
	//								System.out.println("rpg :" + rpg.get("rpg"));
									break;
								case 6:
									if(temp.contains("—")) {
										temp = "Null";
	//									System.out.println(temp);	
									}
									player.put("apg", temp);
	//								System.out.println("apg :"+ apg.get("apg"));
									break;
								case 7:
									if(temp.contains("—")) {
										temp = "Null";
	//									System.out.println(temp);	
									}
									player.put("bpg", temp);
	//								System.out.println("bpg :"+bpg.get("bpg"));
									break;
								}
								n++;
							}
							br2.close();
							players.add(player);
							System.out.println(players.size());
						}	
					}
					System.out.println(teamIDS);
					teamIDS++;
				}
			}
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/nba","root","root");
			/*
			 * update
			 * 
			 * 
			 */
			//insert
			StringBuffer sql = new StringBuffer();
			sql = sql.append("INSERT INTO player values ");
			
			for(int i = 0; i < players.size(); i++){
				HashMap<String, String> player = players.get(i);
				sql = sql.append (
						"(NULL,'"+player.get("firstname")+"','"+player.get("lastname")+"','"+player.get("pos")+"',"+
						player.get("number")+","+player.get("weight")+",'"+player.get("height")+"','"+player.get("born")+
						"',"+player.get("age")+","+player.get("debut")+",'"+player.get("picture")+"','"+player.get("website")+
						"','"+player.get("from")+"',"+player.get("teamID")+","+player.get("mpg")+","+player.get("fg")+","+
						player.get("threeP")+","+player.get("ft")+","+player.get("ppg")+","+player.get("rpg")+
						","+player.get("apg")+","+player.get("bpg")+")"
						);
				if(i != players.size() - 1) sql = sql.append(",\n");
			}
//			System.out.println(sql);
			Statement stmt = conn.createStatement();
			stmt.execute(sql.toString());	
			
		}catch (Exception e) {e.printStackTrace();}
		
		System.out.println("done");
	
		
	}
}

