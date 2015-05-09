

#pragma once

#include "pcb_types.h"

const int NUM_PROCESSES = 30;
class pcb
{
public:
	pcb(void);
	~pcb(void);

	void setCycleCountStart(const int& pid, const int& cycle_count_start);
	void setCycleCount(const int& pid, const int& cycle_count);
	bool setProcessSize(const int& pid, const int& size);//in lines
	bool setInBufSize(const int& pid, const int& size);//in lines
	bool setOutBufSize(const int& pid, const int& size);//in lines
	bool setTempBufSize(const int& pid, const int& size);//in lines
	bool setCodeSize(const int& pid, const unsigned long& size);//in lines
	bool setCpuId(const int& pid, const unsigned int& id);//in lines
	bool setPc(const int& pid, const unsigned long& pc);//in lines
	bool setPriority(const int& pid, const int& priority);//in lines
	bool setStatus(const int& pid, const STATUS& status);//in lines
	bool setCodeStartAddress(const int& pid, const unsigned int& address);//in lines
	bool setInputBufferAddress(const int& pid, const unsigned int& address);//in lines
	bool setOutputBufferAddress(const int& pid, const unsigned int& address);//in lines
	bool setTempBufferAddress(const int& pid, const unsigned int& address);//in lines
	bool setCodeDiskAddress(const int& pid, const unsigned int& address);//in lines
	bool setDataDiskAddress(const int& pid, const unsigned int& address);//in lines
	void setRegister(const int& pid, const int& register_number, const int& new_reg_value);//in lines

	unsigned int getCycleCountStart(const int& pid);
	int getProcessSize(const int& pid);//in lines
	int getCodeSize(const int& pid);//in lines
	int getCpuId(const int& pid);
	int getPc(const int& pid);
	int getPriority(const int& pid);
	int getStatus(const int& pid);
	int getInputBufferSize(const int& pid);
	int getOutputBufferSize(const int& pid);
	int getTempBufferSize(const int& pid);
	int getRegister(const int& pid, const int& register_number);
	unsigned int getCodeDiskAddress(const int& pid);
	unsigned int getDataDiskAddress(const int& pid);
	unsigned int getCodeStartRamAddress(const int& pid);
	unsigned int getInputBufferRamAddress(const int& pid);
	unsigned int getOutputBufferRamAddress(const int& pid);
	unsigned int getTempBufferRamAddress(const int& pid);


	

	PCB getPcbInfo(const int&);
	void outputPcbInfo(const int&);
	void outputPcbInfoNoLabels(const int& pcb_index);

private:
	PCB pcb_array[NUM_PROCESSES+1];//we don't use index '0' so add one
};
