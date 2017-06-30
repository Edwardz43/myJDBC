package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class UpdatePlayer implements Runnable{
	public int totalPlayer = 85, playerCount = 0, n, m;
	
	public UpdatePlayer (int n){
		this.n = n;
		this.m = 5;
	}
	
	@Override
	public void run() {
		
		LinkedList<HashMap<String, String>> players = new LinkedList<HashMap<String, String>>();
		ArrayList<String> urls = DataCatcher.get("link");
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/nba","root","root");
			StringBuffer sql = new StringBuffer("select sum(players) from teams where teamId between "+ (n*m +1)+" and "+ ((n+1)*m));
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
			rs.next();
			totalPlayer = rs.getInt(1);
			System.out.println(totalPlayer);
			
			for(int i = 0; i < m; i ++){
				Document doc;
				String url = urls.get(i+n*m);
				doc = Jsoup.connect("http://www.nba.com/"+url).get();
				Elements html = doc.select(".nba-player-index__trending-item > a[href]");
				
				sql = new StringBuffer("SELECT teamID FROM teams WHERE url = 'http://www.nba.com/"+url+"' ");
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql.toString());
				rs.next();
				Integer teamIDS = rs.getInt(1);
				
				try (BufferedReader br = new BufferedReader(new StringReader(html.toString()));){
					String line;
					
					while((line = br.readLine())!=null){
						HashMap<String, String> player = new HashMap();
						//teamID
						player.put("teamID", teamIDS.toString());
//						System.out.println(player.get("teamID"));
						
						if(line.contains("title")){
							
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
									player.put("firstname", temp);
	//								System.out.println(player.get("firstname"));
								}
								if(line2.contains("last-name")){
									temp = line2.substring(line2.indexOf(">")+2, line2.indexOf("/")-2);
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
									if(temp.length()==0) temp = "Null";
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
									temp = line2.substring(line2.indexOf("span")+5, line2.indexOf("abbr")-2) + "'";
								}
								if(line2.contains("inches")){
									temp2 = line2.substring(line2.indexOf("span")+5, line2.indexOf("abbr")-2) + "''";
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
									player.put("from", temp);
	//								System.out.println(from.get("from"));
								}
								if(line2.contains("DEBUT")){
									temp = line2.substring(line2.lastIndexOf("bottom-info") + 14, line2.lastIndexOf("span") - 3);
									if(temp.contains("—")) {
										temp = "0";
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
								if(temp.contains("—")) {
									temp = "0";
//									System.out.println(temp);	
								}
								
								switch (n) {
								case 0:
									player.put("mpg", temp);
	//								System.out.println("mpg :"+mpg.get("mpg"));
									break;
								case 1:
									player.put("fg", temp);
	//								System.out.println("fg :"+ fg.get("fg"));
									break;
								case 2:
									player.put("threeP", temp);
	//								System.out.println("threeP :"+threeP.get("threeP"));
									break;
								case 3:
									player.put("ft", temp);
	//								System.out.println("ft :"+ ft.get("ft"));
									break;
								case 4:
									player.put("ppg", temp);
	//								System.out.println("ppg :"+ ppg.get("ppg"));
									break;
								case 5:
									player.put("rpg", temp);
	//								System.out.println("rpg :" + rpg.get("rpg"));
									break;
								case 6:
									player.put("apg", temp);
	//								System.out.println("apg :"+ apg.get("apg"));
									break;
								case 7:
									player.put("bpg", temp);
	//								System.out.println("bpg :"+bpg.get("bpg"));
									break;
								}
								n++;
							}
							br2.close();
							players.add(player);
							playerCount  = players.size();
							System.out.println(playerCount);
						}	
					}
//					System.out.println(teamIDS);
					teamIDS++;
				}
			}
			//insert
			sql.delete(0, sql.length()-1);
			sql = sql.append("INSERT INTO nba.players "
					+ "(firstname, lastname, pos, number, weight, height,"
					+ " born, age, debut, picture, website, `[from]`, teamID,"
					+ " mpg, `fg%`, `3p%`, `ft%`, ppg, rpg, apg, bpg) VALUES"
					+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?) ");
			
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			
			for(int i = 0; i < players.size(); i++){
				HashMap<String, String> player = players.get(i);
				String firstname = player.get("firstname");
				String lastname = player.get("lastname");
				String pos = player.get("pos");
				String number = player.get("number");
				String weight = player.get("weight");
				String height = player.get("height");
				String born = player.get("born");
				String age = player.get("age");
				String debut = player.get("debut");
				String picture = player.get("picture");
				String website = player.get("website");
				String from = player.get("from");
				String teamID = player.get("teamID");
				String mpg = player.get("mpg");
				String fg = player.get("fg");
				String threeP = player.get("threeP");
				String ft = player.get("ft");
				String ppg = player.get("ppg");
				String rpg = player.get("rpg");
				String apg = player.get("apg");
				String bpg = player.get("bpg");
				
				
				pstmt.setString(1, firstname);pstmt.setString(2, lastname);
				pstmt.setString(3, pos);pstmt.setString(4, number);
				pstmt.setString(5, weight);pstmt.setString(6, height);
				pstmt.setString(7, born);pstmt.setString(8, age);
				pstmt.setString(9, debut);pstmt.setString(10, picture);
				pstmt.setString(11, website);pstmt.setString(12, from);
				pstmt.setString(13, teamID);pstmt.setString(14, mpg);
				pstmt.setString(15, fg);pstmt.setString(16, threeP);
				pstmt.setString(17, ft);pstmt.setString(18, ppg);
				pstmt.setString(19, rpg);pstmt.setString(20, apg);
				pstmt.setString(21, bpg);
				
				pstmt.addBatch();
				
			}
			pstmt.executeBatch();	
			
		}catch (Exception e) {e.printStackTrace();}
		
		System.out.println("done");
	}
}
