# Guitar Hero
This project is a simple recreation of the popular Guitar Hero game. The code is entirely written in Java, using the Standard Library from Princeton University for graphics and audio. There is a set of pre-loaded and supported songs. However, any song from [here](https://pianoletternotes.blogspot.com/) will work, as long as you include the number of lines in the input.

## Directory
```
.
├── GuitarHero                        # Main project files
│   ├── src                           # Source
│   │   ├── AutoGuitarHero.java       # Automatic player
│   │   ├── GuitarHero.java           # Manual player
│   │   ├── GuitarHeroLite.java       # Light demonstration program
│   │   ├── GuitarHeroVisual.java     # Main program - culminates all other files
│   │   ├── GuitarString.java         # Implements Karplus-Strong algorithm for synthesis
│   │   ├── RingBuffer.java           # Implements Karplus-Strong algorithm for synthesis
│   │   ├── RingBufferTester.java     # Testing file
│   │   ├── StdAudio.java             # Standard Audio from Princeton University
│   │   ├── StdDraw.java              # Standard Draw from Princeton University
│   │   ├── Stopwatch.java            # Stopwatch for timing
│   │   └── Test.java                 # Testing file
│   ├── .txt                          # All files ending in .txt are song files
│   └── ...                           # etc.
├── images                            # Images for demonstration
└── README.md                         # ReadMe
```
## Console
This is what console input looks like.

![Console Image](images/console.png?raw=true "Image")

# Demonstrations
## Image
This is an image of the user interface when the program is ran.
![Image demonstration](images/Demonstration.png?raw=true "Image")

## Video
This is a video demonstration of the program with sound, along with code explanations. (click below)
[![Video demonstration](images/Video.png)](https://www.youtube.com/watch?v=WWzUKfgd2AU "Open in YouTube")


