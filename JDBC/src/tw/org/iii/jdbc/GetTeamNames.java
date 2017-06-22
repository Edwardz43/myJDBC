package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GetTeamNames {

	public static LinkedList<HashMap<String, String>> getName(){
		LinkedList<HashMap<String, String>> name = new LinkedList<HashMap<String, String>>();
		Document doc;
		
		try {
			doc = Jsoup.connect("http://nba.com/teams").get();
			Elements teamLink = doc.select("div.team__list > a ");
			
			BufferedReader br = new BufferedReader(new StringReader(teamLink.toString()));
			String line; 
			
			int i = 0;
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
		Document doc;
		
		try {
			doc = Jsoup.connect("http://nba.com/teams").get();
			Elements teamLink = doc.select("div.team__list > a ");
			
			BufferedReader br = new BufferedReader(new StringReader(teamLink.toString()));
			String line; 
			
			int i = 0;
			while((line = br.readLine()) != null){
				line = line.replaceFirst("<", "");
				HashMap<String, String> tempURL = new HashMap();
				tempURL.put("url", line.substring(line.indexOf("/"), line.indexOf(">")-2));
					
				url.add(tempURL);
				i++;
			}
			br.close();
			
			for(int j = 0; j < url.size(); j++){
				HashMap<String, String> tempURL = new HashMap();
				tempURL = url.get(j);
			}
			
		} catch (IOException e) {e.printStackTrace();}
		return url;
	}
}

