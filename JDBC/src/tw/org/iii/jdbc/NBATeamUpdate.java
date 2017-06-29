package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class NBATeamUpdate {
	public static void main(String[] args) {
		ArrayList<String> names = DataCatcher.get("name");
		ArrayList<String> urls = DataCatcher.get("link");
		ArrayList<Integer> winloss = DataCatcher.getWinLoss();
		ArrayList<String> imgs = DataCatcher.get("logo");
		try {
			Connection conn;
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/nba", "root","root");
			
			for(int i = 0; i < names.size(); i++){
				String name = names.get(i);
				String url = urls.get(i);
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
//				System.out.println("name :"+name+", url :"+url+", win :"+win+", loss :"+ loss+", img :"+img);
			}
		} catch (Exception e) {e.printStackTrace();}
		
	}
	
}

