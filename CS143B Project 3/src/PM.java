//Timothy Pranoto
//38964311
//CS143B Project 3

public class PM {
	private class VA {
		int s,p,w,sp;
		public int getS() {
			return s;
		}
		public int getP() {
			return p;
		}
		public int getW() {
			return w;
		}
		public int getSP() {
			return sp;
		}
		public VA(int addr) {
			String bi = Integer.toBinaryString(addr);
			int length = bi.length();
			for (int i = length; i < 32; i++){
				bi = "0"+bi;
			}
			s = Integer.parseInt(bi.substring(4, 13),2);
			p = Integer.parseInt(bi.substring(13,23),2);
			w = Integer.parseInt(bi.substring(23),2);
			sp = Integer.parseInt(bi.substring(4,23),2);
		}
	}
	private class BM {
		long[] bitmap;
		long[] mask;
		long[] mask2;
		
		public BM() {
			bitmap = new long[32];
			mask = new long[32];
			mask2 = new long[32];
			mask[31] = 1;
			for (int i = 30; i >= 0; i--){
				mask[i] = mask[i+1]<<1;
			}
			for (int i = 0; i < 32; i++) {
				mask2[i] = ~mask[i];
			}
		}

		public void set(int i) {
			bitmap[i/32] = bitmap[i/32] | mask[i%32];
		}
		public int find() {
			for (int i = 0; i < 32; i++) {
				for (int j = 0; j < 32; j++) {
					long test = bitmap[i] & mask[j];
					if (test == 0) {
						return (i*32 ) + j;
					}
				}
			}
			return -1;
		}

		public int findNext() {
			for (int i = 0; i < 32; i++) {
				for (int j = 0; j < 31; j++) {
					long test1 = bitmap[i] & mask[j];
					long test2 = bitmap[i] & mask[j+1];
					if (test1 == 0 && test2 == 0) {
						return (i*32 ) + j;
					}
				}
			}
			return -1;
		}
		
	}
	int[] frame;
	int size = 1024*512;
	boolean change;
	BM bm;
	TLB tlb;
	
	
	public PM(boolean b) {
		change = b;
		this.tlb = new TLB();
		bm = new BM();
		frame = new int[size];
		for (int i = 0; i < 512; i++){
			frame[i] = 0;
		}
		for (int i = 512; i < size; i++){
			frame[i] = -1;
		}
		bm.set(0);
	}

	public void init(String line1, String line2) {
		String[] param1 = line1.split(" ");
		for (int i = 0; i < param1.length; i+=2){
			int segment = Integer.parseInt(param1[i]);
			int addr = Integer.parseInt(param1[i+1]);
			frame[segment] = addr;
			if (addr != -1) {
				for (int j = addr; j < addr + 1024; j++) {
					frame[j] = 0;
				}
				int k = addr/512;
				bm.set(k);
				bm.set(k+1);
			}
		}
		
		String[] param2 = line2.split(" ");
		for (int i = 0; i < param2.length; i+=3){
			int index = frame[Integer.parseInt(param2[i+1])];
			int page = Integer.parseInt(param2[i]);
			int addr = Integer.parseInt(param2[i+2]);
			if (index > 0){
				frame[index + page] = addr;
				if (addr != -1){
					for (int j = addr; j < addr + 512; j++){
						frame[j] = 0;
					}
					bm.set(addr/512);
				}
			}
		}
	}

	private String writeTLB(VA va) {
		int sp = va.getSP();
		if (tlb.search(sp)){
			return "h "+(tlb.highest()+va.getW());
		}
		String F = writeNormal(va);

		if (!F.equals("pf")&&!F.equals("err")){
			int f = Integer.parseInt(F);
			tlb.update(sp,f);
		}
		return "m "+F;
	}

	private String readTLB(VA va) {
		int sp = va.getSP();
		if (tlb.search(sp)){
			return "h "+(tlb.highest()+va.getW());
		}
		int s = frame[va.getS()];
		int p = frame[s+va.getP()];
		String F = readNormal(va);
		if (!F.equals("pf")&&!F.equals("err")){
			if (s != -1 && p != -1)
				tlb.update(sp,p);	
		}

		return "m "+readNormal(va);
	}
	

	private String readNormal(VA va) {
		int s = frame[va.getS()];
		if (s == -1){
			return "pf";
		}
		if (s == 0){
			return "err";
		}
		int p = frame[s+va.getP()];
		if (p == -1){
			return "pf";
		}
		if (p == 0){
			return "err";
		}
		int w = p+va.getW();
		return ""+w;
	}
	
	private String writeNormal(VA va) {
		int s = frame[va.getS()];
		if (s == -1){
			return "pf";
		}
		int p = frame[s+va.getP()];
		if (p == -1){
			return "pf";
		}
		if (s == 0){
			int i = bm.findNext();
			bm.set(i);
			bm.set(i+1);
			int F = i * 512;
			frame[va.getS()] = F;
			for ( int j = F; j < F+1024;j++){
				frame[j] = 0;
			}
			int j = bm.find();
			bm.set(j);
			int P = j*512;
			frame[F+va.getP()] = P;
			for (int k = P; k < P + 512; k++){
				frame[k] = 0;
			}
		} else if (p == 0){
			int i = bm.find();
			bm.set(i);
			int F = i * 512;
			frame[frame[va.getS()]+va.getP()] = F;
			for (int k = F; k < F + 512; k++){
				frame[k] = 0;
			}
		}
		int w = frame[frame[va.getS()]+va.getP()] + va.getW();
		return ""+w;
	}
	
	public String translate(String line) {
		String ret = "";
		String[] words = line.split(" ");
		VA va = null;
		if (change == false){
			for (int i = 0; i < words.length; i+=2){
				va = new VA(Integer.parseInt(words[i+1]));
				if (Integer.parseInt(words[i]) == 0)
					ret += readNormal(va)+" ";
				else
					ret += writeNormal(va) + " ";
			}
		}
		else{
			for (int i = 0; i < words.length; i+=2){
				va = new VA(Integer.parseInt(words[i+1]));
				if (Integer.parseInt(words[i]) == 0)
					ret += readTLB(va)+" ";
				else
					ret += writeTLB(va) + " ";

			}
		}
		return ret;
	}
}