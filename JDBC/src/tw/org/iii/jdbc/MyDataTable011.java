package tw.org.iii.jdbc;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;


public class MyDataTable011 extends JFrame{
	private JTable table;
	private MytableModel model;
	private Connection conn;		 
	private ResultSet rs;
	private int dataCount = 0;
	private String[] fields;
	
	public MyDataTable011(){
		super("客戶資料管理");
		setLayout(new BorderLayout());
		try{
			initDB();
		}catch(Exception e){}
		
		model = new MytableModel();
		table = new JTable(model);
		table.getSelectionModel().addListSelectionListener( (e) -> MyDataTable011.this.whenSelect(e));
		
		JScrollPane jsp = new JScrollPane(table);
		add(jsp , BorderLayout.CENTER);
		
		
		setVisible(true);
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	private class MytableModel extends DefaultTableModel {
		@Override
		public String getColumnName(int column) {
			return fields[column];
		}
		
		@Override
		public int getColumnCount() {
			return fields.length;
		}
		
		@Override
		public int getRowCount() {
			return dataCount;
		}
		
		@Override
		public Object getValueAt(int row, int column) {
			try {
				rs.absolute(row + 1);
				return rs.getString(column + 1);
			} catch (Exception e) {return "---";
			}
			
		}
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
		
		@Override
		public void setValueAt(Object aValue, int row, int column) {
			super.setValueAt(aValue, row, column);
//			System.out.println(row + " : "+ column+" : "+aValue);
		}
	}
	private void whenSelect(ListSelectionEvent e){
		System.out.println(e.getFirstIndex() + "X" + e.getLastIndex());
	}
	
	
	private void initDB() throws Exception{
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		//count row & column
		conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/nba",prop);
		String sql = "select count(*) from players ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		rs.next(); dataCount = rs.getInt(1);
		
		// get Data
		sql = "select * from players order by born asc ";
		pstmt = conn.prepareStatement(sql,
				ResultSet.TYPE_FORWARD_ONLY, 
				ResultSet.CONCUR_UPDATABLE);
		rs = pstmt.executeQuery();
		ResultSetMetaData metadata = rs.getMetaData();
		
		//create data row & column
		fields = new String[metadata.getColumnCount()];

		for(int i = 0; i <= fields.length; i++){
			fields[i] = metadata.getColumnLabel(i + 1);
		}
	}
	
	public static void main(String[] args) {
		new MyDataTable011();
	}

}
