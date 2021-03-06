package tw.org.iii.jdbc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class NBAUpdate_v3 extends JFrame{
	private JButton updateTeam, updatePlayer, updateCareer;
	private int state = 0;
	private static int teamCount = 0;
	private static int totalTeam = 30;
	private static int playerCount = 0;
	private static int totalPlayer = 450;
	private static MyCanvas myCanvas;
	private static int progressBar = 0;
	private Timer timer;
	private UpdateTeam ut;
	private int teamNumber = 6;
	private UpdatePlayer_v2[] up = new UpdatePlayer_v2[teamNumber];
	private Thread tUpdatePlayer[] = new Thread[teamNumber];
	private UpdateCareer[] uc = new UpdateCareer[4];
	private Thread tUpdateCareer[] = new Thread[4];
	private long startTime; 
	private double estTime;
	
	public NBAUpdate_v3(){
		super("NBA Updater");
		setLayout(new BorderLayout());
		
		ut = new UpdateTeam();
		Thread tUpdateTeam = new Thread(ut);
		for(int i = 0; i < up.length; i ++){
			UpdatePlayer_v2 upi = new UpdatePlayer_v2(i);
			up[i] = upi;			
			tUpdatePlayer[i] = new Thread(upi);
		}
		for(int i = 0; i < uc.length; i ++){
			UpdateCareer uci = new UpdateCareer(i);
			uc[i] = uci;			
			tUpdateCareer[i] = new Thread(uci);
		}
		
		updateTeam= new JButton("更新球隊資料");
		updatePlayer= new JButton("更新球員資料");
		updateCareer= new JButton("更新球員生涯數據");
		
		JTabbedPane bottom = new JTabbedPane();
		bottom.add(updateTeam);
		bottom.add(updatePlayer);
		bottom.add(updateCareer);
		myCanvas = new MyCanvas();
		
		updateTeam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(state == 0 || state == 4){
					startTime = System.currentTimeMillis();
					tUpdateTeam.start();
					state = 1;
				} 
			}
		});
		
		updatePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(state == 0 || state == 4){
					startTime = System.currentTimeMillis();
					for(int i = 0; i < tUpdatePlayer.length; i++)
						tUpdatePlayer[i].start();
					state = 2;
				} 
			}
		});
		
		updateCareer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(state == 0 || state == 4){
					startTime = System.currentTimeMillis();
					for(int i = 0; i < tUpdateCareer.length; i++)
						tUpdateCareer[i].start();
					state = 3;
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
				
				if(teamCount == 30) done();
				
			} else if (state == 2){
				playerCount = totalPlayer = 0;
				for(int i = 0; i < up.length; i ++){
					playerCount += up[i].playerCount; 
					totalPlayer += up[i].totalPlayer;
				}
				progressBar = (int)(playerCount*100/totalPlayer);
				estTime = computrTime(playerCount, totalPlayer);
//				System.out.println("progressBar :" + progressBar);
				
				if(playerCount == totalPlayer) done();
				
			}else if(state == 3){
				playerCount =  0;
				totalPlayer = uc[0].totalPlayer;
				for(int i = 0; i < uc.length; i ++){
					playerCount += uc[i].playerCount; 
				}
				progressBar = (int)(playerCount*100/(totalPlayer = totalPlayer == 0 ? 1 : totalPlayer));
				estTime = computrTime(playerCount, totalPlayer);
//				System.out.println("progressBar :" + progressBar);
					
				if(playerCount == totalPlayer) done();
			}
			myCanvas.repaint();
		}
	}
	
	class  MyCanvas extends JPanel{
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.clearRect(0, 0, 640, 480);
			g2d.fillRect(0, 0, 640, 480);
			
			if(state == 1 || state == 2 || state == 3){
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
			if(state == 4){
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
		this.state = 4;
	}
	
	public static void main(String[] args) {
		new NBAUpdate_v3();
	}

}