/*Author: William Pegg
--Class: CS-3243 Spring 2012
--File Name: Long-term_scheduler.h
*/

#ifndef LONGSCHEDULER_H
#define LONGSCHEDULER_H
#include <string>
using namespace std;
// tlj - moved to outside of class and added 'int' type specifier
const int processDataSIZE = 44;//tlj - added 'int'

class LongScheduler {

public:

	int current_process;
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
    
	void moveToDisk();

	//----------------------------------------------------------------
	// This function will move all the processes into the new queue
	//---------------------------------------------------------------- 
	void loadProcesses();

	//---------------------------------------------------------------------
	// This will output all of status of RAM as processes are added to it
	//---------------------------------------------------------------------
	void LTSDump(string str);

};
#endif;