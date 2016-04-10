package wekatool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import weka.associations.Apriori;
import weka.associations.FPGrowth;
import weka.associations.FPGrowth.AssociationRule;
import weka.core.Instances;
import data.Pair;
import data.Utils;

/*
 * Solution for problem one.
 */
public class Cooperation {
	
	public void fpGrowth() {
		BufferedReader datafile = Utils.getReader(Config.COOP_INPUT_FILE);
		Instances data;
		try {
			data = new Instances(datafile);
			
			double deltaValue = 0.00001;
			FPGrowth fpgrowth = new FPGrowth();
			fpgrowth.setDelta(deltaValue);
			fpgrowth.setLowerBoundMinSupport(Config.COOP_SUPPORT);
			fpgrowth.setMinMetric(Config.COOP_CONFIDENCE);
			fpgrowth.setFindAllRulesForSupportLevel(true);
			fpgrowth.setMaxNumberOfItems(2);
			
			try
			{
				fpgrowth.buildAssociations(data);
				List<AssociationRule> assocRules = fpgrowth.getAssociationRules();
				List<Pair<Double, AssociationRule>> rules = new ArrayList<Pair<Double, AssociationRule>>();
				for(AssociationRule assocRule : assocRules) {
					double w = assocRule.getMetricValue() + 
							((assocRule.getTotalSupport() * Config.ADJUST) / assocRule.getTotalTransactions());
					rules.add(new Pair<Double, AssociationRule>(-w, assocRule));
				}
				Collections.sort(rules);
				writeRules(rules);
			}
			catch (Exception e) {
				e.printStackTrace();
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
	}
	
	private void writeRules(List<Pair<Double, AssociationRule>> rules) {
		BufferedWriter writer = Utils.getWriter(Config.COOP_OUTPUT_FILE);
		try {
			writer.write("Name,Cooperator,Support,Confidence,Weight\n");
			for(Pair<Double, AssociationRule> entry : rules) {
				AssociationRule rule = entry.value;
				writer.write(wrap(rule.getPremise().toString()) + ",");
				writer.write(wrap(rule.getConsequence().toString()) + ",");
				writer.write(rule.getTotalSupport() + ",");
				writer.write(rule.getMetricValue() + ",");
				writer.write(-entry.key + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String wrap(String s) {
		return "\"" + s.substring(1, s.length() - 3) + "\"";
	}
	
	public void apriori() {
		BufferedReader datafile = Utils.getReader(Config.COOP_INPUT_FILE);
		Instances data;
		try {
			data = new Instances(datafile);
			
			double deltaValue = 0.00000001;
			double lowerBoundMinSupportValue = 0.1;
			double minMetricValue = 0.2;
			Apriori apriori = new Apriori();
			apriori.setDelta(deltaValue);
			apriori.setLowerBoundMinSupport(lowerBoundMinSupportValue);
			apriori.setMinMetric(minMetricValue);
			
			try
			{
				apriori.buildAssociations(data);
				System.out.println(apriori.toString());
			}
			catch (Exception e) {
				e.printStackTrace();
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
	}
	
	public static void main(String[] args) throws Exception {
		Cooperation cooperation = new Cooperation();
//		cooperation.apriori(); // OutOfMemoryError
		cooperation.fpGrowth();
	}
}
