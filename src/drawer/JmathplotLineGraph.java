package drawer;


import javax.swing.*;

import org.math.plot.*;



/**
 * using third party library: jmathplot.jar 
 * scientific graph drawer
 * @author zhuoli
 *
 */
public class JmathplotLineGraph {	// TEST
	static String audioPath="A5/D2/z08.wav";
	
	public static void main(String[] args) {	
	}
	
	
	
	public static void plot2d(String title,double[] x,double[] y) {


        // create your PlotPanel (you can use it as a JPanel)
        Plot2DPanel plot = new Plot2DPanel();

        plot.setFixedBounds(1, -250, 250);
        // define the legend position
        plot.addLegend("NORTH");
        plot.addLinePlot(title, x, y);
        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("title");
        frame.setSize(1200, 900);
        frame.setContentPane(plot);
        frame.setVisible(true);

	}
	public static void plot2d(String title,double[] y) {

		double[] x = new double[y.length];
		for(int i=0;i<y.length;i++){
			x[i]=i;
		}
        // create your PlotPanel (you can use it as a JPanel)
        Plot2DPanel plot = new Plot2DPanel();
        plot.setAdjustBounds(false);
        // define the legend position
        plot.addLegend("NORTH");
        plot.addLinePlot(title, x, y);
        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("title");
        frame.setSize(1200, 900);
        frame.setContentPane(plot);
        frame.setVisible(true);

	}

}
