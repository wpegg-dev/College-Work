#include <string>
#include <iostream>
#include <fstream>
#include "pcb.h"

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
	cout << pcb_array[pcb_index].io_read_count + pcb_array[pcb_index].io_write_count <<",";
	cout << pcb_array[pcb_index].io_read_count << ",";
	cout << pcb_array[pcb_index].io_write_count << ",";
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
