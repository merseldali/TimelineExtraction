package fr.tse.pri.workers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.jhu.nlp.wikipedia.WikiPage;
import fr.tse.pri.info.InfoDate;

public class Extractor implements Runnable{

	private static final boolean WIKITABLEACTIVATED = false;
	private static final int MAX_THREADS = 2;
	private static final String POISON = new String("poison");
	private BlockingQueue<Object> listPage;
	private BlockingQueue<String[]> listTimeline;
	private BlockingQueue<Object> listInfo;

	public Extractor(BlockingQueue<Object> list){
		super();
		this.listPage = list;
		this.listTimeline = new LinkedBlockingQueue<>();
		this.listInfo = new LinkedBlockingQueue<>();
	}

	public BlockingQueue<String[]> getListTimeline() {
		return listTimeline;
	}

	public static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader (file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");
		try {
			while((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	public void Extract (WikiPage page){
		String title = page.getTitle();
		String text = page.getWikiText();
		if(text != null && !text.equalsIgnoreCase("")){
			@SuppressWarnings("resource")
			Scanner alltext = new Scanner(text);
			String line = null;
			String[] data = null;

			while (alltext.hasNextLine()){
				line = alltext.nextLine();
				if(line.contains("<timeline>") || line.contains("{{#tag:timeline") || line.contains("&lt;timeline&gt;")){
					data = new String[3];
					String neededData = "";
					do{
						line = alltext.nextLine();
						neededData = neededData +" \n " + line; 
					}while((!line.contains("</timeline>") && (!line.contains("&lt;/timeline&gt;")) && !line.contains("}}")) && alltext.hasNextLine());

					data[0] = title;
					data[1] = neededData;
					data[2] = "EasyTimeLine";
					try {
						this.listTimeline.put(data);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(line.contains("{{Graphical timeline")){
					data = new String[3];
					String neededData = "";
					do{
						line = alltext.nextLine();
						neededData = neededData +" \n " + line; 
					}while((!line.contains("}}")) && alltext.hasNextLine());

					data[0] = title;
					data[1] = neededData;
					data[2] = "GraphicalTimeLine";
					try {
						this.listTimeline.put(data);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(line.contains("wikitable")){
					data = new String[3];
					String neededData = "{| class=\"wikitable\"";
					do{
						line = alltext.nextLine();
						neededData = neededData +" \n " + line; 
					}while((!line.contains("|}")) && alltext.hasNextLine());

					data[0] = title;
					data[1] = neededData+"|}";
					data[2] = "WikiTable";
					try {
						if(WIKITABLEACTIVATED){
						this.listTimeline.put(data);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void Start(){

		//Service Executor

		ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);

		ThreadPoolExecutor servicet = (ThreadPoolExecutor) service;

		//Run a thread for each timeline
		service.execute(new ParserCaller(this.listTimeline, this.listInfo));
		service.execute(new Writer(this.listInfo));
		
		boolean continueLoop = true;
		while(continueLoop){
			try {
				Object currentPage = listPage.poll(1, TimeUnit.SECONDS);
				if (!currentPage.equals(POISON)){
					Extract((WikiPage) currentPage);
				} else {
					System.out.println("Extractor : Poison detected");
					this.listTimeline.put(new String[]{POISON});
					continueLoop = false;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Size of list timelines : " + this.listTimeline.size());

		while(servicet.getActiveCount() > 0){
		}

		servicet.shutdown();
	}

	public static void main(String[] args) throws IOException {
		/*
		String monTxt = readFile("timeline.xml");

		BlockingQueue<String[]> list = Extract(monTxt);

		//Service Executor

		ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);

		ThreadPoolExecutor servicet = (ThreadPoolExecutor) service;

		//Run a thread for each timeline
		for(int i=0; i<MAX_THREADS; i++){
			service.execute(new ParserCaller(list));
		}


		while(servicet.getActiveCount() > 0){
		}

		servicet.shutdown();
		 */

		/*
		while (!list.isEmpty()) {
			String[] dataToShow = list.pollLast();
			int k = 0;
			for(String s : dataToShow){
				if (k != 1){
					System.out.println(s);
				}
				k++;
			}
		}
		 */

		//LinkedBlockingQueue<InfoDate> outQueue = new LinkedBlockingQueue<InfoDate>();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Start();
	}
}