package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * The base function definitions of this class must stay the same
 * for the test suite and graphics to use.
 * You can add functions and/or change the functionality
 * of the operations at will.
 *
 */

public class ElevatorScene {
	
	//Semaphores that are accessible from anywhere
	public static Semaphore[] semaphoreIN;
	public static Semaphore[] semaphoreOut;
	public static Semaphore personCountMutex;
	public static Semaphore elevatorWaitMutex;
	public static Semaphore floorCountMutex;
	public static Semaphore exitedCountMutex;
	
	public static ElevatorScene scene;
	
	public static boolean elevatorsMayDie;
	
	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 500;  //milliseconds

	private int numberOfFloors;
	private int numberOfElevators;
	
	private Thread elevatorThread = null;
	
	public static int counter = 6;
	public static int numberOfPeopleInElevator;
	public static int floorCount;
	

	ArrayList<Integer> personCount; //use if you want but
									//throw away and
									//implement differently
									//if it suits you
	ArrayList<Integer> exitedCount = null;

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {
		//semaphore is born initialized locked
		elevatorsMayDie = true;
		
		if(elevatorThread != null) {
			if(elevatorThread.isAlive()){
				try {
					elevatorThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		elevatorsMayDie = false;
		
		scene = this;		
		
		semaphoreIN = new Semaphore[numberOfFloors];
		semaphoreOut = new Semaphore[numberOfFloors];
		
		
		for(int i = 0; i < numberOfFloors; i++) {
			semaphoreIN[0] =  new Semaphore(0);
			
		}
		
		for(int i = 0; i < numberOfFloors; i++) {
			semaphoreOut[i] =  new Semaphore(0);
		}
		
		personCountMutex = new Semaphore(1);
		elevatorWaitMutex = new Semaphore(1);
		floorCountMutex = new Semaphore(1);
		//semaphore1 = new Semaphore(0);
		
		
		
		// Below is a test thread, we are not implementing our real threads this way
/*		elevatorThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					
					if(ElevatorScene.scene.elevatorsMayDie) {
						return;
					}
					for(int i = 0; i < counter; i++) {
						ElevatorScene.semaphore1.release(); // signal
					}
				}
			}
			
		});
		elevatorThread.start();/*
		/**
		 * Important to add code here to make new
		 * threads that run your elevator-runnables
		 * 
		 * Also add any other code that initializes
		 * your system for a new run
		 * 
		 * If you can, tell any currently running
		 * elevator threads to stop
		 */

		//add an elevator thread + start
		
		System.out.println("Elevator a GOGO\n");
		elevatorThread  = new Thread(new Elevator());
		elevatorThread.start();
		
		
		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;

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
		exitedCountMutex = new Semaphore(1);
	}

	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {
		// personthread made, can put srcFloor and dstFloor when fits (later maybe)
		
		Thread thread = new Thread(new Person(sourceFloor, destinationFloor));
		thread.start();
		
		/**
		 * Important to add code here to make a
		 * new thread that runs your person-runnable
		 * 
		 * Also return the Thread object for your person
		 * so that it can be reaped in the testSuite
		 * (you don't have to join() yourself)
		 */

		//dumb code, replace it! should have a semaphore around it so 2 threads wont do at same time
		//personCount.set(sourceFloor, personCount.get(sourceFloor) + 1);
		incrementNumberOfPeopleWaitingAtFloor(sourceFloor);
		
		return thread;  //this means that the testSuite will not wait for the threads to finish
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {

		//dumb code, replace it!
		return floorCount;
	}
		//elevator goes down a floor
		public void decrementElevatorAtFloor(int elevator) {
			
			try {
				ElevatorScene.floorCountMutex.acquire();
					floorCount--;
				ElevatorScene.floorCountMutex.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
	public void incrementNumberOfPeopleInElevator(int elevator) {
		
	}
	
	
	public void decrementNumberOfPeopleInElevator(int elevator) {
		
	}
	
	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {
		
		//dumb code, replace it!
		switch(elevator) {
		case 1: return 1;
		case 2: return 4;
		default: return 3;
		}
	}
	
	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {

		return personCount.get(floor);
	}
	
	public void decrementNumberOfPeopleWaitingAtFloor (int floor) {
		
		try {
			
			ElevatorScene.personCountMutex.acquire();
			
				personCount.set(floor, (personCount.get(floor) - 1));
			
			ElevatorScene.personCountMutex.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void incrementNumberOfPeopleWaitingAtFloor (int floor) {
		
		try {
			
			personCountMutex.acquire();
				
				personCount.set(floor, (personCount.get(floor) + 1));
			
			personCountMutex.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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