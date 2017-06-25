package tw.org.iii.jdbc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBC151 {

	public static void main(String[] args) {
		//
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try(Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/brad",prop)){
			
			PreparedStatement pstmt = conn.prepareStatement("select * from member where id = 2");
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			//解序列化
			ObjectInputStream in = new ObjectInputStream(rs.getBinaryStream("obj"));
			Student s3 = (Student)(in.readObject());
			System.out.println("read obj =>"+s3.getID()+":"+s3.total()+":"+s3.avg());
			in.close();
			
			System.out.println("ok");
			
		}catch(Exception e){
			System.out.println(e);
		}	
	}
	
}

