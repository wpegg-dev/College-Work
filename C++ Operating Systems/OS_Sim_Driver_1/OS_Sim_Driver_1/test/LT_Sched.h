#pragma once

class LT_Sched
{
public:
	LT_Sched(void);
	~LT_Sched(void);
	void shedule(void);
	void schedInit(void);
	void loadJobs(void);
	void saveToDisk(void);
};
