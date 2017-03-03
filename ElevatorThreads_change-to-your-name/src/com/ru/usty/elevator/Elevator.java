package com.ru.usty.elevator;

public class Elevator implements Runnable {
	int currFloor;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		if(ElevatorScene.elevatorsMayDie){
			return;
		}
		
	}
	
	
}
