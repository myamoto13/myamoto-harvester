package com.extia.socialnetharvester.business;
class A {
	private String message;
	
	synchronized void displayMessage() {
		while(message == null){
			try{
				System.out.println("no message, waiting..");
				wait();
			}catch(InterruptedException ex){
				
			}
		}
		System.out.println("message : " + message);
	}

	public synchronized void setMessage(String message) {
		this.message = message;
		notify();
	}
	
	
}
public class Deadlock implements Runnable {
	
	private A a = new A();
	
	Deadlock() throws InterruptedException {
		Thread.currentThread().setName("MainThread");
		Thread t = new Thread(this, "message outputting");
		t.start();
		
		System.out.println("Back in main thread");
		
		Thread.sleep(2000);
		a.setMessage("salut nounou..");
	}
	public void run() {
		a.displayMessage();
	}
	public static void main(String args[]) {
		try {
			new Deadlock();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}