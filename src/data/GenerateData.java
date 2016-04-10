package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wekatool.Config;

/*
 * Generate input file for analysis, including .arff file and info.txt.
 * .arff file contains the itemset to be analyzed. This file will be 
 * used for all the three problems.
 * info.txt contains the first publication year and the number of papers for
 * each researcher. This file will be used for problem two.
 */
public class GenerateData {
	
	private static String inputFile = Config.RAW_FILE;
	
	private static String outputFile = Config.COOP_INPUT_FILE;
	
	private BufferedReader reader;
	
	private BufferedWriter writer;
	
	private List<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
	
	private Map<String, Integer> index = new HashMap<String, Integer>();
	
	private Map<String, Person> info = new HashMap<String, Person>();
	
	private ArrayList<String> people = new ArrayList<String>();
	
	private void init() {
		reader = Utils.getReader(inputFile);
		writer = Utils.getWriter(outputFile);
	}
	
	private void closeAll() {
		try {
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generate() {
		init();
		String line;
		try {
			ArrayList<Integer> tuple = null;
			int year = 2016;
			while((line = reader.readLine()) != null) {
				String [] parts = line.split("\t");
				int num = Integer.valueOf(parts[1]);
				if (num == 0) {
					updateInfo(year);
					if (tuple != null) matrix.add(tuple);
					tuple = new ArrayList<Integer>();
					people.clear();
					year = 2016;
				}
				if (parts[2].equals("author")) {
					String name = new String("");
					for(int i = 3; i < parts.length; i++) {
						if (i != 3) name += " ";
						name += parts[i];
					}
					int id = getIndex(name);
					tuple.add(id);
					people.add(name);
				} else if (parts[2].equals("year")) {
					year = Integer.valueOf(parts[3]);
				}
			}
			updateInfo(year);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		writeArffHeader();
		writeMatrix();
		writeInfo();
		closeAll();
	}
	
	private void updateInfo(int year) {
		for(String name : people) {
			Person p = info.get(name);
			if (p == null) {
				p = new Person(name);
				p.setYear(year);
				p.incPapers();
				info.put(name, p);
			} else {
				if (year < p.getYear()) {
					p.setYear(year);
				}
				p.incPapers();
			}
		}
	}
	
	private void writeArffHeader() {
		try {
			writer.write("@relation 'cooperation'\n");
			ArrayList<Pair<Integer, String>> sortedIndex = new ArrayList<Pair<Integer, String>>();
			for(Map.Entry<String, Integer> entry : index.entrySet()) {
				sortedIndex.add(new Pair<Integer, String>(entry.getValue(), entry.getKey()));
			}
			Collections.sort(sortedIndex);
			for(Pair<Integer, String> pair : sortedIndex) {
				writer.write("@attribute \"" + pair.value + "\" {F, T}\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeMatrix() {
		try {
			writer.write("@data\n");
			for(ArrayList<Integer> tuple : matrix) {
				writer.write("{");
				Collections.sort(tuple);
				int last = -1;
				for(int i = 0; i < tuple.size(); i++) {
					int num = tuple.get(i);
					if (num == last) continue;
					if (i != 0) writer.write(", ");
					writer.write(num + " T");
					last = num;
				}
				writer.write("}\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeInfo() {
		BufferedWriter wr = Utils.getWriter(Config.INFO_FILE);
		try {
			for(Person person : info.values()) {
				wr.write(person.getName() + "," + person.getYear() + "," + person.getPapers() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				wr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int getIndex(String name) {
		Integer i = index.get(name);
		if (i == null) {
			int sz = index.size();
			index.put(name, sz);
			i = sz;
		}
		return i;
	}
	
	public static void main(String args[]) {
		GenerateData cooData = new GenerateData();
		cooData.generate();
	}
}
