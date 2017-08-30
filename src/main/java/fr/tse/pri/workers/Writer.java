package fr.tse.pri.workers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import fr.tse.pri.info.InfoDate;

public class Writer implements Runnable{

	protected BlockingQueue<Object> blockingQueue = null;
	private static final String POISON = new String("poison");
	private static int countInfo = 0;

	public Writer(BlockingQueue<Object> listInfo){
		this.blockingQueue = listInfo;     
	}

	@Override
	public void run() {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter("output.txt"));
			boolean continueLoop = true;
			while(continueLoop){
				Object buffer = blockingQueue.take();

				if(buffer instanceof String){ 
					continueLoop = false;
					System.out.println("Writer : Poison detected!");
					System.out.println("Nb Information gathered : " + countInfo);
					break;
				}
				else if(buffer instanceof InfoDate) {
					writer.write(buffer.toString()+"\n");
					countInfo++;
				}
			}               


		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch(InterruptedException e){

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 

	}
	public static void main(String[] args) {
	}
}