package fr.tse.pri.parsers;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

import fr.tse.pri.info.InfoDate;


public class TimelineParser {
	//Compile pattern here
	public static final Pattern patternpipe = Pattern.compile("\\[+(.*?)\\|+");
	public static final Pattern patternbasic = Pattern.compile("\\[+(.*?)\\]+");
	
	
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
		String[] decompose = text.split("timeline>");

		for (int i = 0; i < decompose.length; i++) {
			if(i % 2 ==1){
				//System.out.println(decompose[i]);
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
		}
		return writer;
	}


	public static ArrayList parse(String text){
		ArrayList<String> listinfoStr = new ArrayList<String>();
		ArrayList<InfoDate> listinfo = new ArrayList<InfoDate>();
		Scanner alltext = new Scanner(text);
		String startyear = null;
		String endyear = null;
		String line = null;
		
		while (alltext.hasNextLine()){
			
			line = alltext.nextLine();
			
			String[] resulttab= new String[]{null,null,null};
			StringBuilder sb = new StringBuilder(line);

			if(line.contains("from:"))
			{
				int index = sb.indexOf("from:");

				while(sb.substring(index+5, index+6).trim().length() == 0){
					sb = sb.deleteCharAt(index+5);
				}

				index = sb.indexOf("from:");

				int i = index;
				while(sb.substring(i, i + 1).trim().length() > 0){
					i++;
				}
				String result = sb.substring(index,i);
				resulttab[0]=result;
				//System.out.println(result);
			}

			if(line.contains("till:"))
			{
				int index = sb.indexOf("till:");

				while(sb.substring(index+5, index+6).trim().length() == 0){
					sb = sb.deleteCharAt(index+5);
				}

				index = sb.indexOf("till:");

				int i = index;
				while((i < sb.length()) && sb.substring(i, i + 1).trim().length() > 0){
					i++;
				}
				String result = sb.substring(index,i);
				resulttab[1]=result;
				//System.out.println(result);
			}

			if(line.contains("text:"))
			{
				int index = sb.indexOf("text:");
				int i = index;
				while((i < sb.length()) && sb.substring(i, i + 1).trim().length() > 0){
					i++;
				}
				String result = sb.substring(index,i);
				if (result.contains("[[")) {
					resulttab[2]=result;
					//System.out.println(result);
				}
			}

			if(line.contains("Period"))
			{
				if((resulttab[0] != null) && (resulttab[1] != null)){
					startyear = resulttab[0];
					endyear = resulttab[1];
				}

			}

			//Cr�ation de l'objet InfoDate
			if((resulttab[0] != null) && (resulttab[1] != null) && (resulttab[2] != null)){
				//Remplacement de "start" par sa valeur
				if(resulttab[0].contains("start")){
					resulttab[0] = startyear;
				}
				
				//Remplacement de "end" par sa valeur
				if(resulttab[1].contains("end")){
					resulttab[1] = endyear;
				}
				
				//Extraction du mot-cl� pour le lien
				if (resulttab[2].contains("|")) {
					Matcher matcher = patternpipe.matcher(resulttab[2]);
					matcher.find();
					resulttab[2] = matcher.group(1);
				} else {
					Matcher matcher = patternbasic.matcher(resulttab[2]);
					matcher.find();
					resulttab[2] = matcher.group(1);
				}

				//listinfo.add(new InfoDate(resulttab));
				//listinfoStr.add(new InfoDate(resulttab).toString());
			}	
		}
		for(InfoDate info : listinfo){
			System.out.println(info);
		}
		return listinfoStr;
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
		//Lecture du fichier
		String monTxt = readFile("C://Users//boual//Desktop//PRI//easytimeline.txt");
		//Filtrer le contenu inutile
		filter(monTxt, "C://Users//boual//Desktop//PRI//neededdata.txt");
		//Lecture du fichier trait�
		String txtfiltered = readFile("C://Users//boual//Desktop//PRI//neededdata.txt");
		//Parsing du fichier trait�
		parse(txtfiltered);


	}
}

