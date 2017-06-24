package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.json.JSONStringer;
import org.json.JSONWriter;

public class JDBC08 {

	public static void main(String[] args) {
		//
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try(Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/brad",prop)){
			
			Statement stmt = conn.createStatement();
			System.out.println(isDataRepeat("amy", stmt));
			
			String account = "rog43", pw =" 7777", realname = "ed lo";
			if(!isDataRepeat(account, stmt)){
				String sql = "insert into member (account, pw, realname)"+
						" values ('"+account+"','"+pw+"','"+realname+"')";
			int n = stmt.executeUpdate(sql);
			System.out.println(n);
			}else{
				System.err.println("帳號重複了!");
			}
			
			System.out.println("ok");
			
		}catch(Exception e){
			System.out.println(e);
		}	
	}
	
	
	static boolean isDataRepeat(String account, Statement stmt) throws Exception{
		String sql = "select count(*) from member where account = '" + account +"'";
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()){
			int num = rs.getInt(1);
			if(num > 0) return true;
			else return false;
		}else{
			throw new Exception("SQL error");
		}
		
	}
}
