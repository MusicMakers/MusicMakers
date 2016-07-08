// This second attempt is a success! However, its input and output is limited to midi files

import java.util.*;
import jm.JMC;
import java.io.*;
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

public class MidiMusicCreator implements JMC {
	public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
	private int m_order = 0;
	private HashMap<ArrayList<Integer>, ArrayList<Integer>> keyMap = new HashMap<ArrayList<Integer>, ArrayList<Integer>>(); 
	private HashMap<ArrayList<Double>, ArrayList<Double>> rhythmMap = new HashMap<ArrayList<Double>, ArrayList<Double>>(); 
	private double[] countList = {C, HNT, CD, CDD, M, DM, DDM, SEMIBREVE, DEMI_SEMI_QUAVER_TRIPLET,
			DEMI_SEMI_QUAVER, SEMI_QUAVER_TRIPLET, SQ, QT, DOTTED_SEMI_QUAVER, Q, CT, DEN, DDEN};
	private ArrayList<Integer> keyList = new ArrayList<Integer> ();
	private ArrayList<Integer> timeList = new ArrayList<Integer> ();
	private ArrayList<Double> rhythmList = new ArrayList<Double> ();
	private ArrayList<Integer> tempKeyList = new ArrayList<Integer> ();
	private ArrayList<Double> tempRhythmList = new ArrayList<Double> ();
	private HashMap<Integer, Double> countMap;
	
	int previousTime = 0;
	int currentTime = 0;
	
	MidiMusicCreator(String midiFile, int order) throws Exception {
		Sequence sequence = MidiSystem.getSequence(new File(midiFile));
		m_order = order;
		count c = new count(sequence.getResolution());
		countMap = c.getCount();
		process(sequence);		
	}
	
	public void process(Sequence s) throws Exception{
		// -1 because the first track is not useful (at least at the moment)
//		int numTracks = s.getTracks().length;
			Track track = s.getTracks()[1];
			for(int i=0; i<track.size(); i++){
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage){
					// convert to short message
					ShortMessage sm = (ShortMessage) message;
					if (sm.getCommand() == NOTE_ON){
						int key = sm.getData1();
						int velocity = sm.getData2();
	                 
						// velocity is 0 implies note fades right away
						if (velocity != 0) {
							currentTime = (int) event.getTick();
							if (currentTime != 0) {
								keyList.add(key);
//								System.out.println("current tick: " + currentTime);
								timeList.add(currentTime - previousTime);
//								System.out.println("Time: " + (currentTime - previousTime));
							}
							
//	                     	System.out.println("Key: " +key);
						}
//						else {
//							currentTime = (int) event.getTick();
//							System.out.println("current tick: " + currentTime);
//							timeList.add(currentTime - previousTime);
//							System.out.println("Time: " + (currentTime - previousTime));
//						}
					}
					previousTime = currentTime;
				}
			}
			// converting integer values to the actual rhythm values suing the 'count' class created
			for (int value: timeList){
//				System.out.println("Value: " + value);
				if (!countMap.containsKey(value)){
//					throw new Exception("There is no such rhythmic value "+ value);
					rhythmList.add(countList[new Random().nextInt(countList.length)]);
				}
				else {
//					System.out.println("Rhythm: " + countMap.get(value));
					rhythmList.add(countMap.get(value));
				}
			}
			createHashTable(keyList, rhythmList, m_order);
		}
	
	public void createHashTable(ArrayList<Integer> klist, ArrayList<Double> rlist, int order) {
		int i = 0;
		int nextInt = 0;
		double nextRhythm = 0;
		while (i<klist.size()-order) {
			for (int j=0; j<order; j++){
				tempKeyList.add(klist.get(j+i));
				tempRhythmList.add(rlist.get(j+i));
				if (j == order-1){
					nextInt = klist.get(order+i);
					nextRhythm = rlist.get(order+i);
				}
			}
//			System.out.println("Temp Key: " + tempKeyList.toString());
//			System.out.println("Rhythm Key: " + tempRhythmList.toString());
			
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
	}
	
	public void generateMusic(int numNotes, int tempo) {
		Score scr = new Score();
		Part part = new Part();
		Phrase phrase = new Phrase();
//		Note note = new Note();
		int [] pitchArray = new int[m_order + numNotes];
		double [] rhythmArray = new double[m_order + numNotes];
		
		// adding the first m_order notes to the start of the song
		for (int i=0; i<m_order; i++) {
//			note = new Note();
//			note.setPitch(keyList.get(i));
//			phrase.addNote(note);
			pitchArray[i] = keyList.get(i);
			rhythmArray[i] = rhythmList.get(i);
			tempKeyList.add(keyList.get(i));
			tempRhythmList.add(rhythmList.get(i));
		}
		// getting next note
		// if there is such a combination of notes
		int nextNote = 0;
		double nextRhythm = 0;
		int count = m_order;
		
		while (count<numNotes){
//			note = new Note();
			if (keyMap.containsKey(tempKeyList)){
				ArrayList<Integer> a = keyMap.get(tempKeyList);
				nextNote = a.get(new Random().nextInt(a.size()));
//				System.out.println("Have");
			}
			// if not
			else {
				nextNote = keyList.get(new Random().nextInt(keyList.size()));
//				System.out.println("Don't have");
			}
			if (rhythmMap.containsKey(tempRhythmList)){
				ArrayList<Double> b = rhythmMap.get(tempRhythmList);
				nextRhythm = b.get(new Random().nextInt(b.size()));
//				System.out.println("Have");
			}
			// if not
			else {
				nextRhythm = rhythmList.get(new Random().nextInt(rhythmList.size()));
//				System.out.println("Don't have");
			}
//			note.setPitch(nextNote);
//			phrase.addNote(note);
			pitchArray[count] = nextNote;
			rhythmArray[count] = nextRhythm;
			tempKeyList.remove(0);
			tempKeyList.add(nextNote);
			tempRhythmList.remove(0);
			tempRhythmList.add(nextRhythm);
//			System.out.println("Note: "+ nextNote);
			count++;
		}
		phrase.addNoteList(pitchArray, rhythmArray);
		part.addPhrase(phrase);
		scr.addPart(part);
		scr.setTempo(tempo);
		String filename = "Second Attempt.mid";
		Write.midi(scr, filename);
		System.out.println(filename);
	}
	
	public static void main(String[] args) throws Exception {		
		// filename, Markov Order
		if (args.length>0) {
			String filename = args[0];
			MidiMusicCreator t = new MidiMusicCreator(filename,2);
//			 numNotes, tempo
			t.generateMusic(100,100);
		}
		else {
			throw new Exception("There are no inputs");
		}
		

		
		
 }
}

