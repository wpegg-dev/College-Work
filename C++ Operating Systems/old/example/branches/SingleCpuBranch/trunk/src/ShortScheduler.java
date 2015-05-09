/**
* Short Term Scheduler for our OS
*
* @author William Jeff Lett
* @author Kevin DeBrito
*/

import java.util.*;

public class ShortScheduler {

		/**
	* Formats a hex string for RAM
	* @param s The hex string to format
	* @return The formatted hex string
	*/
	public static String hexFormat(String s) {
		if(s!=null && s!="") {
			s=s.toUpperCase();
			for(int i=0+s.length();i<8;++i)
				s="0"+s;
			return s;
		}
		return "00000000";
	}

	/**
	* Restore state
	* @param c The cpu to restore
	* @param p The process to restore from
	* @param r The ram to be read from
	*/
	public static void restoreState(CPU c, PCB p, RAM r) {
		//System.out.println("********** restoring state ****** CPU:"+c.toString());
		//p.print();	
		//System.out.println("cache size:"+c.getCacheSize()+" p.size():"+p.getSize());
		int start=p.getInstMemLoc(), end=start+p.getSize();
		//System.out.println("pc start"+start+", pc end:"+end);
		for(int i=0;i<c.getCacheSize();++i) {
			if(i<end-start) {
				//System.out.println("read"+r.read(start+i));
				c.getCache()[i]=hexFormat(r.read(start+i));	
			}
			else {
				c.getCache()[i]="00000000";	
			}
		}		
		c.setRegisters(p.getRegisters());
		c.setPc(p.getPc());
		//System.out.println("********** restoring state ****** CPU:"+c.toString());	
	}
	
	/**
	* Save state
	* @param c The cpu to be saved
	* @param p The process to be saved to 
	* @param r The ram to be read to
	*/
	public static void saveState(CPU c, PCB p, RAM r) {
		int start=p.getInstMemLoc(), end=start+p.getSize();
		for(int i=0;i<end-start;++i) {
			r.write(c.getCache()[i],start+i);	
		}		
		p.setRegisters(c.getRegisters());
		p.setPc(c.getPc());
	}

	/** 
	* Dispatches the next process according to the short term scheduling policy
	* @param p The ready queue to get the process from
	* @return The PCB of the process to run next
	*/
	public static PCB dispatch(ProcessQueue p) {
		
			switch(Driver.schedulingPolicy) {
				case FIFO:
					//System.out.println("FIFO Dispatching");// p.size="+p.getQueue().size());	
					return p.getPcb(0);		
				case SJF:
					int i=0,shortestIndex=-1,shortestLength=Integer.MAX_VALUE;
					Iterator<PCB> itr = p.getQueue().iterator();
	    			while (itr.hasNext()) {
	      			PCB element = itr.next();
						if(element.getProcessSize() < shortestLength) {
							shortestLength=element.getProcessSize();
							shortestIndex=i;	
						}				
	      			++i;
	    			}				
					//System.out.println("SJF Dispatching Size:"+shortestLength);
					return p.getPcb(shortestIndex);
				case LOW_PRIORITY:
					int j=0,lowestIndex=-1,lowestPriority=Integer.MAX_VALUE;					
					Iterator<PCB> itr2 = p.getQueue().iterator();
	    			while (itr2.hasNext()) {
	      			PCB element = itr2.next();
						if(element.getPriority() < lowestPriority) {
							lowestPriority=element.getPriority();
							lowestIndex=j;	
						}				
	      			++j;
	    			}		
										
					//System.out.println("Low Priority Dispatching Priority:"+lowestPriority);
					return p.getPcb(lowestIndex);
					
				case HIGH_PRIORITY:
					int k=0,highestIndex=-1,highestPriority=Integer.MIN_VALUE;					
					Iterator<PCB> itr3 = p.getQueue().iterator();
	    			while (itr3.hasNext()) {
	      			PCB element = itr3.next();
						if(element.getPriority() > highestPriority) {
							highestPriority=element.getPriority();
							highestIndex=k;	
						}				
	      			++k;
	    			}		
										
					//System.out.println("High Priority Dispatching Priority:"+highestPriority);
					return p.getPcb(highestIndex);
					
			}	
		return p.getPcb(0);	
			
	}	
	//return null;
}