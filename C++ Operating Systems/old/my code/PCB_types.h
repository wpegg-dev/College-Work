#pragma once //guard against multiple inclusion



//status of process
enum STATUS
{
	RUNNING,
	READY,
	WAIT,
	NEW,
	TERMINATED
};

struct account_stats
{
	unsigned long cpu_time;
	unsigned long avg_cpu_time;
	unsigned long wait_time;
	unsigned long avg_wait_time;
	unsigned long completion_time;
	unsigned long avg_completion_time;
};
struct register_set
{
	int accumulator;
	int base_register;
};


// one process per PCB
struct PCB 
{

	//unsigned int pid; //process identification - not needed - will use index into 
	//array as PID.
	unsigned int cpu_id;
	unsigned long pc; //program counter
	unsigned long code_size; //code size in bytes
	int priority; //low numbers indicate lower priority
	int status; //RUNNING=0,READY=1,WAIT=2,NEW=3,TERMINATED=4
	int input_buffer_size;
	int output_buffer_size;
	int temp_buffer_size;
	unsigned int code_start_disk_address;// this is the "physical" addr on disk
	unsigned int data_start_disk_address;// this is the "physical" addr on disk
	unsigned int code_start_ram_address;
	unsigned int input_buffer_ram_address;
	unsigned int output_buffer_ram_address;
	unsigned int temp_buffer_ram_address;
	account_stats accounting;
	register_set registers;
};

