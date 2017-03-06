package com.ru.usty.elevator;

public class Elevator implements Runnable {
	public static int goneOut = 0;
	public static boolean elevatorGoingUp = true;
	public static int freeSpaceInElevator = 0;
	
	@Override
	public void run() {
		while(true) {
			if(ElevatorScene.elevatorsMayDie){
				return;			
			}
	
			allAboard();
			elevatorMove();
			getOut();			
		}		
	}
	
	public void elevatorSleeps() {
		try {
			Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void allAboard() {
		// define how many releases are possible
		freeSpaceInElevator = (6 - ElevatorScene.scene.getNumberOfPeopleInElevator(0));
		
		for(int i = 0; i < freeSpaceInElevator; i++) {				
			ElevatorScene.semaphoreIN[ElevatorScene.floorCount].release();
		}
			
		//Elevator thread sleeps
		elevatorSleeps();
		
		// Elevator not in critical session
		ElevatorScene.elevatorInCS = false;
		
		// acquire semaphore times free-space so that they can be released again
		// in case all spaces were not filled while semaphores released for ghosts.
		freeSpaceInElevator = (6 - ElevatorScene.scene.getNumberOfPeopleInElevator(0));
		for(int i = 0; i < freeSpaceInElevator; i++) {		
			try {
				ElevatorScene.semaphoreIN[ElevatorScene.floorCount].acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void elevatorMove() {
		// if the elevator is on top floor, goingUp becomes false
		if(ElevatorScene.scene.getCurrentFloorForElevator(0) == (ElevatorScene.scene.getNumberOfFloors() - 1)) {
			elevatorGoingUp = false;
		}
		else if(ElevatorScene.scene.getCurrentFloorForElevator(0) == 0) { 
			elevatorGoingUp = true; 
		}
			
		if(elevatorGoingUp == true) {
			ElevatorScene.scene.incrementElevatorAtFloor(0);
		}
		else {
			ElevatorScene.scene.decrementElevatorAtFloor(0);
		}
		
		// CRITICAL SESSION, persons cannot acquire in the meantime
		ElevatorScene.elevatorInCS = true;
		
		//Elevator thread sleeps
		elevatorSleeps();
	}
	
	public void getOut() {
		// releasing persons out to their destination floor
		for(int i = 0; i < 6; i++) {				
			ElevatorScene.semaphoreOut[ElevatorScene.floorCount].release();
		}
		
		//Elevator thread sleeps
		elevatorSleeps();
		
		// in case not everyone got out at last stop, get number of people still inside
		int peopleInElevator = (ElevatorScene.scene.getNumberOfPeopleInElevator(0));
		
		// acquire semaphore times people so that they can be released again.
		for(int i = 0; i < peopleInElevator; i++) {		
			try {
				ElevatorScene.semaphoreOut[ElevatorScene.floorCount].acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Elevator thread sleeps
		elevatorSleeps();
	}	
}
