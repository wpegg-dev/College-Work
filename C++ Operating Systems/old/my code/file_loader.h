#pragma once

class file_loader
{
public:
	file_loader(void);
	~file_loader(void);
	bool read_file(pcb&);
};
