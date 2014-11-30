package drawer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

import assignment7.AudioSpectrogram;

public class SpectrogramDrawer {
    public static void main(String[] args) { 
    	BufferedImage  img =new BufferedImage(255,255,BufferedImage.TYPE_INT_BGR);
    	for(int i=0;i<255;i++){
    		for(int j=1;j<255;j++){
	    			int col=i<<16|j<<8;
	    			img.setRGB(i, j, col);
    		}
    	}
    	File f = new File("Spectrogram.png");
    	try {
			ImageIO.write(img, "PNG", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    
//    public static void drawSpectrogram(String fileName, AudioSpectrogram spectrogram){
//    	int interval=1;
//    	int[][] data=spectrogram.getArray2D();
//    	System.out.println("Height: "+data.length/interval);
//    	System.out.println("Width: "+data[0].length);
//
//
//        
//    	for(int i =0;i<data.length;i+=interval){
//    		for(int j=0;j<data[0].length;j++){
//    			int color = data[i][j];
//    			System.out.format("%4d ",color);
//    		}
//    		System.out.println();
//    	}
//
//    }
    
//    public static void drawSpectrogram(String fileName,int[][] data){
//    	if(fileName.contains(".")){
//    		fileName=fileName.substring(0,fileName.indexOf('.'));
//    	}
//    	int frequencyRange=1000;
//    	int audioLength=data[0].length;
//    	System.out.println("Height: "+frequencyRange+", width:"+audioLength);
//    	BufferedImage  img =new BufferedImage(audioLength,frequencyRange,BufferedImage.TYPE_INT_BGR);
//    	for(int i=0;i<audioLength;i++){
//    		for(int j=0;j<frequencyRange;j++){
//    			int col=data[j][i];
//    		
////    			col=16711680;
//    			//  RED GREEN BLUE
//    			img.setRGB(i, j,col);
//    		}
//    	}
//    	File f = new File(fileName+"Spectrogram.png");
//    	try {
//			ImageIO.write(img, "PNG", f);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }




	public static void drawSpectrogram(String fileName, assignment8.AudioSpectrogram spectrogram) {
    	if(fileName.contains(".")){
    		fileName=fileName.substring(0,fileName.indexOf('.'));
    	}
    	int[][] data=spectrogram.getArray2D();
    	int frequencyRange=19981;
    	int audioLength=data[0].length;
    	System.out.println("Height: "+frequencyRange+", width:"+audioLength+'\n');
    	BufferedImage  img =new BufferedImage(audioLength,frequencyRange,BufferedImage.TYPE_INT_BGR);
    	for(int i=0;i<audioLength;i++){
    		for(int j=0;j<frequencyRange;j++){
    			int col=data[j][i];
    		
//    			col=16711680;
    			//  RED GREEN BLUE
    			img.setRGB(i, j,col);
    		}
    	}
    	writePeak(img,spectrogram.getLocalPeaks(),AudioSpectrogram.getOVERLAP_RATIO());
    	
    	File f = new File(fileName+"Spectrogram.png");
    	try {
			ImageIO.write(img, "PNG", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	private static void writePeak(BufferedImage img, String[] localPeaks,
			double overlapRatio) {
		int n=(int) (1/overlapRatio);
		for(String s : localPeaks){
			String[] strs =s.split(":");
			int row=Integer.parseInt(strs[1]);
			int col=Integer.parseInt(strs[0])*n;
			col=(int) (col/(1000*(1-AudioSpectrogram.getOVERLAP_RATIO())));
			drawCircle(row,col,img);
		}
	}




	private static void drawCircle(int row, int col, BufferedImage img) {
	      double PI = 3.1415926535;
	      double i, angle;
	      int x1, y1;
	      int r =7;

	      for(i = 0; i < 360; i += 0.1)
	      {
	            angle = i;
	            x1 = (int) (r * Math.cos(angle * PI / 180));
	            y1 = (int) (r * Math.sin(angle * PI / 180));
	            if(row+y1<0 || row+y1>=img.getHeight() || x1+col<0 || x1+col>=img.getWidth()){
	            	continue;
	            }

    			img.setRGB(col+x1, row+y1, Color.RED.getRGB());
	      }
		
	}
}
