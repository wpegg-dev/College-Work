//#include "StdAfx.h"
#include<iostream>
#include <fstream>
#include<string>
#include<sstream>
#include"disk.h"
#include <iomanip>

using namespace std;
Disk::Disk()
{ 
	nextWord = 0;
	//disk = new char [DISK_SIZE_IN_CHARS];
}
Disk::~Disk()
{ 
	//delete disk;
}
void Disk::write (char a) 
{ 
	disk[nextWord] = a;	
	nextWord ++;
	if(nextWord > DISK_SIZE_IN_CHARS)
		cout << "Disk is Full";
}	
void Disk::read ( int i ) 
{
	if (i < DISK_SIZE_IN_CHARS)
	{
		char b= disk[i];
		cout << b;
	}
	else
		cout << "Error: Outside disk";
}	
char Disk::readChar(int i)
{
	if(i < DISK_SIZE_IN_CHARS)
		return disk[i];
	else
		cout << "Error: Index outside disk " << i << endl;
	return NULL;
}

void Disk::writeLine(char *in, int lineNum)
{
	if( lineNum < DISK_SIZE_IN_LINES) 
	strncpy(&disk[lineNum*8],in,8);
	else cout << "Disk is Full" <<endl;
}
string Disk::readLine(int lineNum)
{
	string return_string;
	stringstream ss;
	char c_array[9];
	if(lineNum < DISK_SIZE_IN_LINES)
	{
		strncpy(c_array, &disk[lineNum*8],8);
		c_array[8] = NULL;
		ss << c_array;
		return_string = ss.str();
		return return_string;
	}
	else
		return NULL;
}

void Disk::dumpNFile(const int& n)
{
	ofstream diskOutFileSet[4];
	string filename[4]={"diskout0.txt","diskout1.txt","diskout2.txt","diskout3.txt"};
	diskOutFileSet[n].open(filename[n].c_str(),fstream::out);
	string fmt[4]={"..........000","..........00","..........0",".........."};

	int k, adj;//counter and adj for formatting output


	diskOutFileSet[n] << endl << "Filename: " << filename[n] << endl << endl ;
	diskOutFileSet[n] << "This file represents disk contents at stage: " << n <<  "." << endl;
	diskOutFileSet[n] << "Note: notations added to faciliate post-sim analysis." << endl << endl;



	for (k=0; k<2027;k++)
	{
		adj = (0 < k/10) + (0< k/100) + (0< k/1000);
		diskOutFileSet[n] <<"0x" + readLine(k) + fmt[adj] << k ;
		diskOutFileSet[n] << endl;
	}



}
void Disk::dumpToFile(void)
{
	ofstream diskOutFile;
	
	diskOutFile.open("diskDumpfile.txt",fstream::out);
	int k;



	for (k=0; k<2027;k++)
	{
		diskOutFile<< "0x" + readLine(k);
		diskOutFile << endl;
	}
}
