import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
@SuppressWarnings("serial")

/**
 * AutoGuitar
 * @author Jack Le
 * 
 * get songs from here: https://pianoletternotes.blogspot.com/
 * 
 */
public class AutoGuitarHero {
	
	private Stopwatch s;
	private GuitarString[] strings;
	private static final Map<String,Integer> map = new HashMap<String,Integer>() {{
		
		put("_C",1);
		put("_C#",2);
		put("_D",3);
		put("_D#",4);
		put("_E",5);
		put("_F",6);
		put("_F#",7);
		put("_G",8);
		put("_G#",9);
		put("_A",10); // 110 hz
		put("_A#",11);
		put("_B",12);
		
		put(".C",13);
		put(".C#",14);
		put(".D",15);
		put(".D#",16);
		put(".E",17);
		put(".F",18);
		put(".F#",19);
		put(".G",20);
		put(".G#",21);
		put(".A",22); // 220 hz
		put(".A#",23);
		put(".B",24);
		
		put("C",25);
		put("C#",26);
		put("D",27);
		put("D#",28);
		put("E",29);
		put("F",30);
		put("F#",31);
		put("G",32);
		put("G#",33);
		put("A",34); // 440 hz
		put("A#",35);
		put("B",36);
		
		put("^C",37);
		put("^C#",38);
		put("^D",39);
		put("^D#",40);
		put("^E",41);
		put("^F",42);
		put("^F#",43);
		put("^G",44);
		put("^G#",45);
		put("^A",46); // 880 hz
		put("^A#",47);
		put("^B",48);
		
		put("+C",49);
		put("+C#",50);
		put("+D",51);
		put("+D#",52);
		put("+E",53);
		put("+F",54);
		put("+F#",55);
		put("+G",56);
		put("+G#",57);
		put("+A",58); // 1760 hz
		put("+A#",59);
		put("+B",60);
	}};
	private List<ArrayList<Chord>> list = new ArrayList<>();
	
	private void read() throws Exception {
		Scanner kb = new Scanner(System.in);
		System.out.println("Which song would you like to play?");
		
		List<String> songs = new ArrayList<>();
		File file = new File("./");
		String[] fileList = file.list();
		Arrays.sort(fileList);
		for(String name : fileList) {
			if(name.endsWith(".txt")) songs.add(name.substring(0,name.length()-4));
		}
		
		System.out.println("Options are:");
		for(String item : songs) {
			System.out.printf("%4s %s\n", " " , item);
		}
		System.out.println("Press enter for manual input.");
		
		
		boolean ok = false;
		String song = "";
		while(!ok) {
			song = kb.nextLine();
			if(song.equals("")) break;
			if(!songs.contains(song)) {
				System.out.println("This is not a supported song.");
			}
			else {
				ok = true;
				break;
			}
		}
		
		Scanner f;
		
		if(song.equals("")) f = new Scanner(System.in);
		else f = new Scanner(new File(song + ".txt"));
		list.add(new ArrayList<Chord>());
		int N = f.nextInt(); f.nextLine();
		while(N-->0) {
			String s = f.nextLine();
			if(s.equals("")) {
				list.add(new ArrayList<Chord>());
				continue;
			}
			
			int n = s.indexOf("|");
			list.get(list.size()-1).add(new Chord(s.charAt(n-1)-'0', s.substring(n+1,s.length()-1)));
		}
		
		
		kb.close();
		f.close();
	}
	
	private class Chord {
		int string;
		String chord;
		
		public Chord(int s, String c) {
			string = s;
			chord = c;
		}
		
		public String toString() {
			return string + " " + chord;
		}
		
		public char at(int i) {
			return chord.charAt(i);
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("Guitar Hero Project CS3\nCreated by Jack Le 2020-2021\n");
		new AutoGuitarHero().run();
	}	
	
	public void run() throws Exception {
		
		read();
		//for(ArrayList<Chord> item : list) System.out.println(item);
		
		strings = new GuitarString[map.size()];
		for(int i = 0; i < map.size(); i++) {
			strings[i] = new GuitarString(440 * Math.pow(1.059464, i - 28));
		}
		
		StdDraw.setCanvasSize(700,700);
		StdDraw.setFont(new Font("Arial", Font.BOLD, 100));
	
		
		s = new Stopwatch();
		
		int currKey = 0;
		int currChord = 0;
		
		while (true) {
			
			if (s.elapsedTime() > 110) {
				s = new Stopwatch();
				
				List<String> keys = new ArrayList<>();
				
				ArrayList<Chord> curr = list.get(currChord);
				
				for(int i = 0; i < curr.size(); i++) {
					if(curr.get(i).at(currKey) != '-') {
						String s = "";
						char c = curr.get(i).at(currKey);
						int string = curr.get(i).string;
						switch(string) {
						case 2 : { s = "_"; break; }
						case 3 : { s = "."; break; }
						case 4 : { s = ""; break; }
						case 5 : { s = "^"; break; }
						case 6 : { s = "+"; break; }
						}
						if(Character.isUpperCase(c)) {
							s += Character.toUpperCase(c) + "#";
						}
						else {
							s += Character.toUpperCase(c);
						}
						keys.add(s);
					}
					
				}
				
				currKey++;
				if(currKey >= curr.get(0).chord.length()) {
					currChord++;
					currKey = 0;
				}
				
				if(currChord >= list.size()) {
					System.out.println("Song finished.");
					break;
				}
				
				for(String key : keys) {
					
					StdDraw.setPenColor(Color.WHITE);
					StdDraw.filledRectangle(0.5, 0.5, 0.2,0.2);
					StdDraw.setPenColor(Color.BLACK);
					StdDraw.text(0.5, 0.5, key);
					
					if(map.get(key.toUpperCase()) == null) {
						System.out.printf("%8s\n",key);
						continue;
					
					}
					else {
						System.out.println(key);
					}
					int index = map.get(key.toUpperCase())-1;
					if(index != -1)
						strings[index].pluck();
					else
						System.out.println(key + " is not a note");
				}
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
