package tw.org.iii.jdbc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*3/27
 * 陽春版的檔案總管 
 * 有開啟文字檔  儲存檔案 離開 三種基本功能
 */
public class FileManager extends JFrame{
	//按鈕
	private JButton open, save, exit;
	//文字區塊
	private JTextArea ta;
	
	//本體建構式
	public FileManager(){
		super("檔案總管");
		setLayout(new BorderLayout());
		
		//設定三個主要按鈕
		open = new JButton("open");
		//建立事件傾聽者ActionListener  用來開起按鈕功能
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//開啟檔案
//				openFile();
			}
		});
		save = new JButton("save");
		//同上
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//儲存檔案
				saveFile();
			}
		});
		exit = new JButton("exit");
		//同上
		exit.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				//"解散"視窗
				dispose();
			}
		});
		
		//設置各種元件
		//TextArea
		ta = new JTextArea();
		//上方區塊  置入三個按鈕
		JPanel top = new JPanel(new FlowLayout());
		top.add(open); top.add(save); top.add(exit);
		add(top, BorderLayout.NORTH);
		add(ta, BorderLayout.CENTER);
		
		//設定TextArea顏色  亮灰色
		ta.setBackground(Color.LIGHT_GRAY);
		//設定文字不超出視窗外  超過會自動換行
		ta.setLineWrap(true);
		//設定顯示的字體  字型大小
		ta.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		//設定視窗大小  可見  右上關閉按鈕
		setSize(800, 640);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	//建立一個開啟檔案的方法
	public void openFile(String file) {
		//new 一個filechooser的實體  來執行開啟檔案
//		JFileChooser fileChooser = new JFileChooser();
//	    int option = fileChooser.showDialog(null, null);   
//	    // 選擇"開啟"  就讀取檔案
//	    if(option == JFileChooser.APPROVE_OPTION) {
	        try {
	        	//每次開啟新檔前 先重置TextArea 不然會重複
	        	ta.setText("");
	        	//上課用到的觀念   用buf來讀取filechooser選到的檔案
//	            BufferedReader br = new BufferedReader(
//	            		new FileReader(fileChooser.getSelectedFile()));
	            //和上課時不同的  TextArea要多加一個line separator來讀取換行符號 然後輸出到textarea 不然無法換行
	            String lineSeparator = System.getProperty("line.separator");
	            //輸出
				ta.append(file);
                ta.append(lineSeparator);
		
	            //關水管
//				br.close();
	        }   
	        catch(Exception e) {
	            JOptionPane.showMessageDialog(null, e.toString(),"無法開啟檔案!", JOptionPane.ERROR_MESSAGE);
	        }
//	    }        
	}
	
	//建立一個儲存的方法
	public void saveFile(){
		try {
			// 跟上面一樣 打開filechooser選單
			JFileChooser fileChooser = new JFileChooser();
		    int option = fileChooser.showSaveDialog(null);
		    //如果選"儲存" 就存檔
		    if(option == JFileChooser.APPROVE_OPTION) {
		        // 選擇要儲存的檔案
		        File file = fileChooser.getSelectedFile();
		        // 進行檔案儲存
		        BufferedWriter br =new BufferedWriter(new FileWriter(file));
		        // 將TextArea的文字寫入檔案
		        br.write(ta.getText());
		        br.close();
		    }
		}catch(IOException e) {
	        JOptionPane.showMessageDialog(null, e.toString(),"無法儲存檔案!", JOptionPane.ERROR_MESSAGE);
		}
	}
}
