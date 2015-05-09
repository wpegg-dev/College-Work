//#include "StdAfx.h"
#include<iostream>
#include <fstream>
#include<string>
#include<sstream>
#include"disk.h"
#include <iomanip>
#include "cpu.h"

using namespace std;
Disk::Disk()
{ 
	nextWord = 0;
	//disk = new char [DISK_SIZE_IN_CHARS];
	for(int i = 0; i< DISK_SIZE_IN_CHARS; i++)
		disk[i] = 'F';

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
	ofstream diskOutFileSet[12];
#ifndef MULTI_PROCESSOR
	string filename[12]={
		"SingleCPUDiskDumpStage0.txt","SingleCPUDiskDumpStage1.txt",
		"SingleCPUDiskDumpStage2.txt","SingleCPUDiskDumpStage3.txt",
		"SingleCPUDiskDumpStage4.txt","SingleCPUDiskDumpStage5.txt",
		"SingleCPUDiskDumpStage6.txt","SingleCPUDiskDumpStage7.txt",
		"SingleCPUDiskDumpStage8.txt","SingleCPUDiskDumpStage9.txt",
		"SingleCPUDiskDumpStage10.txt","SingleCPUDiskDumpStageFINAL.txt"};
#else
	string filename[12]={
		"MultiCPUDiskDumpStage0.txt","MultiCPUDiskDumpStage1.txt",
		"MultiCPUDiskDumpStage2.txt","MultiCPUDiskDumpStage3.txt",
		"MultiCPUDiskDumpStage4.txt","MultiCPUDiskDumpStage5.txt",
		"MultiCPUDiskDumpStage6.txt","MultiCPUDiskDumpStage7.txt",
		"MultiCPUDiskDumpStage8.txt","MultiCPUDiskDumpStage9.txt",
		"MultiCPUDiskDumpStage10.txt","MultiCPUDiskDumpStageFINAL.txt"};
#endif
	diskOutFileSet[n].open(filename[n].c_str(),fstream::out);
	string fmt[4]={"..........000","..........00","..........0",".........."};

	int k, adj;//counter and adj for formatting output


	diskOutFileSet[n] << endl << "Filename: " << filename[n] << endl << endl ;
	diskOutFileSet[n] << "This file represents disk contents at stage: " << n <<  "." << endl;
#ifdef MULTI_PROCESSOR
	diskOutFileSet[n] << "Note: For final contents of disk see MultiCPUDiskDumpStageFINAL.txt"<<"." << endl;
#else
	diskOutFileSet[n] << "Note: For final contents of disk see SingleCPUDiskDumpStageFINAL.txt"<<"." << endl;
#endif
	diskOutFileSet[n] << "Note: Notations added." << endl << endl;



	for (k=0; k<2048;k++)
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



	for (k=0; k<2048;k++)
	{
		diskOutFile<< "0x" + readLine(k);
		diskOutFile << endl;
	}
}
