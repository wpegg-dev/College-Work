#include "ST_Sched.h"
#include "LT_Sched.h"
#include "disk.h"
#include "pcb.h"
#include "PCB_types.h"
#include "file_loader.h"
#include "RAM.h"
#include <queue>
#include <iostream>
#include <fstream>
#include <sstream>
#define NULL 0;
extern queue<int> ready_queue;
extern queue<int> wait_queue;
extern queue<int> new_queue;
extern queue<int> running_queue;
extern queue<int> terminated_queue; 
extern pcb pcb_table;
extern Disk disk1;
extern RAM ram;
using namespace std;



LT_Sched::LT_Sched(void)
{
	//schedule_policy = PRIORITY;
	//schedule_policy = FCFS;
	//schedule_policy = SJF;
}

LT_Sched::~LT_Sched(void)
{
}
void prioritySort(void){
	queue<int> pri_queues[20];
	int current_pri = 0;
	for(int pid_cntr = 1;pid_cntr <=30;pid_cntr++){
		current_pri = pcb_table.getPriority(pid_cntr);
		pri_queues[current_pri].push(pid_cntr);
	}
	for(int pri_cnt = 16; pri_cnt >0; pri_cnt--){
		while(!pri_queues[pri_cnt].empty()){
			new_queue.push(pri_queues[pri_cnt].front());
			pri_queues[pri_cnt].pop();
		}
	}
}

void sjfSort(void){
	queue<int> sjf_queues[30];
	int index = 0;
	for(int pid_cntr = 1;pid_cntr <=30;pid_cntr++){
		index = pcb_table.getCodeSize(pid_cntr);
		sjf_queues[index].push(pid_cntr);
	}
	for(int cnt = 0; cnt <30; cnt++){
		while(!sjf_queues[cnt].empty()){
			new_queue.push(sjf_queues[cnt].front());
			sjf_queues[cnt].pop();
		}
	}
}

void LT_Sched::schedInit(void)
{
	ram.clearRam();
	//loop through PIDs (1 through 30)
#ifdef FCFS_POLICY
	for(int pid_ctr = 1;pid_ctr <= 30; pid_ctr++)
	{
		//  put PID number in new queue
		new_queue.push(pid_ctr);
	}
#endif
#ifdef SJF_POLICY
	sjfSort();
#endif
#ifdef PRIORITY_POLICY
	prioritySort();
#endif
}
void LT_Sched::saveToDisk(void)
{
	for(int i = 1; i <= 30;i++){
	//  if PID in terminate queue
	//    copy code and data from ram to disk 
	//    (perhaps only have to copy data buffers)
		if(pcb_table.getStatus(i) == TERMINATED)
		{
			for(int j = 0; j < (pcb_table.getCodeSize(i) + 44); j++)
			{ 
				int ram_line_number = pcb_table.getCodeStartRamAddress(i) + j;
				int disk_line_number = pcb_table.getCodeDiskAddress(i)+j;
				char line_to_save[9];
				line_to_save[8] = NULL;
				strncpy(line_to_save,ram.readCharLine(ram_line_number),8);
				disk1.writeLine(
					line_to_save ,
					disk_line_number);
			}
			pcb_table.setStatus(i,TERMINATED_SAVED);
		}
	}
}
void LT_Sched::loadJobs(void)
{
#ifdef SJF_POLICY
	loadJobsSJF();
	return;
#endif
#ifdef PRIORITY_POLICY
	loadJobsSJF();
	return;
#endif

	ram.clearRam();
	for(int i = 1; i <= 30;i++)
	{

		if (pcb_table.getStatus(i) == NEW)
		{
			int p_size = pcb_table.getCodeSize(i) + 44;
				//pcb_table.getInputBufferSize(i) + 
				//pcb_table.getOutputBufferSize(i) + 
				//pcb_table.getTempBufferSize(i);
			//    if (PID codesize + 
			//       data size(input,output,&temp buffs))<="remaining" ram size
			//       copy code and data to ram
			//       remove PID from new queue
			//       put pid in ready queue
			//       update pid_table status to 'ready'
			//       update pid_table ram code start address
			//       update pid_table ram input buffer address
			//       update pid_table ram output buffer address
			//       update pid_table ram temp buffer address
			if (p_size <= (ram.howManyFreeBytes() / 8))
			{
				ready_queue.push(new_queue.front());
				new_queue.pop();

				pcb_table.setStatus(i,READY);
				string disk_line;
				pcb_table.setCodeStartAddress(i,ram.getNextFreeIndex()/8);
				for(int k = 0; k < p_size; k++)
				{
					disk_line = disk1.readLine(pcb_table.getCodeDiskAddress(i) + k);
					ram.writeNBytesToNextFreeAdr(disk_line.c_str(), 8);
				}
				pcb_table.setInputBufferAddress(
					i,pcb_table.getCodeStartRamAddress(i) 
					+ pcb_table.getCodeSize(i));
				pcb_table.setOutputBufferAddress(
					i,pcb_table.getInputBufferRamAddress(i) + 20);
				pcb_table.setTempBufferAddress(
					i,pcb_table.getOutputBufferRamAddress(i) + 12);
			}
			//    else //not enough room remaining in ram
			//       do nothing
			else
			{
				//do nothing
			}
		}
	}
}
void LT_Sched::loadJobsSJF(void)
{
	int i = 0;
	ram.clearRam();
	
	while(!new_queue.empty())
	{
		i = new_queue.front();

		if (pcb_table.getStatus(i) == NEW)
		{
			int p_size = pcb_table.getCodeSize(i) + 44;
				//pcb_table.getInputBufferSize(i) + 
				//pcb_table.getOutputBufferSize(i) + 
				//pcb_table.getTempBufferSize(i);
			//    if (PID codesize + 
			//       data size(input,output,&temp buffs))<="remaining" ram size
			//       copy code and data to ram
			//       remove PID from new queue
			//       put pid in ready queue
			//       update pid_table status to 'ready'
			//       update pid_table ram code start address
			//       update pid_table ram input buffer address
			//       update pid_table ram output buffer address
			//       update pid_table ram temp buffer address
			if (p_size <= (ram.howManyFreeBytes() / 8))
			{
				ready_queue.push(i);
				new_queue.pop();

				pcb_table.setStatus(i,READY);
				string disk_line;
				pcb_table.setCodeStartAddress(i,ram.getNextFreeIndex()/8);
				for(int k = 0; k < p_size; k++)
				{
					disk_line = disk1.readLine(pcb_table.getCodeDiskAddress(i) + k);
					ram.writeNBytesToNextFreeAdr(disk_line.c_str(), 8);
				}
				pcb_table.setInputBufferAddress(
					i,pcb_table.getCodeStartRamAddress(i) 
					+ pcb_table.getCodeSize(i));
				pcb_table.setOutputBufferAddress(
					i,pcb_table.getInputBufferRamAddress(i) + 20);
				pcb_table.setTempBufferAddress(
					i,pcb_table.getOutputBufferRamAddress(i) + 12);
			}
			//    else //not enough room remaining in ram
			//       do nothing
			else
			{
				break;
			}
		}
	}
}

void LT_Sched::shedule(void)
{
	//pseudo code for long-term scheduler functions

	//ram_copy-start

	//loop through PIDs (1 through 30)
	for(int i = 1; i <= 30;i++){
	//  if PID in terminate queue
	//    copy code and data from ram to disk 
	//    (perhaps only have to copy data buffers)
		if(pcb_table.getStatus(i) == TERMINATED)
		{
			for(int j = 0; j < (pcb_table.getCodeSize(i) + 44); j++)
			{ 
				int ram_line_number = pcb_table.getCodeStartRamAddress(i) + j;
				int disk_line_number = pcb_table.getCodeDiskAddress(i)+j;
				disk1.writeLine(
					ram.readCharLine(ram_line_number) ,
					disk_line_number);
			}
		}





	//  else if PID in new queue
		else if (pcb_table.getStatus(i) == NEW)
		{
			int p_size = pcb_table.getCodeSize(i) + 44;
				//pcb_table.getInputBufferSize(i) + 
				//pcb_table.getOutputBufferSize(i) + 
				//pcb_table.getTempBufferSize(i);
			//    if (PID codesize + 
			//       data size(input,output,&temp buffs))<="remaining" ram size
			//       copy code and data to ram
			//       remove PID from new queue
			//       put pid in ready queue
			//       update pid_table status to 'ready'
			//       update pid_table ram code start address
			//       update pid_table ram input buffer address
			//       update pid_table ram output buffer address
			//       update pid_table ram temp buffer address
			if (p_size <= (ram.howManyFreeBytes() / 8))
			{
				new_queue.pop();
				ready_queue.push(i);
				pcb_table.setStatus(i,READY);
				string disk_line;
				pcb_table.setCodeStartAddress(i,ram.getNextFreeIndex()/8);
				for(int k = 0; k < p_size; k++)
				{
					disk_line = disk1.readLine(pcb_table.getCodeDiskAddress(i) + k);
					ram.writeNBytesToNextFreeAdr(disk_line.c_str(), 8);
				}
				pcb_table.setInputBufferAddress(
					i,pcb_table.getCodeStartRamAddress(i) 
					+ pcb_table.getCodeSize(i));
				pcb_table.setOutputBufferAddress(
					i,pcb_table.getInputBufferRamAddress(i) + 12);
				pcb_table.setTempBufferAddress(
					i,pcb_table.getOutputBufferRamAddress(i) + 12);
			}
			//    else //not enough room remaining in ram
			//       do nothing
			else
			{
				//do nothing
			}

		}



	}

	//end loop



//ram_copy-end




}