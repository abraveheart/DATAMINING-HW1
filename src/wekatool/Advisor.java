package wekatool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.associations.FPGrowth;
import weka.associations.FPGrowth.AssociationRule;
import weka.core.Instances;
import data.Person;
import data.Utils;

/*
 * Solution for problem three.
 * In a association rule A => B, if B published a paper earlier than A,
 * and B published more papers than A, B is possibly advisor for A. 
 */
public class Advisor {
	
	private Map<String, Person> info = new HashMap<String, Person>();
	
	private AdvisorValidation validation;
	
	public Advisor() {
		readInfo();
		validation = new AdvisorValidation();
	}
	
	public void fpGrowth() {
		BufferedReader datafile = Utils.getReader(Config.ADV_INPUT_FILE);
		Instances data;
		try {
			data = new Instances(datafile);
			
			double deltaValue = 0.00001;
			FPGrowth fpgrowth = new FPGrowth();
			fpgrowth.setDelta(deltaValue);
			fpgrowth.setLowerBoundMinSupport(Config.ADV_SUPPORT);
			fpgrowth.setMinMetric(Config.ADV_CONFIDENCE);
			fpgrowth.setFindAllRulesForSupportLevel(true);
			fpgrowth.setMaxNumberOfItems(2);
			
			BufferedWriter writer = Utils.getWriter(Config.ADV_OUTPUT_FILE);
			try
			{
				fpgrowth.buildAssociations(data);
				List<AssociationRule> assocRules = fpgrowth.getAssociationRules();
				writer.write("Student,Advisor\n");
				for(AssociationRule assocRule : assocRules) {
					String studentName = unwrap(assocRule.getPremise().toString());
					String advisorName = unwrap(assocRule.getConsequence().toString());
					Person student = info.get(studentName);
					if (student == null) {
						System.err.println(studentName + " does not exists!");
						return;
					}
					Person advisor = info.get(advisorName);
					if (advisor == null) {
						System.err.println(advisorName + "does not exists!");
						return;
					}
					if (student.getYear() > advisor.getYear() && student.getPapers() < advisor.getPapers()) {
						writer.write("\"" + studentName + "\",\"" + advisorName + "\"\n");
						validation.validate(studentName, advisorName);
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			} finally {
				writer.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				datafile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("precision: " + validation.getPrecision());
		System.out.println("recall " + validation.getRecall());
	}
	
	private void readInfo() {
		BufferedReader rd = Utils.getReader(Config.INFO_FILE);
		String line;
		try {
			while((line = rd.readLine()) != null) {
				String parts[] = line.split(",");
				Person p = new Person(parts[0]);
				p.setYear(Integer.valueOf(parts[1]));
				p.setPapers(Integer.valueOf(parts[2]));
				info.put(p.getName(), p);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				rd.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String unwrap(String s) {
		return s.substring(1, s.length() - 3);
	}
	
	public static void main(String args[]) {
		Advisor adv = new Advisor();
		adv.fpGrowth();
		String a = "00001";
		if (a.matches("[0-9]+")) {
			System.out.println("match");
		}
	}
}
