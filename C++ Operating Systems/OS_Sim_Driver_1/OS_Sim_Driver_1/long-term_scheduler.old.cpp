/*Author: William Pegg
*Class: CS-3243 Spring 2012
*File Name: Long-termScheduler.cpp
*/
#include "long-term_scheduler.h"
#include "disk.h"
#include "pcb.h"
#include "PCB_types.h"
#include "file_loader.h"
#include "RAM.h"
#include <iostream>
#include <queue>
#define NULL 0;
extern queue<int> ready_queue;
extern queue<int>  wait_queue;
extern queue<int> new_queue;
extern queue<int> running_queue;
extern queue<int> terminated_queue; 
extern pcb pcb_table;
extern Disk disk1;
extern RAM ram;
using namespace std;



LongScheduler::LongScheduler()
{
		//current_process=0;//because PCB starts with pid 1
		//ram;
		//disk1;
}



void LongScheduler::moveToRAM()
{
	for(int i = 1; i < 2027; i++)
	{
		//move all jobs into the new_queue
		//tlj - you might consider moving this outside of the above for loop or maybe put it in to another function that runs earlier.
		for(int j = 1; j<=30; j++)
		{
			new_queue.push(j);//where j is the pid for the process held in the PCB
		}
		
		//move jobs into RAM
		//tlj - I don't think you meant to use 'i' in the following call to pcb_table.getCodeSize().
		if( (pcb_table.getCodeSize(i) + 44) < ram.howManyFreeBytes())//check to make sure that there is enough space in RAM to put jobs
		{
			//remove job from new_queue and place in ready_queue
			ram.writeNBytesToNextFreeAdr(disk1.readLine(i).c_str(),8);
			//tlj put PID number into "ready" queue
			//tlj remove same PID number from "new" queue.
			
		}
		else
		{
			//check to see if there are jobs in the new_queue and if so runmoveToRAM()
			
		}
	}
	
	
	
	
	//if (!loadProcess())
	//	{
	//		return;
	//	}

	//	int* listlen;
	//	int listlength = OS_Sim_Driver_1.new_queue.list_length(listlen);
	//	for(current_process=0;current_process<listlength;++current_process) {	
	//		
	//		process = OS_Sim_Driver_1.new_queue[0];

	//		int inbuffer = process.getInputBufferSize(current_process);
	//		int outbuffer = process.getOutputBufferSize(current_process);
	//		int tempbuffer = process.getTempBufferSize(current_process);
	//		int dataSize = inbuffer + outbuffer + tempbuffer;														

	//		//Check to see if there's enough room left in ram.  return if not.
	//		if((process.getCodeSize(current_process)+dataSize*2>ram.free())) {
	//			cout << "No More room in RAM" << endl;
	//			return;
	//		}
	//		
	//		int ramInstLoc=0;//set initial process instuctions location in RAM
	//		int ramDataLoc=0;//set initial process data location in RAM
	//		
	//		//this will take the process instruction information from the PCB in the disk and move it to RAM
	//		for(int here=process.getCodeDiskAddress(current_process),i=process.getCodeSize(current_process);i>0;--i,++here) {
	//			if(process.getCodeStartRamAddress(current_process) == -1) {
	//				process.setCodeStartRamAddress(ram.write(disk.read(here)));	
	//			}
	//			else	
	//				ram.write(disk.read(here));			
	//		} 

	//		//this will take the process data inforation from the PCB in the disk to RAM
	//		for(int here=process.getDataDiskAddress(current_process),i=dataSize;i>0;--i,++here) {
	//			if(process.getInputBufferRamAddress(current_process) == -1) {
	//				process.setInputBufferAddress(current_process, ram.write(disk.read(here)));	
	//			}				
	//			else	
	//				ram.write(disk.read(here))
	//		}
	//					
	//		process.setStatus(current_process, READY);
	//		OS_Sim_Driver_1.new_queue.getQueue().remove(process);
	//		OS_Sim_Driver_1.ready_queue.getQueue().add(process);		
	//	}
}

//void LongScheduler::end(PCB currentProcess, CPU c)
//{
//			current_process = 0;
//			int inbuffer = process.getInputBufferSize(current_process);
//			int outbuffer = process.getOutputBufferSize(current_process);
//			int tempbuffer = process.getTempBufferSize(current_process);
//			int dataSize = inbuffer + outbuffer + tempbuffer;
//
//	for(int here=currentProcess.getCodeSize(),there=currentProcess.getDataDiskAddress(),i=dataSize;i>0;--i,++here,++there) 
//	{
//		disk.write(c.getCache()[here],there);	
//	}
//}

//bool LongScheduler::loadProcess()
//{
//	int p = OS_Sim_Driver_1.new_queue.list_length(n);
//	if( p > 0)
//   		return true;
//   	else
//   		return false;
//}

