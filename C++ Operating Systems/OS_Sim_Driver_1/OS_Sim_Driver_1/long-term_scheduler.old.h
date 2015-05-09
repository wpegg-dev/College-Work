/*Author: William Pegg
--Class: CS-3243 Spring 2012
--File Name: Long-termScheduler.h
*/

#ifndef LONGSCHEDULER_H
#define LONGSCHEDULER_H

// tlj - moved to outside of class and added 'int' type specifier
const int processDataSIZE = 44;//tlj - added 'int'

class LongScheduler {

public:
	//-----------------------------
	//FUNCTIONS
	//-----------------------------
	//Default Constructor. starts the ready queue and current process value
	LongScheduler();
	
	//----------------------------------------------------------------------------------------
	// Iterates through the newQueue and places processes into RAM and into the readyqueue
	//----------------------------------------------------------------------------------------
	void moveToRAM();
	
	//----------------------------------------------------------
	// Writes a process's cache from RAM back into the disk
	//----------------------------------------------------------
    
	//void writeBackToDisk();

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