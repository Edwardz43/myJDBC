package tw.org.iii.jdbc;

import java.util.HashMap;
import java.util.LinkedList;

public class CreateTeam {

	public static void main(String[] args) {
		LinkedList<HashMap<String, String>> names = DataCatcher.getName();
		LinkedList<HashMap<String, String>> url = DataCatcher.getURL();
		for(int i = 0; i <names.size(); i++) {
			System.out.println(names.get(i).get("name")+","+url.get(i).get("url"));
			
		}
			
	}

}
