package autozoomSound;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Zoom_ {

	Robot robot;
	int zoomFactor = 2;
	PointerInfo pi;
	JPanel gui;
	JLabel output;
	Timer t;

	public Zoom_() throws AWTException {
		robot = new Robot();
		gui = new JPanel(new BorderLayout(2, 2));
		output = new JLabel("Point to zoom");
		gui.add(output, BorderLayout.PAGE_END);
		final int size = 256;
		final BufferedImage bi = new BufferedImage(
				size, size, BufferedImage.TYPE_INT_RGB);
		final JLabel zoomLabel = new JLabel(new ImageIcon(bi));
		gui.add(zoomLabel, BorderLayout.CENTER);

		MouseListener factorListener = new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if(zoomFactor == 2) {
					zoomFactor = 4;
				} else if (zoomFactor == 4) {
					zoomFactor = 8;	
				} else if (zoomFactor == 8) {
					zoomFactor = 2;
				}
				showInfo();
			}
		};	
		
		zoomLabel.addMouseListener(factorListener);

		ActionListener zoomListener = (ActionEvent e) -> {
			pi = MouseInfo.getPointerInfo();
			Point p = pi.getLocation();
			Rectangle r = new Rectangle (
				p.x - (size / (2 * zoomFactor)),
				p.y - (size / (2 * zoomFactor)),
				(size / zoomFactor),
				(size / zoomFactor));
				BufferedImage temp = robot.createScreenCapture(r);
				Graphics g = bi.getGraphics();
				g.drawImage(temp, 0, 0, size, size, null);
				g.setColor(new Color(255, 0, 0));
				int x = (size/2)-1;
				int y = (size/2)-1;
				zoomLabel.repaint();
				showInfo();
			};
			t = new Timer(40, zoomListener);
			t.start();
		}

		public void stop() {
			t.stop();
		}

		public Component getGui() {
			return gui;
		}

		public void showInfo() {
			pi = MouseInfo.getPointerInfo();
			output.setText("Zoom" + zoomFactor + " Point:" + pi.getLocation());
		}

		public static void main(String[] args) {
			Runnable r = () -> {
				try { 
					final Zoom_ zm = new Zoom_();
	                final JFrame f = new JFrame("Mouse Zoom");
	                f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	                f.add(zm.getGui());
	                f.setResizable(false);
	                f.pack();
	                f.setLocationByPlatform(true);
	                f.setAlwaysOnTop(true);
	                f.setVisible(true);

	                WindowListener closeListener = new WindowAdapter() {
	                	
	                	public void windowClosing(WindowEvent e) {
	                		zm.stop();
	                		f.dispose();
	                	}
	                };
	                f.addWindowListener(closeListener);
				} catch (AWTException e) {
					e.printStackTrace();
				
				}

			};
			
			SwingUtilities.invokeLater(r);

		}
	

}


