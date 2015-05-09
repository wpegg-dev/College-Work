//OS_Sim_Driver_1.cpp: 
//Defines the entry point for the console application.
//

#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include "pcb.h"
#include "ram.h"
#include "disk.h"
#include "file_loader.h"
#include "LT_Sched.h"
#include "ST_Sched.h"
#include "cpu.h"
//#include "queue.h"
//#include "long-term_scheduler.h"
#include <stdlib.h>
#include <queue>
using namespace std;
#include "PCB_types.h"

queue<int> ready_queue;
queue<int>  wait_queue;
queue<int> new_queue;

void displayPidTable(const int& stage);

pcb pcb_table;
Disk disk1;
RAM ram;
RAM lts_ram;
LT_Sched lt_sched;
ST_Sched st_sched;
const int NUM_OF_CPUS = 4;
cpu Cpu[NUM_OF_CPUS];
void simSTS(void);


int getNextIdleCPU(void){

	for(int i=0;i<NUM_OF_CPUS;i++){
		if(Cpu[i].getCpuState() == IDLE)return i;
	}
	return -1;//indicates none are idle
}

bool checkForAllCpusIdle(void){
	for(int i = 0;i< NUM_OF_CPUS;i++){
		if(Cpu[i].getCpuState() != IDLE)return false;
	}
	return true;
}

void main()
{
	int stage = 0;
	int current_pid = 0;
	ofstream ramfile;
	file_loader fl1;

	fl1.read_file(pcb_table);//load DataFile2.txt

	displayPidTable(stage);
	disk1.dumpNFile(stage);
	stage++;

	lt_sched.schedInit();
	cout << "after running lt_sched.schedInit" << endl;
	displayPidTable(stage);
	disk1.dumpNFile(stage);
	stage++;

	lt_sched.loadJobs();//load jobs into ram and put in ready queue
	cout << "after running lt_sched.loadJobs first time" << endl;
	disk1.dumpNFile(stage);
	displayPidTable(stage);
	stage++;

#ifdef MULTI_PROCESSOR
	int cpu_id;

	while(!checkForAllCpusIdle() || !ready_queue.empty())
	{
		while((cpu_id = getNextIdleCPU()) >= 0){
			if(current_pid = st_sched.dispatch()){
				st_sched.contextSwitchLoadState( cpu_id, current_pid);
			}
			else break;
		}
		for(int i = 0;i<NUM_OF_CPUS;i++){
			if(Cpu[i].getPID() > 0){
				Cpu[i].run(Cpu[i].getPID());
			}
		}
		for(int i = 0;i<NUM_OF_CPUS;i++){
			if(Cpu[i].getCpuState() == IDLE)
				st_sched.contextSwitchSaveState(i, Cpu[i].getPID());
		}
	}

#else
	//schedJob returns a pid until readyqueue is empty
	//assignment used in while - not a typo use '='
	while(current_pid = st_sched.dispatch())
	{
		st_sched.contextSwitchLoadState( 0, current_pid);
		Cpu[0].run(current_pid);
		st_sched.contextSwitchSaveState( 0, current_pid);
	}
#endif
	disk1.dumpNFile(stage);
	displayPidTable(stage);
	stage++;

	lt_sched.saveToDisk();
	cout << "after running lt_sched.saveToDisk first time" << endl;
	disk1.dumpNFile(stage);
	displayPidTable(stage);
	stage++;

	lt_sched.loadJobs();
	cout << "after running lt_sched.loadJobs second time" << endl;
	disk1.dumpNFile(stage);
	displayPidTable(stage);
	stage++;

#ifdef MULTI_PROCESSOR



	while(!checkForAllCpusIdle() || !ready_queue.empty())
	{
		while((cpu_id = getNextIdleCPU()) >= 0){
			if(current_pid = st_sched.dispatch()){
				st_sched.contextSwitchLoadState( cpu_id, current_pid);
			}
			else break;
		}
		for(int i = 0;i<NUM_OF_CPUS;i++){
			if(Cpu[i].getPID() > 0){
				Cpu[i].run(Cpu[i].getPID());
			}
		}
		for(int i = 0;i<NUM_OF_CPUS;i++){
			if(Cpu[i].getCpuState() == IDLE)
				st_sched.contextSwitchSaveState(i, Cpu[i].getPID());
		}
	}

#else
	//assignment in while - not a typo USE '='
	while(current_pid = st_sched.dispatch())
	{
		st_sched.contextSwitchLoadState( 0, current_pid);
		Cpu[0].run(current_pid);
		st_sched.contextSwitchSaveState( 0, current_pid);
	}
#endif
	cout << "after running st sched and cpu 2nd time" << endl;

	disk1.dumpNFile(stage);
	displayPidTable(stage);
	stage++;

	lt_sched.saveToDisk();
	disk1.dumpNFile(stage);
	cout << "after running lt_sched.saveToDisk second time" << endl;
	displayPidTable(stage);

	
}

void displayPidTable(const int& stage){
#ifdef MULTI_PROCESSOR
	cout << "Process info for MULTI-processor sim (CPU IDs are 0,1,2,3)" << endl;
#else
	cout << "Process info for SINGLE-processor sim (CPU ID is 0)" << endl;
#endif
	cout << "Stage of system run= " << stage << endl;
	cout << "pid,cpu_id,time start,time used,I/O total,read cnt(I/O),write cnt(I/O),pc,code_sz,pri,status,ibuf_sz,obuf_sz,tbuf_sz,codeD_adr,dataD_adr,p_sz,codeR_adr,in_bufR_adr,out_bufR_adr,tmp_bufR_adr" << endl;
	for(int i=1;i<31;i++){
		pcb_table.outputPcbInfoNoLabels(i);
	}
}

 void displayQueues(void){
	 cout << "NEW queue has following PIDs" << endl;
	 queue<int> temp_queue;
	 int temp;
	 while(!ready_queue.empty())
	 {
		 temp_queue.push(ready_queue.back());
		 ready_queue.pop();
	 }
	 cout << "READY queue has following PIDs" << endl;
 }