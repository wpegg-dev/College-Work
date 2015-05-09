/** 
* A disk / harddrive emulator

* @author William Jeff Lett
* @author Kevin DeBrito
*/

public class Disk {
	
	/** Array to hold our data. */
	private static String [] disk;
	
	/** Size of disk */
	private static int size;
	
	/** The next free word in this disk */	
	private static int nextFreeWord;
		
	/**
	* Creates a disk of default size
	*/
	public Disk() {
		disk = new String[Driver.DISK_SIZE];
		size = Driver.DISK_SIZE;
		nextFreeWord = 0;
	}
	
	/**
	* Creates a disk of a specific size
	* @param s Size of disk to be created
	*/
	public Disk(int s) {
		disk = new String[s];	
		size = disk.length;
		nextFreeWord = 0;
	}
	
	/** 
	* Returns the size of this disk
	* @return The size of this disk
	*/
	public static int size() { return size; }

	/** 
	* Returns true if the disk is full, false if not
	* @return	True if the disk is full, false if not
	*/
	private static boolean full() { return nextFreeWord==size; }
	
	/**
	* Returns true if the disk is empty, false if not
	* @return True if the disk is empty, false if not
	*/		
	private static boolean empty() { return nextFreeWord==0; }

	/**
	* Returns the space left in disk
	* @return The space left in disk
	*/
	public static int free() {
		return Driver.DISK_SIZE-nextFreeWord;	
	}	

	/**
	* Returns a string value of a certain part of the disk
	* @param b Where to begin the string
	* @param e Where the end the string
	*/
	public String toString(int b, int e) {
		String d = "Disk Contents "+b+"-"+e+":\n";
		for(int i=b;i<e;++i) {
			int width=12;
			if(disk[i]==null)
				d+="00000000";
			else					
				d+=disk[i];	
			if(i%width!=width-1)
				d+=",";
			if(i%width==width-1)
				d+=":"+i+"\n";	
		}
		d+="\nnextFreeWord="+nextFreeWord;
		return d;
	}


	/** 
	* Returns a string value of this disk
	* @return A string value of this disk
	*/
	public String toString() {
		String r = "Disk Contents:";
		for(int i=0;i<disk.length;++i) {
			if(disk[i]!=null)
				r+="\n["+i+"]="+disk[i];
		}
		r+="\nnextFreeWord="+nextFreeWord;
		return r;
	}
	
	/**
	* Returns a wider representation of the disk (to see it all on 1 screen for debugging)
	* @return A string value of this disk
	*/
	public String toWideString() {
		String d="Disk["+size+"]:\n";
		for(int i=0;i<disk.length;++i) {
			int width=12;
			if(disk[i]==null)
				d+="00000000";
			else					
				d+=disk[i];	
			if(i%width!=width-1)
				d+=",";
			if(i%width==width-1)
				d+=":"+i+"\n";	
		}
		return d;
	}
	
	/**
	* Erases the contents of this disk
	*/
	public static void erase() {
		for(int i=0;i<disk.length;++i) {
			disk[i]="00000000";	
		}	
		nextFreeWord=0;
	}
	
	/**
	* Write a word to disk
	* @param word The word to be written
	* @return Where the word was written on disk
	*/ 
	public static int write(String word) { 
		if(full()) {
			System.out.println("HD Full");
			return 0;		
		}		
		else {
			disk[nextFreeWord] = word;	
			++nextFreeWord;
			return nextFreeWord-1;
		}	
	}

	/**
	* Reads from an address on disk
	* @param a The address to read from
	* @return The word that was read
	*/
	public static String read(int a) {
		if(a < Driver.DISK_SIZE)
			return disk[a];
		else {
			System.out.println("Error: Outside disk");
			return "";		
		}	
	}	
	
}