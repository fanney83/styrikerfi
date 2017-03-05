package com.ru.usty.elevator;

public class Elevator implements Runnable {
	
	
	//private static int currFloor;
	boolean elevatorGoingUp = true;


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if(ElevatorScene.elevatorsMayDie){
				return;			
			}
				System.out.println("ElevatorScene.scene.numberOfPeopleInElevator: " + ElevatorScene.numberOfPeopleInElevator);
				System.out.println("Curr Floor: " + ElevatorScene.scene.getCurrentFloorForElevator(0)+ "\n");
	
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
		
		//ElevatorScene.isInCritical = false; //now a person can acquire 

		
		}
		
	}
	
	
}
