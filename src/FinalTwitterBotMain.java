/* Programmer: Madison Leyens
 * Date: Fall 2020
 * Description: This is the Main Class for my Twitter Bot in which I am training my Markov Generator on data from a TXT file and generating from it.
 * I have inlcuded keyPressed() functionality that allows the user to tweet these generations.
 * 
 */

import processing.core.*;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.jaunt.JauntException;

import jm.music.data.Score;
import jm.util.Play;
import jm.util.Read;

//This class serves as a template for creating twitterbots and demonstrates string tokenizing and web scraping and the use of the 
//twitter API
public class FinalTwitterBotMain extends PApplet {

	//MelodyPlayer player; // play a midi sequence
	MidiFileToNotes midiNotes; // read a midi file

	private ArrayList<String> tokens;
	private static String HEYER_TWITTER_URL = "https://twitter.com/LeyensMadison";
	private static int TWITTER_CHAR_LIMIT = 140; // I understand this has changed... but forget limit

	// useful constant strings -- for instance if you want to make sure your tweet
	// ends on a space or ending punctuation, etc.
	private static final String fPUNCTUATION = "\",.!?;:()/\\";
	private static final String fENDPUNCTUATION = ".!?;,";
	private static final String fREALENDPUNCTUATION = ".!?";

	private static final String fWHITESPACE = "\t\r\n ";

	// example twitter hastag search term
	private static final String fPASSIVEAGG = "passiveaggressive";
	private static final String fCOMMA = ",";

	// handles twitter api
	TwitterInteraction tweet;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("FinalTwitterBotMain"); // Not really using processing functionality but ya know, you _could_. UI not
		// required.

	}

	public void settings() {
		size(1000, 1000); // dummy window

	}

	public void setup() {

		// create generators for pitch and rhythm
		ProbabilityGenerator<Integer> pitchGenerator = new ProbabilityGenerator<Integer>();
		ProbabilityGenerator<Double> rhythmGenerator = new ProbabilityGenerator<Double>();

		MarkovGenerator<Integer> pitchMarkovGenerator = new MarkovGenerator<Integer>();
		MarkovGenerator<Double> rhythmMarkovGenerator = new MarkovGenerator<Double>();

		// returns a url
		String filePath = getPath("mid/gardel_por.mid");
		midiNotes = new MidiFileToNotes(filePath); // creates a new MidiFileToNotes
		midiNotes.setWhichLine(0); // which line to read in --> this object only reads one line (or ie, voice or
		// ie, one instrument)'s worth of data from the file

		// training the generators for pitch and rhythm to get the pitch and rhythm
		// arrays from the MIDI Notes object
		pitchGenerator.train(midiNotes.getPitchArray());
		rhythmGenerator.train(midiNotes.getRhythmArray());

		pitchMarkovGenerator.train(midiNotes.getPitchArray());
		rhythmMarkovGenerator.train(midiNotes.getRhythmArray());

		// enables use to generate and play a MIDI sequence file
//				player = new MelodyPlayer(this, 100.0f);
//				player.setup();
//				player.setMelody(pitchGenerator.generate(20));
//				player.setRhythm(rhythmGenerator.generate(20));
//				player.setMelody(pitchMarkovGenerator.generate(20));
//				player.setRhythm(rhythmMarkovGenerator.generate(20));

//		loadNovel("data/Catechism.txt"); // TODO: must train from another source
		//		println("Token size:"+tokens.size());

		// TODO: train an AI algorithm (eg, Markov Chain) and generate text for markov
		// chain status

		// can train on twitter statuses -- note: in your code I would put this part in
		// a separate function
		// but anyhow, here is an example of searching twitter hashtag. You have to pay
		// $$ to the man to get more results. :(
		// see TwitterInteraction class
		// ArrayList<String> tweetResults = tweet.searchForTweets("John Cage");
		// for (int i = 0; i < tweetResults.size(); i++) {
		// println(tweetResults.get(i)); //just prints out the results for now
		// }

		// Make sure within Twitter limits (used to be 140 but now is more?)
		//		String status = "Hello, world -- I am a twitterbot!";
		//		tweet.updateTwitter(status);

		// prints the text content of the sites that come up with the google search of
		// dogs
		//		//you may use this content to train your AI too
				Scraper scraper = new Scraper(); 
				ArrayList<String> results;
				try {
					results = scraper.scrapeAZResults("dogs");
					
					//print your results
					System.out.println(results); 
					
					scraper.scrape("http://google.com",  "dogs"); //see class documentation
		
				} catch (JauntException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				Scraper songScraper = new Scraper(); 
//				ArrayList<String> resultsOne;
//				try {
//					//will put input from user in a variable that will be the parameter below
//					resultsOne = songScraper.scrapeGoogleResults("everlasting love");
//					resultsTwo = songScraper.scrapeGoogleResults("everlasting love");
//					
//					//print your results
//					System.out.println(resultsOne); 
//					System.out.println(resultsTwo); 
//					
//					songScraper.scrape("https://www.azlyrics.com/",  "everlasting love"); //see class documentation
//					
//				} catch (JauntException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

	}

	// this loads the novel 'The Grand Sophy' given a path p -- but really will load
	// any file.
//	void loadNovel(String p) {
//		String filePath = getPath(p);
//		Path path = Paths.get(filePath);
//		tokens = new ArrayList<String>();
//
//		try {
//			List<String> lines = Files.readAllLines(path);
//
//			for (int i = 0; i < lines.size(); i++) {
//
//				TextTokenizer tokenizer = new TextTokenizer(lines.get(i));
//				ArrayList<String> t = tokenizer.parseSearchText();
//				tokens.addAll(t);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			println("Oopsie! We had a problem reading a file!");
//		}
//	}

	void printTokens() {
		for (int i = 0; i < tokens.size(); i++)
			print(tokens.get(i) + " ");
	}

	// get the relative file path
	String getPath(String path) {

		String filePath = "";
		try {
			filePath = URLDecoder.decode(getClass().getResource(path).getPath(), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	public void draw() {
//		player.play();
		textSize(12);
		fill(0, 102, 153);
		text("Input a song title or lyric to generate a tweet", width / 4, height / 4);
		text("Press 0 to run default song title or lyric and generate a tweet", width / 4, height / 4);
		text("Press 1 to start Unit Test 1 (Probability Transition Table)", width / 4, height / 4 + 100);
		text("Press 2 to start Unit Test 2 (Generating and Tweeting a 20-word Line)", width / 4, height / 4 + 150);
		text("Press 3 to start Unit Test 3 (Generating and Tweeting a 20-word Line 10,000 times)", width / 4,
				height / 4 + 200);

	}

	public void keyPressed() {
		MarkovGenerator<String> markov = new MarkovGenerator(); // declaring generator
		markov.train(tokens); // training generator on tokens taken from TXT file
		tweet = new TwitterInteraction(); //declaring tweet as an object of the TwitterInteraction class

		// UNIT TEST STUFF
		// DECLARING GENERATORS
		// why do I have probability generators declared and training in setup?

		// Project 1 - this calls the Probability Generator class delineated by Pitch
		// and Rhythm
		ProbabilityGenerator<Integer> generatorPitch = new ProbabilityGenerator();
		ProbabilityGenerator<Double> generatorRhythm = new ProbabilityGenerator();

		// Project 2 - declaring Markov Generator array lists for pitches and rhythms
		MarkovGenerator<Integer> mPitches = new MarkovGenerator();
		MarkovGenerator<Double> mRhythms = new MarkovGenerator();

		// MidiNotesMary setup
		MidiFileToNotes midiNotesMary; // read a midi file
		String filePath = getPath("mid/MaryHadALittleLamb.mid"); // returns a url
		midiNotesMary = new MidiFileToNotes(filePath);// creates a new MidiFileToNotes
		midiNotesMary.setWhichLine(0); // which line to read in --> this object only reads one line (or ie, voice or
		// ie, one instrument)'s worth of data from the file

		// TRAINING GENERATORS
		// Project 1 - training the generators for pitch and rhythm to get the pitch and
		// rhythm arrays from the MIDI Notes Mary object
		generatorPitch.train(midiNotesMary.getPitchArray());
		generatorRhythm.train(midiNotesMary.getRhythmArray());

		// Project 2 - training the markov generators for pitch and rhythm to get the
		// pitch and rhythm arrays from the MIDI Notes Mary object
		mPitches.train(midiNotesMary.getPitchArray());
		mRhythms.train(midiNotesMary.getRhythmArray());

		if (key == '0') {
			String seed = "I"; //creating a variable that holds the string "I" which later be included in a parameter (and posted in front of generated content)
			ArrayList<String> sentence = new ArrayList<>(markov.generate(20, seed)); //declaring an ArrayList "sentence" that will take our markov generated sequence of 20 and the seed variable above
			String status = ""; //status starts out empty
			for (int i = 0; i < sentence.size(); i++) { //looping through the ArrayList
				status = status + sentence.get(i) + " "; //adding each word from the text to the one before with a space in between (adding all strings)
			}
			tweet.updateTwitter(status); //tweeting the strung together string

		} else if (key == '1') {
			// runs Unit Test 1 when the user presses "1" which prints our Markov
			// Generator transition table using Probability Generator initial tokens and
			// Mary Had a Little Lamb
			int initTokenOne = generatorPitch.generate(); // generating using Probability Generator an initial token to
			// use for pitches
			double initTokenTwo = generatorRhythm.generate(); // generating using Probability Generator an initial token
			// to use for rhythms

			mPitches.printTransitionTable("Markov Unit Test 1 Pitches:"); // printing the transition table of the Markov
			// Generator pitches using Mary Had a Little
			// Lamb
			mRhythms.printTransitionTable("Markov Unit Test 1 Rhythms:"); // printing the transition table of the Markov
			// Generator rhythms using Mary Had a Little
			// Lamb
		} else if (key == '2') {
			// runs Unit Test 2 when the the user presses "2" which generates &
			// prints one 20-note melody from Mary Had a Little Lamb using our Markov
			// Generator
			System.out.println(mPitches.generate(20));
			System.out.println(mRhythms.generate(20));

		} else if (key == '3') {
			// runs Unit Test 3 when the user presses "3" and probabilities are
			// calculated and printed for melodies of 20 notes 10,000 times
			// creating new markov generators
			MarkovGenerator<Integer> markovPitchesTestThree = new MarkovGenerator();
			MarkovGenerator<Double> markovRhythmsTestThree = new MarkovGenerator();

			// training the new probability generators on the existing generators generating
			// melodies of 20
			for (int i = 0; i < 10000; i++) {
				markovPitchesTestThree.train(mPitches.generate(20));
				markovRhythmsTestThree.train(mRhythms.generate(20));
			}
			// printing the pitch and rhythm transition tables for 10000
			// 20-note melodies
			markovPitchesTestThree.printTransitionTable("Markov Unit Test 3 Pitches:");
			markovRhythmsTestThree.printTransitionTable("Markov Unit Test 3 Rhythms:");

		}

	}

}
