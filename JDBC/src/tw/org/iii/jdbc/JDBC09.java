package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

public class JDBC09 {

	public static void main(String[] args) {
		//
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try(Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/brad",prop)){
			
			Statement stmt = conn.createStatement();
			String account = "brad", pw =" 123";
			HashMap<String, String> result;
			if((result = checkMember(stmt, account, pw)) !=null){
				System.out.println("Welcom, " + result.get("realname"));
			}else{
				System.out.println("Error Login");
			}
			System.out.println("ok");
			
		}catch(Exception e){
			System.out.println(e);
		}	
	}
	
	static HashMap<String, String> checkMember(Statement stmt, String account, String pw) throws Exception{
		HashMap<String, String>  ret = new HashMap<String, String>();
		
		String sql = "select * from member where account = '"+
						account+"' and pw = '"+pw+"' ";
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()){
			//right person
			ret.put("id",rs.getString(1));
			ret.put("account",rs.getString(2));
			ret.put("pw",rs.getString(3));
			ret.put("realname",rs.getString(4));
			
		}else{
			//error account
			return null;
		}
		return ret;
	}
}

