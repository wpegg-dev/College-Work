//#include "StdAfx.h"
#include "pcb.h"
#include "file_loader.h"

//#include "stdafx.h"
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>

//#include "disk.h"

using namespace std;

extern pcb pcb_table;
//edtern disk disk1;
const string SPC = " ";//just something i use for debugging


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
	
	ifstream myfile ("DataFile2.txt",ifstream::in);
	string line;
	int pid,sz,pri,out_buf_sz,in_buf_sz,tmp_buf_sz;
	static int last_pid = 0;
	
	if (myfile.is_open())
	{
		while(getline (myfile,line))
		{

			//if(myfile.eof())break;


			//cout << line << endl;
			//cout << line.substr(0,2) << endl;
			if(line.substr(0,6) == "// JOB")
			{
				sscanf(line.c_str(),"%*s%*s%x%x%x",&pid,&sz,&pri);
				cout << "testing sccanf logic " << pid << SPC<< sz << SPC<< pri << endl;
				pcb_table.setCodeSize(pid,sz);
				pcb_table.setPriority(pid, pri);
				pcb_table.setStatus(pid, NEW);
				pcb_table.setPc(pid,0);
				pcb_table.setCpuId(pid,0);
			}

			else if(line.substr(0,8) == "// Data ")
			{
				//use pid from last job
				sscanf(line.c_str(),"%*s%*s%x%x%x",&in_buf_sz,&out_buf_sz,&tmp_buf_sz);
				cout << "DATA line " << in_buf_sz << SPC << out_buf_sz << SPC << tmp_buf_sz << endl;
				pcb_table.setInBufSize(pid,in_buf_sz);
				pcb_table.setOutBufSize(pid,out_buf_sz);
				pcb_table.setTempBufSize(pid,tmp_buf_sz);
			}
			else if(line.substr(0,2) == "0x")
			{
				raw_data_line_cnt++;

				//write line to DISk
				//disk1.writeLine(line.substr(2,8));
			}
			//an anomaly in text file - got to check for both - just for debug anyway
			else if((line.substr(0,6) == "// END")||(line.substr(0,5) == "//END"))
			{
				total_raw_data_line_cnt += raw_data_line_cnt;
				cout << "line cnt this section:  " << raw_data_line_cnt << endl;
				cout << "line cnt total for all: " << total_raw_data_line_cnt << endl;
				raw_data_line_cnt = 0;
			}

		}
    




		myfile.close();
  
	}	



	

	return status;
}

