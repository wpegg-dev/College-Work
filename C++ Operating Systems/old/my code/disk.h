
#pragma once
const int DISK_SIZE_IN_CHARS = 16384;
const int DISK_SIZE_IN_LINES = 2048;
class Disk {
	private:
		char * disk;
		int nextWord;
	public:
		Disk();
		~Disk();
		void write (char a);
		void writeLine(char *in,int lineNum);
		void read ( int i );
};
