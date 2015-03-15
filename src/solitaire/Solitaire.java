package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() 
	{
		if (deckRear == null)
		{
			return;
		}
		CardNode temp = deckRear.next;
		CardNode prev = deckRear;
		while (temp.cardValue != 27)
		{
			temp = temp.next;
			prev = prev.next;
		}
		if (temp.equals(deckRear))
		{
			deckRear = temp.next;		
		}
		else if (temp.next.equals(deckRear))
		{
			deckRear = temp;
		}
		prev.next = temp.next;
		temp.next = prev.next.next;
		prev.next.next = temp;
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() 
	{
		if (deckRear == null)
		{
			return;
		}
		CardNode temp = deckRear.next;
		CardNode prev = deckRear;
		while (temp.cardValue != 28)
		{
			temp = temp.next;
			prev = prev.next;
		}
		if (temp.equals(deckRear))
		{
			deckRear = temp.next;		
		}
		else if (temp.next.equals(deckRear))
		{
			deckRear = temp.next.next;
		}
		else if (temp.next.next.equals(deckRear))
		{
			deckRear = temp;
		}
		prev.next = temp.next;
		temp.next = prev.next.next.next;
		prev.next.next.next = temp;
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() 
	{
		CardNode temp1 = deckRear.next;
		CardNode temp2, placeholder;
		CardNode prev1 = deckRear;
		
		while (temp1.cardValue != 27 && temp1.cardValue != 28)
		{
			temp1 = temp1.next;
			prev1 = prev1.next;
		}
		
		temp2 = temp1.next;
		
		while (temp2.cardValue != 28 && temp2.cardValue != 27)
		{
			temp2 = temp2.next;
		}
		if (deckRear.next.equals(temp1))
		{
			placeholder = temp2;
		}
		else
		{
			placeholder = prev1;
		}
		if (deckRear.equals(temp2))
		{
			deckRear = placeholder;
			return;
		}
		else
		{
			prev1.next = temp2.next;
			temp2.next = deckRear.next;
			deckRear.next = temp1;
		}
		
		deckRear = placeholder;
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() 
	{		
		int count = deckRear.cardValue;
		if (count == 28)
		{
			count = 27;
		}
		CardNode temp = deckRear.next;
		CardNode prev = deckRear.next;
		while (prev.next != deckRear)
		{
			prev = prev.next;
		}
		for (int n = 0; n < count - 1; n++)
		{
			temp = temp.next;
		}
		prev.next = deckRear.next;
		deckRear.next = temp.next;
		temp.next = deckRear;
	}
	
        /**
         * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
         * counts down based on the value of the first card and extracts the next card value 
         * as key, but if that value is 27 or 28, repeats the whole process until a value
         * less than or equal to 26 is found, which is then returned.
         * 
         * @return Key between 1 and 26
         */
	int getKey() 
	{
		jokerA();
		jokerB();
		tripleCut();
		countCut();
		CardNode temp = deckRear.next;
		int first = deckRear.next.cardValue;
		if (first == 28)
		{
			first = 27;
		}
		for (int n = 0; n < first - 1; n++)
		{
			temp = temp.next;
		}
		if (temp.next.cardValue <= 26)
		{
			return temp.next.cardValue;
		}
		else
		{
			getKey();
		}
		return -1;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) 
	{	
		String result = "";
		char a, b;
		int alphabet, keystream, total;
		for (int n = 0; n < message.length(); n++)
		{
			if (Character.isUpperCase(message.charAt(n)))
			{
				a = Character.toLowerCase(message.charAt(n));
				alphabet = a - 'a' + 1;
				keystream = getKey();
				total = alphabet + keystream;
				
				if (total > 26)
				{
					total -= 26;
				}
				
				b = (char)(total + 'a' - 1);
				b = Character.toUpperCase(b);
				result += b;
			}
		}
		return result;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) 
	{	
		String result = "";
		char a, b;
		int alphabet, keystream, total;
		for (int n = 0; n < message.length(); n++)
		{
			if (Character.isUpperCase(message.charAt(n)))
			{
				a = Character.toLowerCase(message.charAt(n));
				alphabet = a - 'a' + 1;
				keystream = getKey();
				total = alphabet - keystream;
				if (alphabet < keystream)
				{
					total += 26;
				}
				b = (char)(total + 'a' - 1);
				b = Character.toUpperCase(b);
				result += b;
			}
		}
		return result;
	}
}
