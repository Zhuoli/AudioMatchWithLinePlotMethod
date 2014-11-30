package test;



public class FFTTest {
	static int count=0;
	public static void main(String[] args){
		int i = 1732;
		System.out.println(round(i,100));
	}
	
	public static int round(int number, int digits){
		int roundedNumber = (number + 5*digits) / 1000 * 1000;
		return roundedNumber;
	}
}
class Pair{
	int x;
	int y;
	public Pair(int x,int y){
		this.x=x;
		this.y=y;
	}
	@Override
	public int hashCode(){
		return (this.x+this.y);
	}
	@Override
	public boolean equals(Object p){
		if(p instanceof Pair){
			Pair o=(Pair)p;
			if(this.x==o.x && this.y==o.y){
				return true;
			}
		}
		return false;
	}
}