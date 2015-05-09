//#include "StdAfx.h" 
#include "string.h"
#include "RAM.h"

RAM::RAM(void)
{
	memory = new char[RAM_SIZE_IN_ASCII_CHARS];
	next_free_byte_ptr = memory;
	bytes_free = RAM_SIZE_IN_ASCII_CHARS;
	end_addr_ptr = memory + RAM_SIZE_IN_ASCII_CHARS;
	
}

RAM::~RAM(void)
{
}
int RAM::howManyFreeBytes(void)
{
	return(bytes_free);
}
int RAM::howManyUsedBytes(void)
{
	return(RAM_SIZE_IN_ASCII_CHARS - bytes_free);
}
//returns the index of the next free location of ram
//useful if you are unsure if you're about to attempt to 
//copy something too big for available memory (i.e. long scheduler).
int RAM::getNextFreeIndex(void)
{
	return (int)(next_free_byte_ptr - memory);
}
void RAM::clearRam(void)
{
	next_free_byte_ptr = memory;
	bytes_free = RAM_SIZE_IN_ASCII_CHARS;
	end_addr_ptr = memory + RAM_SIZE_IN_ASCII_CHARS;
}

bool RAM::writeNBytesToNextFreeAdr(const char * input_str, const int & n)
{
	if((next_free_byte_ptr + n) > end_addr_ptr)
	{
		return false;
	}
	else
	{
		strncpy(next_free_byte_ptr, input_str, n);
		next_free_byte_ptr += n;
		bytes_free -= n;
		return true;
	}
}
bool RAM::checkIfValidAddress(const char * target_address)
{
	if(target_address < memory)return false;
	if(target_address > end_addr_ptr) return false;
	return true;
}
char * RAM::readCharLine(const int & line_n)
{
	char return_char_array[8];
	strncpy(return_char_array,&memory[line_n * 8],8);
	return return_char_array;
}

bool RAM::writeNBytesToIndexedLoc(const char* from, 
							 const int& n, 
							 const int& index)
{
	//need to make sure write successful
	//return true on success, false otherwise
	bool status = true;

	strncpy(&memory[index*8], from, n);

	return true;
}