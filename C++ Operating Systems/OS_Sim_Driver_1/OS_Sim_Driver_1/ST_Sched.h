#pragma once

class ST_Sched
{
public:
	ST_Sched(void);
	~ST_Sched(void);
	int dispatch(void);
	int m_dispatch(int* pid);
	bool contextSwitchSaveState(const int&, const int&);
	bool contextSwitchLoadState(const int&, const int&);
};
