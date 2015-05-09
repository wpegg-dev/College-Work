
#include <string>
#pragma once
using namespace std;
const int FRAME_DISK_SIZE_IN_CHARS = 16384;
const int FRAME_DISK_SIZE_IN_LINES = 2048;
const int FRAME_SIZE_IN_CHARS = 32;
const int FRAME_SIZE_IN_LINES = 4;
const int SIZE_IN_FRAMES = 510;
class Frame_Disk {
	private:
		//char * disk;
		char disk[FRAME_DISK_SIZE_IN_CHARS];
		int nextWord;
	public:
		Frame_Disk();
		~Frame_Disk();
		void write (char a);
		void writeLine(char *in,int lineNum);
		void read ( int i );
		char readChar(int i);
		string readLine(int);
		void dumpToFile(void);
		void dumpNFile(const int& n);
};
