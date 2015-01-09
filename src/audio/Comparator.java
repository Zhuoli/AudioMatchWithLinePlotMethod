package audio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @purpose: comparator containers, 
 */


public class Comparator {
	static double THRESHOLD=3;
	ArrayList<Audio> container1=null;
	ArrayList<Audio> container2=null;
	
	HashMap<Long, String> containerHM=null;
	

	
	// contain files from args1 and files from args3
	public Comparator(String[] files1,String[] files2){
		container1=new ArrayList<Audio>();
		container2=new ArrayList<Audio>();
		fillContainerWithMultiThreads(container1,files1);
		fillContainerWithMultiThreads(container2,files2);
	}
	
	// utilize moltile-core process to fill container faster
	private void fillContainerWithMultiThreads(ArrayList<Audio> container,String[] files){
	      int nrOfProcessors = Runtime.getRuntime().availableProcessors();
	      ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);

	      for(String file : files){
	    	  eservice.execute(new ContainerThread(container,file));
	      }
	      eservice.shutdown();
	      try {
			eservice.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
	}
	
	// compare all the files in each container against each other
	public void compare(){
		//special case when the second container only has 1 file, indicating
		//that we need to check that file against all the files in container1

		int valueSamplesPerSecond = (int) (1/(1-AverageAmplitude.overlapRatio));
		int valuesPerZone=(Client.SongSampleSize*valueSamplesPerSecond);
		for(Audio audio1 : container1){
			for(Audio audio2 : container2){
				if(isMatch(audio1,audio2,valueSamplesPerSecond,valuesPerZone)){
					continue;
				}
			}
		}
	}

	private boolean isMatch(Audio audio1, Audio audio2, int valueSamplesPerSecond, int valuesPerZone) {
		int[] hashvalue1=audio1.getNegativeHashValuePerSecondWithOverlap();
		int[] hashvalue2=audio2.getNegativeHashValuePerSecondWithOverlap();
		for(int array1Start=0;array1Start<hashvalue1.length-valuesPerZone;array1Start++){
			for(int array2Start=0;array2Start<hashvalue2.length-valuesPerZone;array2Start++){
				if(isMathStartHereCompareAngle(1,0,audio1,array1Start,audio2,array2Start,valuesPerZone)){
					System.out.println("MATCH "+audio1.filename+" "+audio2.filename+" "+(array1Start+0.0)/valueSamplesPerSecond+" "+(array2Start+0.0)/valueSamplesPerSecond);
					if(Client.DEBUG){
						String s1=audio1.getFileName()+" "+((array1Start+0.0)/valueSamplesPerSecond)+"s";
						String s2=audio2.getFileName()+" "+((array2Start+0.0)/valueSamplesPerSecond)+"s";
						Audio.drawWaveFile(Arrays.copyOfRange(hashvalue1, array1Start, hashvalue1.length),s1,Arrays.copyOfRange(hashvalue2, array2Start, hashvalue2.length),s2);
					}
					
					return true;
				}
			}
		}


		return false;
	}
	

	// compare algorithm3: compare angle
	private boolean isMathStartHereCompareAngle(int termination,int acculError,Audio audio1,int start1,Audio audio2,int start2,int valuesPerZone){
		int threshold=5;
		int DIFF_ANGLE=30;
		int count=6;
		int[] array1Angles=termination==1?audio1.getPositiveHashValuesAngle():audio1.getNegativeHashValuesAngle();
		int[] array2Angles=termination==1?audio2.getPositiveHashValuesAngle():audio2.getNegativeHashValuesAngle();
		long sum=0;
		int n=Math.min(array1Angles.length-start1, array2Angles.length-start2);
		n=Math.min(n, valuesPerZone);
		if(n<valuesPerZone){
			return false;
		}
		for(int i=0;i<n;i++){
			if(Math.abs(array1Angles[start1+i]-array2Angles[start2+i])>DIFF_ANGLE && --count<=0){
				return false;
			}
			sum+=array1Angles[start1+i]-array2Angles[start2+i];
		}
		acculError+=(int) (sum/n);			
		
		// termination condition
		if(termination==0){
			return acculError<threshold;
		}
		
		if(acculError<threshold){
			int offset=(int) (1/(1-AverageAmplitude.overlapRatio));
			for(int i=start1-offset;i<start1+offset;i++){
				for(int k=start2-offset;k<start2+offset;k++){	
					//error index case
					if(i<0 || k<0){
						continue;
					}
					if(isMathStartHereCompareAngle(0,acculError,audio1,i,audio2,k,valuesPerZone)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
}



class ContainerThread  implements Runnable {
	ArrayList<Audio> container;
	String file;
	public ContainerThread(ArrayList<Audio> container, String file){
		this.container=container;
		this.file=file;
	}
	@Override
	public void run() {
			Audio audio=null;
			audio = Audio.getInstance(file);
			if(audio!=null){
				synchronized(this.container){
					container.add(audio);
				}
			}
	}
}
	