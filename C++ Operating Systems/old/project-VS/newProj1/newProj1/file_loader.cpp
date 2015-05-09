
#include "pcb.h"
#include "file_loader.h"

//#include "stdafx.h"
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>

#include "disk.h"

using namespace std;

extern pcb pcb_table;
extern Disk disk1;
const string SPC = " ";//just something i use for debugging


#define DEBUG_LOADER 0




file_loader::file_loader(void)
{
}

file_loader::~file_loader(void)
{
}

bool file_loader::read_file(pcb& pcb_in)
{

	int raw_data_line_cnt = 0;
	int total_raw_data_line_cnt = 0;
	bool status  = true;
	
	//ifstream myfile ("DataFile2.txt",ifstream::in);
	ifstream myfile;
	ofstream outfile;
	ofstream outfile_readline;
	myfile.open("DataFile2.txt", fstream::in);
	outfile.open("outfile.txt",fstream::out);
	outfile_readline.open("outfile_readline.txt",fstream::out);
	string line;
	
	string test_string = "test";
	//char * test_ch_p = test_string;
	char * test_ch_p1 = "test";
	int disk_line_counter = 0;

	int pid,sz,pri,out_buf_sz,in_buf_sz,tmp_buf_sz;
	static int last_pid = 0;
	
	if (myfile.is_open())
	//if (true)
	{
		while(getline (myfile,line))
		{

			total_raw_data_line_cnt++;

			//if(myfile.eof())break;


			//cout << line << endl;
			//cout << line.substr(0,2) << endl;
			if(line.substr(0,6) == "// JOB")
			{
				sscanf(line.c_str(),"%*s%*s%x%x%x",&pid,&sz,&pri);
				pcb_table.setCodeSize(pid,sz);
				pcb_table.setProcessSize(pid,sz+44);
				pcb_table.setPriority(pid, pri);
				pcb_table.setStatus(pid, NEW);
				pcb_table.setPc(pid,0);
				pcb_table.setCpuId(pid,0);
				pcb_table.setCodeDiskAddress(pid,disk_line_counter);
			}

			else if(line.substr(0,8) == "// Data ")
			{
				//use pid from last job
				sscanf(line.c_str(),"%*s%*s%x%x%x",&in_buf_sz,&out_buf_sz,&tmp_buf_sz);
				#if DEBUG_LOADER
				cout << "DATA line " << in_buf_sz << SPC << out_buf_sz << SPC << tmp_buf_sz << endl;
				#endif
				pcb_table.setInBufSize(pid,in_buf_sz);
				pcb_table.setOutBufSize(pid,out_buf_sz);
				pcb_table.setTempBufSize(pid,tmp_buf_sz);
				pcb_table.setDataDiskAddress(pid,disk_line_counter);
			}
			else if(line.substr(0,2) == "0x")
			{
				raw_data_line_cnt++;
				char test_line[9];

				
				for(int i =0;i<8;i++)
				{
					test_line[i] = line[2+i];
				}
				test_line[8] = NULL;
				disk1.writeLine(test_line,disk_line_counter++);
				
			}
			//an anomaly in text file - got to check for both - just for debug anyway
			else if((line.substr(0,6) == "// END")||(line.substr(0,5) == "//END"))
			{
				#if DEBUG_LOADER
				cout << "line cnt this section:  " << disk_line_counter << endl;
				#endif
			}

		}
    




		myfile.close();

  
	}	



	cout << endl << endl << "test contents of disk" << endl;

	
	for (int j=0; j< 16216; j++)
	//for (int j=0; j< DISK_SIZE_IN_CHARS; j++)
	//for (int j=0; j< 100; j++)
	{
		outfile << disk1.readChar(j);
		if(((j+1)%8)==0)
		{
			outfile << endl;
		}
	}
	
	//for (int j=0; j<DISK_SIZE_IN_LINES;j++)
	for (int k=0; k<2027;k++)
	{
		outfile_readline<< "0x" + disk1.readLine(k);
		outfile_readline << endl;
	}
	

	cout << "raw_data_line_cnt" << raw_data_line_cnt << endl;
	cout << "disk_line_counter" << disk_line_counter << endl;
	cout << "char count" << raw_data_line_cnt * 8 << endl;
	cout << "total line cnt" << total_raw_data_line_cnt << endl;

	return status;
}

