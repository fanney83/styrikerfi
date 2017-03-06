package com.ru.usty.elevator;

//class Person implements the interface Runnable
public class Person implements Runnable {
	
	int sourceFloor, destinationFloor, elevator;
	
	public Person(int sourceFloor, int destinationFloor) {
		this.sourceFloor = sourceFloor;
		this.destinationFloor = destinationFloor;
		this.elevator = 0;
	}
		
	@Override
	public void run() {
		try {
			// do nothing if elevator is NOT in it's critical session
			while(!ElevatorScene.elevatorInCS){}
			
			//person appears and starts waiting at its source floor
			ElevatorScene.scene.incrementNumberOfPeopleWaitingAtFloor(this.sourceFloor);

			this.elevator = ElevatorScene.scene.whatElevator;
			
			//person acquires access to elevator
			ElevatorScene.semaphoreIN[this.elevator][this.sourceFloor].acquire();
			
			ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(this.sourceFloor);
		
			ElevatorScene.scene.incrementNumberOfPeopleInElevator(0);
			
			// acquire access out of elevator
			ElevatorScene.semaphoreOut[Elevator.num][this.destinationFloor].acquire();
			
			ElevatorScene.scene.decrementNumberOfPeopleInElevator(0);
			
			ElevatorScene.scene.personExitsAtFloor(this.destinationFloor);			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 			
	}
}
