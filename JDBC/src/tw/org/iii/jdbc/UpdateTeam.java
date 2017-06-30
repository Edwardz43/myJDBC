package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class UpdateTeam implements Runnable{
	public int teamCount, totalTeam = 30;
	@Override
	public void run(){
		teamCount = 0;
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
				teamCount++;
				System.out.println("teamCount :"+teamCount);
			}
			
			//insert
			try (Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/nba", "root","root");){
				
				StringBuffer sql = new StringBuffer();
				sql = sql.append("insert into teams (name, win, loss, players, url, logo) values (?,?,?,?,?,?) ");
				PreparedStatement pstmt = conn.prepareStatement(sql.toString());
				
				for(int i = 0; i < teams.size(); i++){
  					HashMap<String, String> team = teams.get(i);
 					sql = sql.append("('"+team.get("name")+"','"+ team.get("win") +"','"+ team.get("loss") +
 							"','"+ team.get("players") +"','"+ team.get("url")+"','"+team.get("logo")+"') ");
 					if(i != teams.size() - 1) sql = sql.append(",\n");
 					String name = team.get("name");
 					String win = team.get("win");
 					String loss = team.get("loss");
 					String players = team.get("players");
 					String url = team.get("url");
 					String logo = team.get("logo");
 					
 					pstmt.setString(1, name);
 					pstmt.setString(2, win);
 					pstmt.setString(3, loss);
 					pstmt.setString(4, players);
 					pstmt.setString(5, url);
 					pstmt.setString(6, logo);
 					pstmt.addBatch();
  				}
 				pstmt.executeBatch();
			}
			System.out.println("done");
			
		} catch (Exception e) {e.printStackTrace();}
	}
}
