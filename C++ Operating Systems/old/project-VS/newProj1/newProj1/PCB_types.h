#pragma once //guard against multiple inclusion
#include "cpu.h"



//status of process
enum STATUS
{
	RUNNING,//0
	READY,//1
	WAIT,//2
	NEW,//3
	TERMINATED,//4
	TERMINATED_SAVED//5
};

struct account_stats
{
	unsigned long cpu_time;
	unsigned int cycle_count_last_total;
	unsigned int cycle_count_last_start;
	unsigned long avg_cpu_time;
	unsigned long wait_time;
	unsigned long avg_wait_time;
	unsigned long completion_time;
	unsigned long avg_completion_time;
};


// one process per PCB
struct PCB 
{

	//unsigned int pid; //process identification - not needed - will use index into 
	//array as PID.

	unsigned int cpu_id;
	unsigned long pc; //program counter
	unsigned long code_size; //code size in lines (words)
	int priority; //low numbers indicate lower priority
	//status set to NEW by LT_SChed.init; to READY by LT loadjobs
	int status; //RUNNING=0,READY=1,WAIT=2,NEW=3,TERMINATED=4
	int input_buffer_size;//size in lines (12 for phase1)
	int output_buffer_size;//size in lines (12 for phase1)
	int temp_buffer_size;//size in lines (12 for phase1)
	int process_size;//size (in lines) of code+inputbuf+outbuf+tempbuf
	unsigned int code_start_disk_address;// this is the "physical" addr on disk
	unsigned int data_start_disk_address;// this is the "physical" addr on disk
	unsigned int code_start_ram_address;//actual line adr ram object (0-1023)
	unsigned int input_buffer_ram_address;//actual line adr ram object (0-1023)
	unsigned int output_buffer_ram_address;//actual line adr ram object (0-1023)
	unsigned int temp_buffer_ram_address;//actual line adr ram object (0-1023)
	account_stats accounting;
	int registers[NUM_REGISTERS];
};

