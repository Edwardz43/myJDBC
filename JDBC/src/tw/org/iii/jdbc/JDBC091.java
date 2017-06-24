package tw.org.iii.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

public class JDBC091 {

	public static void main(String[] args) {
		//
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try(Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/brad",prop)){
			
			Statement stmt = conn.createStatement();
			String account = "brad", pw ="123";
			Member loginMember;
			if((loginMember = checkMember(stmt, account, pw)) !=null){
				System.out.println("Welcom, " + loginMember.realname);
			}else{
				System.out.println("Error Login");
			}
			System.out.println("ok");
			
		}catch(Exception e){
			System.out.println(e);
		}	
	}
	
	static Member checkMember(Statement stmt, String account, String pw) throws Exception{
		String sql = "select * from member where account = '"+
						account+"' and pw = '"+pw+"' ";
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()){
			//right person
			Member member = new Member(
					rs.getString(1), rs.getString(2), rs.getString(4));
			return member;
		}else{
			//error account
			return null;
		}
		
	}
}

class Member {
	String id, account, realname;
	Member(String id, String account, String realname){
		this.id = id;this.account = account;this.realname = realname;
	}
}
