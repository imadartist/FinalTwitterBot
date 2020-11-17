/*
 * Programmer: Madison Leyens
 * Class: Markov Generator
 * Date: 9.20.2020
 * Description: Generating melodies not just based on probability of occurrence of a given note but also using the probability of 
 * the note appearing at a given position in the sequence of notes
 */

import java.util.ArrayList;

//Markov Generator will use ProbabilityGenerator to get the counts and probabilities

public class MarkovGenerator<T> extends ProbabilityGenerator<T> {
	ArrayList<ArrayList<Integer>> transitionTable = new ArrayList();
	ProbabilityGenerator<T> probGen = new ProbabilityGenerator();
		
	

	MarkovGenerator() {
		super();
	}

	void train(ArrayList<T> newTokens) { // filling the empty transition table
		probGen.train(newTokens);
		int lastIndex = -1; // last index starts when there is no index, transition table is empty
		for (int i = 0; i < newTokens.size(); i++) {// for all new notes/"tokens"/inputs
			int tokenIndex = alphabet.indexOf(newTokens.get(i));// tokenIndex is the index of each new
																// note/"token"/input in alphabet
			if (tokenIndex == -1) { // if current token is not found in the alphabet
				tokenIndex = alphabet.size(); // set tokenIndex to the size of alphabet

				ArrayList<Integer> myRow = new ArrayList(); // create a row array list to add a new row
				transitionTable.add(myRow);// add the new row to the transition table
				for (int m = 0; m < alphabet.size(); m++) { // new row will start at 0 and loop through all possible
															// values in alphabet
					myRow.add(0); // add a 0 to myRow array list
				}

				alphabet.add(newTokens.get(i)); // add the newToken to alphabet
				for (int j = 0; j < transitionTable.size(); j++) { // looping through all of the transition table
					ArrayList<Integer> row = transitionTable.get(j); // using the new token at myRow to a new column
																		// (horizontal expansion)
					row.add(0);
				}
			}

			// adding the counts to the transition table
			if (lastIndex > -1) { // if we have a token and it is not the first time through
				ArrayList<Integer> row = transitionTable.get(lastIndex); // get whatever the lastIndex is in transition
																			// table and set it to the row array
				Integer myElement = row.get(tokenIndex); // get tokenIndex
				myElement++;// increment the tokenIndex
				row.set(tokenIndex, myElement);
			}
			lastIndex = tokenIndex; // setting current index to previous for next loop
		}
		
	}

	void printTransitionTable(String s) {
		
		System.out.println(s);
		System.out.println("-----Transition Table-----");
		
		System.out.println(alphabet);
		
		for (int i = 0; i < transitionTable.size(); i++) {
			ArrayList<Integer> row = transitionTable.get(i);
			float sum = 0;
			for (int a = 0; a < row.size(); a++) {
				sum = row.get(a) + sum;

			}
			for (int b = 0; b < row.size(); b++) {
				if (sum == 0) {
					System.out.print(0);
				} else {
					System.out.print(" " + row.get(b) / sum + " " );
				}
			}
			System.out.println(" ");
		}
	}

	T generate(T initToken) {
		T newToken = null;
		//ArrayList<T> newProbs = new ArrayList<T>();
		int tokenIndex = alphabet.indexOf(initToken); // use initToken to get index from alphabet
		// if index not in data set
		if (tokenIndex == -1) {
			System.out.print("Not Available in Data Set");
		} else {
			// use index to find row
			ArrayList<Integer> row = transitionTable.get(tokenIndex);
			total = 0;
			for (int a = 0; a < row.size(); a++) {
				// total the counts in the row
				total = row.get(a) + total;
			}
			if (total == 0) {
				return probGen.generate();
			} else {
				// divide each count by the total
				alphabet_counts = row;
				return super.generate();
			}

		}

		return newToken;
	}

	ArrayList<T> generate(int length, T initToken) { //calls the method above for the melody length the user inputs
		ArrayList<T> newSequence = new ArrayList<T>();
		T newToken = initToken; //set newToken equal to initToken
		for (int i = 0; i < length; i++){ //iterates through the melody length 
		newSequence.add(newToken); //add the initToken as newToken to the newSequence array list
		newToken = generate(newToken); //set newToken equal to the value generated from new token
		
		}
	
		return newSequence;
	}

	ArrayList<T> generate(int length) {
		return generate(length, probGen.generate());
	}
	
	
}