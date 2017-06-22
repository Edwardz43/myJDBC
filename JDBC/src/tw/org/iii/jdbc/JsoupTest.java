package tw.org.iii.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupTest {

	public static void main(String[] args) {
		try {
			String team = "http://www.nba.com/teams/"+"";
			Document doc = Jsoup.connect(team).get();
			Elements nums = doc.select(".nba-player-index__trending-item > a");
//			System.out.println(nums.size());
//			System.out.println(nums);
			BufferedReader br = new BufferedReader(new StringReader(nums.toString()));
			String line; 
			
			int i = 0;
			LinkedList temp = new LinkedList();
			while((line = br.readLine()) != null){
				if(line.contains("title"))
					temp.add(line.substring(line.indexOf("/"), line.indexOf(">")-2));
				i++;
			}
			br.close();
			
			for(int j = 0; j < temp.size(); j++){
				System.out.println(temp.get(j));
			}
			
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
}

