

import java.util.*;
import java.io.*;

/**
* Loader for our OS
*
* @author William Jeff Lett
* @author Kevin DeBrito
*/
public class Loader {
	
	/**
	* Loads the data from disk
	* @param d The disk to load from
	* @return A ProcessQueue of processes in Disk but not RAM.
	*/
	public static int load(Disk d, ProcessQueue unReadyQueue) {
		BufferedReader in;
		//ProcessQueue unReadyQueue=null;
		try {
			//unReadyQueue = new ProcessQueue();
			boolean dataStart=false, instStart=false;
			in = new BufferedReader(new FileReader(Driver.FILE_2_NAME));	
			String line;
			int processIndex, lines=0;
			PCB thisPcb=null;
			while ((line = in.readLine()) != null) { 	
				if(line.contains("JOB")) {
					processIndex=unReadyQueue.startProcess(line);
					thisPcb=unReadyQueue.getPcb(processIndex);									
					instStart=true;
				}
				else if(line.contains("Data")) {
					lines=0;
					unReadyQueue.startData(line,thisPcb);
					dataStart=true;
				}
				else if(line.contains("END")) {
					unReadyQueue.endData(thisPcb,lines);
				} 
				else if(line.length() > 0) {
					int here=d.write(line.substring(2,10));
					++lines;
					if(instStart) {
						thisPcb.setInstDiskLoc(here);
						thisPcb.setPc(here);
						instStart=false;	
					}
					if(dataStart) {
						thisPcb.setDataDiskLoc(here);
						dataStart=false;						
					}
				}			
  			}	
			in.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return unReadyQueue.largestJobSize();
	}
}