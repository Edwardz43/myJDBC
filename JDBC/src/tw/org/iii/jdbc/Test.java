package tw.org.iii.jdbc;

import java.util.ArrayList;

public class Test {

	
	public static void main(String[] args) {
		ArrayList<String> urls = DataCatcher.get("link");
		ArrayList<String> logos = DataCatcher.get("logo");
		ArrayList<String> names = DataCatcher.get("name");
		for(int i = 0; i < urls.size(); i++) {
			System.out.println(urls.get(i) + " : " + logos.get(i) + " : " + names.get(i));
		}
	}

}

