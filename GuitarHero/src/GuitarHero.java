public class GuitarHero {
	
	public static void main(String[] args) {
		String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
		GuitarString[] strings = new GuitarString[keyboard.length()];
		for(int i = 0; i < keyboard.length(); i++) {
			strings[i] = new GuitarString(440 * Math.pow(1.05956, i - 24));
			//System.out.println(keyboard.charAt(i) + " " + (440 * Math.pow(1.05956, i - 24)));
			
		}
		
		StdDraw.setCanvasSize(700,700);
		StdDraw.picture(0.5, 0.5, "image.png",0.8,1);
		
		while (true) {
			// check if the user has typed a key, and, if so, process it
			if (StdDraw.hasNextKeyTyped()) {
				
				// the user types this character
				char key = StdDraw.nextKeyTyped();
				int index = keyboard.indexOf(key);
				if(index != -1)
					strings[index].pluck();
				else
					System.out.println(key + " is not a note");
				
				//System.out.println(strings[index].rb);
			}
			
			
			// compute the superposition of the samples
			double sample = 0;
			for(GuitarString item : strings) {
				sample += item.sample();
			}
			// send the result to standard audio
			StdAudio.play(sample);
			//System.out.println(sample);

			// advance the simulation of each guitar string by one step
			for(int i = 0; i < strings.length; i++) {
				strings[i].tic();
			}
		}
		 
	}
}
