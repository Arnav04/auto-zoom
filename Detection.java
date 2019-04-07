package autozoomSound;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.AWTException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import net.sourceforge.javaflacencoder.FLACFileWriter;

public class Detection {

	private final Microphone mic = new Microphone(FLACFileWriter.FLAC);
	private final GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
	String oldText = "";

	public Detection() {

		// Duplex Communication
		duplex.setLanguage("en");

		duplex.addResponseListener(new GSpeechResponseListener() {

			public void onResponse(GoogleResponse gr) {
				String output = "";


				// Google Cloud Response
				GoogleResponse googleResponse = null; // might need to delete...
				output = googleResponse.getResponse();
				System.out.println(output);
				if (output != null) {
					try {
						makeDecision(output);
					} catch (AWTException e) {
						e.printStackTrace();
					}
				} else
					System.out.println("Output was null");
			}

		});

		// start speech detection

		startSpeechDetection();

	}

	/**
	 * This method makes a decision on whether to zoom based on the word "zoom" of the Speech Recognition
	 * @throws AWTException 
	 */

	public void makeDecision(String output) throws AWTException {
		output = output.trim();

		// Prevent duplication of responses
		if (!oldText.equals(output))
			oldText = output;
		else
			return;

		if (output.contains("zoom")) {
			final Zoom_ zm = new Zoom_();
			Runnable r = () -> {
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

			};
			
			SwingUtilities.invokeLater(r);

			
		}
		else if (output.contains("stop")) {
			
		}
	}

	public void startSpeechDetection() {
		// Start a new thread
		new Thread(() -> {
			try {
				duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
			} catch (LineUnavailableException | InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
	public void stopSpeechDetection() {
		mic.close();	
	}

	public static void main(String[] args) {
		Detection d = new Detection();
		d.stopSpeechDetection();
		
	}

}