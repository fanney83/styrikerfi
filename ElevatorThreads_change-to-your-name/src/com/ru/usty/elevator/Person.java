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
			
			//Elevator needs peace to clear up before take-off no interruption from persons
			ElevatorScene.elevatorWaitMutex.acquire(); //wait
				//person acquires access to elevator
				ElevatorScene.semaphoreIN[sourceFloor].acquire();
			ElevatorScene.elevatorWaitMutex.release();
			
			ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(sourceFloor);
			
			System.out.println("Person-thread released..!");
			
			//person thread is inside the elevator so increase number	
			ElevatorScene.scene.incrementNumberOfPeopleInElevator(0);
			
			System.out.println("number of people waiting on floor " + sourceFloor + ": " + ElevatorScene.scene.getNumberOfPeopleWaitingAtFloor(this.sourceFloor));
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
	}

}