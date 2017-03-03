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
			
			//auka semaphora sem við köllum á acquire fyrir og release strax á eftir
			ElevatorScene.elevatorWaitMutex.acquire();
			
				 ElevatorScene.semaphore1.acquire(); // wait
			
			ElevatorScene.elevatorWaitMutex.release();

		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(scourceFloor);
		
		System.out.println("Person-thread released..!");
	}

}