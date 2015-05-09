#include "ST_Sched.h"
#include "LT_Sched.h"
#include "disk.h"
#include "pcb.h"
#include "PCB_types.h"
#include "file_loader.h"
#include "RAM.h"
#include <queue>
#include <iostream>
#include <fstream>
#include <sstream>
#include "cpu.h"
extern queue<int> ready_queue;
extern queue<int> wait_queue;
extern queue<int> new_queue;
extern queue<int> running_queue;
extern queue<int> terminated_queue; 
extern pcb pcb_table;
extern Disk disk1;
extern RAM ram;
using namespace std;


cpu::cpu(void)
{
	program_counter=0;
	cpu_cycle_count = 0;
	int i = 0;

	for(i = 0;i<NUM_REGISTERS;i++)
		registers[i] = 0;
	cpu_id = 0;

	cpu_state = IDLE;
}

cpu::~cpu(void)
{
}

void cpu::setPc(const int& new_program_counter)
{
	program_counter = new_program_counter;
}

void cpu::setRegister(const int& register_index,
					  const int& register_value)
{
	registers[register_index] = register_value;
}

void cpu::setPID(const int & pid){current_pid = pid;}

void cpu::resetCache(void)
{
	int i;
	for(i = 0;i<MAX_CACHE_COUNT;i++)
	{
		cache[i].cache_char_cnt = 0;
		cache[i].ram_line_index = -1;//-1 to indicate empty
	}
}
void cpu::saveCacheBlockToRam(const int& to_line_offset, 
							  const int& pid, 
							  CACHE_TYPE cache_type)
{
	int i, ram_write_start_line, line_write_count;
	ram_write_start_line = cache[cache_type].ram_line_index;
	line_write_count = cache[cache_type].cache_char_cnt / 8;

	ram.writeNBytesToIndexedLoc(cache[cache_type].cache_mem,
		cache[cache_type].cache_char_cnt,
		cache[cache_type].ram_line_index);
	
};


//write a whole cache (of type cache type) from ram
void cpu::writeRamBlockToCache(const int& from_line_offset, 
							   const int& pid, 
							   CACHE_TYPE cache_type)
{
	int i = 0;
	int line_write_count = 0;
	//ram_read_start_line corresponds to index as used in RAM(0 to 1023)
	int ram_read_start_line = 0;
	char ram_line[9];
	ram_line[8] = 0;

	switch(cache_type){
		case INSTR:
			ram_read_start_line = pcb_table.getCodeStartRamAddress(pid);
			line_write_count = pcb_table.getCodeSize(pid);
			break;
		case INPUT:
			ram_read_start_line = pcb_table.getInputBufferRamAddress(pid);
			line_write_count = 20;
			break;
		case OUTPUT:
			ram_read_start_line = pcb_table.getOutputBufferRamAddress(pid);
			line_write_count = 12;
			break;
		case TEMP:
			ram_read_start_line = pcb_table.getTempBufferRamAddress(pid);
			line_write_count = 12;
			break;
	};
	ram_read_start_line += from_line_offset;//account for offset
	line_write_count -= from_line_offset;//don't read in too much if offset

	if(line_write_count > CACHE_SIZE_IN_WORDS)
		line_write_count = CACHE_SIZE_IN_WORDS;

	for(i = 0; i < line_write_count;i++){
		strncpy(
			ram_line,
			ram.readCharLine(ram_read_start_line + i),8);
		strncpy(&cache[cache_type].cache_mem[i * 8],ram_line,8);
	}
	cache[cache_type].cache_char_cnt = line_write_count * 8;
	cache[cache_type].ram_line_index = ram_read_start_line;
	cache[cache_type].offset = from_line_offset;
}
//index is offset into buf section 
bool cpu::checkCache(int index, CACHE_TYPE cache_type, int pid){

	if(index >= (cache[cache_type].offset + cache[cache_type].cache_char_cnt/8))
		//i.e. need higher block of memory from ram - not in cache
		return false;//cache miss
	else if (index < cache[cache_type].offset)
		//i.e. need lower block of memory from ram - not in cache
		return false;//cache miss
	else
		return true;//cache hit
}

char* cpu::fetchCacheLine(CACHE_TYPE cache_type, int line_index){

	//line_index is the real index offset is where cache starts
	int effective_address = line_index - cache[cache_type].offset;
	return(&cache[cache_type].cache_mem[effective_address*8]);
}


unsigned int cpu::convertCharLineToNum(char * char_line)
{
	unsigned int instr = 0;
	sscanf(char_line,"%X",&instr);
	return instr;
}
unsigned int cpu::convertCacheLineToNum(CACHE_TYPE cache_type, int line_index)
{
	unsigned int return_value = 0;
	char line[9];
	line[8]=0;
	strncpy(line,fetchCacheLine(cache_type, line_index),8);
	sscanf(line,"%X",&return_value);
	return return_value;
}
/*
run: run pid until it ends due to some 
reason (e.g. terminate, io, time-slice expire).
return the reason for halt. Before running it is assumed a viable process
has been loaded in cache and PC is updated.
*/
int cpu::run(const int& pid)
{
	//return TERMINATED;
	char instr_char_line[9];
	instr_char_line[8]= 0;//need null termination so atoi will work
	unsigned int instr_int_line = 0;
	unsigned int instr_type = 0;
	OPCODE_TYPE opcode;
	int process_state;


	pcb_table.setStatus(pid, RUNNING);
	cpu_state = EXECUTING;

#ifndef MULTI_PROCESSOR
	do{//start loop
#endif
	cpu_cycle_count++;
	//increment cycle count
	//check to see if pc points to instructions in cache (checkCache(PC,INSTR))
	if(!checkCache(program_counter, INSTR, pid))
		//  if miss - swap instr cache (swapCacheAtIndex(index))
		writeRamBlockToCache(program_counter, pid, INSTR);
	//	fetch line (unsigned int instr = fetchCacheLine(index))
	strncpy(instr_char_line,fetchCacheLine(INSTR, program_counter),8);
	instr_int_line = convertCharLineToNum(instr_char_line);
	instr_type = instr_int_line & INSTR_TYPE_MASK;
	opcode = (OPCODE_TYPE)((instr_int_line & OPCODE_MASK) >> 24);

	//	parse line parseInstrc(instr)
	//  perform actions from opcode
	//  increment program counter (pc)
	program_counter++;//don't read this until next loop - but 
	//program_counter might be written to in processInstr.
	process_state = processInstr(instr_type, instr_int_line);
	if(process_state == TERMINATED){
		cpu_state = IDLE;
		pcb_table.setStatus(pid, TERMINATED);
	}
#ifndef MULTI_PROCESSOR
	}while(process_state != TERMINATED);//end 
#endif
	//}while(program_counter < 30);//end 

	return cpu_state;
}

int cpu::getPC(void){return program_counter;}
int cpu::getRegister(const int& n){return registers[n];}
size_t cpu::getCacheCharCnt(void){return cache_char_cnt;}
//const char* cpu::getCache(void){return cache;}

int cpu::processInstr(const int& type, const unsigned int& inst_line)
{
	int returned_pid_status = RUNNING;
	switch(type){
		case INSTRUCTION:
			returned_pid_status = processArith(inst_line);
			break;
		case COND_BRANCH:
			returned_pid_status = processCondBr(inst_line);
			break;
		case UNCOND_BRANCH:
			returned_pid_status = processUncondBr(inst_line);
			break;
		case IO:
			returned_pid_status = processIo(inst_line);
			break;
	}
	return returned_pid_status;
}
int  cpu::processArith(const unsigned int& instr){
	int returned_pid_status = RUNNING;
	OPCODE_TYPE opcode=(OPCODE_TYPE)((instr & OPCODE_MASK) >> 24);
	unsigned int s_reg_1 = (instr & 0x00f00000)>>20;
	unsigned int s_reg_2 = (instr & 0x000f0000)>>16;
	unsigned int d_reg   = (instr & 0x0000f000)>>12;

	switch(opcode){
		case MOV:
			registers[s_reg_1] = registers[s_reg_2];
			break;
		case ADD:
			registers[d_reg] = registers[s_reg_1] + registers[s_reg_2];
			break;
		case SUB:
			registers[d_reg] = registers[s_reg_1] - registers[s_reg_2];
			break;
		case MUL:
			registers[d_reg] = registers[s_reg_1] * registers[s_reg_2];
			break;
		case DIV:
			if(registers[s_reg_2] == 0){
				cout << "DIV BY ZERO (DIV) " << "PC=" <<program_counter<<endl;
				cout << "pid=" << current_pid << endl;
				cout << "s_reg_1=" << s_reg_1 << endl;
				cout << "reg[s_reg_1]=" << registers[s_reg_1] << endl;
				cout << "s_reg_2=" << s_reg_2 << endl;
				exit(1);
			}
			registers[d_reg] = registers[s_reg_1] / registers[s_reg_2];
			break;
		case AND:
			registers[d_reg] = registers[s_reg_1] & registers[s_reg_2];
			break;
		case OR:
			registers[d_reg] = registers[s_reg_1] | registers[s_reg_2];
			break;
		case SLT:
			if(registers[s_reg_1] < registers[s_reg_2]){
				registers[d_reg] = 1;
			}
			else{
				registers[d_reg] = 0;
			}
			break;
		case NOP:
			break;
	}
	return returned_pid_status;
}
int cpu::processCondBr(const unsigned int& instr){
	int returned_pid_status = RUNNING;
	OPCODE_TYPE opcode=(OPCODE_TYPE)((instr & OPCODE_MASK) >> 24);
	unsigned int b_reg = (instr & 0x00f00000)>>20;
	unsigned int d_reg = (instr & 0x000f0000)>>16;
	unsigned int address =  instr & 0x0000ffff;//get 16 low-order bits
	unsigned int data = address;//to prevent confusion when it's really data
	int buf_type, d_buf_type, b_buf_type ;
	//buf_line_adr is "line" adr corresponding to address. it is corrected
	//so each section (code, input, temp, etc.) starts with zero. section
	//type is returned in buf_type. use this instead of address for updating
	//program counter.
	unsigned int buf_line_adr = 
		convToBufSectLineAdr(address/4,current_pid,buf_type);
	if(!checkCache(buf_line_adr,(CACHE_TYPE)buf_type,current_pid)){//cache miss
		writeRamBlockToCache(buf_line_adr,current_pid,(CACHE_TYPE)buf_type);
	}
	unsigned int d_reg_line_adr = 
		convToBufSectLineAdr(registers[d_reg]/4,current_pid,d_buf_type);
	unsigned int b_reg_line_adr = 
		convToBufSectLineAdr(registers[b_reg]/4,current_pid,b_buf_type);

	switch(opcode){
		case ST:
			if(d_reg == 0){//use address
				writeNumberToCache((CACHE_TYPE)buf_type,
					registers[b_reg],
					buf_line_adr);
			}
			else{
				writeNumberToCache((CACHE_TYPE)d_buf_type,
					registers[b_reg],
					d_reg_line_adr);
			}
			break;
		case LW:
			registers[d_reg] = 
				convertCacheLineToNum((CACHE_TYPE)b_buf_type, b_reg_line_adr);
			break;
		case MOVI:
			if(address==0){
				registers[d_reg] = registers[b_reg];
			}
			else{
				registers[d_reg] = address;
			}
			break;
		case ADDI:
			registers[d_reg] += address;
			break;
		case MULI:
			registers[d_reg] *= address;
			break;
		case DIVI:
			if(address == 0){
				cout << "divide by zero" << endl;
				exit(1);
			}
			registers[d_reg] /= address;
			break;
		case LDI:
			registers[d_reg] = address;
			break;
		case SLTI:
			registers[d_reg] = 1;
			if(registers[b_reg] > address)
				registers[d_reg] = 0;
			break;
		case BEQ:
			if(registers[d_reg] == registers[b_reg])
			{
				program_counter = buf_line_adr;
			}
			break;
		case BNE:
			if(registers[d_reg] != registers[b_reg])
			{
				program_counter = buf_line_adr;
			}
			break;
		case BEZ:
			if(registers[d_reg] == 0){
				program_counter = buf_line_adr;
			}
			break;
		case BNZ:
			if(registers[b_reg] != 0){
				program_counter = buf_line_adr;
			}
			break;
		case BGZ:
			if(registers[b_reg] > 0){
				program_counter = buf_line_adr;
			}
			break;
		case BLZ:
			if(registers[b_reg] < 0){
				program_counter = buf_line_adr;
			}
			break;
		case NOP:
			break;
	}
	return returned_pid_status;
}
int cpu::processUncondBr(const unsigned int& instr){
	int returned_pid_status = RUNNING;
	OPCODE_TYPE opcode=(OPCODE_TYPE)((instr & OPCODE_MASK) >> 24);
	unsigned int address =  instr & 0x00ffffff;//get 24 low-order bits

	switch(opcode){
		case HLT:
			returned_pid_status = TERMINATED;
			break;
		case JMP:
			program_counter = address;
			break;
		case NOP:
			break;
	}
	return returned_pid_status;
}
int cpu::processIo(const unsigned int& instr){
	int returned_pid_status = RUNNING;
	OPCODE_TYPE opcode=(OPCODE_TYPE)((instr & OPCODE_MASK) >> 24);
	unsigned int reg_1   = (instr & 0x00f00000)>>20;
	unsigned int reg_2   = (instr & 0x000f0000)>>16;
	unsigned int address =  instr & 0x0000ffff;
	int buf_type, reg_1_buf_type, reg_2_buf_type;

	unsigned int buf_line_adr = 
		convToBufSectLineAdr(address/4,current_pid,buf_type);
	if(!checkCache(buf_line_adr,(CACHE_TYPE)buf_type,current_pid)){//cache miss
		writeRamBlockToCache(buf_line_adr,current_pid,(CACHE_TYPE)buf_type);
	}
	unsigned int reg_1_line_adr = 
		convToBufSectLineAdr(registers[reg_1]/4,current_pid,reg_1_buf_type);
	unsigned int reg_2_line_adr = 
		convToBufSectLineAdr(registers[reg_2]/4,current_pid,reg_2_buf_type);
	
	
	switch(opcode){
		case RD:
			if(reg_2==0){
				registers[reg_1]=convertCacheLineToNum(INPUT,buf_line_adr);
			}
			else{
				registers[reg_1]=convertCacheLineToNum(INPUT,reg_2_line_adr);
			}
			break;
		case WR:
			if(reg_2==0){
				writeNumberToCache(OUTPUT,registers[reg_1],buf_line_adr);
			}
			else{
				writeNumberToCache(OUTPUT,registers[reg_1],reg_2_line_adr);
			}
			break;
	}
	return returned_pid_status;
}

/* takes unsigned int as input, then converts it to char array,
then writes it to cache specified in type (cache type) and index (line number).
we only write to temp and output buffer. both sections are smaller than
cache size so a cache "miss/hit" doesn't have to be checked for.*/
void cpu::writeNumberToCache(const CACHE_TYPE& type, 
							 const unsigned int& value, 
							 const int& index)
{
	char buffer[11];
	buffer[10]=0;
	sprintf(buffer, "%.8X",value);
	strncpy(&cache[type].cache_mem[index*8], buffer, 8);
}


/* 
the input address is a line addr starting with code. First line
of code would be an address of zero. Function determines which of
the four sections the address is pointing to (code, input, output, or temp
buffer) and returns that type in buffer_type.  Then the section line address 
which corresponds to input address is calculated and return. For instance
if an input line address of 30 is input and code size is 20 - then the
input address points to the input buffer section and specifically - at 
line 10 (i.e. 30 - 20 = 10).  So 10 is returned along with buf type of 
INPUT.
*/
int cpu::convToBufSectLineAdr(const int& address, 
						const int& pid,
						int& buffer_type)
{
	if(address >= pcb_table.getCodeSize(pid)){
		if((address - pcb_table.getCodeSize(pid)) < 20){
			buffer_type = INPUT_BUF;
			return address - pcb_table.getCodeSize(pid);
		}
		else if((address - pcb_table.getCodeSize(pid)) < 32){
			buffer_type = OUTPUT_BUF;
			return (address - pcb_table.getCodeSize(pid)) - 20;
		}
		else if((address - pcb_table.getCodeSize(pid)) < 44){
			buffer_type = TEMP_BUF;
			return (address - pcb_table.getCodeSize(pid)) - 32;
		}
	}
	buffer_type = CODE_BUF;
	return address;
}
