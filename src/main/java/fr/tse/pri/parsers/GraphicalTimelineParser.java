package fr.tse.pri.parsers;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import fr.tse.pri.info.AtData;
import fr.tse.pri.info.InfoDate;
public class GraphicalTimelineParser {
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
	public static BufferedWriter filter (String text, String path) throws IOException{
		//Parsing algorithm
		BufferedWriter writer = null;
		new FileWriter(new File(path)).close();
		String[] decompose = text.split("Graphical timeline");
		//System.out.println(decompose[1]);
		for (int i = 1; i < decompose.length; i++) {
			try
			{
				writer = new BufferedWriter( new FileWriter(path,true));
				writer.write(decompose[i]);
			}
			catch ( IOException e )
			{
			}
			finally
			{
				try
				{
					if ( writer != null)
						writer.close( );
				}
				catch ( IOException e)
				{
				}
			}
		}
		return writer;
	}
	public static ArrayList parse (String text){
		ArrayList<String> output = new ArrayList<String>();
		ArrayList<AtData> output1 = new ArrayList<AtData>();
		ArrayList<InfoDate> outputData = new ArrayList<InfoDate>();
		Scanner alltext = new Scanner(text);
		InfoDate info = new InfoDate ();
		String[] resulttab= new String[]{null,null,null};
		String line = null;
		int compteur= 0;
		int i = -1;
		while (alltext.hasNextLine()){
			line = alltext.nextLine();
			if (line.contains("from=")){
				compteur++;
				String[] sides = line.split("from=");
				//System.out.println("FROM : "+Double.valueOf(sides[1]));
				resulttab[0]= sides[1];
				info.setbeginDate(sides[1]); 
			}
			if (line.contains("to=")){
				compteur++;
				String[] sides = line.split("to=");
				//System.out.println("TO : " +sides[1]);
				resulttab[1]= sides[1];
				info.setendDate(sides[1]); 
			}
			if (line.contains("title=")||line.contains("text=")){
				compteur++;
				if (line.contains("title=")){
					String[] sides = line.split("title=");
					//System.out.println("text : "+sides[1].split("]]")[0].substring(sides[1].split("]]")[0].indexOf("[[")+2));
					resulttab[2]= sides[1].split("]]")[0].substring(sides[1].split("]]")[0].indexOf("[[")+2);
					info.setLink(sides[1].split("]]")[0].substring(sides[1].split("]]")[0].indexOf("[[")+2)); 
				}//outputData.add(info);
				if (line.contains("text=")){
					String[] sides = line.split("text=");
					if (sides.length==1){
						//System.out.println("text : N/A");
						resulttab[2]= "N/A";
						info.setLink("N/A");
					}
					else{
						//System.out.println("text : " +sides[1].split("]]")[0].substring(sides[1].split("]]")[0].indexOf("[[")+2));
						resulttab[2]= sides[1].split("]]")[0].substring(sides[1].split("]]")[0].indexOf("[[")+2);
						info.setLink((sides[1]).split("]]")[0].substring(sides[1].split("]]")[0].indexOf("[[")+2)); 
					}
				}
			}
			if (compteur == 3){		
				compteur =0;
				//outputData.add(new InfoDate(resulttab));
				//output.add(new InfoDate(resulttab).toString());
				//System.out.println("Salut"+info.getYearB() +" "+ info.getYearE()+" " + info.getLink());
			}
			if (line.contains("at=")){
				AtData info1 =new AtData(0,"");
				String line1 = alltext.nextLine();
				//System.out.println(line1);
				String[] sides = line.split("at=");
				//System.out.println("AT :"+Double.valueOf(sides[1]));
				info1.setValue(Double.valueOf(sides[1]));
				String[] texts = line1.split("]]");
				//System.out.println("TEXT :" +texts[0].substring(texts[0].indexOf("[[")+2));
				info1.setData(texts[0].substring(texts[0].indexOf("[[")+2));
				output1.add(info1);
				output.add(info1.toString());
			}
		}
		for (String string : output) {
			System.out.println(string);
		}
		return output;
	}
	public static boolean exist (ArrayList <String> output, String info) {
		boolean res = false;
			for (String string : output) {
				if (string.equals(info))
					res =true;
			}
		return res;
	}
	public static void main(String[] args) throws IOException {
		String monTxt = readFile("C://Users//boual//Desktop//PRI//graphicaltimeline.txt");
		filter(monTxt, "C://Users//boual//Desktop//PRI//neededgraphicaldata.txt");
		String txtfiltered = readFile("C://Users//boual//Desktop//PRI//neededgraphicaldata.txt");
		parse(txtfiltered);
	}
}