package assignment8AveAmplitude;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import plot.Plot;
import plot.PlotFrame;

//
// Audio class
public  class Audio {
	public String filename = null;
	private int[][] data = null;
	public int[][] getData() {
		return data;
	}

	private AverageAmplitude AverageAmplitude=null;

	AudioHeader header=null;
	private BufferedInputStream bis = null;
	// Audio header information
	public AudioHeader getHeader(){
		return this.header;
	}
	// 
	public int[] getPostiveHashValuePerSecondWithOverlap(){
		return AverageAmplitude.getPostiveHashValuePerInterval();
	}
	public int[] getNegativeHashValuePerSecondWithOverlap(){
		return AverageAmplitude.getNegativeHashValuePerInterval();
	}
	public int[] getPositiveHashValuesAngle(){
		return AverageAmplitude.getPostiveAngles();
	}
	public int[] getNegativeHashValuesAngle(){
		return AverageAmplitude.getNegativeAngles();
	}
	// Return instance of Audio
	public static Audio getInstance(String filePath){

		Audio audio=null;
		String fileCanonicalPath = 
				Convert2StandardFormat.Convert2CanonicalFormat(filePath);
		if(fileCanonicalPath==null){
			return null;
		}
		String fileName=getActualName(filePath);
		FileInputStream fis=null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(fileCanonicalPath);
			bis = new BufferedInputStream(fis);
			audio = new Audio(bis,fileCanonicalPath,fileName);
		} catch (Exception e) {
			if(Assignment8.DEBUG){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			audio=null;
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (fis != null)
					fis.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if(!filePath.equals(fileCanonicalPath)){
			removeCanonicalPath(fileCanonicalPath);
		}
		return audio;
	}
	// remove the temporary file 
	private static void removeCanonicalPath(String fileCanonicalPath) {
		Process p=null;
		try {
			String str="rm "+fileCanonicalPath;
			p =java.lang.Runtime.getRuntime().exec(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized(p){
			try {
				p.wait(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	// trime  the path and get the file name
	private static String getActualName( String filePath){
		String[] strs = filePath.split("/");
		String fileName=strs[strs.length-1];
		return fileName;
	}
	
	public Audio(BufferedInputStream bis, String fileCanonicalPath, String fileName) {
		this.bis=bis;
		this.filename = fileName;
		byte[] fileArray=readFile2ByteArray(new File(fileCanonicalPath));
		byte[] dataheader=Arrays.copyOfRange(fileArray, 0, 44);
		this.header = AudioHeader.getInstance(fileName,dataheader,fileArray.length-44);
		
		readString(WaveConstants.LENCHUNKDESCRIPTOR);
		readLong();
		readString(WaveConstants.LENWAVEFLAG);

		readString(WaveConstants.LENFMTSUBCHUNK);
		readLong();
		readInt();
		readInt();
		readLong();
		readLong();
		readInt();
		readInt();
		readString(WaveConstants.LENDATASUBCHUNK);
		readLong();


		this.data = new int[header.numChannels][(fileArray.length-44)/(header.numChannels*header.bitePerSample/8)];

		// read channel data
		for (int i = 0; i < this.data[0].length; ++i) {
			for (int n = 0; n < header.getNumChannels(); ++n) {
				if (header.bitePerSample == 8) {
					try {
						this.data[n][i] = bis.read();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (header.bitePerSample == 16) {
					this.data[n][i] = this.readInt();
				}
//				System.out.println(this.data[n][i] );
			}
		}
		AverageAmplitude=new AverageAmplitude(this.data,header);
	}


	
	private int readInt() {
		byte[] buf = new byte[2];
		int res = 0;
		try {
			if (bis.read(buf) != 2)
				throw new IOException("no more data!!!");
			res = (buf[0] & 0x000000FF) | (((int) buf[1]) << 8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	private long readLong() {
		long res = 0;
		try {
			long[] l = new long[4];
			for (int i = 0; i < 4; ++i) {
				l[i] = bis.read();
				if (l[i] == -1) {
					throw new IOException("no more data!!!");
				}
			}
			res = l[0] | (l[1] << 8) | (l[2] << 16) | (l[3] << 24);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	private String readString(int len) {
		byte[] buf = new byte[len];
		try {
			if (bis.read(buf) != len)
				throw new IOException("no more data!!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(buf);
	}
	// read file to array
	static byte[] readFile2ByteArray(File file){
		byte[] fileArray=null;
		try{
			InputStream inputStream = new FileInputStream(file);
			fileArray = new byte[(int)file.length()];
			inputStream.read(fileArray, 0, fileArray.length);
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File not found");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} 
		

		return fileArray;
	}
	public String getFileName() {
		return this.header.getFileName();
	}
	//  Draw methods all used for debugging 
	public static void drawWaveFile(Audio reader1, Audio reader2) {
		//
		String[] pamss = new String[] { "-r", "-g", "-b" };
			PlotFrame frame = Plot.figrue(String.format("%s %s %dHZ %dBit %dCH", "negative "+reader1.getFileName(),reader2.getFileName(), reader1.header.getSampleRate(), reader1.header.getBitesPerSecond(), reader1.header.getNumChannels()));
			frame.setSize(500, 200);
			Plot.hold_on();
			Plot.plot(Integers2Doubles(reader1.getPostiveHashValuePerSecondWithOverlap()), pamss[0]);
			Plot.plot(Integers2Doubles(reader2.getPostiveHashValuePerSecondWithOverlap()), pamss[1]);
			Plot.hold_off();
		
			pamss = new String[] { "-r", "-g", "-b" };
			frame = Plot.figrue(String.format("%s %s %dHZ %dBit %dCH","postive "+ reader1.getFileName(),reader2.getFileName(), reader1.header.getSampleRate(), reader1.header.getBitesPerSecond(), reader1.header.getNumChannels()));
			frame.setSize(500, 200);
			Plot.hold_on();
			Plot.plot(Integers2Doubles(reader1.getNegativeHashValuePerSecondWithOverlap()), pamss[0]);
			Plot.plot(Integers2Doubles(reader2.getNegativeHashValuePerSecondWithOverlap()), pamss[1]);
			Plot.hold_off();

	}
	// ??????????????????
	public static void drawWaveFile(int[] data1,String name1, int[] data2, String name2) {
//		System.out.println();
//		System.out.println(name1+":"+name2);
//		for(int v : data1){
//			System.out.format("%9d", v);
//		}
//		System.out.println();
//		for(int v : data2){
//			System.out.format("%9d", v);
//		}
//		System.out.println();
//		int n= Math.min(data1.length, data2.length);
//		for(int i=0;i<n;i++){
//			int rate=data1[i]-data2[i];
//			System.out.format(" %4d ", rate);
//		}
//		System.out.println();
		String[] pamss = new String[] { "-r", "-g", "-b" };
			PlotFrame frame = Plot.figrue(name1+" "+name2);
			frame.setSize(500, 200);
			Plot.hold_on();
			Plot.plot(Integers2Doubles(data1), pamss[0]);
			Plot.plot(Integers2Doubles(data2), pamss[1]);
			Plot.hold_off();

	}
	
	// ??????????????????
	public static void drawRawData(int[] data1,String name1, int[] data2, String name2) {
		int N=Math.min(data1.length, data2.length);
		long dif = 0;
		for(int i=0;i<N;i++){
			dif+=Math.abs(data1[i]-data2[i]);
		}
		System.out.println("raw data differ MSE: "+dif/N);
		String[] pamss = new String[] { "-r", "-g", "-b" };
			PlotFrame frame = Plot.figrue(name1+" "+name2);
			frame.setSize(500, 200);
			Plot.hold_on();
			Plot.plot(Integers2Doubles(data1), pamss[0]);
			Plot.plot(Integers2Doubles(data2), pamss[1]);
			Plot.hold_off();

	}
	// ??????????????????
	public static void drawWaveFile(Audio audio) {
		//
		String[] pamss = new String[] { "-r", "-g", "-b" };
		    PlotFrame  frame = Plot.figrue(String.format("%s %dHZ %dBit %dCH", audio.getFileName(),  audio.header.getSampleRate(),audio.header.getBitesPerSecond(), audio.header.getNumChannels()));
			frame.setSize(500, 200);
			Plot.hold_on();
			for (int i = 0; i < audio.header.numChannels; ++i) {
				// ??????i????????????
				int[] data = audio.getData()[i];
				// ??????
				Plot.plot(Integers2Doubles(data), pamss[i % pamss.length]);
			}
			Plot.hold_off();
	}
	// int ?????? ????????? double??????
	// JavaPlot ?????????double???????????????
	public static double[] Integers2Doubles(int[] raw) {
		double[] res = new double[raw.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = raw[i];
		}
		return res;
	}

	public static void main(String[] args){
		Audio reader1 = Audio.getInstance("A6/D4/MMw.wav");
		Audio reader2 = Audio.getInstance("A6/D5/Piste1.wav");
		
		drawWaveFile(reader1,reader2);
//		drawRawData(reader1.getData()[1],"",reader2.getData()[1],"");
	}


}
