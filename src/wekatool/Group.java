package wekatool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import weka.associations.FPGrowth;
import weka.associations.FPGrowth.AssociationRule;
import weka.associations.FPGrowth.BinaryItem;
import weka.core.Instances;
import data.Utils;

/*
 * Solution for problem three, but is not encouraged.
 * Please see our solution using self-written FPGrowth.
 */
public class Group {
	public void fpGrowth() {
		BufferedReader datafile = Utils.getReader(Config.GRP_INPUT_FILE);
		Instances data;
		try {
			data = new Instances(datafile);
			
			double deltaValue = 0.00001;
			double lowerBoundMinSupportValue = 0.0001;
			double minMetricValue = 0.2;
			FPGrowth fpgrowth = new FPGrowth();
			fpgrowth.setDelta(deltaValue);
			fpgrowth.setLowerBoundMinSupport(lowerBoundMinSupportValue);
			fpgrowth.setMinMetric(minMetricValue);
			fpgrowth.setFindAllRulesForSupportLevel(true);
			
			try
			{
				fpgrowth.buildAssociations(data);
				List<AssociationRule> rules = fpgrowth.getAssociationRules();
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
	
	private void writeRules(List<AssociationRule> rules) {
		BufferedWriter writer = Utils.getWriter(Config.GRP_OUTPUT_FILE);
		try {
			writer.write("Group\n");
			for(AssociationRule rule : rules) {
				for(BinaryItem item : rule.getPremise()) {
					writer.write(item.getAttribute().name() + ",");
				}
				for(BinaryItem item : rule.getConsequence()) {
					writer.write(item.getAttribute().name() + ",");
				}
				writer.write("\n");
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
	
	public static void main(String args[]) {
		Group group = new Group();
		group.fpGrowth();
	}
}
