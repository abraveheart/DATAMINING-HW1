package wekatool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import weka.associations.FPGrowth;
import weka.associations.FPGrowth.AssociationRule;
import weka.core.Instances;
import data.Utils;

public class Cooperation {
	
	public void fpGrowth() {
		BufferedReader datafile = Utils.getReader(Config.COOP_INPUT_FILE);
		Instances data;
		try {
			data = new Instances(datafile);
			
			double deltaValue = 0.00000001;
			double lowerBoundMinSupportValue = 0.00005;
			double minMetricValue = 0.2;
			FPGrowth fpgrowth = new FPGrowth();
			fpgrowth.setDelta(deltaValue);
			fpgrowth.setLowerBoundMinSupport(lowerBoundMinSupportValue);
			fpgrowth.setMinMetric(minMetricValue);
			fpgrowth.setFindAllRulesForSupportLevel(true);
			fpgrowth.setMaxNumberOfItems(2);
			
			try
			{
				fpgrowth.buildAssociations(data);
				List<AssociationRule> assocRules = fpgrowth.getAssociationRules();
				Map<Double, AssociationRule> rules = new TreeMap<Double, AssociationRule>();
				for(AssociationRule assocRule : assocRules) {
					double w = assocRule.getMetricValue() + 
							((assocRule.getTotalSupport() * 100.0) / assocRule.getTotalTransactions());
					rules.put(-w, assocRule);
				}
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
	
	private void writeRules(Map<Double, AssociationRule> rules) {
		BufferedWriter writer = Utils.getWriter(Config.COOP_OUTPUT_FILE);
		try {
			writer.write("Name,Cooperator,Support,Confidence,Weight\n");
			for(Map.Entry<Double, AssociationRule> entry : rules.entrySet()) {
				AssociationRule rule = entry.getValue();
				writer.write(wrap(rule.getPremise().toString()) + ",");
				writer.write(wrap(rule.getConsequence().toString()) + ",");
				writer.write(rule.getTotalSupport() + ",");
				writer.write(rule.getMetricValue() + ",");
				writer.write(-entry.getKey() + "\n");
//				System.out.println("consequence " + rule.getConsequence());
//				System.out.println("consequence support " + rule.getConsequenceSupport());
//				System.out.println("metric value " + rule.getMetricValue());
//				System.out.println("premise support " + rule.getPremiseSupport());
//				System.out.println("total support " + rule.getTotalSupport());
//				System.out.println("total transactions " + rule.getTotalTransactions());
//				System.out.println("metric type " + rule.getMetricType());
//				System.out.println("premise " + rule.getPremise());
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
	
	public static void main(String[] args) throws Exception {
		Cooperation cooperation = new Cooperation();
		cooperation.fpGrowth();
	}
}
