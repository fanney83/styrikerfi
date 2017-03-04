package com.ru.usty.elevator;

//class Person implements the interface Runnable
public class Person implements Runnable {
	
	int scourceFloor, destinationFloor;
	
	
	public Person(int scourceFloor, int destinationFloor) {
		this.scourceFloor = scourceFloor;
		this.destinationFloor = destinationFloor;
	}
	
	
	@Override
	public void run() {
		try {
			
			//person appears and start waiting at its source floor
			
			//Elevator needs peace to clear up before take-off no interruption from persons
			ElevatorScene.scene.incrementNumberOfPeopleWaitingAtFloor(this.scourceFloor);
			
			while(ElevatorScene.isInCritical){};
			ElevatorScene.elevatorWaitMutex.acquire(); //wait
			
				 ElevatorScene.semaphoreIN[scourceFloor].acquire();
			
			ElevatorScene.elevatorWaitMutex.release();
			
			ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(scourceFloor);
			
			System.out.println("Person-thread released..!");
			
			//person thread is inside the elevator so increase number
			
			
			ElevatorScene.scene.incrementNumberOfPeopleInElevator(0);
			
			System.out.println("number of people waiting on floor " + scourceFloor + ": " + ElevatorScene.scene.getNumberOfPeopleWaitingAtFloor(this.scourceFloor));
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
	}

}