package tw.org.iii.jdbc;

import java.io.Serializable;

public class Student implements Serializable{
	private int ch, eng, math;
	private String id;
	public Student(String id, int ch, int eng, int math){
		this.id = id;this.ch = ch;this.eng = eng;this.math = math;
	}
	
	public int total(){
		return ch + eng + math;
	}
	
	public double avg(){
		return total() / 3.0;
	}
	
	public String getID(){return id;}
}
