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



LongScheduler::LongScheduler()
{
		current_process = 1;// this will start the process at the 1st pid
}



void LongScheduler::moveToRAM()
{
	ofstream longSchedLog_RAM;
	longSchedLog_RAM.open("longSchedLog-RAM.txt", fstream::out);

	int j = current_process;

	// write the processes into RAM
	for(j; j<=30; j++)//should loop 30 times for the number of totalt processes
		{

			
			longSchedLog_RAM << "PID: " << j << endl;
			longSchedLog_RAM << "Free Space in RAM: " << ram.howManyFreeBytes() << endl;

			if(((pcb_table.getCodeSize(j) + 44)*8 ) < ram.howManyFreeBytes())//check to make sure that there is enough room to write the whole process to RAM
			{

				longSchedLog_RAM << "Process size: " << ((pcb_table.getCodeSize(j)+44)*8) << endl;

				for(int k = 0; k <= (pcb_table.getCodeSize(j)+44); k++)//loop to write the entire process to RAM
				{
					ram.writeNBytesToNextFreeAdr(disk1.readLine(k).c_str(),8);//writing process to RAM
				}
				
				
				longSchedLog_RAM << "Successfully transferred process " << j << " into RAM" << endl;
				longSchedLog_RAM << "Amount of space left in RAM: " << ram.howManyFreeBytes() << endl;

				//move pid for process that was just written to RAM from the new queue to the running queue
				running_queue.push(new_queue.front());
				pcb_table.setStatus(j,RUNNING);//to update the status of the process in the PCB
				longSchedLog_RAM << "Successfully added process to running queue" << endl;

				//remove pid for process that was just written to RAM from new queue
				new_queue.pop();
				longSchedLog_RAM << "Successfully removed process from new queue" << endl << endl;
			}
			else
			{
				
				current_process = j;// this will be so that the next time the moveToRAM functon is called, it will know where to start so
				                    // not to duplicate the processes
				return;
			}
		}

	longSchedLog_RAM.close();//close ostream
}


void LongScheduler::moveToDisk()
{
	ofstream longSchedLog_Disk;
	longSchedLog_Disk.open("longSchedLog-Disk.txt", fstream::out);
	
		//check PCB for terminated processes
	for(int i = 1; i<=30; i++)
	{

	longSchedLog_Disk << "PID: " << i << endl;
	longSchedLog_Disk << "Amount of free space in RAM: " << ram.howManyFreeBytes() << endl;

		//if process is terminated, continue to remove process information from RAM
		if(pcb_table.getStatus(i) == TERMINATED)
		{
			longSchedLog_Disk << "Terminated PID: " << i << endl;

			//remove process from RAM int Disk
			for(int j = 0; j < (pcb_table.getCodeSize(i) + 44); j++)
			{ 
				int ram_line_number = pcb_table.getCodeStartRamAddress(i) + j;
				int disk_line_number = pcb_table.getCodeDiskAddress(i)+j;
				disk1.writeLine(ram.readCharLine(ram_line_number),	disk_line_number);

				longSchedLog_Disk << "Process Sucessfully written to Disk" << endl;

			}
		}

		terminated_queue.pop();
		longSchedLog_Disk << "Process Successfully removed from terminated queue." << endl;


		//reallocate space in RAM for removed process
		//Troy can you write a function to allow for me to pass the current process number 
		//and the function will allocate the appropriate amount of space into RAM

		longSchedLog_Disk << "Amount of free space in RAM: " << ram.howManyFreeBytes() << endl << endl;
	}

	longSchedLog_Disk.close();
}



void LongScheduler::loadProcesses()
{
	for(int i = 30; i<=30; i++)
	{
		new_queue.push(i);//where i is the pid for the process held in the PCB
	}
}