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

	public static void main(String[] args) {
		LinkedList<HashMap<String, String>> name = new LinkedList<HashMap<String, String>>();
		LinkedList<HashMap<String, String>> url = new LinkedList<HashMap<String, String>>();
		Document doc;
		try {
			doc = Jsoup.connect("http://nba.com/teams").get();
			Elements teamLink = doc.select("div.team__list > a ");
			System.out.println(teamLink);
			
			BufferedReader br = new BufferedReader(new StringReader(teamLink.toString()));
			String line; 
			
			int i = 0;
			while((line = br.readLine()) != null){
				line = line.replaceFirst("<", "");
				HashMap<String, String> temp = new HashMap();
				temp.put("name", line.substring(line.indexOf(">")+1, line.indexOf("<")-1));
				name.add(temp);
				i++;
			}
			br.close();
			
			for(int j = 0; j < name.size(); j++){
				HashMap<String, String> temp = new HashMap();
				temp = name.get(j);
				System.out.println(temp.toString());
			}
			
//			for(int j = 0; j < temp.size(); j++){
//				System.out.println(temp.get(j));
//			}
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
}

