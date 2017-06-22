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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/*3/27
 * 陽春版的檔案總管 
 * 有開啟文字檔  儲存檔案 離開 三種基本功能
 */
public class FileManager extends JFrame{
	//按鈕
	private JButton open, save, exit;
	//文字區塊
	private JTextArea ta;
	private JScrollPane js;//圖像太大的話 設定視窗可以滾動
	
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
		js = new JScrollPane(ta, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//上方區塊  置入三個按鈕
		JPanel top = new JPanel(new FlowLayout());
		top.add(open); top.add(save); top.add(exit);
		add(top, BorderLayout.NORTH);
		add(js, BorderLayout.CENTER);
		
		//設定TextArea顏色  亮灰色
		ta.setBackground(Color.LIGHT_GRAY);
		//設定文字不超出視窗外  超過會自動換行
		ta.setLineWrap(true);
		//設定顯示的字體  字型大小
		ta.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		//設定視窗大小  可見  右上關閉按鈕
		setSize(800, 640);
		setLocation(500, 250);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	//建立一個開啟檔案的方法
	public void openFile(String file) {

        try {
        	ta.setText("");
            String lineSeparator = System.getProperty("line.separator");
			ta.append(file);
            ta.append(lineSeparator);
        }   
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(),"無法開啟檔案!", JOptionPane.ERROR_MESSAGE);
        }     
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
