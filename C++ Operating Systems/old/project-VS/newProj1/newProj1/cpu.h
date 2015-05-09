#pragma once
#define MULTI_PROCESSOR
const int CACHE_MAX_SIZE = 2000;
const int NUM_REGISTERS = 20;
const int CACHE_SIZE_IN_WORDS = 16;//WORDS = LINES
const int CACHE_SIZE_IN_BYTES = 128;//16 words (16 * 8)
typedef enum CPU_STATE{IDLE,INITIALIZED,EXECUTING};

typedef struct CACHE_STRUCT{
	//ram_line_index is the actual ram line address (0-1023) corresponding
	//to first cache line in this cache block
	int ram_line_index;
	int cache_char_cnt;//valid characters in cache
	char cache_mem[CACHE_SIZE_IN_BYTES];
	//offset into section in ram (i.e. if cache mirrors instruction block
	//in ram starting at 20th word past "code_start_ram_address" as stored in pcb
	//then this value should be 20
	int offset;
};
typedef enum BUF_TYPE{CODE_BUF,INPUT_BUF,OUTPUT_BUF,TEMP_BUF};
enum CACHE_TYPE{INSTR,INPUT,OUTPUT,TEMP,MAX_CACHE_COUNT};//0,1,2,3
const int MAX_CACHE_TYPE_COUNT = MAX_CACHE_COUNT;
	

const unsigned int INSTRUCTION   =0;
const unsigned int COND_BRANCH   =0X40000000;
const unsigned int UNCOND_BRANCH =0X80000000; 
const unsigned int IO            =0XC0000000;
const unsigned int INSTR_TYPE_MASK = 0XC0000000;
typedef enum OPCODE_TYPE{
	RD,WR,ST,LW,MOV,ADD,SUB,MUL,DIV,AND,OR,MOVI,
	ADDI,MULI,DIVI,LDI,SLT,SLTI,HLT,NOP,JMP,BEQ,
	BNE,BEZ,BNZ,BGZ,BLZ,OPCODE_MASK= 0X3F000000};



class cpu
{
public:
	cpu(void);
	~cpu(void);

	int run(const int&);
	int getPC(void);
	int getRegister(const int& );
	unsigned int getCycleCount(void){return cpu_cycle_count;};
	size_t getCacheCharCnt(void);
	char* fetchCacheLine(CACHE_TYPE cache_type, int line_index);
	void resetCache(void);

	void setPc(const int&);
	void setPID(const int&);
	int getPID(void){return current_pid;};
	CPU_STATE getCpuState(void){return cpu_state;};
	void setCpuState(CPU_STATE new_state){cpu_state = new_state;};
	void setRegister(const int& index, const int& value);
	void loadCacheLine(const char* input_ptr);
	void writeCacheLineToRam(const int& from_index, const int& to_index, const int& pid);
	void writeRamLineToCache(const int& from_index, const int& to_index, const int& pid);
	//writeRamBlockToCache will fill up cache from ram location
	void writeRamBlockToCache(const int& from_line_offset, const int& pid, CACHE_TYPE cache_type);
	void saveCacheBlockToRam(const int& to_line_offset, const int& pid, CACHE_TYPE cache_type);
	bool checkCache(int index, CACHE_TYPE cache_type, int pid);
	unsigned int convertCharLineToNum(char * char_line);
	unsigned int convertCacheLineToNum(CACHE_TYPE cache_type, int line_index);
	int  processInstr(const int& type, const unsigned int& inst_line);
	int  processArith(const unsigned int& instr);
	int  processCondBr(const unsigned int& instr);
	int  processUncondBr(const unsigned int& instr);
	int  processIo(const unsigned int& instr);

private:
	unsigned int cpu_cycle_count;
	CPU_STATE cpu_state;
	int current_pid;
	//program_counter doesn't take offset of cache into account; 
	//it is relative to instruction
	// as stored in ram (i.e. program starts at zero)
	int program_counter;
	int registers[NUM_REGISTERS];
	int cpu_id;
	//char cache[CACHE_MAX_SIZE];
	CACHE_STRUCT cache[MAX_CACHE_COUNT];
	int cache_char_cnt;

	int convToBufSectLineAdr(const int& address, 
						const int& pid,
						int& buffer_type);
	void writeNumberToCache(const CACHE_TYPE& type, 
							 const unsigned int& value, 
							 const int& index);
};
