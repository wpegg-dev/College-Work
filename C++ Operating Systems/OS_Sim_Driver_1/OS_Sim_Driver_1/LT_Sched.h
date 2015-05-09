#pragma once

typedef enum SCH_POLICY{
	FCFS,
	SJF,
	PRIORITY};
//#define FCFS_POLICY
//#define SJF_POLICY
#define PRIORITY_POLICY
class LT_Sched
{
	SCH_POLICY schedule_policy;
public:
	LT_Sched(void);
	~LT_Sched(void);
	void shedule(void);
	void schedInit(void);
	void loadJobs(void);

	void loadJobsSJF(void);
	void saveToDisk(void);
};
