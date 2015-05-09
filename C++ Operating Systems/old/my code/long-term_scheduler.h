/*Author: William Pegg
--Class: CS-3243 Spring 2012
--File Name: Long-termScheduler.h
*/

#ifndef LONGSCHEDULER_H
#define LONGSCHEDULER_H
#include "disk.h"
#include "pcb.h"
#include "PCB_types.h"
#include "file_loader.h"
#include "OS_Sim_Driver_1.cpp"


class LongScheduler {

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
	//
	Node n;

	const processDataSIZE = 44;

	//-----------------------------
	//FUNCTIONS
	//-----------------------------
	//Default Constructor. starts the ready queue and current process value
	LongScheduler();
	
	//-----------------------------
	// Constructor with ram and disk where r The ram to use and d The disk to use
	//-----------------------------
	LongScheduler(RAM r, Disk d);	
	
	//----------------------------------------------------------------------------------------
	// Iterates through the newQueue and places processes into RAM and into the readyqueue
	//----------------------------------------------------------------------------------------
	void moveToRAM();
	
	//----------------------------------------------------------
	// Writes a process's cache from RAM back into the disk
	//----------------------------------------------------------
	void end(PCB currentProcess,CPU c);

	//----------------------------------------------------------------
	// This function returns whether there is processes left to load
	// This returns true is there are processes to load
	//---------------------------------------------------------------- 
	bool loadProcess();

	private:
		/** current process's index */
		int current_process;
};
#endif;