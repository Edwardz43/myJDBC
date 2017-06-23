package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupTest {
	static int teamCount = 0;
	static int totalTeam = 30;
	static int playerCount = 0;
	static int totalPlayer = 450;
	
	public static void main(String[] args) {

		
	}
	
	public static void updateTeam(){
		LinkedList<HashMap<String, String>> names = DataCatcher.getName();
		LinkedList<HashMap<String, String>> urls = DataCatcher.getURL();
		LinkedList <Integer> winloss = DataCatcher.getWinLoss();
		LinkedList<String> imgs = DataCatcher.getImg();
		int teamProgress = (int)((teamCount * 100)/totalTeam); 
		try {
			Connection conn;
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/nba", "root","root");
			
			for(int i = 0; i < names.size(); i++){
				String name = names.get(i).get("name");
				String url = urls.get(i).get("url");
				Integer win = winloss.get(i*2);
				Integer loss = winloss.get(i*2+1);
				String img = imgs.get(i);
				
				/* create
				 * String sql = "CREATE TABLE `nba`.`team` 
				 * ( `id` INT UNSIGNED NOT NULL AUTO_INCREMENT , 
				 *   `name` VARCHAR(250) NOT NULL , 
				 *   `win` INT UNSIGNED NOT NULL , 
				 *   `loss` INT UNSIGNED NOT NULL , 
				 *   `website` VARCHAR(250) NOT NULL , 
				 *   `picture` VARCHAR(250) NOT NULL , 
				 *    PRIMARY KEY (`id`)) ENGINE = InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;";
				 *    
				 *    update
				 */
				
				//insert
				String sql = "insert into team (name, win,loss, website, picture) "
						+ "values ('"+ name +"','"+ win +"','"+ loss +"','"+ url +"','"+ img+"');";
				
				Statement stmt = conn.createStatement();
				stmt.execute(sql);
				teamCount++;
//				System.out.println("name :"+name+", url :"+url+", win :"+win+", loss :"+ loss+", img :"+img);
			}
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void updatePlayer() {
		int playerProgress = (int)((playerCount * 100)/totalPlayer); 
		HashMap<String, String> firstname = new HashMap(); HashMap<String, String> lastname = new HashMap();
		HashMap<String, String> pos = new HashMap(); HashMap<String, String> number = new HashMap();
		HashMap<String, String> weight = new HashMap(); HashMap<String, String> height = new HashMap();
		HashMap<String, String> website = new HashMap();HashMap<String, String> debut = new HashMap();
		HashMap<String, String> born = new HashMap(); HashMap<String, String> age = new HashMap();
		HashMap<String, String> from = new HashMap();HashMap<String, String> teamID = new HashMap();
		HashMap<String, String> mpg = new HashMap(); HashMap<String, String> fg = new HashMap();
		HashMap<String, String> threeP = new HashMap(); HashMap<String, String> ft = new HashMap();
		HashMap<String, String> ppg = new HashMap(); HashMap<String, String> rpg = new HashMap();
		HashMap<String, String> apg = new HashMap(); HashMap<String, String> bpg = new HashMap();
		HashMap<String, String> picture = new HashMap();
		
		LinkedList<HashMap<String, String>> urls = DataCatcher.getURL();
		Integer teamIDS = 1;
		
		try {
			for(int i = 0; i < urls.size(); i ++){
				Document doc;
				String url = urls.get(i).get("url");
				doc = Jsoup.connect(url).get();
				Elements html = doc.select(".nba-player-index__trending-item > a[href]");
				BufferedReader br = new BufferedReader(new StringReader(html.toString()));
				String line;
				
				while((line = br.readLine())!=null){
					
					if(line.contains("title")){
						LinkedList<HashMap<String, String>> player = new LinkedList<HashMap<String, String>>();
						//teamID
						teamID.put("teamID", teamIDS.toString());
//						System.out.println(teamID.get("teamID"));
						
						//website
						String debug = "http://www.nba.com"+line.substring(line.indexOf("/"), line.indexOf(">") - 1);
						if(debug.contains("'")) debug = debug.replace("'", "''");
						website.put("website", debug);
//						System.out.println(website.get("website"));
						
						// firstname & last name
						doc = Jsoup.connect(website.get("website")).get();
						Elements test = doc.select(".nba-player-header__details-bottom");
						BufferedReader br2 = new BufferedReader(new StringReader(test.toString()));
						String line2;String temp = "";
						while((line2 = br2.readLine())!= null){
							if(line2.contains("first-name")){
								temp = line2.substring(line2.indexOf(">")+2, line2.indexOf("/")-2);
								if(temp.contains("'")) {
									temp = temp.replace("'", "''");
									System.out.println(temp);	
								}
								firstname.put("firstname", temp);
//								System.out.println(firstname.get("firstname"));
							}
							if(line2.contains("last-name")){
								temp = line2.substring(line2.indexOf(">")+2, line2.indexOf("/")-2);
								lastname.put("lastname", temp);
//								System.out.println(lastname.get("lastname"));
							}
						}
						
//						number &pos
						test = doc.select(".nba-player-header__details-top >span");
						br2 = new BufferedReader(new StringReader(test.toString()));
						while((line2 = br2.readLine())!= null){
							if(line2.contains("jersey-number")){
								temp = line2.substring(line2.indexOf("#")+1, line2.indexOf("/")-1);
								number.put("number", temp);
//								System.out.println(number.get("number"));
							}
							if(line2.contains("Forward Center")){
								temp = line2.substring(line2.indexOf(">")+1, line2.indexOf("/")-1);
								pos.put("pos", temp);
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
								height.put("height", temp+temp2);
//								System.out.println(height.get("height"));
							}
							if(line2.contains("pounds")){
								temp3 = line2.substring(line2.indexOf("span")+5, line2.indexOf("abbr")-2);
								weight.put("weight", temp3);
//								System.out.println(weight.get("weight"));
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
								born.put("born", temp);
//								System.out.println(born.get("born"));
							}
							if(line2.contains("AGE")){
								temp = line2.substring(line2.lastIndexOf("span") - 11, line2.lastIndexOf("span") - 9);
								age.put("age", temp);
//								System.out.println(age.get("age"));
							}
							if(line2.contains("FROM")){
								temp = line2.substring(line2.lastIndexOf("bottom-info") + 14, line2.lastIndexOf("span") - 3);
								if(temp.contains("'")) {
									temp = temp.replace("'", "");
//									System.out.println(temp);	
								}
								from.put("from", temp);
//								System.out.println(from.get("from"));
							}
							if(line2.contains("DEBUT")){
								temp = line2.substring(line2.lastIndexOf("bottom-info") + 14, line2.lastIndexOf("span") - 3);
								if(temp.contains("—")) {
									temp = "Null";
//									System.out.println(temp);	
								}
								debut.put("debut", temp);
//								System.out.println("debut");
							}
//							System.out.println(line2);
						}
						
						//picture
						test = doc.select("img");
						br2 = new BufferedReader(new StringReader(test.toString()));
						line2 = br2.readLine();
						temp = line2.substring(line2.indexOf("src")+7,line2.length()-2);
						picture.put("picture", temp);
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
								mpg.put("mpg", temp);
//								System.out.println("mpg :"+mpg.get("mpg"));
								break;
							case 1:
								if(temp.contains("—")) {
									temp = "Null";
//									System.out.println(temp);	
								}
								fg.put("fg", temp);
//								System.out.println("fg :"+ fg.get("fg"));
								break;
							case 2:
								if(temp.contains("—")) {
									temp = "Null";
//									System.out.println(temp);	
								}
								threeP.put("threeP", temp);
//								System.out.println("threeP :"+threeP.get("threeP"));
								break;
							case 3:
								if(temp.contains("—")) {
									temp = "Null";
//									System.out.println(temp);	
								}
								ft.put("ft", temp);
//								System.out.println("ft :"+ ft.get("ft"));
								break;
							case 4:
								if(temp.contains("—")) {
									temp = "Null";
//									System.out.println(temp);	
								}
								ppg.put("ppg", temp);
//								System.out.println("ppg :"+ ppg.get("ppg"));
								break;
							case 5:
								if(temp.contains("—")) {
									temp = "Null";
//									System.out.println(temp);	
								}
								rpg.put("rpg", temp);
//								System.out.println("rpg :" + rpg.get("rpg"));
								break;
							case 6:
								if(temp.contains("—")) {
									temp = "Null";
//									System.out.println(temp);	
								}
								apg.put("apg", temp);
//								System.out.println("apg :"+ apg.get("apg"));
								break;
							case 7:
								if(temp.contains("—")) {
									temp = "Null";
//									System.out.println(temp);	
								}
								bpg.put("bpg", temp);
//								System.out.println("bpg :"+bpg.get("bpg"));
								break;
							}
							n++;
						}
						Connection conn = DriverManager.getConnection(
								"jdbc:mysql://localhost/nba","root","root");
						/*
						 * update
						 * 
						 * 
						 */
						//insert
						String sql = "INSERT INTO player values "+ 
								"(NULL,'"+firstname.get("firstname")+"','"+lastname.get("lastname")+"','"+pos.get("pos")+"',"+number.get("number")+
								","+weight.get("weight")+",'"+height.get("height")+"','"+born.get("born")+"',"+age.get("age")+","+debut.get("debut")+
								",'"+picture.get("picture")+"','"+website.get("website")+"','"+from.get("from")+"',"+teamID.get("teamID")+
								","+mpg.get("mpg")+","+fg.get("fg")+","+threeP.get("threeP")+","+ft.get("ft")+","+ppg.get("ppg")+","+rpg.get("rpg")+
								","+apg.get("apg")+","+bpg.get("bpg")+");";
						
						Statement stmt = conn.createStatement();
						stmt.execute(sql);
						playerCount++;
					}
				}
				System.out.println(teamIDS);
				teamIDS++;
			}
		}catch (Exception e) {e.printStackTrace();}
		System.out.println("done");
	}
}

