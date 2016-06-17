package com.tcs.tree.view;

import com.marconi.fusion.X36.X36AlarmDetailReport;

public class TreeNodeAlarm extends AbstractSetTypeUserTreeNode{
	
	private X36AlarmDetailReport alarm;
	
	public TreeNodeAlarm(X36AlarmDetailReport alarm) {
		super(alarm);
		this.alarm = alarm;
	}

	@Override
	public String getName() {		
		return alarm.getAlProbableStr().getValue().toString();
	}

	@Override
	public int getId() {		
		return alarm.getAlarmId().getValue();
	}


	@Override
	public String getSpecificInfo() {		
		return alarm.getAlDetailStr().getValue().toString();
	}

}
