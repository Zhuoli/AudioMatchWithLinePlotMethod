package test;

public class atanTest {
	public static void main(String[] args){
		double thirty=Math.PI/6;
		System.out.println("thirty: "+thirty);
		System.out.println(Math.sin(thirty));
		System.out.println(Math.tan(thirty));
		System.out.println(getAngel(Math.atan(2)));
	}
	
	public static double getAngel(double angle){
		return angle*180/Math.PI;
	}
}
