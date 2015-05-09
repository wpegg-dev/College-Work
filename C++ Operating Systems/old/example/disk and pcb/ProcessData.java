package process_control;

import log_files.ErrorLog;


/**
 * Process Class:  This class represents a process within a virtual operating system.
 * This object will be utilized by the PCB (Program Control Block) to hold vital information
 * that will be used to determine efficiency and other metrics for later validation.
 * 
 * Methods:
 * 			Process();
 * 			setProcName(string);
 * 			setMemStartLoc(int);
 * 			setProgInstructCount(int);
 * 			setBaseReg(int);
 * 			setJobPriority(int);
 * 			setInputBuffer(int);
 * 			setOutputBuffer(int);
 * 			setTempBuffer(int);
 * 			getProcName();
 * 			getMemStartLoc();
 * 			getProgInstructCount();
 * 			getBaseReg();
 * 			getJobPriority();
 * 			getInputBuffer();
 * 			getOutputBuffer();
 * 			getTempBuffer();
 * 
 * Variables:
 * 			String procName;
 * 			int memStartLocation;
 * 			int progInstructCount;
 * 			int jobPriority;
 * 			int inputBuffer;
 * 			int outputBuffer;
 * 			int tempBuffer;
 * 			Enum Status;
 * 
 * @author Brandon Mckune
 * @version 1.0
 * 
 *	Date started: Sept. 16, 2008
 */
public class ProcessData 
{
	/*
	The CPU is supported by a PCB, which may have the following (suggested) structure:

	typedef struct PCB {
		cpuid:				// information the assigned CPU (for multiprocessor system)
		struct state:		// record of environment that is saved on interrupt
							// including the pc, registers, permissions, buffers, caches, active  
							// pages/blocks
							 * code-size;
		struct registers:	// accumulators, index, general
		struct sched:		// burst-time, priority, queue-type, time-slice, remain-time
		struct accounts:	// cpu-time, time-limit, time-delays, start/end times, io-times
		struct memories:	// page-table-base, pages, page-size
							// base-registers Ð logical/physical map, limit-reg
		struct progeny:		// child-procid, child-code-pointers
		parent: ptr;		// pointer to parent (if this process is spawned, else ÔnullÕ)
		struct resources:	// file-pointers, io-devices Ð unitclass, unit#, open-file-tables
		status;				// {running, ready, blocked, new}
		status_info:		// pointer to Ôready-list of active processesÕ or 
							// Ôresource-list on blocked processesÕ
		priority: integer;	// of the process
	}
	 */
	//Process Id.  This is located on line // JOB PID
	private int pid;

	//Instruction Disk locations & length
	private int memStartLocation;

	//Given job instruction count by mid number in //JOB # # #
	private int instructCount;

	//Instruction RAM base-register
	private int baseReg;

	//Given job priorty by last Hexidecimal in //JOB # # #
	private int jobPriority;

	//Number of words in each buffer.
	private int inputBuffer;
	private int outputBuffer;
	private int tempBuffer;

	//Program Status
	private enum Status{ ready, waiting, running, terminated, created };
	private int procState;
	
	private State state;

	public final static int PROCESS_READY = 0;
	public final static int PROCESS_WAIT = 1;
	public final static int PROCESS_RUN = 2;
	public final static int PROCESS_TERMINATE = 3;
	public final static int PROCESS_DEFAULT = 4;

	/**
	 * Default Constructor:: The method sets all object variables to values that
	 * are invalid to any process within the scope of this project.
	 */
	protected ProcessData()
	{
		pid = 0;
		memStartLocation = -1;
		instructCount = 0;
		baseReg = -1;
		jobPriority = 0;
		inputBuffer = -1;
		outputBuffer = -1;
		tempBuffer = -1;
		state = new State();
		procState = ProcessData.PROCESS_DEFAULT;
	}
	
	protected int getPID()
	{
		return pid;
	}
	
	protected int getMemStartLocation()
	{
		return memStartLocation;
	}
	
	protected int getInstructCount()
	{
		return instructCount;
	}
	
	protected int getBaseReg()
	{
		return baseReg;
	}
	
	protected int getJobPriority()
	{
		return jobPriority;
	}
	
	protected int getInputBuffer()
	{
		return inputBuffer;
	}
	
	protected int getOutputBuffer()
	{
		return outputBuffer;
	}
	
	protected int getTempBuffer()
	{
		return tempBuffer;
	}
	
	protected int getProcState()
	{
		return procState;
	}
	
	protected void setProcessID(int id)
	{
		if(id > 0)
		{
			pid = id;
		}
		else
		{
			ErrorLog.getInstance().writeError("Process.java::setProcessID(int) || >> ID # < 1. -- ");
			throw new IllegalArgumentException();
		}
	}

	protected void setMemStartLocation(int location)
	{
		if( location >= 0)
		{
			memStartLocation = location;
		}
		else
		{
			ErrorLog.getInstance().writeError("Process.java::setMemStartLocation(int) || >> "
					+ "Memory location invalid. -- ");
			throw new IllegalArgumentException();
		}
	}

	protected void setInstructCount(int count)
	{
		if( count > 0)
		{
			instructCount = count;
		}
		else
		{
			ErrorLog.getInstance().writeError("Process.java::setInstructCount(int) || >> "
					+ "Instruction count input < 1. -- ");
			throw new IllegalArgumentException();
		}
	}

	protected void setBaseReg(int reg)
	{
		if(reg >= 0)
		{
			baseReg = reg;
		}
		else
		{
			ErrorLog.getInstance().writeError("Process.java::setBaseReg(int) || >> "
					+ "Base Register number invalid.  reg < 0. -- " );
			throw new IllegalArgumentException();
		}
	}

	protected void setJobPriority(int priority)
	{
		if(priority > 0)
		{
			jobPriority = priority;
		}
		else
		{
			ErrorLog.getInstance().writeError("Process.java::setJobPriority(int) || >> "
					+ "priority < 1. -- ");
			throw new IllegalArgumentException();
		}
	}

	protected void setInputBuffer(int buff)
	{
		if(buff >= 0)
		{
			inputBuffer = buff;
		}
		else
		{
			ErrorLog.getInstance().writeError("Process.java::setInputbuffer(int) || >> "
					+ "buff < 0. -- ");
			throw new IllegalArgumentException();
		}
	}

	protected void setOutputBuffer(int outBuff)
	{
		if(outBuff >= 0)
		{
			outputBuffer = outBuff;
		}
		else
		{
			ErrorLog.getInstance().writeError("Process.java::setOutputBuffer(int) || >> "
					+ "outBuff < 0. --");
			throw new IllegalArgumentException();
		}
	}

	protected void setTempBuffer(int tempBuff)
	{
		if(tempBuff >= 0)
		{
			tempBuffer = tempBuff;
		}
		else
		{
			ErrorLog.getInstance().writeError("Process.java::setOutputBuffer(int) || >> "
					+ "tempBuff < 0. -- ");
			throw new IllegalArgumentException();
		}
	}

	protected void setProcState(int state)
	{
		if(0 <= state && state <= 4)
		{
			procState = state;
		}
		else
		{
			ErrorLog.getInstance().writeError("Process.java::setProcState(int) || >> "
					+ "Invalid parameter value. 0 <= X <= 4. -- ");
			throw new IllegalArgumentException();
		}
	}
	
	public State getState()
	{
		return state;
	}
	
	protected String getProcessState()
	{
		switch(procState)
		{
		case(0):
			return "Ready";
		case(1):
			return "Waiting";
		case(2):
			return "Running";
		case(3):
			return "Terminated";
		case(4):
			return "Undefined Process";
		default:
			return null;	
		}
	}
}

