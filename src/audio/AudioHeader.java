package audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AudioHeader {
	String fileName;

	String format;
	int sampleRate;
	int bitePerSample;
	int numChannels;
	int audioLength;
	
	
	private AudioHeader(String fileName,String format,int nc,
			int sampleRate,int bps,int audioLength){
		this.fileName=fileName;
		this.format=format;
		this.numChannels=nc;
		this.sampleRate=sampleRate;
		this.bitePerSample=bps;
		this.audioLength=audioLength;
		
	}
	

	// check audio file headline format
	public static AudioHeader getInstance(
			String fileName,byte[] bytes,int datasize) {
		String format="";
		int nc=0;
		int sampleRate;
		int bps;
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		//Check format = wave
		if (byteBuffer.get(8) != 87 || byteBuffer.get(9) != 65 ||
			bytes[10] != 86 || bytes[11] != 69) {
			if (Client.DEBUG) { System.err.print("ERROR: NOT Wave Format"+"  "); }
			return null;		
		}
		format="wav";
		//Check audio format = PCM
		if (byteBuffer.get(20) != 1 || byteBuffer.get(21) != 0) {
			if (Client.DEBUG) { System.err.print("ERROR: NOT PCM"+"  "); }
			return null;
		}
		//Check channels = stereo or Mono
		if (byteBuffer.get(22) == 2 && byteBuffer.get(23) == 0) {
			nc=2;
		}else if(byteBuffer.get(22)==1 && byteBuffer.get(23)==0){
			nc=1;
		}else{
			if(Client.DEBUG){System.err.print(
					"ERROR: Incorrect num of channels"+"  ");}
			return null;
		}
		//Check sample rate is 44.1 kHz
		sampleRate=byteBuffer.getInt(24);
		if(sampleRate!=11025 && sampleRate!=22050 
				&& sampleRate!=44100 && sampleRate!=48000){
			if(Client.DEBUG){
				System.err.print(
						"ERROR: Incorrect sample rate "+sampleRate+"  ");}
			return null;
		}
		//check BitsPerSample is 16
		bps=byteBuffer.getShort(34);
		if (bps != 16 && bps != 8) {
			if (Client.DEBUG) {
				System.out.print(
						"ERROR: Incorrect bites per sample "+ bps+"  ");}
				return null;
		}
		int audioLength=0;
		audioLength=datasize/(sampleRate*nc*(bps/8));
		return new AudioHeader(fileName,format,nc,sampleRate,bps,audioLength);
	}
	//@overwrite
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Filename: "+this.fileName+"\n");
		sb.append("Audio Format: "+this.format+"\n");
		sb.append("Sample Rate: "+this.sampleRate+"\n");
		sb.append("Bites per Second: "+this.bitePerSample+"\n");
		sb.append("Number of Channels: "+this.numChannels+"\n");
		sb.append("Audio length: "+this.audioLength);
		sb.append('\n');
		return sb.toString();
	}
	public String getFileName() {
		return fileName;
	}

	public String getFormat() {
		return format;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public int getBitesPerSecond() {
		return bitePerSample;
	}

	public int getNumChannels() {
		return numChannels;
	}

	public int getAudioLength() {
		return audioLength;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
