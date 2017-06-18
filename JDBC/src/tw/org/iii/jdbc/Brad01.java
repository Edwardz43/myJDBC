package tw.org.iii.jdbc;

public class Brad01 {

	public static void main(String[] args) {
		p1 obj = new Brad011();
		Brad011 obj2 = (Brad011) obj;
		
		
	}
}


interface p1 {
	void m1();
}


class Brad011 implements p1{
	public void m1(){}
	void m2(){}
}