package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

public class JDBC10 {

	public static void main(String[] args) {
		//
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try(Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/brad",prop)){
			
			Statement stmt = conn.createStatement();
			
//			String sql = "select * from gift where ProduceOrg = '水里鄉農會'";
			String keyword = "彰化";
			String sqlCond = "where Name like '%"+keyword+"%' or Feature like '%"+keyword+
					"%' or SalePlace like '%"+keyword+"%' ";
			String sqlCount = "select count(*) as num from gift " + sqlCond;
//			System.out.println(sqlCount);
			ResultSet rsCount = stmt.executeQuery(sqlCount);
			if(!rsCount.next()) return;
			
			int total = rsCount.getInt("num");
			int rpp = 20;
			int page = 1;
			int lastPage = (total % rpp == 0)?total / rpp : total / rpp + 1;
			page = (page<=lastPage)?page:lastPage;
			
			int start = (page-1)*rpp;
			String sql = "select * from gift "+sqlCond+" limit "+start+", "+rpp+"";
//			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			int i = 0;
			while(rs.next()){
				String gid = rs.getString("gid");
				String name = rs.getString("Name");
				System.out.println((++i)+" : "+gid+" : "+name);
			}
			
		}catch(Exception e){
			System.out.println(e);
		}	
	}
	
}

