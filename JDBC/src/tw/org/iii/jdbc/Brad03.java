package tw.org.iii.jdbc;

public class Brad03 {

	public static void main(String[] args) {
		MyRunnable mr = new MyRunnable();
		Thread t1 = new Thread(mr);
		t1.start();
		
		//--------------------
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < 10; i++){
					System.out.println(i);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}; 
		
		Thread t2 = new Thread(r1);
		t2.start();
		//----------------
		
		Runnable r2 = () -> {
			for(int i = 0; i < 10; i++){
				System.out.println(i);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		Thread t3 = new Thread(r2);
		t3.start();
		
		new Thread(() -> System.out.println("ok")).start();
	}
}

class MyRunnable implements Runnable{

	@Override
	public void run() {
		for(int i = 0; i < 10; i++){
			System.out.println(i);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
