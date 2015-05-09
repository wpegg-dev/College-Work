#include "StdAfx.h"
#include<iostream>
//#include<string>
#include"disk.h"
using namespace std;
Disk::Disk()
{ 
	nextWord = 0;
	disk = new char [DISK_SIZE_IN_CHARS];
}
Disk::~Disk()
{ 
	delete disk;
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
		cout << b<<endl;
	}
	else
		cout << "Error: Outside disk";
}	
void Disk::writeLine(char *in, int lineNum)
{
	if( lineNum < DISK_SIZE_IN_LINES) 
	strncpy(&disk[lineNum*8],in,8);
	else cout << "Disk is Full" <<endl;
}


/*void main(){
	Disk H;
//	char * a=
	for(int i=0;i<30;i++){
		char a='a';
		H.write(a);
	}
//	H.writeLine()
	H.read(2);
}*/