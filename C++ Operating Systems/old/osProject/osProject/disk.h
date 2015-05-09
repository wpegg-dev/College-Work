
#include <string>
#pragma once
using namespace std;
const int DISK_SIZE_IN_CHARS = 16384;
const int DISK_SIZE_IN_LINES = 2048;
class Disk {
	private:
		//char * disk;
		char disk[DISK_SIZE_IN_CHARS];
		int nextWord;
	public:
		Disk();
		~Disk();
		void write (char a);
		void writeLine(char *in,int lineNum);
		void read ( int i );
		char readChar(int i);
		string readLine(int);
		void dumpToFile(void);
		void dumpNFile(const int& n);
};
