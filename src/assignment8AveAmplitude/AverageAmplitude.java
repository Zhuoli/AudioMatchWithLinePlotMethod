package assignment8AveAmplitude;

// average amplitudes class, line plot data array
public class AverageAmplitude {
	// overlap ratio of each sample interval
	public static double overlapRatio=0.9;
	// length of time interval to compute average amplitude
	static int HsashvalueInterval=1;
	int[] monoChannel=null;
	int[] PostiveHashValuePerInterval=null;
	int[] NegativeHashValuePerInterval=null;
	int[] postiveAngles=null;
	int[] negativeAngles=null;
	public int[] getPostiveAngles() {
		if(postiveAngles==null){
			postiveAngles=calculateAngles(PostiveHashValuePerInterval,0);
		}
		return postiveAngles;
	}
	public int[] getNegativeAngles(){
		if(negativeAngles==null){
			negativeAngles=calculateAngles(NegativeHashValuePerInterval,0);
		}
		return negativeAngles;
	}

	public int[] getPostiveHashValuePerInterval() {
		return PostiveHashValuePerInterval;
	}

	public int[] getNegativeHashValuePerInterval() {
		return NegativeHashValuePerInterval;
	}
	// given audio samples data and audio header
	//  
	public AverageAmplitude(int[][] data, AudioHeader header){
		int valuesPerSecond=(int) (1/(1-overlapRatio));
		int audioSamplesPerInterval=HsashvalueInterval*header.sampleRate;
		int offset=(int) (header.sampleRate*(1-overlapRatio));
		
		this.PostiveHashValuePerInterval=
				new int[(header.audioLength-HsashvalueInterval)*valuesPerSecond];
		this.NegativeHashValuePerInterval=
				new int[(header.audioLength-HsashvalueInterval)*valuesPerSecond];
		this.monoChannel=convert2monochannel(data);
		for(int i=0;i<PostiveHashValuePerInterval.length;i++){
			long posAccumul=0;
			long negAccumul=0;
			int start=i*offset;
			for(int k=start;k<start+audioSamplesPerInterval;k++){
				if(this.monoChannel[k]>0){
					posAccumul+=this.monoChannel[k];
				}else{
					negAccumul+=Math.abs(this.monoChannel[k]);
				}
			}
			PostiveHashValuePerInterval[i]=
					(int) (posAccumul/audioSamplesPerInterval);
			NegativeHashValuePerInterval[i]=
					(int) (negAccumul/audioSamplesPerInterval);
		}
	}
	// calculate angles of this data array.
	private int[] calculateAngles(int[] array, int start) {
		int deltX=(int)(500*(1-AverageAmplitude.overlapRatio));
		int[] angles=new int[array.length-1];
		for(int i=start;i<array.length-1;i++){
			int deltY=array[i+1]-array[i];
			angles[i]=getAngel(Math.atan(deltY/(0.0+deltX)));
		}
		return angles;
	}
	public  int getAngel(double angle){
		return (int) (angle*180/Math.PI);
	}
	// convert canonical channels to monochannel
	private int[] convert2monochannel(int[][] data){
		int numChannel=data.length;
		int[] monoChannel=new int[data[0].length];
		for(int i=0;i<data[0].length;i++){
			int ave=0;
			for(int ch=0;ch<numChannel;ch++){
				ave+=data[ch][i];
			}
			monoChannel[i]=ave/numChannel;
		}
		return monoChannel;
	}
	
}
