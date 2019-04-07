import java.awt.*;

import org.opencv.core.Rect;

public class Main {

	public static void main(String[] args) throws Exception {	
		
		faceDetect detectface = new faceDetect();
		Robot robot = new Robot();
		pupilDetect detectpupil = new pupilDetect();
		mouseMover mouseMover = new mouseMover();
		mouseMover.moveMouse(720, 450);
		
		mouseMover.zoom();
		detectface.findlefteye();
		Point home = detectpupil.pupil();
		System.out.println(home.x);
		System.out.println("MOVE\nMOVEMOVE\nMOVEMOVEMOVE\nMOVEMOVEMOVEMOVE\nMOVEMOVEMOVEMOVEMOVE");
		Thread.sleep(2000);
		
		while (true) {
			detectface.findlefteye();
			Point pupil = detectpupil.pupil();
			System.out.println(pupil.x);
			
			int differenceX = (home.x-pupil.x)*72;
			int differenceY = (home.y-pupil.y)*36;
			
			System.out.println(differenceX);
			
			mouseMover.moveMouse(720+differenceX, 450);
			
			//home.x+differenceX
			//home.y-differenceY
		
		}
	}
}
		
		
		
		
		
		
		
		

			
		
		
		
		/*
		mouseMover mouseMover = new mouseMover();
		mouseMover.zoom();
		
		int x = 0;
		int y = 0;
		
		while (x < 1440) {
			mouseMover.moveMouse(x, y);
			x += 2;
			y++;
			Thread.sleep(1);
		}
		
		mouseMover.zoomoff();
		*/
		
		
		
		
		
		/*
		faceDetect detectface = new faceDetect();
		pupilDetect detectpupil = new pupilDetect();
		mouseMover mouseMover = new mouseMover();
		Point home = new Point(720, 10);
		mouseMover.moveMouse(home.x, 10);
		mouseMover.zoom();
	
		Rect eye = detectface.findlefteye();
		int eyex = eye.width/2;
		Point pupil = detectpupil.pupil();

		int olddisplacement = eyex-pupil.x;
		Point oldpupil = detectpupil.pupil();
		int multi = 36;
		
		while (true) {
			eye = detectface.findlefteye();
			
			eyex = eye.width/2;
			pupil = detectpupil.pupil();
			
			int displacement = eyex-pupil.x;
			
			int differenceX = (olddisplacement-displacement)*multi;
			
			mouseMover.moveMouse(home.x-differenceX, 10);
			home.x = home.x+differenceX;
		}
		 */

