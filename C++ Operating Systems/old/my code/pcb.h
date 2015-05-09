

#pragma once

#include "pcb_types.h"

const int NUM_PROCESSES = 30;
class pcb
{
public:
	pcb(void);
	~pcb(void);

	bool setInBufSize(const int&, const int&);
	bool setOutBufSize(const int&, const int&);
	bool setTempBufSize(const int&, const int&);
	bool setCodeSize(const int&, const unsigned long&);
	bool setCpuId(const int&, const unsigned int&);
	bool setPc(const int& pid, const unsigned long&);
	bool setPriority(const int&, const int&);
	bool setStatus(const int&, const STATUS&);
	bool setCodeStartAddress(const int&, const unsigned int&);
	bool setInputBufferAddress(const int&, const unsigned int&);
	bool setOutputBufferAddress(const int&, const unsigned int&);
	bool setTempBufferAddress(const int&, const unsigned int&);
	bool setCodeDiskAddress(const int&, const unsigned int&);
	bool setDataDiskAddress(const int&, const unsigned int&);
	int getCodeSize(const int&);
	int getCpuId(const int&);
	int getPc(const int&);
	int getPriority(const int&);
	int getStatus(const int&);
	int getInputBufferSize(const int&);
	int getOutputBufferSize(const int&);
	int getTempBufferSize(const int&);
	unsigned int getCodeDiskAddress(const int&);
	unsigned int getDataDiskAddress(const int&);
	unsigned int getCodeStartRamAddress(const int&);
	unsigned int getInputBufferRamAddress(const int&);
	unsigned int getOutputBufferRamAddress(const int&);
	unsigned int getTempBufferRamAddress(const int&);



	PCB getPcbInfo(const int&);
	void outputPcbInfo(const int&);

private:
	PCB pcb_array[NUM_PROCESSES];
};
