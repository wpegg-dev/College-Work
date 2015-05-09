/*
 * 	Brandon McKune
 * 	OS-Project Phase 1
 * 	PCB 
 */

package process_control;

import java.util.*;
import log_files.ErrorLog;


public class PCB implements PCBInterfaceFactory
{
	private List<ProcessData> pcb;
	private static PCB database;
	
	private PCB()
	{
		pcb = new ArrayList<ProcessData>();
	}
	
	public static synchronized PCB getInstance()
	{
		if(database == null)
		{
			database = new PCB();
		}
		
		return database;
	}
	
	public int addJob(String job)
	{
		ProcessData temp = new ProcessData();
		
		if(job != null)
		{
			temp = PCB.parse(job);
			pcb.add(temp);
			return temp.getPID();
		}
		else
		{
			ErrorLog.getInstance().writeError("PCB::addJob || >> Input parameter is null.");
			throw new IllegalArgumentException();
		}
	}
	
	public void addData(String data, int jobNum)
	{
		ProcessData temp = new ProcessData();
		ProcessData job;
		
		if(data != null)
		{
			temp = PCB.parse(data);
			if( 0 < jobNum && jobNum < pcb.size())
			{
				temp = pcb.get(jobNum--);
				if(temp.getPID() == jobNum)
				{
					
				}
			}
			else
			{
				ErrorLog.getInstance().writeError("PCB::addData || >> Parameter job num out of bounds.");
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			ErrorLog.getInstance().writeError("PCB::addData || >> data parameter = null.");
			throw new IllegalArgumentException();
		}
	}
	
	private static ProcessData parse(String s)
	{
		//Parsing scoped variables.
		int index = 0;
		String initialStr = s;
		String temp = "";
		String hexStr = "";
		
		//Process variables defined.
		ProcessData process = new ProcessData();
		int pid, jobPriority, size;
		int inputBuff, outputBuff, tempBuff;
		List<Character> hexList = new ArrayList<Character>();
		
		
		if(initialStr.contains("JOB"))
		{
			//Start parsing
			index = initialStr.indexOf('B');
			
			//Cut off "// JOB " from string.
			temp = initialStr.substring(index + 2);
			index = temp.indexOf(' ');
			
			pid = Integer.parseInt(temp.substring(0, index));
			temp = temp.substring(index + 1);
			index = temp.indexOf(' ');
			hexStr = temp.substring(0, index);
				size = Integer.parseInt(hexStr, 16);
				
			temp = temp.substring(index + 1);
			jobPriority = Integer.parseInt(temp);
			
			process.setProcessID(pid);
			process.setJobPriority(jobPriority);
			process.setInstructCount(size);
			
			return process;
		}
		else if(initialStr.contains("Data"))
		{
			//do stuff
		}
		else
		{
			System.err.println("parse::PCB||Invalid input parameter; non-PCB element entered.");
			throw new IllegalArgumentException();
		}
		
		//fix when complete
		return null;
	}

	public static void main(String [] args)
	{
		PCB temp = PCB.getInstance();
		int num = temp.addJob("// JOB 2 1D 9");
		
		System.out.println(num);
	}
}
