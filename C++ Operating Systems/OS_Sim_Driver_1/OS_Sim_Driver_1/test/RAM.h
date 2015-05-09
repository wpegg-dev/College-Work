#pragma once

const int RAM_SIZE_IN_WORDS = 1024;
const int RAM_SIZE_IN_BYTES = 4096;
const int RAM_SIZE_IN_ASCII_CHARS = RAM_SIZE_IN_WORDS * 8;

class RAM
{
public:
	RAM(void);
	~RAM(void);
	int howManyUsedBytes(void);
	int howManyFreeBytes(void);
	int getNextFreeIndex(void);
	//bool writeNBytes(const char *, const int &);
	//writeNBytesToNextFreeAdr returns true if successful
	//returns false (chars not copied) if unnsuccessfull
	bool writeNBytesToNextFreeAdr(const char *, const int &);
	bool writeNBytesToIndexedLoc(
		const char* from, 
		const int& n, 
		const int& index);
	bool checkIfValidAddress(const char *);
	char * readCharLine(const int &);
	void clearRam(void);


private:
	char * next_free_byte_ptr;
	char * end_addr_ptr;
	char * memory;
	int bytes_free;
};
