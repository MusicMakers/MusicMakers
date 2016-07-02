package jMusicTest;

import jm.util.Read;
import jm.audio.io.AudioFileIn;
import jm.util.Write;

import java.util.ArrayList;

// First attempt at creating a markov model for music
// Using code from CS2020 2016 PS6

public class MusicCreator {
	int m_order;
	int numChars;
	float[] audioFile;
	AudioFileIn file;
			
	MusicCreator(String fileName) {
		try{
			file = new AudioFileIn(fileName);
			audioFile = Read.audio(fileName);
			m_order = 1000000;
			numChars = audioFile.length;
			System.out.println(numChars);
		}
		catch(Exception e)
		{
			// If there is an error reading in the file, there isn't much we can do.
			// Print out an error, and later the program will exit.
			System.out.println(e);
			System.out.println("Unable to read in object database. Please check the filename, the path, and that the file is formatted correctly.");
		}	
	}
	
	/**
	 * Generate music from the Markov model.
	 * In order to generate music, begin with a audio data 'kgram' equal to the first k floats from your 
	 * music file. Generate the next float by calling nextFloat on your Markov Model, using 
	 * the initial kgram as your input audio data. Then update the kgram, dropping the first float 
	 * and adding the newly generated flaot to the end. Finally, output the new float and 
	 * continue until you have generated n floats. If you ever reach a point where there is no 
	 * possible next character, then begin again, initializing the kgram to the first k characters 
	 * in the text file.
	 */

	public void generateMusic(){
		System.out.println("Generating Music");
		MusicMakersMarkov markovModel = new MusicMakersMarkov(audioFile, m_order);
		ArrayList<Float> curr = new ArrayList<Float> ();
		float[] wanted = new float[numChars];
		int j = 0;
//		while (count<m_order){
//			if (audioFile[j] == 0.0) {
//				j++;
//				wanted[j] = audioFile[j];
//				continue;
//			}
//			curr.add(audioFile[j]);
//			wanted[j] = audioFile[j];
//			count++;
//		}
		while (wanted.length<numChars) {
			// first k floats of audiofile
			// call next float
//			System.out.println("m");
//			System.out.println(curr.size());
			ArrayList<Float> nextFloat = markovModel.nextFloatChunk(curr);
			// add the new float
//			System.out.println(curr);
			for (Float f: nextFloat){
				wanted[j] = f;
				j++;
			}
			curr = nextFloat;
		}
		Write.audio(wanted, "First Attempt.wav");
	}
	
	public static void main (String[] args){
		MusicCreator a = new MusicCreator("2-01 Theme from Variations on _Twinkle, Twinkle, Little Star_ K.265.aif");
		a.generateMusic();
	}
}

