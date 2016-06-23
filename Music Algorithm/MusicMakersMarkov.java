package jMusicTest;

import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

// First attempt at creating a markov model for music
// Using code from CS2020 2016 PS6

public class MusicMakersMarkov {
		private int m_order;
		private float[] m_audioFile;
		private ArrayList<Float> sub = new ArrayList<Float>();
		private ArrayList<Float> next = new ArrayList<Float>();
		private ArrayList<ArrayList<Float>> values = new ArrayList<ArrayList<Float>> ();
//		private int kgramFre = 0;
//		private int kgramFreChar = 0;
		private HashMap<ArrayList<Float>, ArrayList<ArrayList<Float>>> hash = new HashMap<ArrayList<Float>, ArrayList<ArrayList<Float>>> ();
		
		/**
		 * jMusic has a function Read.audio(filename) that enables users to read certain audio files,
		 * namely au, aiff, and wav files. The audio data is stored as floating point numbers from -1 
		 * to 1 in jMusic.
		 * 
		 * @param data
		 * @param order
		 */
		MusicMakersMarkov(float[] data, int order) {
			System.out.println("Preparing Markov");
			m_order = order;
			m_audioFile = data;
			this.createHTable();
		}
		
		/**
		 * Returns the order of the Markov model
		 * @return
		 */
		public int order() {
			return m_order;
		}
		
		/**
		 * Creates a hashtable that takes in a portion of audio data as key and stores a floating array 
		 * as the value.
		 */
		public void createHTable() {
			System.out.println("Preparing Hash Table");
			for (int i=0; i<m_audioFile.length; i++) {
//				if (m_audioFile[i] == (float) 0.0 || m_audioFile[i+1] == (float) 0.0){
//					System.out.println("i: " + i );
//					continue;
//				}
				// taking a portion of audio data of a particular order
				for (int j=i; j<m_order+i; j++){
					sub.add(m_audioFile[j]);
//					System.out.println("i: " + i );
//					System.out.println("j: " + j );
				}
//				System.out.println(sub.toString());
				// take the next audio data
				for (int k=m_order+i; k<m_order+m_order+i; k++){
					next.add(m_audioFile[k]);
//					System.out.println("i: " + i );
//					System.out.println("j: " + j );
				}
				// if the hashtable does not contain the portion of audio data, put it in the hashtable as the key
				// Add the next audio data as the value
				if (!hash.containsKey((sub))) {
					ArrayList<ArrayList<Float>> value = new ArrayList<ArrayList<Float>> ();
					value.add(next);
					hash.put(sub, value);
				}
				// If the hashtable already contains the portion of audio data,
				// just merely add the next audio data as the value
				else {
					ArrayList<ArrayList<Float>> value = hash.get(sub);
					value.add(next);
					hash.remove(sub);
					hash.put(sub, value);	
				}
				values.add(next);
				sub.clear();
				next.clear();
//				System.out.println("i: " + i);
			}
		}
		
		public int getHashSize() {
			return hash.size();
		}
		
//		/**
//		 * Returns the number of times the specified audio data kgram appears in the input audio file. 
//		 * The kgram must be the length specified by the order of the Markov model.
//		 * @param kgram
//		 * @return
//		 */
//			
//		public int getFrequency(ArrayList<Float> kgram) {
//			
//			// checking if the length of kgram is equal to the order
//			if (kgram.size() != m_order) {
//				throw new IllegalArgumentException("kgram is not of the same length as the order stated in the Markov model");
//			}
//			
//			// checking through linearly the possible substrings
//			for (int i=0; i<m_audioFile.length-m_order; i++) {
//				for (int j=i; j<m_order+i; j++){
//					sub.add(m_audioFile[j]);
//				}
//				if (sub.equals(kgram)) {
//					kgramFre++;
//				}
//				sub.clear();
//			}
//			return kgramFre;		
//		}
//		
//		/**
//		 * Returns the number of times the specified float ‘c’ appears immediately after the 
//		 * audio data kgram in the input text. The kgram must be the length specified by the order of 
//		 * the Markov model. 
//		 * @param kgram
//		 * @param c
//		 * @return
//		 */
//		public int getFrequency(ArrayList<Float> kgram, float c) {
//			// checking if the length of kgram is equal to the order
//			if (kgram.size() != m_order) {
//				throw new IllegalArgumentException("kgram is not of the same length as the order stated in the Markov model");
//			}
//			
//			
//			if (hash.containsKey(kgram)){
//				// getting the array list of values that occur after audio data 'kgram'
//				ArrayList<Float> v = hash.get(kgram);
//				// checking if specific float appears after audio data 'kgram'
//				for (int i=0; i<v.size(); i++) {
//					if (v.get(i) == c) {
//						kgramFreChar++;
//					}
//				}
//			}
//			else{
//				throw new IllegalArgumentException("kgram is invalid");
//			}
//			
//			return kgramFreChar;		
//		}
		
		/**
		 * Returns a random flaot. The probability of a float ‘c’ should be equal to 
		 * getFrequency(kgram,c)/getFrequency(kgram). That is, the probability of float ‘c’
		 * should be equal to the frequency that ‘c’ follows the array list kgram in the audiofile. 
		 * If there is no possible next character, then return any random float (for now - first attempt)
		 * @param kgram
		 * @return
		 */
		public ArrayList<Float> nextFloatChunk(ArrayList<Float> kgram) {
			// checking if length of kgram is equal to the order
			if (kgram.size() != m_order) {
				throw new IllegalArgumentException("kgram is not of the same length as the order stated in the Markov model");
			}
			
			// there is no such combination of kgram
			// So merely return any random floating number from the hashtable
			else if (!hash.containsKey(kgram)){
				int index = new Random().nextInt(values.size());
				return values.get(index);
			}
			
			// get the integer count array associated with the substring kgram.
			ArrayList<ArrayList<Float>> value = hash.get(kgram);
			
			// returning a random character out of the array
			ArrayList<Float> c = value.get(new Random().nextInt(value.size()));
			
			return c;
		}
}

