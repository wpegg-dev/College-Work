//driver.h

class OS_Sim_Driver_1{
	public:
		pcb pcb_table;
		Disk disk1;
		queue ready_queue;
		queue wait_queue;
		queue new_queue;
		queue running_queue;
		queue terminated_queue;

};