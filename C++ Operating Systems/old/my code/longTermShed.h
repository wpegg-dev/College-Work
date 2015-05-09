/*Author: William Pegg
--Class: CS-3243 Spring 2012
--File Name: longTermSched.h
*/

#pragma once
#include "disk.h"
#include "pcb.h"
#include "PCB_types.h"
#include "file_loader.h"
#include "queue.h"


class LongSched {
//class LongTermSched {
	 
public:
	//-----------------------------
	//VARIABLES
	//-----------------------------
	//RAM	
	RAM ram;
	// Disk
	Disk disk;
	// Our current Process
	PCB process;
	//percentage of ram being used
	double percentRAM;
	queue newQueue, readyQueue, runningQueue, waitingQueue, terminatedQueue;

	//-----------------------------
	//FUNCTIONS
	//-----------------------------
	//Default Constructor. starts the ready queue and current process value
	LongSched();
	
	//-----------------------------
	// Constructor with ram and disk where r The ram to use and d The disk to use
	//-----------------------------
	LongSched(RAM r, Disk d);	
	
	//------------------------------------------------------------------------------
	//Will scan the PCB to check and put the processes in the appropriate queues
	//------------------------------------------------------------------------------
	void setQueues();


	//----------------------------------------------------------------------------------------
	// Iterates through the newQueue and places processes into RAM and into the readyqueue
	//----------------------------------------------------------------------------------------
	void moveToRAM();
	
	//----------------------------------------------------------
	// Gets the process from the Disk
	//----------------------------------------------------------
	void getFromDisk(PCB currentProcess);

	//----------------------------------------------------------------
	// This function returns whether there is processes left to load
	// This returns true is there are processes to load
	//---------------------------------------------------------------- 
	bool loadProcess();

	private:
		/** current process's index */
		int current_process;
};