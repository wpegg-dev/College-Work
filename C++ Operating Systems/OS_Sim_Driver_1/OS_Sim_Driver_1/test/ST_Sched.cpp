//#include "StdAfx.h"
#include "ST_Sched.h"
#include "LT_Sched.h"
#include "cpu.h"
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
extern cpu Cpu[10];
using namespace std;

ST_Sched::ST_Sched(void)
{
}

ST_Sched::~ST_Sched(void)
{
}
int ST_Sched::dispatch(void)
{
	//true if job was successfully set for running
	//false otherwise (i.e. all jobs finished).
	int this_pid = 0;

	if(ready_queue.empty())return false;
	this_pid = ready_queue.front();
	ready_queue.pop();
	return this_pid;

	
}
int ST_Sched::m_dispatch(int* pid)
{
	//true if job was successfully set for running
	//false otherwise (i.e. all jobs finished).
	int cnt = 0;
	//int pid[4] = {0,0,0,0};
	
	while(!ready_queue.empty() && cnt <4){
		pid[cnt++]=ready_queue.front();
		ready_queue.pop();
	}
	return cnt;
}
/*
saveState transfers information from the CPU
to the PID.  This will run once a process
is done (terminated, time slice ends,
interrupted, waiting, etc.) in the CPU.
*/
bool ST_Sched::contextSwitchSaveState(const int& cpu_number, const int& pid)
{
	int i,j,k;
	size_t cache_char_cnt = Cpu[cpu_number].getCacheCharCnt();

	//save off program counter (Pc)
	pcb_table.setPc(pid,Cpu[cpu_number].getPC());

	//save off all registers
	for(i=0;i<NUM_REGISTERS;i++){
		pcb_table.setRegister(pid,i,Cpu[cpu_number].getRegister(i));
	}

	//store off cache (only need to write non-code sections)

	Cpu[cpu_number].saveCacheBlockToRam(0,pid,INPUT);//might not be needed
	Cpu[cpu_number].saveCacheBlockToRam(0,pid,OUTPUT);
	Cpu[cpu_number].saveCacheBlockToRam(0,pid,TEMP);//might not be needed


	//might be better to do this somewhere else.?.
	Cpu[cpu_number].resetCache();

	Cpu[cpu_number].setPID(0);//indicate no loaded process

	pcb_table.setCycleCount(pid, 
		Cpu[cpu_number].getCycleCount() -
		pcb_table.getCycleCountStart(pid));



	return true;

}

bool ST_Sched::contextSwitchLoadState(const int& cpu_number, const int& pid)
{
	int i;

	Cpu[cpu_number].resetCache();
	//load CPU program counter with pid's current program cntr
	Cpu[cpu_number].setPc(pcb_table.getPc(pid));
	Cpu[cpu_number].setPID(pid);
	pcb_table.setCpuId(pid,cpu_number);

	//load CPU registers with pid's current register values
	for(i = 0; i < NUM_REGISTERS; i++)
		Cpu[cpu_number].setRegister(i,pcb_table.getRegister(pid,i));

	//load caches(instr,input,output,temp) with process image
	//for loading a state always set offset to zero

	Cpu[cpu_number].writeRamBlockToCache(0,pid,INSTR);
	Cpu[cpu_number].writeRamBlockToCache(0,pid,INPUT);
	Cpu[cpu_number].writeRamBlockToCache(0,pid,OUTPUT);
	Cpu[cpu_number].writeRamBlockToCache(0,pid,TEMP);

	Cpu[cpu_number].setCpuState(INITIALIZED);

	pcb_table.setCycleCountStart(pid,Cpu[cpu_number].getCycleCount());

	return true;
}


