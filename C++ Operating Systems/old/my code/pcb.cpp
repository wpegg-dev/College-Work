#include "StdAfx.h"
#include <string>
#include <iostream>
#include <fstream>
#include "pcb.h"

using namespace std;

pcb::pcb(void)
{
	for(int i = 0;i < NUM_PROCESSES;i++)
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

	cout << "cpu_id: " << pcb_array[pcb_index].cpu_id << endl;
	cout << "pc: " << pcb_array[pcb_index].pc << endl;
	cout << "code_size: " << pcb_array[pcb_index].code_size << endl;
	cout << "priority: " << pcb_array[pcb_index].priority << endl;
	cout << "status: " << pcb_array[pcb_index].status << endl;
	cout << "in buf sz: " << pcb_array[pcb_index].input_buffer_size << endl;
	cout << "out buf sz: " << pcb_array[pcb_index].output_buffer_size << endl;
	cout << "tmp buf sz: " << pcb_array[pcb_index].temp_buffer_size << endl;

}
pcb::~pcb(void)
{
}


bool pcb::setInBufSize(const int& pid, const int& size)
{
	bool success_status = true;
	pcb_array[pid].input_buffer_size = size;
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
