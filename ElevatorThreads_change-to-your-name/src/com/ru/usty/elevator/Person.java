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
			// do nothing if elevator is NOT in it's critical session
			while(!ElevatorScene.elevatorInCS){}
			
			//person appears and starts waiting at its source floor
			ElevatorScene.scene.incrementNumberOfPeopleWaitingAtFloor(this.sourceFloor);

			//ElevatorScene.elevatorWaitMutex.acquire(); //wait
			
			//person acquires access to elevator
			ElevatorScene.semaphoreIN[this.sourceFloor].acquire();
			//ElevatorScene.elevatorWaitMutex.release();
			
			ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(this.sourceFloor);
		
			ElevatorScene.scene.incrementNumberOfPeopleInElevator(0);
			
			// acquire access out of elevator
			ElevatorScene.semaphoreOut[this.destinationFloor].acquire();
			
			ElevatorScene.scene.decrementNumberOfPeopleInElevator(0);
			
			ElevatorScene.scene.personExitsAtFloor(this.destinationFloor);			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 			
	}
}
