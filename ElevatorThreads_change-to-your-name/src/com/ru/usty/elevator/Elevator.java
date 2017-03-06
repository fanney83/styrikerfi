package com.ru.usty.elevator;

public class Elevator implements Runnable {
	public boolean elevatorGoingUp = true;
	public static int num;
	
	public Elevator(int elevatorNumber) {
		num = elevatorNumber;
	}
	
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
		int freeSpaceInElevator = (6 - ElevatorScene.scene.getNumberOfPeopleInElevator(num));
		
		for(int i = 0; i < freeSpaceInElevator; i++) {				
			ElevatorScene.semaphoreIN[num][ElevatorScene.floorCount].release();
		}
			
		//Elevator thread sleeps
		elevatorSleeps();
		
		// Elevator not in critical session
		ElevatorScene.elevatorInCS = false;
		
		// acquire semaphore times free-space so that they can be released again
		// in case all spaces were not filled while semaphores released for ghosts.
		freeSpaceInElevator = (6 - ElevatorScene.scene.getNumberOfPeopleInElevator(num));
		for(int i = 0; i < freeSpaceInElevator; i++) {		
			try {
				ElevatorScene.elevatorWaitMutex.acquire();
				ElevatorScene.scene.whatElevator = num;
				ElevatorScene.semaphoreIN[num][ElevatorScene.floorCount].acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ElevatorScene.elevatorWaitMutex.release();
	}
	
	public void elevatorMove() {
			
			// if the elevator is on top floor, goingUp becomes false
			if(ElevatorScene.scene.getCurrentFloorForElevator(num) == (ElevatorScene.scene.getNumberOfFloors() - 1)) {
				elevatorGoingUp = false;
			}
			else if(ElevatorScene.scene.getCurrentFloorForElevator(num) == 0) { 
				elevatorGoingUp = true; 
			}
				
			if(elevatorGoingUp == true) {
				ElevatorScene.scene.incrementElevatorAtFloor(num);
			}
			else {
				ElevatorScene.scene.decrementElevatorAtFloor(num);
			}
			
			// CRITICAL SESSION, persons cannot acquire in the meantime
			ElevatorScene.elevatorInCS = true;
			
			//Elevator thread sleeps
			elevatorSleeps();

	}
	
	public void getOut() {
		// number of people possibly getting out
		int peopleInElevator = (ElevatorScene.scene.getNumberOfPeopleInElevator(num));
		// releasing persons out to their destination floor
		for(int i = 0; i < peopleInElevator; i++) {				
			ElevatorScene.semaphoreOut[num][ElevatorScene.floorCount].release();
		}
		
		//Elevator thread sleeps
		elevatorSleeps();
		
		// in case not everyone got out at last stop, get number of people still inside
		peopleInElevator = (ElevatorScene.scene.getNumberOfPeopleInElevator(num));
		
		// acquire semaphore times people so that they can be released again.
		for(int i = 0; i < peopleInElevator; i++) {		
			try {
				ElevatorScene.semaphoreOut[num][ElevatorScene.floorCount].acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Elevator thread sleeps
		elevatorSleeps();
	}	
}
