package wekatool;

import java.io.BufferedReader;
import java.io.IOException;

import weka.associations.FPGrowth;
import weka.core.Instances;
import data.Utils;

public class Cooperation {
	
	public void fpGrowth() {
		BufferedReader datafile = Utils.getReader(Config.COOP_INPUT_FILE);
		Instances data;
		try {
			data = new Instances(datafile);
//		data.setClassIndex(data.numAttributes() - 1);
			
			double deltaValue = 0.00000001;
		double lowerBoundMinSupportValue = 0.0001;
			double minMetricValue = 0.2;
			int numRulesValue = 5;
//		double upperBoundMinSupportValue = 1.0;
			String fpgrowth_result;
			FPGrowth fpgrowth = new FPGrowth();
			fpgrowth.setDelta(deltaValue);
		fpgrowth.setLowerBoundMinSupport(lowerBoundMinSupportValue);
			fpgrowth.setNumRulesToFind(numRulesValue);
//		fpgrowth.setUpperBoundMinSupport(upperBoundMinSupportValue);
			fpgrowth.setMinMetric(minMetricValue);
			fpgrowth.setFindAllRulesForSupportLevel(true);
//		String[] fpoptions = new String[] {"-N", "7"};
//		fpgrowth.setOptions(fpoptions);
			
			try
			{
				fpgrowth.buildAssociations(data);
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			fpgrowth_result = fpgrowth.toString();
			
			System.out.println(fpgrowth_result);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		Cooperation cooperation = new Cooperation();
		cooperation.fpGrowth();
	}
}
