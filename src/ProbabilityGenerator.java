import java.util.ArrayList;

/*Programmer: Madison Leyens
 * Class: ProbabilityGenerator
 * Date: 9.7.2020
 * Description: Assigning distinct MIDI values of a given song based on occurrence relative to the whole to then generate new values based on probability of occurrence
 */
public class ProbabilityGenerator<T> {
	//declaring ArrayList alphabet (tokens) and alphabet_counts (number of occurrences of each token)
	ArrayList<T> alphabet;
	ArrayList<Integer> alphabet_counts;
	//declaring and instantiating the total variable
	float total = 0;

	//instantiating the ArrayLists in the main class constructor
	ProbabilityGenerator() {
		alphabet = new ArrayList<T>();
		alphabet_counts = new ArrayList<Integer>();
		
	}

	// it is training probability generator with new data
	void train(ArrayList<T> newTokens) {
		//for loop including all of the newTokens ArrayList)
		for (int i = 0; i < newTokens.size(); i++) {
			//finding the index of each newToken in alphabet
			int index = alphabet.indexOf(newTokens.get(i));
			if (index == -1) {
				//adding a newToken and counting its occurrence as 0 if there is no token there and subtracting one from alphabet size (and thus index)
				alphabet.add(newTokens.get(i));
				alphabet_counts.add(0);
				index = alphabet.size()-1;
			} 
			//getting and incrementing the counts (occurrences) of each index and setting the two within the alphabet_counts ArrayList
				int val = alphabet_counts.get(index);
				val++;
				alphabet_counts.set(index,val);
	
		}
		//setting total to be all notes (newTokens) in a given file
		total = newTokens.size()+ total;
	}
	
	//using the above to print pitch and rhythm MIDI value tokens from alphabet and the probability of occurrence using alphabet_counts and total
	void printProbabilityDistribution(String s) {
		
		System.out.println(s);
		System.out.println("-----Probability Distribution-----");
		
		//for loop that runs for as many unique count elements as present
		for (int i = 0; i < alphabet_counts.size(); i++) {
	
			//prints each MIDI value token
			System.out.print("Token: ");
			System.out.print(alphabet.get(i) + " | ");
			
			//prints probability of value by dividing the # of occurrences of a given MIDI value by the total number of values in the file
			System.out.print("Probability: ");
			System.out.print(alphabet_counts.get(i)/total);
			System.out.println();
			
		}
	}

	T generate() {
		
		//Array List that will hold all of the collected probabilities for each value
		ArrayList<Float> probs = new ArrayList<Float>();
		//Array List that will hold the sums of the calculated probabilities
		ArrayList<Float> sumProbs = new ArrayList<Float>();
		
		//putting all probabilities contained in alphabet in the probs array
		for (int i = 0; i < alphabet.size(); i++) {
			probs.add(alphabet_counts.get(i)/total);
		}
		
		//putting the first index of probs in sumProbs and then adding the each index in probs to the previous indexes (probabilities) in the loop
		sumProbs.add(probs.get(0));
		for (int i =1; i < probs.size(); i++) {
			sumProbs.add(sumProbs.get(i-1)+probs.get(i));
		}
		
		
		//generating random indexes
		float randIndex = (float)Math.random();
		boolean found = false;
		int i = 0;
		
		//finding what probability category the random index generated falls into and incrementing
		while (!found && i<sumProbs.size()) {
			found = randIndex < sumProbs.get(i);
			i++;
		}
		//getting from alphabet and returning the value of the index
		return alphabet.get(i-1);
		
	
		}

	
	//Array List that takes how many notes will be generated in the melody
	ArrayList<T> generate(int length) {
		ArrayList<T> newSequence = new ArrayList<T>();
		//holds and generates new melodies
		for (int i = 0; i < length; i++) {
			newSequence.add(generate());

		}
		//returns new melodies
		return newSequence;
	}
	}
	