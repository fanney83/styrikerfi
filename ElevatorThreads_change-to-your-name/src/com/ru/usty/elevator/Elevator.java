package com.ru.usty.elevator;

public class Elevator implements Runnable {
	
	
	//private static int currFloor;
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if(ElevatorScene.elevatorsMayDie){
				return;			
			}
			System.out.println("ElevatorScene.scene.numberOfPeopleInElevator: " + ElevatorScene.numberOfPeopleInElevator);
			int numOfPeopleInElevator = (6 - ElevatorScene.numberOfPeopleInElevator);
			
			System.out.println("number of people in elevator: " +  numOfPeopleInElevator + "\n");
			
			ElevatorScene.isInCritical = true; //person can't acquire atm
			for(int i = 0; i < numOfPeopleInElevator; i++) {
				try {
					ElevatorScene.elevatorWaitMutex.acquire();
						ElevatorScene.semaphoreIN[ElevatorScene.floorCount].release();
					ElevatorScene.elevatorWaitMutex.release();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			ElevatorScene.isInCritical = false; //now a person can acquire 

		
		}
		
	}
	
	
}
