package com.ru.usty.elevator;

//class Person implements the interface Runnable
public class Person implements Runnable {
	
	int sourceFloor, destinationFloor;
	
	public Person(int sourceFloor, int destinationFloor) {
		this.sourceFloor = sourceFloor;
		this.destinationFloor = destinationFloor;
	}
		
	@Override
	public void run() {
		try {
			
			//person appears and start waiting at its source floor
			ElevatorScene.scene.incrementNumberOfPeopleWaitingAtFloor(this.sourceFloor);
			
			//while(ElevatorScene.isInCritical){};
			//ElevatorScene.elevatorWaitMutex.acquire(); //wait
			
			//person acquires access to elevator
			ElevatorScene.semaphoreIN[this.sourceFloor].acquire();
			//ElevatorScene.elevatorWaitMutex.release();
			
			ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(this.sourceFloor);
		
			ElevatorScene.scene.incrementNumberOfPeopleInElevator(0);
			ElevatorScene.scene.incrementNumPersonsGoingOutAtDestination(this.destinationFloor);
			
			System.out.println("number of people waiting on floor " + sourceFloor + ": " + ElevatorScene.scene.getNumberOfPeopleWaitingAtFloor(this.sourceFloor));
			
			// acquire access out of elevator
			ElevatorScene.semaphoreOut[this.destinationFloor].acquire();
			
			ElevatorScene.scene.decrementNumberOfPeopleInElevator(0);
			ElevatorScene.scene.decrementNumPersonsGoingOutAtDestination(this.destinationFloor);
			
			ElevatorScene.scene.personExitsAtFloor(this.destinationFloor);
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 	
		
	}

}