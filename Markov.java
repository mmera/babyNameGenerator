package hw4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class Markov {
	
	private HashMap<String,HashMap<String,Integer>> trainingData = new HashMap<>();
	private HashSet<String> oldNames = new HashSet<String>();
	private int order;
	private int gender; 
	private int minLength; 
	private int maxLength;
	private int numNamesGen; 
	private Random rand = new Random();
	
	/*
	 * Constructor for Markov model.
	 * The user decides these inputs at the start of the program (in the Main class) 
	 */
	public Markov(int gender, int maxNum, int minLength, int maxLength, int order){
		this.gender      = gender;
		this.numNamesGen = maxNum;
		this.minLength   = minLength;
		this.maxLength   = maxLength;
		this.order       = order; 
	}
	
	/*
	 * Returns a list of names. 
	 */
	public HashSet<String> listNewNames(){
		readFile();
		HashSet<String> newNames = new HashSet<>();
		while (newNames.size() < numNamesGen){
			String temp = "";
			newNames.add(checkName(temp));
		}
		return newNames;
	}
	
	/*
	 * Method to check requirements set by user and 
	 * to check that names are not repeated.
	 */
	private String checkName(String name){
		while(name.length() < minLength || name.length()>maxLength || oldNames.contains(name))
			name = generateNewName();
		return name; 
	}
	
	/*
	 * Method that reads the names in line by line from the file the user chose.
	 */
	private void readFile() {
		String babyNames; 
		BufferedReader in = null;
		
		if (gender == 1){
			babyNames = "/Users/Marco/Documents/DataStructures/workspace/ArtificialIntelligence/data/namesBoys.txt";
		}
		else babyNames = "/Users/Marco/Documents/DataStructures/workspace/ArtificialIntelligence/data/namesGirls.txt";
		
		try {
			in = new BufferedReader(new FileReader(babyNames));
			String name = in.readLine();
			while(name != null){
				parseName(name.toLowerCase());
				oldNames.add(name);
				name = in.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error: "+ babyNames + " not found!");
			System.exit(1);
		}
	}
	
	/*
	 * Method to parse name from the list. 
	 * Appends '_' to the beginning and end of the name. 
	 * Then it builds the Markov model using a nested HashMap. 
	 * The key of the outer map is the substring grabbed from the name (the length depends on the order of the model).
	 * The value of the outer map is another HashMap the contains:
	 * key: the letter that came after the substring. 
	 * value: the number of times it occurred.   
	 */
	private void parseName(String name){
		
		for(int i=0; i<order;i++){
			name = '_'+name+'_';
		}
		
		for(int i=0; i+order<name.length();i++){
			String keyA = name.substring(i, i+order);
			String keyB = Character.toString(name.charAt(i+order));
			
			if(trainingData.containsKey(keyA)){
				if(trainingData.get(keyA).containsKey(keyB)){
					trainingData.get(keyA).put(keyB, trainingData.get(keyA).get(keyB)+1);
				}
				else{
					trainingData.get(keyA).put(keyB, 1);
				}
			}
			
			else{
				HashMap<String, Integer> temp = new HashMap<>();
				temp.put(keyB, 1);
				trainingData.put(keyA, temp);
			}
		}
	}

	/*
	 * Method to generate new name. Uses the getNextLetter method to build the name, 
	 * only stopping when it reaches a '_'. Then it trims the '_' off and capitalizes 
	 * the name.
	 */
	private String generateNewName(){
		
		String newName      = "";
		String currentKey   = "";
		int    currentIndex = 1; 
		
		for(int i=0; i<order; i++){
			newName = "_"+newName;
		}
		
		newName = newName + getNextLetter(newName);
		
		while(newName.endsWith("_") == false){
			currentKey = newName.substring(currentIndex, currentIndex+order);
			newName = newName + getNextLetter(currentKey);
			currentIndex++;
		}
		
		newName = newName.replaceAll("[^A-Za-z0-9]", "");
		newName = newName.substring(0,1).toUpperCase() + newName.substring(1);
		
		return newName;
	}
	
	/*
	 *Helper method for generateNewName method. It stores all the letters that map to a given key in 
	 *an ArrayList, adding them the number of times they occurred. For example, if the value for "___", is 
	 *{a, 90} then it adds the letter 'a' 90 times to the ArrayList. Then it indexes into the ArrayList using 
	 *a random number, and returns the letter corresponding to the index. 
	 */
	private String getNextLetter(String currentKey){
		
		ArrayList<String>allLetters = new ArrayList<>();
		Integer valB;

		for(Map.Entry<String, HashMap<String, Integer>> entryA : trainingData.entrySet()){
			if(currentKey.equals(entryA.getKey())){
				HashMap<String, Integer> mapB = entryA.getValue();
				for(String keyB:mapB.keySet()){
					valB = mapB.get(keyB);
					for(int i=0; i<valB;i++){
						allLetters.add(keyB);
					}
				}
			}
		}
		
		int randIndex     = rand.nextInt(allLetters.size());
		String nextLetter = allLetters.get(randIndex);
		return nextLetter;
	}
	
}
