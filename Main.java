package hw4;

import java.util.HashSet;
import java.util.Scanner;

public class Main {
	
	private static int gender; 
	private static int minLength;
	private static int maxLength;
	private static int order; 
	private static int numberOfNames; 
	
	/*
	 * Simple method to get input from user.
	 * Input asked for:
	 * 
	 * gender: if the names generated are for boys or girls
	 * 
	 * minimum name length: how short do you want the name to be?
	 * 
	 * maximum name length: how long do you want the name to be? 
	 *
	 * Markov order: how complex will the name be? The higher the order the more "normal the name".
	 * 
	 * Number of names: How many names will be generated? 
	 * 
	 * 
	 */
	private static void getInput(){
		Scanner in = new Scanner(System.in);
		print("Welcome to Baby-Name-O-Matic");
		print("The baby name generator with an attitude!");
		print("Please select: \n\t1. Boy \n\t2. Girl");
		
		gender = in.nextInt();
		while(gender!=1 && gender!=2){
			print("Please pick boy or girl!");
			gender = in.nextInt();
		}
		
		print("Please enter the minimum name length:");
		minLength = in.nextInt();
		while(minLength<3){
			print("That name is too short! No one named Bo is taken seriously...");
			print("Please enter the minimum name length again:");
			minLength = in.nextInt();
		}
		
		print("Please enter the maximum name length:");
		maxLength = in.nextInt();
		while(maxLength<minLength || maxLength>12){
			print("Make the name shorter...you want to remember how to spell it right??");
			print("Please enter the maximum name length again:");
			maxLength = in.nextInt();
		}
		
		print("How unique do you want the name for your snowflake to be? (Markov Order)");
		print("Please enter a number 1-4:");
		
		order = in.nextInt();
		while(order<1 || order > 4){
			print("Let's not get too crazy!");
			print("Please enter a number between 1 and 4!");
			order = in.nextInt();
		}
		
		print("How many names do you want to generate?");
		numberOfNames = in.nextInt();
		in.close();
		
	}
	/*
	 * Print method
	 */
	private static void print(String input){
		System.out.println(input);
	}
	
	public static void main(String[] args){
		getInput();
		Markov generator      = new Markov(gender, numberOfNames, minLength, maxLength, order);
		HashSet<String> names = generator.listNewNames();
		
		for(String name: names){
			print(name);
		}
	}
}
