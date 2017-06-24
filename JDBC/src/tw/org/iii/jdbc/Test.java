package tw.org.iii.jdbc;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Test extends JFrame{
	private JButton updateTeam, updatePlayer;
	private int state = 0;
	private static int teamCount = 0;
	private static int totalTeam = 30;
	private static int playerCount = 0;
	private static int totalPlayer = 450;
	private static MyCanvas myCanvas;
	private static int progressBar = 0;
	private Timer timer;
	
	public Test(){
		super("NBA Updater");
		setLayout(new BorderLayout());
		
		updateTeam= new JButton("更新球隊資料");updatePlayer= new JButton("更新球員資料");
		JTabbedPane bottom = new JTabbedPane();
		bottom.add(updateTeam);bottom.add(updatePlayer);
		myCanvas = new MyCanvas();
		
		updateTeam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(state == 0){
					updateTeam();
					state = 1;
				} 
			}
		});
		
		updatePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(state == 0){
					updatePlayer();
					state = 2;
				} 
			}
		});
		
		add(myCanvas, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		timer = new Timer();
		timer.schedule(new ViewTask(), 0, 20);
		setSize(640, 480);
		setLocation(getWidth()/2, getHeight() / 2);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	class ViewTask extends TimerTask{
		@Override
		public void run() {
			if(state == 1)progressBar = (int)(teamCount*10/totalTeam);
			else progressBar = (int)(playerCount*10/totalPlayer);
			teamCount++;
			myCanvas.repaint();
		}
	}
	
	class  MyCanvas extends JPanel{
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.clearRect(0, 0, 640, 480);
			g2d.fillRect(0, 0, 640, 480);
			g2d.setColor(Color.white);
			g2d.setFont(new Font("Serif", Font.BOLD, 26));
			g2d.drawString(progressBar+"%", 295, 180);
			g2d.setColor(Color.gray);
			g2d.drawRect(98, 198, 443, 23);
			g2d.setColor(Color.BLUE);
			g2d.fillRect(100, 200, 440*progressBar/100, 20);
			System.out.println("progressBar :"+440*progressBar/100);
			
		}
	}
	
	public void updateTeam(){
		LinkedList<HashMap<String, String>> teams = new LinkedList<HashMap<String, String>>();
		Document doc;
		
		try {
			doc = Jsoup.connect("http://nba.com/teams").get();
			Elements teamLinks = doc.select("div.team__list");
			
			//name & url
			try(BufferedReader br = new BufferedReader(
				new StringReader(teamLinks.toString()));)
			{
				String line;
				while((line = br.readLine()) != null){
					HashMap<String, String> team = new HashMap();
					if(line.contains("href=\"/teams")){
						team.put("name", line.substring(line.indexOf("\">")+2, line.indexOf("</a>")));
						team.put("url", "http://www.nba.com"+line.substring(line.indexOf("/teams"), line.indexOf("\">")));
						teams.add(team);
					}
				}
			}
			
			//logo
			try(BufferedReader br = new BufferedReader(
					new StringReader(teamLinks.toString()));)
			{
				String line;int n = 0;
				while((line = br.readLine()) != null){
					if(line.contains("logo")){
						HashMap<String, String> team = teams.get(n);
						team.put("logo", line.substring(line.indexOf("src=") + 7, line.indexOf("\" alt")));
						n++;
					}
				}
			}
			
			// win & loss
			for(int i = 0; i < teams.size(); i ++){
				Document doc2;
				String teamUrl = teams.get(i).get("url");
				doc2 = Jsoup.connect(teamUrl).get();
				Elements teamLink = doc2.select(".stat");
				try (
						BufferedReader br2 = new BufferedReader(new StringReader(teamLink.toString()));
						)
				{
					String line2; int n = 0;
					while((line2 = br2.readLine())!= null){
						if(n == 0) {
							line2 = line2.substring(line2.indexOf("stat\">") + 7, line2.indexOf("</s") - 1);
							teams.get(i).put("win", line2);
						}else {
							line2 = line2.substring(line2.indexOf("stat\">") + 7, line2.indexOf("</s") - 1);
							teams.get(i).put("loss", line2);
						}			
						n++;
					}
				}
				//players
				doc2 = Jsoup.connect(teamUrl).get();
				teamLink = doc2.select(".nba-player-index__row");
				
				try (
						BufferedReader br2 = new BufferedReader(new StringReader(teamLink.toString()));
						)
				{
					String line2; int n = 0;
					while((line2 = br2.readLine())!= null){
						if(line2.contains("nba-player-index__trending-item"))n++;
						teams.get(i).put("players", ""+n);
					}
				}
			}
			
			//insert
			try (Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/nba", "root","root");){
				
				StringBuffer sql = new StringBuffer();
				sql = sql.append("insert into team (name, win, loss, players, url, logo) values \n");
				for(int i = 0; i < teams.size(); i++){
					HashMap<String, String> team = teams.get(i);
					sql = sql.append("('"+team.get("name")+"','"+ team.get("win") +"','"+ team.get("loss") +
							"','"+ team.get("players") +"','"+ team.get("url")+"','"+team.get("logo")+"') ");
					if(i != teams.size() - 1) sql = sql.append(",\n");
				}
//				System.out.println(sql.toString());
				Statement stmt = conn.createStatement();
				stmt.execute(sql.toString());
			}	
			System.out.println("done");
			
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void updatePlayer() {
		LinkedList<HashMap<String, String>> players = new LinkedList<HashMap<String, String>>();
		LinkedList<HashMap<String, String>> urls = DataCatcher.getURL();
		Integer teamIDS = 1;
		try {
			for(int i = 0; i < urls.size(); i ++){
				Document doc;
				String url = urls.get(i).get("url");
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
	
	public static void main(String[] args) {
		new Test();
	}

}
