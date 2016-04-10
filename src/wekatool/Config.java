package wekatool;

public class Config {
	
	public static final String RAW_FILE = "fieldFile.txt";
	
	/*
	 * problem one
	 */
	public static final String COOP_INPUT_FILE = "cooperation.arff";
	public static final String COOP_OUTPUT_FILE = "cooperation.csv";
	public static final double COOP_CONFIDENCE = 0.1;
	public static final double COOP_SUPPORT = 0.00003;
	public static final double ADJUST = 100.0;
	
	/*
	 * problem two
	 */
	public static final String INFO_FILE = "info.txt";
	public static final String ADV_INPUT_FILE = "cooperation.arff";
	public static final String ADV_OUTPUT_FILE = "advisor.csv";
	public static final double ADV_CONFIDENCE = 0.05;
	public static final double ADV_SUPPORT = 0.00003;
	
	public static final String ADV_VALIDATION = "advisor_validation.txt";
	
	/*
	 * problem three
	 */
	public static final String GRP_INPUT_FILE = "cooperation.arff";
	public static final String GRP_OUTPUT_FILE = "group.txt";
	
	
}
