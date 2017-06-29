package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class DataCatcher_Demo {
	
	public static void main(String[] args){
		
		
		
	}
	
	public static LinkedList<String> getImg(){
		LinkedList<String> result = new LinkedList<String>();
		try {
			Document doc = Jsoup.connect("http://www.nba.com/teams").get();
			Elements img = doc.select(".team__list > img");
			BufferedReader br = new BufferedReader(new StringReader(img.toString()));
			String line;
			while((line = br.readLine()) != null){
				int n = line.lastIndexOf("=") - 12;
				line = line.substring(n , n+7);
				result.add(line);
			}
		} catch (IOException e) {e.printStackTrace();}
		
		return result;
	}
	
 	public static LinkedList<Integer> getWinLoss(){
		LinkedList<HashMap<String, String>> urls = getURL();
		LinkedList<Integer> winLoss = new LinkedList<Integer>();
		for(int i = 0; i < urls.size(); i++){
			String url = urls.get(i).get("url");
			Document doc;
			String team = url; 
			try {
				doc = Jsoup.connect(team).get();
				Elements teamLink = doc.select(".stat");
				String temp = teamLink.toString();
				BufferedReader br = new BufferedReader(new StringReader(temp));
				String line;
				Integer win = 0; Integer loss = 0; 
				for(int j = 0; j < 2; j++){
					line = br.readLine();
					line= line.substring(line.indexOf(">") + 2, line.indexOf("/") - 2);
					winLoss.add(Integer.parseInt(line));
				}
				
				br.close();
				
			} catch (IOException e) {e.printStackTrace();}
		}
		return winLoss;
	}
	
	public static LinkedList<HashMap<String, String>> getName(){
		
		LinkedList<HashMap<String, String>> name = new LinkedList<HashMap<String, String>>();
		Document doc;
		
		try {
			doc = Jsoup.connect("http://nba.com/teams").get();
			Elements teamLink = doc.select("div.team__list > a ");
			
			BufferedReader br = new BufferedReader(new StringReader(teamLink.toString()));
			String line; 
			
			while((line = br.readLine()) != null){
				line = line.replaceFirst("<", "");
				HashMap<String, String> tempName = new HashMap();
				tempName.put("name", line.substring(line.indexOf(">")+1, line.indexOf("<")-1));
				name.add(tempName);
			}
			br.close();
			
			for(int j = 0; j < name.size(); j++){
				HashMap<String, String> tempName = new HashMap();
				tempName = name.get(j);
			}
			
		} catch (IOException e) {e.printStackTrace();}
		return name;
	}
	
	public static LinkedList<HashMap<String, String>> getURL(){
		LinkedList<HashMap<String, String>> url = new LinkedList<HashMap<String, String>>();
		try {
			Document doc;
			doc = Jsoup.connect("http://nba.com/teams").get();
			Elements teamLink = doc.select("div.team__list > a ");
			try(BufferedReader br = new BufferedReader(
					new StringReader(teamLink.toString()));)
				{
					String line;
					while((line = br.readLine()) != null){
						HashMap<String, String> tempURL = new HashMap();
						if(line.contains("href=\"/teams")){
							tempURL.put("url", "http://www.nba.com"+line.substring(line.indexOf("/teams"), line.indexOf("\">")));
							url.add(tempURL);
							if(url.size() == 3) return url;
						}
					}
				}
//			for(int i = 0; i < url.size(); i ++) System.out.println(url.get(i));
		} catch (Exception e) {e.printStackTrace();}
		return url;
	}
}

