import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
@SuppressWarnings("serial")

/**
 * AutoGuitarHeroVisual
 * @author Jack Le
 * 
 * get songs from here: https://pianoletternotes.blogspot.com/
 * 
 */
public class GuitarHeroVisual {
	
	private static volatile GuitarString[] strings;
	private static final double yShift = 0.017;
	private static Thread audioThread;
    private static Thread visualThread;
    private static boolean finished;
    private static boolean manual;
    private String pressed;
	private volatile Queue<ArrayList<Letter>> queue;
	private volatile boolean goodKey;
	private List<ArrayList<Chord>> list;
	private static int score;
	private static int totalScore;
	private static final Map<String,String[]> map = new HashMap<String,String[]>() {{
		
		put("_C",new String[] {"1","q"});
		put("_C#",new String[] {"2","w"});
		put("_D",new String[] {"3","e"});
		put("_D#",new String[] {"4","r"});
		put("_E",new String[] {"5","t"});
		put("_F",new String[] {"6","y"});
		put("_F#",new String[] {"7","u"});
		put("_G",new String[] {"8","i"});
		put("_G#",new String[] {"9","o"});
		put("_A",new String[] {"10","p"}); // 110 hz
		put("_A#",new String[] {"11","a"});
		put("_B",new String[] {"12","s"});
		
		put(".C",new String[] {"13","d"});
		put(".C#",new String[] {"14","f"});
		put(".D",new String[] {"15","g"});
		put(".D#",new String[] {"16","h"});
		put(".E",new String[] {"17","j"});
		put(".F",new String[] {"18","k"});
		put(".F#",new String[] {"19","l"});
		put(".G",new String[] {"20","z"});
		put(".G#",new String[] {"21","x"});
		put(".A",new String[] {"22","c"}); // 220 hz
		put(".A#",new String[] {"23","v"});
		put(".B",new String[] {"24","b"});
		
		put("C",new String[] {"25","n"});
		put("C#",new String[] {"26","m"});
		put("D",new String[] {"27","Q"});
		put("D#",new String[] {"28","W"});
		put("E",new String[] {"29","E"});
		put("F",new String[] {"30","R"});
		put("F#",new String[] {"31","T"});
		put("G",new String[] {"32","Y"});
		put("G#",new String[] {"33","U"});
		put("A",new String[] {"34","I"}); // 440 hz
		put("A#",new String[] {"35","O"});
		put("B",new String[] {"36","P"});
		
		put("^C",new String[] {"37","A"});
		put("^C#",new String[] {"38","S"});
		put("^D",new String[] {"39","D"});
		put("^D#",new String[] {"40","F"});
		put("^E",new String[] {"41","G"});
		put("^F",new String[] {"42","H"});
		put("^F#",new String[] {"43","J"});
		put("^G",new String[] {"44","K"});
		put("^G#",new String[] {"45","L"});
		put("^A",new String[] {"46","Z"}); // 880 hz
		put("^A#",new String[] {"47","X"});
		put("^B",new String[] {"48","C"});
		
		put("+C",new String[] {"49","V"});
		put("+C#",new String[] {"50","B"});
		put("+D",new String[] {"51","N"});
		put("+D#",new String[] {"52","M"});
		put("+E",new String[] {"53","1"});
		put("+F",new String[] {"54","2"});
		put("+F#",new String[] {"55","3"});
		put("+G",new String[] {"56","4"});
		put("+G#",new String[] {"57","5"});
		put("+A",new String[] {"58","6"}); // 1760 hz
		put("+A#",new String[] {"59","7"});
		put("+B",new String[] {"60","8"});
	}};
	private static final Map<String,Integer> keyboard  = new HashMap<String,Integer>() {{
		put("q",1);
		put("w",2);
		put("e",3);
		put("r",4);
		put("t",5);
		put("y",6);
		put("u",7);
		put("i",8);
		put("o",9);
		put("p",10);
		put("a",11);
		put("s",12);
		put("d",13);
		put("f",14);
		put("g",15);
		put("h",16);
		put("j",17);
		put("k",18);
		put("l",19);
		put("z",20);
		put("x",21);
		put("c",22);
		put("v",23);
		put("b",24);
		put("n",25);
		put("m",26);
		put("Q",27);
		put("W",28);
		put("E",29);
		put("R",30);
		put("T",31);
		put("Y",32);
		put("U",33);
		put("I",34);
		put("O",35);
		put("P",36);
		put("A",37);
		put("S",38);
		put("D",39);
		put("F",40);
		put("G",41);
		put("H",42);
		put("J",43);
		put("K",44);
		put("L",45);
		put("Z",46);
		put("X",47);
		put("C",48);
		put("V",49);
		put("B",50);
		put("N",51);
		put("M",52);
		put("1",53);
		put("2",54);
		put("3",55);
		put("4",56);
		put("5",57);
		put("6",58);
		put("7",59);
		put("8",60);
	}};
	
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
		System.out.println("Press enter for manual song input.");
		
		
		boolean ok = false;
		String song = "";
		String chosen = "";
		while(!ok) {
			song = kb.nextLine();
			if(song.equals("")) break;
			boolean found = false;
			for(String item : songs) {
				if(item.toLowerCase().equals(song.toLowerCase())) {
					found = true;
					chosen = item;
					break;
				}
			}
			if(!found) {
				System.out.println("This is not a supported song.");
			}
			else {
				ok = true;
				break;
			}
		}
		
		System.out.println("Do you want the song to play automatically? (Y/N)");
		String s1 = kb.nextLine().toLowerCase();
		while(true) {
			if(s1.equals("y") || s1.equals("yes")) {
				manual = false;
				break;
			}
			else if(s1.equals("n") || s1.equals("no")) {
				manual = true;
				break;
			}
			else {
				System.out.println("This is not a valid response.");
			}
		}
		
				
		Scanner f;
		
		if(song.equals("")) f = new Scanner(System.in);
		else f = new Scanner(new File(chosen + ".txt"));
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
	
	private class Letter {
		String key;
		double x;
		double y;
		boolean played;
		int index;
		Color c;
		
		public Letter(String key, double x, double y, int ind) {
			this.key = key;
			this.x = x;
			this.y = y;
			played = false;
			index = ind;
			c = new Color(245,78,78);
		}
		
		public String toString() {
			return key + " " + x + " " + y;
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Guitar Hero Project CS3\nCreated by Jack Le 2020-2021\n");
		GuitarHeroVisual gh = new GuitarHeroVisual();
		gh.start();
		gh.waitForFinish();
		if(!visualThread.isAlive() && !audioThread.isAlive()) {
			System.out.println("Threads are dead.");
		}
		StdDraw.clear();
		StdDraw.setPenColor(Color.RED);
		StdDraw.text(0.5, 0.5, "Your score was: " + score + " out of " + totalScore);
		StdDraw.show();
	}	
	
	public void start() throws Exception {
		queue = new LinkedList<>();
		goodKey = true;
		list = new ArrayList<>();
		pressed = "";
		strings = new GuitarString[map.size()];
		
		read();
		for(int i = 0; i < map.size(); i++) {
			strings[i] = new GuitarString(440 * Math.pow(1.059464, i - 28));
		}
		StdDraw.enableDoubleBuffering();
		StdDraw.setCanvasSize(1200,600);
		
		Visuals visual = new Visuals();
        visualThread = new Thread(visual);
        visualThread.start();
	    Audio audio = new Audio();
        audioThread = new Thread(audio);
        audioThread.start();
	}
	
	public void waitForFinish() throws InterruptedException {
		audioThread.join();
		visualThread.join();
	}
		
	private class Audio implements Runnable {
		@Override
		public void run() {
			while (!finished) {
				
				double sample = 0;
				for(GuitarString item : strings) {
					sample += item.sample();
					item.tic();
				}
				StdAudio.play(sample);
			}
			System.out.println("Audio finished.");
			StdAudio.close();
		}
	}
	
	private class Visuals implements Runnable {
		@Override
		public void run() {
			
			int currKey = 0;
			int currChord = 0;
			Stopwatch st = new Stopwatch();
			boolean keysLeft = true;
			
			while (true) {
				
				if(manual) {
					if (StdDraw.hasNextKeyTyped()) {
						pressed =  "" + StdDraw.nextKeyTyped();
						//System.out.println(pressed);
						
						if(keyboard.containsKey(pressed)) {
							int index = keyboard.get(pressed)-1;
							strings[index].pluck();
						}
						else {
							System.out.println("not a note");
						}
						
						boolean ok = false;
						outer: for(ArrayList<Letter> list : queue) {
							if(list.get(0).y <= 0.18 && list.get(0).y >= 0.08) {
								for(Letter t : list) {
									if(!t.played && t.key.equals(pressed)) {
										score++;
										totalScore++;
										ok = true;
										t.played = true;
										t.c = Color.GREEN;
										break outer;
									}
								}
							}
						}
						goodKey = ok;
						
					}
				}
				
				if (st.elapsedTime() > 110) {
					st = new Stopwatch();
					List<String> keys;
					ArrayList<Chord> curr;
					
					if(keysLeft) {
						keys = new ArrayList<>();
						
						curr = list.get(currChord);
						
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
							System.out.println("Key-reading finished.");
							keysLeft = false;
						}
						
						ArrayList<Letter> let = new ArrayList<>();
						for(String key : keys) {
							int index = Integer.parseInt(map.get(key.toUpperCase())[0]);
							let.add(new Letter(map.get(key.toUpperCase())[1], index/60.0, 0.98, index-1));
						}
						if(let.size() > 0) queue.add(let);
					}
					
					
					StdDraw.clear();
					
					double x = 1.0/60;
					for(int i = 0; i < 62; i++) {
						StdDraw.setPenColor(new Color(245,245,245));
						StdDraw.line(i*x-0.042,0,i*x-0.042, 1);
					}
					
					for(ArrayList<Letter> list : queue) {
						
						StdDraw.setPenRadius(0.004);
						StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
						StdDraw.line(0,0.09,1,0.09);
						StdDraw.line(0,0.17,1,0.17);
						StdDraw.setPenRadius(0.003);
						
						for(Letter t : list) {
							
							t.y -= yShift;
							StdDraw.setPenColor(t.c);
							StdDraw.text(t.x,t.y,t.key);
							
							if(manual) {
								if(t.y < 0.08 && !t.played) {
									goodKey = false;
									t.played = true;
									totalScore++;
								}
							}
							else {
								if(!t.played && t.y < 0.15) {
									score++;
									totalScore++;
									int index = t.index;
									if(index != -1)
										strings[index].pluck();
									else
										System.out.println("not a note");
									
									t.played = true;
									t.c = Color.GREEN;
								}
							}
							
						}
						
						if(manual) {
							StdDraw.setPenColor(goodKey ? StdDraw.GREEN : StdDraw.RED);
						}
						else {
							StdDraw.setPenColor(StdDraw.GREEN);
						}
						StdDraw.line(0,0.13,1,0.13);
						
						StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
						StdDraw.filledRectangle(0, 1, 1.06, 0.06);	
						
						StdDraw.setPenColor(StdDraw.WHITE);
						StdDraw.text(0.5, 0.965, "Score: "+score);
						
					}
					StdDraw.show();
					
					while(!queue.isEmpty() && queue.peek().get(0).y < -0.06) queue.poll();
					
					if(!keysLeft && queue.isEmpty()) {
						System.out.println("Visuals finished.");
						finished = true;
						break;
					}
					
					
				}
			}
		}
	}
	
}
