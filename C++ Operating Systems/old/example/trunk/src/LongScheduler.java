import java.util.ArrayList;

/**
* Long Scheduler for our OS
* 
* @author William Jeff Lett
* @author Kevin DeBrito
*/
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
	public void start() {
		if (!loadProcess())
		{
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
	* Writes a process's cache from RAM back into the disk
	*/
	public void end(PCB currentProcess, CPU c) {	
		for(int here=currentProcess.getProcessSize(),there=currentProcess.getDataDiskLoc(),i=currentProcess.getDataSize();i>0;--i,++here,++there) {
			disk.write(c.getCache()[here],there);	
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
 
}
        

