// Its input and output is limited to midi files

import java.util.*;
import jm.JMC;

import java.io.File;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

//import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;

public class MidiMusicCreatorImproved implements JMC {
	public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
	private int m_order = 0;
	private int instrument = 0;
	private int instrumentCount = 0;
	private int previousTime = 0;
	private int currentTime = 0;
		
	private double[] countList = {C, HNT, CD, CDD, M, DM, DDM, SEMIBREVE, DEMI_SEMI_QUAVER_TRIPLET,
			DEMI_SEMI_QUAVER, SEMI_QUAVER_TRIPLET, SQ, QT, DOTTED_SEMI_QUAVER, Q, CT, DEN, DDEN};
	private ArrayList<Integer> keyList = new ArrayList<Integer> ();
	private ArrayList<Integer> timeList = new ArrayList<Integer> ();
	private ArrayList<Double> rhythmList = new ArrayList<Double> ();
	private ArrayList<Integer> tempKeyList = new ArrayList<Integer> ();
	private ArrayList<Double> tempRhythmList = new ArrayList<Double> ();
	
	private HashMap<Integer, Double> countMap;
	private HashMap<ArrayList<Integer>, ArrayList<Integer>> keyMap = new HashMap<ArrayList<Integer>, ArrayList<Integer>>(); 
	private HashMap<ArrayList<Double>, ArrayList<Double>> rhythmMap = new HashMap<ArrayList<Double>, ArrayList<Double>>(); 
	
	/**
	 * Constructor (takes in user inputs)
	 * @param midiFile
	 * @param order
	 * @param numNotes
	 * @param tempo
	 * @param s
	 * @throws Exception
	 */
	MidiMusicCreatorImproved(String midiFile, int order, int numNotes, int tempo, Score s) throws Exception {
		Sequence sequence = MidiSystem.getSequence(new File(midiFile));
		m_order = order;
		count c = new count(sequence.getResolution()); // for the rhythm
		countMap = c.getCount();
		process(sequence, numNotes, tempo, s);		
	}
	
	/**
	 * Method to extract the sequence of notes and counts of notes into 2 different ArrayLists (keylist and rhythmlist)
	 * @param s
	 * @param numNotes
	 * @param tempo
	 * @param scr
	 */
	public void process(Sequence s, int numNotes, int tempo, Score scr){
		// getting number of different parts there are in the midi file. The parts are called tracks in java.
		int numTracks = s.getTracks().length;
		// Start from 1 because the first track does not seem to have anything useful (at least at the moment)
		for (int j=1; j<numTracks; j++){
			Track track = s.getTracks()[j];
			instrumentCount++;
			// Each track contains information about the music itself
			for(int i=0; i<track.size(); i++){
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage){
					// Convert to short message
					ShortMessage sm = (ShortMessage) message;
					// NOTE_ON occurs when a note is pressed
					if (sm.getCommand() == NOTE_ON){
						int key = sm.getData1(); // note
						int velocity = sm.getData2(); 	                 
						// How information is stored in each short message is that each note will be pressed and released. 
						// When it is pressed, it will have a certain non-zero velocity. When it is released, its velocity is 0.
						if (velocity != 0) {
							// To determine the length of a note (ie crochet, minim, semibreve etc...)
							currentTime = (int) event.getTick();
							if (currentTime != 0) {
								keyList.add(key);
								timeList.add(currentTime - previousTime);
							}						
						}
					}
					// This command allows us to determine the type of instrument used in the midi file
					else if (sm.getCommand() == 0xc0) {
						instrument = sm.getData1();
					}
					previousTime = currentTime;
				}
			}
			// Converting integer values to the actual rhythm values suing the 'count' class created
			for (int value: timeList){
				// When there is no such count (for some reason), return a random count.
				if (!countMap.containsKey(value)){
					rhythmList.add(countList[new Random().nextInt(countList.length)]);
				}
				else {
					// getting corresponding count from the hashmap
					rhythmList.add(countMap.get(value));
				}
			}
			// preparing the hashTable for the Markov model.
			createHashTable(keyList, rhythmList, m_order, numNotes, tempo, scr);
			keyList.clear();
			rhythmList.clear();
			countMap.clear();
			timeList.clear();
		}		
	}
	
	/**
	 * Method to create hashmaps for Markov Model (keyMap and rhythmMap)
	 * @param klist
	 * @param rlist
	 * @param order
	 * @param numNotes
	 * @param tempo
	 * @param scr
	 */
	public void createHashTable(ArrayList<Integer> klist, ArrayList<Double> rlist, int order, int numNotes, int tempo, Score scr) {
		int i = 0;
		int nextInt = 0;
		double nextRhythm = 0;
		while (i<klist.size()-order) {
			for (int j=0; j<order; j++){
				tempKeyList.add(klist.get(j+i));
				tempRhythmList.add(rlist.get(j+i));
				// End of iteration. Need to get the next note/rhythm
				if (j == order-1){
					nextInt = klist.get(order+i);
					nextRhythm = rlist.get(order+i);
				}
			}
			
			if (!keyMap.containsKey(tempKeyList)){
				ArrayList<Integer> a = new ArrayList<Integer> ();
				a.add(nextInt);
				keyMap.put(tempKeyList, a);
			}
			else {
				ArrayList<Integer> a = keyMap.get(tempKeyList);
				a.add(nextInt);
				keyMap.remove(tempKeyList);
				keyMap.put(tempKeyList, a);
			}
			
			if (!rhythmMap.containsKey(tempRhythmList)){
				ArrayList<Double> b = new ArrayList<Double> ();
				b.add(nextRhythm);
				rhythmMap.put(tempRhythmList, b);
			}
			else {
				ArrayList<Double> b = rhythmMap.get(tempRhythmList);
				b.add(nextRhythm);
				rhythmMap.remove(tempRhythmList);
				rhythmMap.put(tempRhythmList, b);
			}
			tempKeyList.clear();
			tempRhythmList.clear();
			i++;
		}
		generateMusic(numNotes, tempo, scr);
	}
	
	/**
	 * Method that creates/compose the different parts and phrases that will be inserted into the score scr.
	 * @param numNotes
	 * @param tempo
	 * @param scr
	 */
	public void generateMusic(int numNotes, int tempo, Score scr) {
		
		Part part = new Part("Inst", instrument, instrumentCount);
		Phrase phrase = new Phrase();
		
		int [] pitchArray = new int[m_order + numNotes];
		double [] rhythmArray = new double[m_order + numNotes];
		
		// Adding the first m_order notes to the start of the song
		for (int i=0; i<m_order; i++) {
			pitchArray[i] = keyList.get(i);
			rhythmArray[i] = rhythmList.get(i);
			tempKeyList.add(keyList.get(i));
			tempRhythmList.add(rhythmList.get(i));
		}
		
		// getting next note
		int nextNote = 0;
		double nextRhythm = 0;
		int count = m_order;
		
		while (count<numNotes){
			// if there is such a combination of notes
			if (keyMap.containsKey(tempKeyList)){
				ArrayList<Integer> a = keyMap.get(tempKeyList);
				nextNote = a.get(new Random().nextInt(a.size()));
			}
			// if not
			else {
				nextNote = keyList.get(new Random().nextInt(keyList.size()));
			}
			if (rhythmMap.containsKey(tempRhythmList)){
				ArrayList<Double> b = rhythmMap.get(tempRhythmList);
				nextRhythm = b.get(new Random().nextInt(b.size()));
			}
			// if not
			else {
				nextRhythm = rhythmList.get(new Random().nextInt(rhythmList.size()));
			}
			
			pitchArray[count] = nextNote;
			rhythmArray[count] = nextRhythm;
			tempKeyList.remove(0);
			tempKeyList.add(nextNote);
			tempRhythmList.remove(0);
			tempRhythmList.add(nextRhythm);
			count++;
		}
		
		// Everything is done 
		phrase.addNoteList(pitchArray, rhythmArray);
		part.addPhrase(phrase);
		scr.addPart(part);
		scr.setTempo(tempo);
		tempKeyList.clear();
		tempRhythmList.clear();
		
	}
	
	public static void main(String[] args) throws Exception {		
		
		if (args.length>0) {
			String filename = args[0];
			String outputName = args[1];
			int order = Integer.parseInt(args[2]);
			int numNotes = Integer.parseInt(args[3]);
			int tempo = Integer.parseInt(args[4]);
			Score scr = new Score();
			
			// filename, Markov Order, num notes, tempo, score
			MidiMusicCreatorImproved t = new MidiMusicCreatorImproved(filename, order, numNotes, tempo, scr);
			Write.midi(scr, outputName);
			System.out.println(outputName);
		}
		else {
			throw new Exception("There are no/invalid inputs");
		}
//		MidiMusicCreatorImproved t = new MidiMusicCreatorImproved("brahms-lullaby-wiegenlied.mid", 2, 100, 100, scr);
//		Write.midi(scr, "MarkovMusic.mid");
	}
}

