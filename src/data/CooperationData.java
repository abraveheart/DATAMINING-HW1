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

public class CooperationData {
	
	private static String inputFile = "fieldFile.txt";
	
	private static String outputFile = Config.COOP_INPUT_FILE;
	
	private BufferedReader reader;
	
	private BufferedWriter writer;
	
	private List<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
	
	private Map<String, Integer> index = new HashMap<String, Integer>();
	
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
			while((line = reader.readLine()) != null) {
				String [] parts = line.split("\t");
				int num = Integer.valueOf(parts[1]);
				if (num == 0) {
					if (tuple != null) matrix.add(tuple);
					tuple = new ArrayList<Integer>();
				}
				if (parts[2].equals("author")) {
					String name = new String("");
					for(int i = 3; i < parts.length; i++) {
						if (i != 3) name += " ";
						name += parts[i];
					}
					int id = getIndex(name);
					tuple.add(id);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		writeArffHeader();
		writeMatrix();
		closeAll();
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
		CooperationData cooData = new CooperationData();
		cooData.generate();
	}
}
