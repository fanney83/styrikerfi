package com.ru.usty.elevator;

public class Elevator implements Runnable {
	
	
	int currFloor;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if(ElevatorScene.elevatorsMayDie){
				return;			
			}
			
			int numOfPeopleInElevator = (6 - ElevatorScene.scene.numberOfPeopleInElevator);
			
			for(int i = 0; i < numOfPeopleInElevator; i++) {
				
				ElevatorScene.semaphoreIN[ElevatorScene.floorCount].release();
			}
		
		}
		
	}
	
	
}
