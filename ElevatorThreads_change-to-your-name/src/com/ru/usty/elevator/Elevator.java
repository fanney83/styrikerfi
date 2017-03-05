package com.ru.usty.elevator;

public class Elevator implements Runnable {
	public static int goneOut = 0;
	public static boolean elevatorGoingUp = true;

	@Override
	public void run() {
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
			
			// CRITICAL SESSION, persons cannot acquire in the meantime
			ElevatorScene.elevatorInCS = true;
			
			// acquire semaphore times free-space so that the can be released again
			// in case all spaces were not filled while semaphores released for ghosts.
			freeSpaceInElevator = (6 - ElevatorScene.scene.getNumberOfPeopleInElevator(0));
			for(int i = 0; i < freeSpaceInElevator; i++) {		
				try {
					ElevatorScene.semaphoreIN[ElevatorScene.floorCount].acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
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
			
			//Elevator thread sleeps
			try {
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
				}
			
			int personsGoingOut = (ElevatorScene.scene.numPersonsGoingOutAtDestination(
								   ElevatorScene.scene.getCurrentFloorForElevator(0)));
			
			// releasing persons out to their destination floor
			for(int i = 0; i < personsGoingOut; i++) {	
				goneOut += personsGoingOut;
				System.out.println("personsGoneOut: " + goneOut);
				ElevatorScene.semaphoreOut[ElevatorScene.scene.getCurrentFloorForElevator(0)].release();
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
