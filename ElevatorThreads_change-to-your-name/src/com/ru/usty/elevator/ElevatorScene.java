package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ElevatorScene {
	
	//Semaphores that are accessible from anywhere
	public static Semaphore[] semaphoreIN;
	public static Semaphore[] semaphoreOut;
	public static Semaphore personCountMutex;	
	//public static Semaphore elevatorWaitMutex;
	public static Semaphore floorCountMutex;	// keeping track of floor status
	public static Semaphore elevatorCountMutex; // for counting inside elevator
	public static Semaphore exitedCountMutex;
	
	public static boolean elevatorsMayDie;
	public static boolean elevatorInCS = false; //when in critical section person can't acquire()

	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 500;  //milliseconds

	private int numberOfFloors;
	private int numberOfElevators;
	public static int numberOfPeopleInElevator = 0;
	public static int floorCount = 0;
	
	private Thread elevatorThread = null;
	public static ElevatorScene scene;
	
	ArrayList<Integer> personCount;
	ArrayList<Integer> exitedCount = null;

	//Base function: New scenario
	public void restartScene(int numberOfFloors, int numberOfElevators) {
		// cleaning up threads, new scene starts
		elevatorsMayDie = true;

		if(elevatorThread != null) {
			if(elevatorThread.isAlive()){
				try {
					elevatorThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}		
		}
		
		elevatorsMayDie = false;
		
		elevatorInCS = true;
		floorCount = 0;
		scene = this;	
		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;
		
		semaphoreIN = new Semaphore[numberOfFloors];
		semaphoreOut = new Semaphore[numberOfFloors];
		
		// initialize semaphores on each floor with 0
		for(int i = 0; i < numberOfFloors; i++) {
			semaphoreIN[i] =  new Semaphore(0);		
		}
		
		for(int i = 0; i < numberOfFloors; i++) {
			semaphoreOut[i] =  new Semaphore(0);
		}
		
		personCountMutex = new Semaphore(1);
		//elevatorWaitMutex = new Semaphore(1);
		floorCountMutex = new Semaphore(1);
		exitedCountMutex = new Semaphore(1);
		
		elevatorThread  = new Thread(new Elevator(numberOfElevators-1));
		elevatorThread.start();
		
		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;
		elevatorCountMutex = new Semaphore(1);

		personCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfFloors; i++) {
			this.personCount.add(0);
		}

		if(exitedCount == null) {
			exitedCount = new ArrayList<Integer>();
		}
		else {
			exitedCount.clear();
		}
		for(int i = 0; i < getNumberOfFloors(); i++) {
			this.exitedCount.add(0);
		}
	}

	public Thread addPerson(int sourceFloor, int destinationFloor) {
		// person thread made and started
		Thread thread = new Thread(new Person(sourceFloor, destinationFloor));
		thread.start();
		
		return thread;  //this means that the testSuite will not wait for the threads to finish
	}

	// What floor is the elevator at
	public int getCurrentFloorForElevator(int elevator) {
		return floorCount;
	}
	//elevator goes down a floor
	public void decrementElevatorAtFloor(int elevator) {	
		try {
			ElevatorScene.floorCountMutex.acquire();
				floorCount--; // critical session, mutex for safe counting
			ElevatorScene.floorCountMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//elevator goes up a floor
	public void incrementElevatorAtFloor(int elevator) {		
		try {
			ElevatorScene.floorCountMutex.acquire();
				floorCount++;
			ElevatorScene.floorCountMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}			
	}

	// How many persons are inside an elevator
	public int getNumberOfPeopleInElevator(int elevator) {
		return numberOfPeopleInElevator;
	}
	
	// increase when person enters elevator
	public void incrementNumberOfPeopleInElevator(int elevator) {
		try {
			ElevatorScene.elevatorCountMutex.acquire();
				numberOfPeopleInElevator++;
			ElevatorScene.elevatorCountMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// decrease when person exits elevator
	public void decrementNumberOfPeopleInElevator(int elevator) {
		try {
			ElevatorScene.elevatorCountMutex.acquire();
				numberOfPeopleInElevator--;
			ElevatorScene.elevatorCountMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// How many people are waiting on floor
	public int getNumberOfPeopleWaitingAtFloor(int floor) {
		return personCount.get(floor);
	}
	
	// person gets inside elevator, decrease
	public void decrementNumberOfPeopleWaitingAtFloor (int floor) {		
		try {			
			ElevatorScene.personCountMutex.acquire();			
				personCount.set(floor, (personCount.get(floor) - 1));			
			ElevatorScene.personCountMutex.release();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// person is added with source floor = floor
	public void incrementNumberOfPeopleWaitingAtFloor (int floor) {	
		try {		
			ElevatorScene.personCountMutex.acquire();				
				personCount.set(floor, (personCount.get(floor) + 1));			
			ElevatorScene.personCountMutex.release();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Base function: definition must not change, but add your code if needed
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	//Base function: no need to change unless you choose
	//				 not to "open the doors" sometimes
	//				 even though there are people there
	public boolean isElevatorOpen(int elevator) {
		return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {
		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}

	//Person threads must call this function to
	//let the system know that they have exited.
	//Person calls it after being let off elevator
	//but before it finishes its run.
	public void personExitsAtFloor(int floor) {
		try {
			
			exitedCountMutex.acquire();
			exitedCount.set(floor, (exitedCount.get(floor) + 1));
			exitedCountMutex.release();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public int getExitedCountAtFloor(int floor) {
		if(floor < getNumberOfFloors()) {
			return exitedCount.get(floor);
		}
		else {
			return 0;
		}
	}

}