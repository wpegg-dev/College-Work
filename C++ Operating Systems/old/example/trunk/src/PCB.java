
import java.util.*;

/**
* A Process Control Block class
*
* @author William Jeff Lett
* @author Kevin DeBrito
*/
public class PCB {

	/**
	* Default Constructor
	*/
	public PCB() {
		pid=-1;
		status = Driver.Status.NEW;
		registers = new int[Driver.NUM_REGISTERS];	
		for(int i=0;i<Driver.NUM_REGISTERS;++i) {
			registers[i]=0;	
		}		
		priority=-1;
		pc=-1;
		processSize=-1;
		inputBufferSize=-1;
		outputBufferSize=-1;
		tempBufferSize=-1;
		dataDiskLoc=-1;
		instDiskLoc=-1;
		dataSize=-1;
		dataMemLoc=-1;
		instMemLoc=-1;	
		cyclesRan=0;
		cyclesWaited=0;
		realRunTime=0;
		realWaitTime=0;
		ioCount=0;
	}
	
	/** Process ID */
	private int pid;

	/** Status of this PCB */
	private Driver.Status status;
	
	/** Registers for this PCB */
	private int[] registers;
	
	/** Priority of this Process */
	private int priority;
	
	/** Program Counter */
	private int pc;
	
	/** Size of the instructions of this process */
	private int processSize;
	
	/** The size of the input buffer */
	private int inputBufferSize;
		
	/** The size of the output buffer */
	private int outputBufferSize;
	
	/** The size of the temp buffer */
	private int tempBufferSize;	
	
	/** Input Buffer */
	private String[] inputBuffer;
	
	/** Output Buffer */
	private String[] outputBuffer;

	/** Temp Buffer */
	private String[] tempBuffer;
	
	/** The length of the data for this process */
	private int dataSize;
	
	/** The location of the data for this process in RAM */
	private int dataMemLoc;
	
	/** The location of the data for this process in Disk */
	private int dataDiskLoc;
	
	/** The location of the instructions for this process in RAM */	
	private int instMemLoc;	
	
	/** The location of the instructions for this process in disk */
	private int instDiskLoc;
	
	/** The real waiting time for this process */
	private long realWaitTime;
	
	/** The real run time for this process */
	private long realRunTime;
	
	/** The cycles waited for this process */
	private int cyclesWaited;
	
	/** The cycles ran for this process */
	private int cyclesRan;
	
	/** The number of IO requests for this process */
	private int ioCount;
	


	/* Getters */
	
	/**
	* Gets the process id
	* @return The process ID of this PCB
	*/
	public int getPid() { return pid; }
	
	/** 
	* Gets the status of this process
	* @return The status of this process
	*/	
	public Driver.Status getStatus() { return status; }
	
	/**
	* Gets the registers of this process
	* @return The registers of this process
	*/	
	public int[] getRegisters() { return registers; }
	
	/**
	* Gets the priority of this process
	* @return The priority of this process
	*/	
	public int getPriority() { return priority; }
	
	/** 
	* Gets the program counter for this process
	* @return The program counter for this process
	*/	
	public int getPc() { return pc; }
	
	/**
	* Gets the size of process instructions
	* @return The size of the process instructions
	*/	
	public int getProcessSize() { return processSize; }
	
	/**
	* Gets the location of the data in disk
	* @return The location of the data in disk
	*/	
	public int getDataDiskLoc() { return dataDiskLoc; }

	/** 
	* Gets the location of the process instructions in disk
	* @return The location of the process instructions in disk
	*/	
	public int getInstDiskLoc() { return instDiskLoc; }

	/** 
	* Gets the size of the data section for this process
	* @return The size of the data section for this process
	*/
	public int getDataSize() { return dataSize; }

	/**	
	* Gets the location of the data section of this process in RAM
	* @return The location of the data section of this process in  RAM
	*/
	public int getDataMemLoc() { return dataMemLoc; }

	/**
	* Gets the location of the process instructions in RAM
	* @return The location of the process instructions in RAM
	*/
	public int getInstMemLoc() { return instMemLoc; }

	/**
	* Gets the size of the input buffer
	* @return The size of the input buffer
	*/
	public int getInputBufferSize() { return inputBufferSize; }

	/**
	* Gets the size of the output buffer
	* @return The size of the output bugger
	*/
	public int getOutputBufferSize() { return outputBufferSize; }

	/**
	* Gets the size of the temp buffer
	* @return The size of the temp buffer
	*/
	public int getTempBufferSize() { return tempBufferSize; }
	
	/**
	* Gets the input buffer
	* @return The input buffer
	*/
	public String[] getInputBuffer() { return inputBuffer; }
	
	/**
	* Gets the output buffer
	* @return The output buffer
	*/
	public String[] getOutputBuffer() { return outputBuffer; }
	
	/** 
	* Gets the temp buffer
	* @return The temp buffer
	*/
	public String[] getTempBuffer() { return tempBuffer; }
	
	/**
	* Gets the total size of the process
	* @return The total size of the process
	*/
	public int getSize() { return processSize+dataSize; }
						
	/**
	* Gets the total waiting time of this process
	* @return The time this process has waited to run in milliseconds
	*/
	public long getRealWaitTime() { return realWaitTime; }
	
	/**
	* Gets the total running time of this process
	* @return The time this process has ran in milliseconds
	*/
	public long getRealRunTime() { return realRunTime; }
	
	/**
	* Gets the waiting cycles of this process
	* @return The number of cycles this process has been waiting
	*/
	public int getCyclesWaited() { return cyclesWaited; }

	/**
	* Gets the running cycles of this process
	* @return The number of cycles this process has been running
	*/
	public int getCyclesRan() { return cyclesRan; }
		
	/**
	* Gets the number of IO requests for this process
	* @return Returns the number of IO requests
	*/
	public int getIoCount() { return ioCount; }
	
	
	/* Setters */	
	
	/** 
	* Sets the process ID
	* @param p The new process ID
	*/
	public void setPid(int p) { pid=p; }

	/**
	* Sets the status of the process
	* @param s The new status
	*/
	public void setStatus(Driver.Status s) { status=s; }

	/**
	* Sets the registers of the process
	* @param r The new registers
	*/
	public void setRegisters(int[] r) { registers=r; } 

	/** 
	* Sets the priority of the process
	* @param p The new priority
	*/
	public void setPriority(int p) { priority=p; }

	/**
	* Sets the program counter
	* @param p The new program counter
	*/
	public void setPc(int p) { pc=p; }

	/**
	* Sets the size of the process instructions
	* @param s The new size
	*/
	public void setProcessSize(int s) { processSize=s; }
	
	/**
	* Sets the location of the data in disk
	* @param d The new location
	*/
	public void setDataDiskLoc(int d) { dataDiskLoc=d; }

	/**
	* Sets the location of the process instructions in disk
	* @param i The new location
	*/
	public void setInstDiskLoc(int i) { instDiskLoc=i; }

	/** 
	* Sets the length of the data section of this process
	* @param d The new length
	*/
	public void setDataSize(int d) { dataSize=d; }
	
	/**
	* Sets the location of the data in RAM
	* @param d The new location
	*/
	public void setDataMemLoc(int d) { dataMemLoc=d; }

	/**
	* Sets the location of the process instructions in memory
	* @param i The new location
	*/
	public void setInstMemLoc(int i) { instMemLoc=i; }	

	/** 
	* Sets the size of the input buffer
	* @param s The new size
	*/
	public void setInputBufferSize(int s) { inputBufferSize=s; }
	
	/**
	* Sets the size of the output buffer
	* @param s The new size
	*/
	public void setOutputBufferSize(int s) { outputBufferSize=s; }
	
	/**
	* Sets the size of the temp buffer
	* @param s The new size
	*/
	public void setTempBufferSize(int s) { tempBufferSize=s; }
	
	/**
	* Sets the input buffer
	* @param b The new buffer
	*/
	public void setInputBuffer(String[] b) { inputBuffer=b; }
	
	/**
	* Sets the output buffer
	* @param b The new buffer
	*/
	public void setOutputBuffer(String[] b) { outputBuffer=b; }

	/**
	* Sets the temp buffer
	* @param b The new buffer
	*/
	public void setTempBuffer(String[] b) { tempBuffer=b; }
	
	/**
	* Sets the total waiting time of this process
	* @param l The time this process has waited to run in milliseconds
	*/
	public void setRealWaitTime(long l) { realWaitTime=l; }
	
	/**
	* Sets the total running time of this process
	* @param l The time this process has ran in milliseconds
	*/
	public void setRealRunTime(long l) { realRunTime=l; }
	
	/**
	* Sets the waiting cycles of this process
	* @param i The number of cycles this process has been waiting
	*/
	public void setCyclesWaited(int i) { cyclesWaited=i; }

	/**
	* Sets the running cycles of this process
	* @param i The number of cycles this process has been running
	*/
	public void setCyclesRan(int i) { cyclesRan=i; }
		
	/**
	* Sets the number of IO requests for this process
	* @param i The number to set the IO count to
	*/
	public void setIoCount(int i) { ioCount=i; }
	
	/** Increments the cycles waited for this process */
	public void addCycleWaited() { ++cyclesWaited; }
	
	/** Increments the cycles ran for this process */
	public void addCycleRan() { ++cyclesRan; }
	
	/** Increments the IO requests for this process */
	public void addIo() { ++ioCount; }
	
	/** Adds a time to the real run time of this process */
	public void addRunTime(long l) { realRunTime+=l; }	
	
	/** Adds a time to the real wait time of this process */	
	public void addWaitTime(long l) { realWaitTime+=l; }	
	
	/**
	* Puts extra spaces to make a string a certain length
	* @param s The string to format
	* @param i The length to make the string
	*/
	public String addSpaces(String s, int i) {
		if(s.length() >= i)
			return s;
		else {
			for(int j=s.length();j<i;++j) {
				s+=" ";	
			}
			return s;
		}
	}	

	/**
	* Prints this prettier so jeff likes the way it looks
	*/
	public void printPretty() {
		String s="";
		s+=addSpaces(("Process ID: "+pid),16);
		s+=addSpaces(("Process Size: "+processSize),18);
		s+=addSpaces(("Cycles Ran: "+cyclesRan),17);
		s+=addSpaces(("Cycles Waited:"+cyclesWaited),20);
		s+=addSpaces(("I/O Requests: "+ioCount),18);
		s+=addSpaces(("Run Time: ("+realRunTime+"ms)"),17);
		s+=addSpaces(("Waiting Time: ("+realWaitTime+"ms)"),22);
		s+=addSpaces(("Completion Time: ("+(realRunTime+realWaitTime)+"ms)"),17);
		System.out.println(s);
	}	
	
	/**
	* Prints this process
	*/
	public void print() {
		System.out.println("************ PCB **************");
		System.out.println("Process Id:"+pid);	
		System.out.println("Status:"+status);	
		String regString = "Registers: ["+registers[0];
		for(int i=1;i<Driver.NUM_REGISTERS;++i) {
				regString += ", " + registers[i];
		}
		regString += "]";
		System.out.println(regString);
		System.out.println("Priority:"+priority);	
		System.out.println("Program Counter:"+pc);
		System.out.println("Process Size:"+processSize);
		System.out.println("Data Size:"+dataSize);
		System.out.println("Location of Data on Disk:"+dataDiskLoc);
		System.out.println("Location of Instructions on Disk:"+instDiskLoc);
		System.out.println("Location of Data in RAM:"+dataMemLoc);
		System.out.println("Location of Instruction in RAM:"+instMemLoc);
		System.out.println("Process Run Time: ("+realRunTime+"ms)");
		System.out.println("Process Wait Time: ("+realWaitTime+"ms)");	
		System.out.println("Process Completion Time: ("+(realRunTime+realWaitTime)+"ms)");
		System.out.println("Cycles to run:"+cyclesRan);
		System.out.println("I/O Operations:"+ioCount);
	//	System.out.println("T:"+tempBufferSize);								
	}
	
}