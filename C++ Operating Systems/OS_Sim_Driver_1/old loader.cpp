#include "StdAfx.h"
#include "pcb.h"
#include "file_loader.h"

#include "stdafx.h"
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>

//#include "disk.h"

using namespace std;

extern pcb pcb_table;


file_loader::file_loader(void)
{
}

file_loader::~file_loader(void)
{
}

bool file_loader::read_file(pcb& pcb_in)
{

	bool status  = true;
	
	ifstream myfile ("DataFile1.txt",ifstream::in);
	string line;
	int pcb_index = 0;
	size_t code_size = 0;

	//string size_string;
	int num = 0;
	string num_string;

	//std::cout << std::setiosflags(std::ios_base::right) << std::setw(10) << "test" << endl;
	
	if (myfile.is_open())
	{
		while(getline (myfile,line))
		{

			//if(myfile.eof())break;


			//cout << line << endl;
			//cout << line.substr(0,2) << endl;
			if(line.substr(0,6) == "// JOB")
			{
				string s1;
				string s2;
				const string SPC = " ";//just something i use for debugging
				int id,sz,pri;//pid, size, priority

				sscanf(line.c_str(),"%*s%*s%x%x%x",&id,&sz,&pri);

				pcb_table.setCodeSize(id,sz);
				pcb_table.setPriority(id, pri);
				pcb_table.setStatus(id, NEW);
				pcb_table.setPc(id,0);
				pcb_table.setCpuId(id,0);




				cout << "testing new sccanf logic " << id << SPC<< sz << SPC<< pri << endl;
				cout << "JOB command line" << endl;
				cout << line.substr(7,7) << endl;
				if(line.substr(8,1) == " ")
				{
					cout << "pid is 1 char: " << line.substr(7,1) << endl;
					if(line.substr(10,1) == " ")
					{

				
						string size_string(line.substr(9,1));
						stringstream SStream(size_string);
						SStream >> num;

						num_string = line.substr(9,1);

						sscanf(line.substr(9,1).c_str(),"%x",&num);

						
						//sscanf(num_string.c_str(),"%x",&num);

						cout << "size is 1 char: " << line.substr(9,1) << " " << num <<  endl;

						if(line.substr(12,1) == "\n")
							cout << "size of priority is 1 char: " << line.substr(11,1) << endl;
						else
							cout << "size of priority is 2 char: " << line.substr(11,2) << endl;

					}
					else
					{
						sscanf(line.substr(8,3).c_str(),"%x",&num);

						cout << "size is 2 chars: " << line.substr(9,2) <<  " " << num << endl;
						if(line.substr(13,1) == "\n")
							cout << "size of priority is 1 char: " << line.substr(12,1) << endl;
						else
							cout << "size of priority is 2 char: " << line.substr(12,2) << endl;
					}
				}
				else
				{
					cout << "pid is 2 chars: " << line.substr(7,2) << endl;
				}
				
			}

			if(line.substr(0,6) == "// DAT")
			{
				cout << "DATA command line" << endl;
			}

			//if(strncmp((line.begin(),line.begin()++2),"//"))
			//if(line.substr(0,2)
			//	cout << "command line!" << endl;
    
		}
    
		myfile.close();
  
	}	



	

	return status;
}

