/**
* A queue of process control blocks
*
* @author William Jeff Lett
* @author Kevin DeBrito
*/

import java.util.*;

public class ProcessQueue {
	
	/** ArrayList of process control blocks */
	private ArrayList<PCB> pcb;
	
	/**
	* Constructor
	*/
	public ProcessQueue() {
		pcb = new ArrayList<PCB>();
			
	}	
	
	/**
	* Returns the largest job in this queue
	* @return The largest job's size
	*/
	public int largestJobSize() {
		int maxVal=-1;		
		Iterator<PCB> itr = pcb.iterator();
	   while (itr.hasNext()) {
	     PCB element = itr.next();
			if((element.getProcessSize()+element.getDataSize())>maxVal)
				maxVal=(element.getProcessSize()+element.getDataSize());
	   }
	   return maxVal;
	}	
	
	/**
	* Gets an arraylist of process control blocks
	* @return An ArrayList of <PCB>
	* @see PCB
	*/
	public ArrayList<PCB> getQueue() { return pcb; }	
	
	/**
	* Gets a process control block
	* @return A PCB
	* @see PCB
	*/
	public PCB getPcb(int i) { 
	return pcb.get(i); }
	
	/**
	* Sets the pcb
	* @param p The pcb the be set to
	*/
	public void setPcb(ArrayList<PCB> p) { pcb=p; }	
	
	/**
	* Returns the size of the pcb
	* @return The size of the pcb
	*/
	public int size() {return pcb.size();}
	
	/** 
	* Returns if the queue is empty
	* @return True if the queue is empty, false if not
	*/
	public boolean isEmpty() {return pcb.size()==0;}

	/**
	* Starts putting a process into disk
	* @param s The line to be put
	* @return The PCB that was created
	*/	
	public static int startProcess(String s) {
		PCB tempJob = new PCB();
		int id, size, priority, index;
		String idHex, sizeHex;
		String tempString;
		index = s.indexOf('B');
		
		tempString = s.substring(index+2);
		index = tempString.indexOf(' ');
		idHex = tempString.substring(0,index);
		id = Integer.parseInt(idHex, 16);
		
		tempString = tempString.substring(index+1);
		index= tempString.indexOf(' ');
		sizeHex = tempString.substring(0,index);
		size = Integer.parseInt(sizeHex, 16);
				
		tempString = tempString.substring(index+1);	
		priority = Integer.parseInt(tempString, 16);	
				
		tempJob.setPid(id);
		tempJob.setProcessSize(size);
		tempJob.setPriority(priority);
				
		Driver.getNewQueue().add(tempJob);	
		return Driver.getNewQueue().size()-1;	
	}		
	
	/**
	* Parses the start of data section
	* @param s The string to process
	* @param p The pcb to enter the info into
	*/
	public static void startData(String s, PCB p) {
		int index, inputSize, outputSize, tempSize;
		String tempString, inputHex, outputHex;
						
		index = s.lastIndexOf("Data")+5;
		tempString = s.substring(index);
		index = tempString.indexOf(' ');
		inputHex = tempString.substring(0,index);
		inputSize = Integer.parseInt(inputHex, 16);
						
		tempString = tempString.substring(index+1);
		index= tempString.indexOf(' ');
		outputHex = tempString.substring(0,index);
		outputSize = Integer.parseInt(outputHex, 16);
		
		tempString = tempString.substring(index+1);	
		tempSize = Integer.parseInt(tempString, 16);			
				
		p.setInputBufferSize(inputSize);
		p.setOutputBufferSize(outputSize);
		p.setTempBufferSize(tempSize);
	}
	
	/**
	* Ends the data section of this process
	* @param p The pcb whose data section we will end
	* @param l The length of the data section
	*/
	public static void endData(PCB p, int l) {
		p.setDataSize(l);
	}
	
	/** 
	* Gets and prints the statstics for a terminated queue
	*/
	public static void printStats(ProcessQueue p) {
		Iterator<PCB> itr=p.getQueue().iterator();
		long totalWaitTime=0, totalRunTime=0, totalCompletionTime=0;
		long avgWaitTime=0, avgRunTime=0, avgCompletionTime=0;		
		int totalIoCount=0, totalCyclesRan=0, totalCyclesWaited=0;
		int avgIoCount=0, avgCyclesRan=0, avgCyclesWaited=0,counter=0;
		while(itr.hasNext()) {
			PCB element=itr.next();
			totalWaitTime+=element.getRealWaitTime();
			totalRunTime+=element.getRealRunTime();
			totalCompletionTime+=element.getRealWaitTime()+element.getRealRunTime();
			totalIoCount+=element.getIoCount();
			totalCyclesRan+=element.getCyclesRan();
			totalCyclesWaited+=element.getCyclesWaited();
			++counter;
		}
		if(counter!=0) {
			avgWaitTime=totalWaitTime/counter;
			avgRunTime=totalRunTime/counter;
			avgCompletionTime=totalCompletionTime/counter;
			avgIoCount=totalIoCount/counter;
			avgCyclesRan=totalCyclesRan/counter;
			avgCyclesWaited=totalCyclesWaited/counter;
			System.out.println("***** Process Statistics *****");
			System.out.println("Average Cycles Ran      : "+avgCyclesRan);
			System.out.println("Average Running Time    : ("+avgRunTime+"ms)");
			System.out.println("Average I/O Requests    : "+avgIoCount);
			System.out.println("Average Cycles Waited   : "+avgCyclesWaited);		
			System.out.println("Average Waiting Time    : ("+avgWaitTime+"ms)");
			System.out.println("Average Completion Time : ("+avgCompletionTime+"ms)");
		}
	}	
	
	/** 
	* Prints this process queue pretty like
	* @param pcb The process queue to print pretty like
	*/
	public static void printPretty(ProcessQueue pcb) {
		if(pcb.size()>0)
			System.out.println("******************************************************** PROCESS STATISTICS ********************************************************");
		Iterator<PCB> itr = pcb.getQueue().iterator();
	   while (itr.hasNext()) {
	     PCB element = itr.next();
	     element.printPretty();
	   }
	}	
	
	/**
	* Prints this process queue
	* @param pcb The process queue to print
	*/
	public static void print(ProcessQueue pcb) {
		System.out.println("****** Process Queue ******");	
		Iterator<PCB> itr = pcb.getQueue().iterator();
	   while (itr.hasNext()) {
	     PCB element = itr.next();
			System.out.println("index:"+pcb.getQueue().indexOf(element));	      
	     element.print();
	   }
	}	
}