//william Pegg
//LongTermSched.cpp
//
#include "longTermShed.h"
#include "pcb.h"

LongSched::LongSched()
{
	current_process = -1; // this will set the initail process to -1 so the next process run should be the first item in the PCB
	ram; //still need code for RAM
	Disk; // initialize the Disk information 
}


//may or may not be used...
LongSched::LongSched(RAM r, Disk d)
{
 current_process = -1; // this will set the initail process to -1 so the next process run should be the first item in the PCB
 ram = r; //still need code for RAM
 disk = d; // will initialize the paramter d to the disk 
}



void LongSched::moveToRAM()
{
	if(!loadProcess()) // to check if there are any processes to load
	{
		return;
	}

	int totalProcesses = 30; // this is because there will only be 30 processes in the PCB

	for(current_process = 1;current_process <= totalProcesses; ++current_process) // this will be a loop that will insert the processes into RAM
	{
		process = process.
	}

}


