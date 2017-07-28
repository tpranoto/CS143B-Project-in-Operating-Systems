//Timothy Pranoto
//38964311
//CS143B Project 3

public class TLB {
	private class TLBEntry {
		int count;
		int sp = -1;
		int frame = -1;
		

		public int getFreq() {
			return count;
		}
		public int getSP() {
			return sp;
		}
		public int getFrame() {
			return frame;
		}
		public void setFreq(int count) {
			this.count = count;
		}
		public void setSp(int sp) {
			this.sp = sp;
		}
		public void setFrame(int frame) {
			this.frame = frame;
		}

		public void dec(){
			count--;
			if (count < -1)
				count = -1;
		}
	}
	TLBEntry[] entries;
	
	public TLB(){
		entries = new TLBEntry[4];
		for (int i = 0; i < 4; i++){
			entries[i] = new TLBEntry();
		}
	}
	
	public boolean search(int sp){
		int index = 0;
		for (int i = 0; i < 4; i++){
			if (entries[i].getSP() == sp){
				index = i;
				for (int j = 0; j< 4; j++){
					if (entries[j].getFreq() > entries[index].getFreq()){
						entries[j].dec();
					}
				}
				entries[index].setFreq(3);
				return true;
			}
		}
		return false;
	}
	
	public int highest(){
		for (int i = 0; i < 4; i++){
			if (entries[i].getFreq() == 3){
				return entries[i].getFrame();
			}
		}
		return 0;
	}
	public void update(int sp, int frame){
		for (int i = 0; i < 4; i++){
			entries[i].dec();
		}
		for (int i = 0; i < 4; i++){
			if (entries[i].getFreq() == -1){
				entries[i].setFreq(3);
				entries[i].setSp(sp);
				entries[i].setFrame(frame);
				return;
			}
		}
	}
	
}