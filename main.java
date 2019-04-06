
public class main {

	public static void main(String[] args) throws Exception {
		DetectFace df = new DetectFace();
		CircleDetection cd = new CircleDetection();
		
		df.findlefteye();
		Thread.sleep(1000);
		df.findlefteye();
		cd.main(args);
		
	}

}
