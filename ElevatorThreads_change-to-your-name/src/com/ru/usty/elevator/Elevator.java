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
			
			int freeSpaceInElevator = (6 - ElevatorScene.scene.getNumberOfPeopleInElevator(0));
			System.out.println("numberOfPeopleInElevator: "+ ElevatorScene.scene.getNumberOfPeopleInElevator(0));
			System.out.println("freeSpaceInElevator: "+ freeSpaceInElevator);
			for(int i = 0; i < freeSpaceInElevator; i++) {
				
				ElevatorScene.semaphoreIN[ElevatorScene.floorCount].release();
				System.out.println("semaphoreIN value for floor: "+ ElevatorScene.floorCount + ": " + 
						ElevatorScene.semaphoreIN[ElevatorScene.floorCount]);
			}
			
			//Elevator thread sleeps
			try {
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}
		
	}
	
	
}
