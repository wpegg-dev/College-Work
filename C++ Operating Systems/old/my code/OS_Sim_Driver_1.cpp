// OS_Sim_Driver_1.cpp : Defines the entry point for the console application.
//

//#include "stdafx.h"
#include <string>
#include <iostream>
#include <fstream>
#include "pcb.h"
#include "disk.h"
#include "file_loader.h"
#include "queue.h"
using namespace std;


#include "PCB_types.h"
//pcb pcb_table;
//Disk disk1;
//queue ready_queue;
//queue wait_queue;
//queue new_queue;
//queue running_queue;
//queue terminated_queue;

void displayPidTable(void);



//int _tmain(int argc, _TCHAR* argv[])
int main(int argc, char * argv[])
{
	cout << "Welcome to the OS Sim Driver Part 1 !!" << endl;

	string test_string = "01234567";
	
	//char* test_str_chr = test_string.c_str;
	

	file_loader fl1;
	PCB test_pcb; 


	//loader();

	fl1.read_file(pcb_table);
	/*while(true)
	{
		scheduler();
		dispatcher();
		cpu();
	}
*/


	//displayPidTable();

	return 0;
}

void displayPidTable(void)
{
	for(int i=0;i<31;i++)
	{
		cout << endl << "PID: " << i << endl;
		pcb_table.outputPcbInfo(i);
	}
}