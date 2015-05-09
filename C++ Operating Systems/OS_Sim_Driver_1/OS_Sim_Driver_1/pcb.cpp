#include<iostream>
#include <fstream>
#include<string>
#include<sstream>
#include "pcb.h"
#include <iomanip>
#include "cpu.h"
#include "LT_Sched.h"

using namespace std;

pcb::pcb(void)
{
	for(int i = 1;i <= NUM_PROCESSES;i++)
	{
		pcb_array[i].code_size = 0;
		pcb_array[i].cpu_id = 0;
		pcb_array[i].pc = 0;
		pcb_array[i].priority = 0;
		pcb_array[i].status = NEW;
		pcb_array[i].input_buffer_size = 0;
		pcb_array[i].output_buffer_size = 0;
		pcb_array[i].temp_buffer_size = 0;
	}
	
}
void pcb::outputPcbInfo(const int& pcb_index)
{


	cout << "cpu_id: " << pcb_array[pcb_index].cpu_id ;
	cout << "pc: " << pcb_array[pcb_index].pc ;
	cout << "code_sz: " << pcb_array[pcb_index].code_size ;
	cout << "pri: " << pcb_array[pcb_index].priority ;
	cout << "status: " << pcb_array[pcb_index].status ;
	cout << "in buf sz: " << pcb_array[pcb_index].input_buffer_size ;
	cout << "out buf sz: " << pcb_array[pcb_index].output_buffer_size ;
	cout << "tmp buf sz: " << pcb_array[pcb_index].temp_buffer_size << endl << endl;

}
void pcb::outputPcbInfoNoLabels(const int& pcb_index)
{
	string status_string[6] = {
	"RUNNING",
	"READY",
	"WAIT",
	"NEW",
	"TERMINATED",
	"SAVED_TERMINATED"};


	cout << pcb_index << ",";
	cout << pcb_array[pcb_index].cpu_id << "," ;
	cout << pcb_array[pcb_index].accounting.cycle_count_last_start << ",";
	cout << pcb_array[pcb_index].accounting.cycle_count_last_total << ",";
	cout << pcb_array[pcb_index].accounting.io_read_count + 
		pcb_array[pcb_index].accounting.io_write_count <<",";
	cout << pcb_array[pcb_index].accounting.io_read_count << ",";
	cout << pcb_array[pcb_index].accounting.io_write_count << ",";
	cout << pcb_array[pcb_index].process_size<<",";
	cout << pcb_array[pcb_index].accounting.cache_hit_count << ",";
	cout << pcb_array[pcb_index].accounting.cache_miss_count << ",";
	cout << pcb_array[pcb_index].accounting.cache_used * 16 << ",";
	cout << pcb_array[pcb_index].pc << "," ;
	cout << pcb_array[pcb_index].code_size << "," ;
	cout << pcb_array[pcb_index].priority << "," ;
	cout << status_string[pcb_array[pcb_index].status] << "," ;
	cout << pcb_array[pcb_index].input_buffer_size << "," ;
	cout << pcb_array[pcb_index].output_buffer_size << "," ;
	cout << pcb_array[pcb_index].temp_buffer_size << ",";
	cout << pcb_array[pcb_index].code_start_disk_address << ",";
	cout << pcb_array[pcb_index].data_start_disk_address << ",";
	cout << pcb_array[pcb_index].process_size << ",";
	cout << pcb_array[pcb_index].code_start_ram_address << ",";
	cout << pcb_array[pcb_index].input_buffer_ram_address << ",";
	cout << pcb_array[pcb_index].output_buffer_ram_address << ",";
	cout << pcb_array[pcb_index].temp_buffer_ram_address << ",";
	cout << endl;

}

void pcb::outputSummary(void){
	string status_string[6] = {
	"RUNNING",
	"READY",
	"WAIT",
	"NEW",
	"TERMINATED",
	"SAVED_TERMINATED"};

	ofstream summary;
#ifdef FCFS_POLICY
	string filename[2] = {"FCFS-Single-CPU Summary.csv","FCFS-Multi-CPU Summary.csv"};
#endif
#ifdef SJF_POLICY
	string filename[2] = {"SJF-Single-CPU Summary.csv","SJF-Multi-CPU Summary.csv"};
#endif
#ifdef PRIORITY_POLICY
	string filename[2] = {"PRIORITY-Single-CPU Summary.csv","PRIORITY-Multi-CPU Summary.csv"};
#endif
#ifdef MULTI_PROCESSOR
	summary.open(filename[1].c_str(),fstream::out);
#else
	summary.open(filename[0].c_str(),fstream::out);
#endif
	int avg_wait_time = 0;
	int avg_comp_time = 0;
	int avg_io_read = 0;
	int avg_io_write = 0;
	int avg_io = 0;
	int avg_ram_used = 0;
	int avg_cache_used = 0;
	int tot_wait_time = 0;
	int tot_comp_time = 0;
	int tot_io_read = 0;
	int tot_io_write = 0;
	int tot_io = 0;
	int tot_ram_used = 0;
	int tot_cache_used = 0;
#ifdef MULTI_PROCESSOR
	summary << "Process info for MULTI-processor sim (CPU IDs are 0-3)" << endl;
#else
	summary << "Process info for SINGLE-processor sim (CPU ID is 0)" << endl;
#endif
	summary << "pid,cpu_id,wait time,comp time,I/O total,read cnt(I/O),write cnt(I/O)," ;
	summary << "ram used(words),";
	summary <<"cache hit,cache miss,cache used(words),pc,code_sz,pri,status,ibuf_sz,";
	summary <<"obuf_sz,tbuf_sz,codeD_adr,dataD_adr,p_sz,codeR_adr,in_bufR_adr,out_bufR_adr,tmp_bufR_adr" << endl;

	for(int i=1;i<31;i++){
		summary << i << ",";
		summary << pcb_array[i].cpu_id << "," ;
		summary << pcb_array[i].accounting.cycle_count_last_start << ",";
		summary << pcb_array[i].accounting.cycle_count_last_total << ",";
		summary << pcb_array[i].accounting.io_read_count + 
			pcb_array[i].accounting.io_write_count <<",";
		summary << pcb_array[i].accounting.io_read_count << ",";
		summary << pcb_array[i].accounting.io_write_count << ",";
		summary << pcb_array[i].process_size<<",";
		summary << pcb_array[i].accounting.cache_hit_count << ",";
		summary << pcb_array[i].accounting.cache_miss_count << ",";
		summary << pcb_array[i].accounting.cache_used * 16 << ",";
		summary << pcb_array[i].pc << "," ;
		summary << pcb_array[i].code_size << "," ;
		summary << pcb_array[i].priority << "," ;
		summary << status_string[pcb_array[i].status] << "," ;
		summary << pcb_array[i].input_buffer_size << "," ;
		summary << pcb_array[i].output_buffer_size << "," ;
		summary << pcb_array[i].temp_buffer_size << ",";
		summary << pcb_array[i].code_start_disk_address << ",";
		summary << pcb_array[i].data_start_disk_address << ",";
		summary << pcb_array[i].process_size << ",";
		summary << pcb_array[i].code_start_ram_address << ",";
		summary << pcb_array[i].input_buffer_ram_address << ",";
		summary << pcb_array[i].output_buffer_ram_address << ",";
		summary << pcb_array[i].temp_buffer_ram_address << ",";
		summary << endl;

		tot_wait_time += pcb_array[i].accounting.cycle_count_last_start;
		tot_comp_time += pcb_array[i].accounting.cycle_count_last_total;
		tot_io_read += pcb_array[i].accounting.io_read_count;
		tot_io_write += pcb_array[i].accounting.io_write_count;
		tot_io += (pcb_array[i].accounting.io_read_count + pcb_array[i].accounting.io_write_count);
		tot_ram_used += pcb_array[i].process_size;
		tot_cache_used += (pcb_array[i].accounting.cache_used * 16);
	}
	avg_wait_time = tot_wait_time / 30;
	avg_comp_time = tot_comp_time / 30;
	avg_io = tot_io / 30;
	avg_io_read = tot_io_read / 30;
	avg_io_write = tot_io_write / 30;
	avg_ram_used = tot_ram_used / 30;
	avg_cache_used = tot_cache_used / 30;

	summary << endl << endl;
	summary << "           SUMMARY OF SIMULATION        " << endl;
	summary << "avg,tot,avg,tot,avg,tot,avg,tot,avg,tot,avg,tot,avg,tot" << endl;
	summary << "wait,wait,comp,comp,IO,IO,read(io),read(io),write(io),write(io),ram,ram,cache,cache" << endl;
	summary << avg_wait_time << "," ;
	summary << tot_wait_time << "," ;
	summary << avg_comp_time << "," ;
	summary << tot_comp_time << "," ;
	summary << avg_io << "," ;
	summary << tot_io << "," ;
	summary << avg_io_read << "," ;
	summary << tot_io_read << "," ;
	summary << avg_io_write << "," ;
	summary << tot_io_write << "," ;
	summary << avg_ram_used << "," ;
	summary << tot_ram_used << "," ;
	summary << avg_cache_used << "," ;
	summary << tot_cache_used << "," ;
}



pcb::~pcb(void)
{
}

void pcb::setRegister(const int& pid,
					  const int& register_number, 
					  const int& new_reg_value)
{
	pcb_array[pid].registers[register_number] = new_reg_value;
}

int pcb::getRegister(const int& pid, 
					 const int& register_number)
{
	return pcb_array[pid].registers[register_number];
}


void pcb::setCycleCountStart(const int& pid, const int& cycle_count_start)
{
	pcb_array[pid].accounting.cycle_count_last_start = cycle_count_start;
}
unsigned int pcb::getCycleCountStart(const int& pid)
{
	return pcb_array[pid].accounting.cycle_count_last_start;
}

void pcb::setCycleCount(const int& pid, const int& cycle_count)
{
	pcb_array[pid].accounting.cycle_count_last_total = cycle_count;
}

bool pcb::setInBufSize(const int& pid, const int& size)
{
	bool success_status = true;
	pcb_array[pid].input_buffer_size = size;
	return success_status;
}

bool pcb::setProcessSize(const int& pid, const int& size)
{
	bool success_status = true;
	pcb_array[pid].process_size = size;
	return success_status;
}
bool pcb::setTempBufSize(const int& pid, const int& size)
{
	bool success_status = true;
	pcb_array[pid].temp_buffer_size = size;
	return success_status;
}
bool pcb::setCodeStartAddress(const int& pid, const unsigned int& address)
{
	bool success_status = true;
	pcb_array[pid].code_start_ram_address = address;
	return success_status;
}
bool pcb::setInputBufferAddress(const int& pid, const unsigned int& address)
{
	bool success_status = true;
	pcb_array[pid].input_buffer_ram_address = address;
	return success_status;
}
bool pcb::setOutputBufferAddress(const int& pid, const unsigned int& address)
{
	bool success_status = true;
	pcb_array[pid].output_buffer_ram_address = address;
	return success_status;
}
bool pcb::setTempBufferAddress(const int& pid, const unsigned int& address)
{
	bool success_status = true;
	pcb_array[pid].temp_buffer_ram_address = address;
	return success_status;
}

bool pcb::setCodeDiskAddress(const int& pid, const unsigned int& address)
{
	bool success_status = true;
	pcb_array[pid].code_start_disk_address = address;
	return success_status;
}
bool pcb::setDataDiskAddress(const int& pid, const unsigned int& address)
{
	bool success_status = true;
	pcb_array[pid].data_start_disk_address = address;
	return success_status;
}


bool pcb::setOutBufSize(const int& pid, const int& size)
{
	bool success_status = true;
	pcb_array[pid].output_buffer_size = size;
	return success_status;
}


bool pcb::setCodeSize(const int& pid, const unsigned long& size)
{
	bool success_status = true;
	pcb_array[pid].code_size = size;
	return success_status;
}

bool pcb::setCpuId(const int& pid, const unsigned int& id)
{
	bool success_status = true;
	pcb_array[pid].cpu_id = id;
	return success_status;
}

bool pcb::setPc(const int& pid, const unsigned long& pc)
{
	bool success_status = true;
	pcb_array[pid].pc = pc;
	return success_status;
}
bool pcb::setPriority(const int& pid, const int& priority)
{
	bool success_status = true;
	pcb_array[pid].priority = priority;
	return success_status;
}
bool pcb::setStatus(const int& pid, const STATUS& status)
{
	bool success_status = true;
	pcb_array[pid].status = status;
	return success_status;
}
int pcb::getProcessSize(const int& pid)
{
	return pcb_array[pid].process_size;
}
int pcb::getCodeSize(const int& pid)
{
	return pcb_array[pid].code_size;
}
int pcb::getCpuId(const int& pid)
{
	return pcb_array[pid].cpu_id;
}
int pcb::getPc(const int& pid)
{
	return pcb_array[pid].pc;
}
int pcb::getPriority(const int& pid)
{
	return pcb_array[pid].priority;
}
int pcb::getStatus(const int& pid)
{
	return pcb_array[pid].status;
}

int pcb::getInputBufferSize(const int& pid)
{
	return pcb_array[pid].input_buffer_size;
}
int pcb::getOutputBufferSize(const int& pid)
{
	return pcb_array[pid].output_buffer_size;
}
int pcb::getTempBufferSize(const int& pid)
{
	return pcb_array[pid].temp_buffer_size;
}
unsigned int pcb::getCodeDiskAddress(const int& pid)
{
	return pcb_array[pid].code_start_disk_address;
}
unsigned int pcb::getDataDiskAddress(const int &pid)
{
	return pcb_array[pid].data_start_disk_address;
}
unsigned int pcb::getCodeStartRamAddress(const int &pid)
{
	return pcb_array[pid].code_start_ram_address;
}
unsigned int pcb::getInputBufferRamAddress(const int &pid)
{
	return pcb_array[pid].input_buffer_ram_address;
}
unsigned int pcb::getOutputBufferRamAddress(const int &pid)
{
	return pcb_array[pid].output_buffer_ram_address;
}
unsigned int pcb::getTempBufferRamAddress(const int &pid)
{
	return pcb_array[pid].temp_buffer_ram_address;
}

PCB pcb::getPcbInfo(const int& pid)
{
	PCB return_pcb_info;
	return_pcb_info = pcb_array[pid];
	return return_pcb_info;
}
