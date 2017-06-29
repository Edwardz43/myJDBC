package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataCatcher {
	
 	public static ArrayList<Integer> getWinLoss(){
		ArrayList<String> urls = get("link");
		ArrayList<Integer> winLoss = new ArrayList<Integer>();
		for(int i = 0; i < urls.size(); i++){
			String url = urls.get(i);
			Document doc;
			String team = url; 
			try {
				doc = Jsoup.connect(team).get();
				Elements teamLink = doc.select(".stat");
				String temp = teamLink.toString();
				BufferedReader br = new BufferedReader(new StringReader(temp));
				String line;
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
	
	public static ArrayList<String> get(String type){
		ArrayList<String> result = new ArrayList<String>();
		String selector = "", beginKey= "", endKey= "";
		int stringBegin = 0, stringEnd = 0;
		switch (type) {
		case "logo":
			selector =".team__list > img";
			beginKey ="src"; endKey = "svg";
			stringBegin = 7 ; stringEnd = 3;
			break;
		case "name":
			selector ="div.team__list > a";
			beginKey ="\">"; endKey = "</a>";
			stringBegin = 2 ; stringEnd = 0;
			break;
		case "link":
			selector ="div.team__list > a";
			beginKey ="href"; endKey = "\">";
			stringBegin = 7 ; stringEnd = 0;
			break;
		default:
			break;
		}
		try {
			Document doc = Jsoup.connect("http://nba.com/teams").get();
			Elements elements = doc.select(selector);
			for(Element element : elements){
				String temp = element.toString();
				result.add(temp.substring(temp.indexOf(beginKey) + stringBegin ,temp.indexOf(endKey) + stringEnd));
			}
		} catch (Exception e) {e.printStackTrace();}
		return result;
	}
}

