package wekatool;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import data.Utils;

/*
 * Validation process.
 * The precision is not accurate.
 */
public class AdvisorValidation {
	
	private Map<String, String> advInfo = new HashMap<String, String>();
	
	private int total = 0;
	
	private int correct = 0;
	
	public AdvisorValidation() {
		BufferedReader reader = Utils.getReader(Config.ADV_VALIDATION);
		String line;
		try {
			while((line = reader.readLine()) != null) {
				String parts[] = line.split("\t");
				advInfo.put(parts[0], parts[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean compare(String student, String advisor) {
		String corAdvisor = advInfo.get(student);
		if (corAdvisor == null) return false;
		String corParts[] = corAdvisor.split(" ");
		String parts[] = advisor.split(" ");
		int cl = corParts.length;
		int pl = parts.length;
		if (parts[pl - 1].matches("[0-9]+")) {
			System.out.println("digists: " + parts[pl - 1]);
			pl--;
		}
		for(int i = 0, j = 0; i < cl && j < pl;i++, j++) {
			while(i < corParts.length && corParts[i].isEmpty()) i++;
			if (i == corParts.length) return false;
			if (corParts[i].charAt(corParts[i].length() - 1) == '.') {
				corParts[i] = corParts[i].substring(0, corParts[i].length() - 1);
			}
			if (parts[j].charAt(parts[j].length() - 1) == '.') {
				parts[j] = parts[j].substring(0, parts[j].length() - 1);
			}
			if (!corParts[i].contains(parts[j]) && !parts[j].contains(corParts[i])) {
				return false;
			}
		}
		System.out.println(student + " " + advisor);
		return true;
	}
	
	public void validate(String student, String advisor) {
		boolean isValid = compare(student, advisor);
		if (isValid) {
			correct++;
		}
		total++;
	}
	
	public double getPrecision() {
		return correct * 1.0 / total;
	}
	
	public double getRecall() {
		System.out.println("correct: " + correct);
		return correct * 1.0 / advInfo.size();
	}
}
