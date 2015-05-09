/**
* Long Scheduler for our OS
* 
* @author William Jeff Lett
* @author Kevin DeBrito
*/

import java.util.ArrayList;

public class LongScheduler 
{
	/** Ram */	
	RAM ram;
	
	/** Disk */
	Disk disk;
	
	/** Our current process's index */
	private int current_process;

	/** Our current Process */
	PCB process;

	///percentage of ram being used
	double percentRAM = 0;

	/** Average */
	public double average;

	/** Percent */
	public double percent;
	
	///Default Constructor. starts the ready queue and current process value
	public LongScheduler ()
	{
		current_process=-1;
		ram=null;
		disk=null;			
	}
	
	/** 
	* Constructor with ram and disk
	* @param r The ram to use
	* @param d The disk to use
	*/
	public LongScheduler(RAM r, Disk d) {
		current_process=-1;			
		ram=r;
		disk=d;	
	}	
	
	/**
	* Iterates through the newQueue and places processes into RAM and into the readyqueue
	*/
	public void start()
	{
		if (!loadProcess())
		{
			//System.out.println("all processes done");
			return;
		}
		for(current_process=0;current_process<Driver.newQueue.size();++current_process) {	
			
			process = Driver.newQueue.getPcb(0);
						
			//Check to see if there's enough room left in ram.  return if not.
			if((process.getProcessSize()+process.getDataSize()>ram.free())) {
				System.out.println("No more room in RAM");
				return;
			}
			
			int ramDataLoc=0, ramInstLoc=0;
			
			for(int here=process.getInstDiskLoc(),i=process.getProcessSize();i>0;--i,++here) {
				if(process.getInstMemLoc() == -1) {
					process.setInstMemLoc(ram.write(disk.read(here)));	
				}
				else	
					ram.write(disk.read(here));			
			} 
			for(int here=process.getDataDiskLoc(),i=process.getDataSize();i>0;--i,++here) {
				if(process.getDataMemLoc() == -1) {
					process.setDataMemLoc(ram.write(disk.read(here)));	
				}				
				else	
					ram.write(disk.read(here));
			}
						
			process.setStatus(Driver.Status.READY);
			Driver.newQueue.getQueue().remove(process);
			Driver.readyQueue.getQueue().add(process);		
		}
	}
	
	/**
	* This function returns whether there is processes left to load
	* @return This returns true is there are processes to load
	*/ 
	public boolean loadProcess() 
   {
   	if(Driver.newQueue.size() > 0)
   		return true;
   	else
   		return false;
   }
   
   /**
   * Returns the percentage of RAM used currently or overall 
	* @param per Sets whether to return current RAM % or overall. (0 for current, 1 for overall)
   */     
   public void percentageRAM(int per)
   {
    	switch (per)
     	{
   	  	case 0:
        		average=(ram.size()-ram.free());
        		percent=average/ram.size(); 		
        		System.out.println("Percentage of RAM used: " +percent*100);
        		break;
        	case 1:
        		Driver.totalPercent=Driver.sumPercent/Driver.totalCycleCounter;
        		System.out.println("Total percentage used on RAM:   " + Driver.totalPercent*100);
        		break;
    	}
	}
    
}
        

