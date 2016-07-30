package jMusicTest;
import java.util.HashMap;
import jm.JMC;

public class count implements JMC{
	public HashMap<Integer, Double> ct = new HashMap<Integer, Double> ();
	
	count(int base){
		ct.put(base, C); // 1.0
		ct.put((int) base*4/3, HNT); // 1.333333333333333333
		ct.put((int) (base*1.5), CD); // 1.5
		ct.put((int) (base*1.75), CDD); // 1.75
		ct.put((int) base*2, M); // 2.0
		ct.put((int) base*3, DM); // 3.0
		ct.put((int) (base*3.5), DDM); // 3.5
		ct.put((int) base*4, SEMIBREVE); // 4.0
		ct.put((int) base/12, DEMI_SEMI_QUAVER_TRIPLET); // 0.08333333333333333
		ct.put((int) base/8, DEMI_SEMI_QUAVER); // 0.125
		ct.put((int) base/6, SEMI_QUAVER_TRIPLET); // 0.16666666666666666666
		ct.put((int) base/4, SQ); // 0.25
		ct.put((int) base/3, QT); // 0.3333333333333333333
		ct.put((int) base/8*3, DOTTED_SEMI_QUAVER); // 0.375
		ct.put((int) base/2, Q); // 0.5
		ct.put((int) base/3*2, CT); // 0.6666666666666666666
		ct.put((int) base/4*3, DEN); // 0.75
		ct.put((int) base/8*7, DDEN); // 0.875
		
	}
	
	public HashMap<Integer, Double> getCount() {
		return ct;
	}
}
