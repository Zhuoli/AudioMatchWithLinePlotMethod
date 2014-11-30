package test;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class ConcurrencyTest {
	
	public static void main(String[] args){
		ConcurrencyTest t = new ConcurrencyTest();
		t.fillContainerWithMultiThreads();
	}
	private void fillContainerWithMultiThreads(){
		System.out.println("fillContainerWithMultiThreads");
		int nThreads=10;
		ExecutorService exec = Executors.newFixedThreadPool(nThreads);
		for(int i=0;i<nThreads;i++){
			exec.execute(new ContainerThread());
		}
		try {
			exec.shutdown();
			exec.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Terminated");
	}
}

class ContainerThread implements Runnable{
	public ContainerThread(){
	}
	@Override
	public void run(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(this);
	}
}
