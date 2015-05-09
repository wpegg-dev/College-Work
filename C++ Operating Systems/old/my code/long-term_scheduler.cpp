/*Author: William Pegg
*Class: CS-3243 Spring 2012
*File Name: Long-termScheduler.cpp
*/
#include "long-term_scheduler.h"
#include "OS_Sim_Driver_1.h"
#include <iostream>
#define NULL 0;
using namespace std;

double percentRAM = 0;

LongScheduler::LongScheduler()
{
		current_process=-1;
		ram=NULL;
		disk;
}

LongScheduler::LongScheduler(RAM r, Disk d)
{
		current_process=-1;			
		ram=r;
		disk=d;
}

void LongScheduler::moveToRAM()
{
	if (!loadProcess())
		{
			return;
		}

		int* listlen;
		int listlength = OS_Sim_Driver_1.new_queue.list_length(listlen);
		for(current_process=0;current_process<listlength;++current_process) {	
			
			process = OS_Sim_Driver_1.new_queue[0];

			int inbuffer = process.getInputBufferSize(current_process);
			int outbuffer = process.getOutputBufferSize(current_process);
			int tempbuffer = process.getTempBufferSize(current_process);
			int dataSize = inbuffer + outbuffer + tempbuffer;														

			//Check to see if there's enough room left in ram.  return if not.
			if((process.getCodeSize(current_process)+dataSize*2>ram.free())) {
				cout << "No More room in RAM" << endl;
				return;
			}
			
			int ramInstLoc=0;//set initial process instuctions location in RAM
			int ramDataLoc=0;//set initial process data location in RAM
			
			//this will take the process instruction information from the PCB in the disk and move it to RAM
			for(int here=process.getCodeDiskAddress(current_process),i=process.getCodeSize(current_process);i>0;--i,++here) {
				if(process.getCodeStartRamAddress(current_process) == -1) {
					process.setCodeStartRamAddress(ram.write(disk.read(here)));	
				}
				else	
					ram.write(disk.read(here));			
			} 

			//this will take the process data inforation from the PCB in the disk to RAM
			for(int here=process.getDataDiskAddress(current_process),i=dataSize;i>0;--i,++here) {
				if(process.getInputBufferRamAddress(current_process) == -1) {
					process.setInputBufferAddress(current_process, ram.write(disk.read(here)));	
				}				
				else	
					ram.write(disk.read(here));
			}
						
			process.setStatus(current_process, READY);
			OS_Sim_Driver_1.new_queue.getQueue().remove(process);
			OS_Sim_Driver_1.ready_queue.getQueue().add(process);		
		}
}

void LongScheduler::end(PCB currentProcess, CPU c)
{
			current_process = 0;
			int inbuffer = process.getInputBufferSize(current_process);
			int outbuffer = process.getOutputBufferSize(current_process);
			int tempbuffer = process.getTempBufferSize(current_process);
			int dataSize = inbuffer + outbuffer + tempbuffer;

	for(int here=currentProcess.getCodeSize(),there=currentProcess.getDataDiskAddress(),i=dataSize;i>0;--i,++here,++there) 
	{
		disk.write(c.getCache()[here],there);	
	}
}

bool LongScheduler::loadProcess()
{
	int p = OS_Sim_Driver_1.new_queue.list_length(n);
	if( p > 0)
   		return true;
   	else
   		return false;
}

