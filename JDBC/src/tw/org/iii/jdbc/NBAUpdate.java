package tw.org.iii.jdbc;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class NBAUpdate extends JFrame{
	private JButton updateTeam, updatePlayer;
	private int state = 0;
	private static int teamCount = 0;
	private static int totalTeam = 30;
	private static int playerCount = 0;
	private static int totalPlayer = 450;
	private static MyCanvas myCanvas;
	private static int progressBar = 0;
	private Timer timer;
	private UpdateTeam ut;
	private UpdatePlayer up;
	private long startTime; 
	private double estTime;
	
	public NBAUpdate(){
		super("NBA Updater");
		setLayout(new BorderLayout());
		
//		ut = new UpdateTeam();
		Thread tUpdateTeam = new Thread(ut);
//		up = new UpdatePlayer();
		Thread tUpdatePlayer = new Thread(up);
		
		updateTeam= new JButton("更新球隊資料");updatePlayer= new JButton("更新球員資料");
		
		JTabbedPane bottom = new JTabbedPane();
		bottom.add(updateTeam);bottom.add(updatePlayer);
		myCanvas = new MyCanvas();
		
		updateTeam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(state == 0 || state == 3){
					startTime = System.currentTimeMillis();
					tUpdateTeam.start();
					state = 1;
				} 
			}
		});
		
		updatePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(state == 0 || state == 3){
					startTime = System.currentTimeMillis();
					tUpdatePlayer.start();
					state = 2;
				} 
			}
		});
		
		add(myCanvas, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		timer = new Timer();
		timer.schedule(new ViewTask(), 0, 20);
		setSize(640, 480);
		setLocation(getWidth()/2, getHeight() / 2);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	class ViewTask extends TimerTask{
		@Override
		public void run() {
			if(state == 1){
				teamCount = ut.teamCount; 
				totalTeam = ut.totalTeam;
				progressBar = (int)(teamCount*100/totalTeam);
				estTime = computrTime(teamCount, totalTeam);
//				System.out.println("progressBar :" + progressBar);
				myCanvas.repaint();	
				if(teamCount == 30) done();
			} 
			else{
				playerCount = up.playerCount; 
				totalPlayer = up.totalPlayer;
				progressBar = (int)(playerCount*100/totalPlayer);
				estTime = computrTime(playerCount, totalPlayer);
//				System.out.println("progressBar :" + progressBar);
				myCanvas.repaint();	
				if(playerCount == totalPlayer) done();
			}
			
		}
	}
	
	class  MyCanvas extends JPanel{
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.clearRect(0, 0, 640, 480);
			g2d.fillRect(0, 0, 640, 480);
			
			if(state == 1 || state == 2){
				g2d.setColor(Color.white);
				g2d.setFont(new Font("Serif", Font.BOLD, 26));
				g2d.drawString(progressBar+"%", 295, 180);
				g2d.setColor(Color.gray);
				g2d.drawRect(98, 198, 443, 23);
				g2d.setColor(Color.BLUE);
				g2d.fillRect(100, 200, 440*progressBar/100, 20);
				g2d.setColor(Color.white);
				g2d.setFont(new Font("Serif", Font.BOLD, 14));
				if(teamCount > 0 || playerCount > 0)g2d.drawString("預估完成時間 : " +
						(((int)estTime < 1)?"小於1":(int)estTime) + "分鐘", 235, 250);
			}
			if(state == 3){
				g2d.setColor(Color.red);
				g2d.setFont(new Font("Serif", Font.BOLD, 26));
				g2d.drawString("更新完成!", 255, 180);
			}
		}
	}
	
	public double computrTime(int x, int y){
		double temp = (x == 0)? 0 :(y - x) / (double)x *(System.currentTimeMillis() - startTime)/60000;
//		System.out.println((int)estTime);
		return temp;
	}
	
	public void done(){
		this.state = 3;
	}
	
	public static void main(String[] args) {
		new NBAUpdate();
	}

}