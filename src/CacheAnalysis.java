import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.ask.naf.utils.MurmurHash3;

public class CacheAnalysis extends LogReader

{
	private static final String TAB = "\t";
	private static final String Q = "q=";
	private static final String AMP = "&";
	private static final String AMP_MODE = "&mode=";
	private static final String BRACE = ")";
	private static final String EMPTY = "";
	private static final String US = "US";
	static Logger log = Logger.getLogger("CacheAnalysis");
	private static long hour12 = 12 * 3600000;;
	private static long hour24 = 24 * 3600000;;
	static long hour6 = 6 * 3600000;// Long.parseLong(args[0]);
	static HashMap<Long, TimeStamps> Cache = new HashMap<Long, TimeStamps>();
	private static long ts;

	public CacheAnalysis(String filePath) throws Exception {
		super(filePath);
		// TODO Auto-generated constructor stub
	}
	
	public static void cleanup(long currenttime){
		TimeStamps ts=null;
		List<Long> valuestodelete=new ArrayList<Long>();
		for(Long key:Cache.keySet()){
			ts = Cache.get(key);
			if(currenttime - ts.hr6 > hour6 && currenttime - ts.hr12 > hour12 && currenttime - ts.hr24 > hour24 ){
				valuestodelete.add(key);
			}
		}
		
		for(Long value:valuestodelete){
			Cache.remove(value);
		}
	}

	public static void main(String[] args) {
		hour6 = Long.parseLong(args[0])*3600000;
		hour12 = Long.parseLong(args[1])*3600000;
		hour24 = Long.parseLong(args[2])*3600000;
		LogReader file = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

		// CACHE BUILDING
		String line = null;
		
		TimeStamps tsmp;
		StringBuilder out = new StringBuilder();
		long count=0;
		long prevcount=0;
		
		for (int y = 8; y <= 8; y++) {
			for (int z = 0; z <= 2; z++) {
				DecimalFormat myFormatter = new DecimalFormat("00");
				String hour = myFormatter.format(z);
				String day = myFormatter.format(y);
				try {
					System.out.println("File: " + day + "/" + day + "-" + hour
							+ ".log");
					System.out.println("Total: "+ count +" file: " + (count-prevcount)+ " Cache size: "+ Cache.size());
					prevcount = count;
					file = new LogReader(day + "/" + day + "-" + hour + ".log");
					cleanup(ts);
					System.out.println("Cache size after cleanup " +Cache.size());
				} catch (Exception e1) { 
					e1.printStackTrace();
					continue;
				}

				for (String line1 : file) {
					try {
						out.setLength(0);
						line = line1;
						String timestamp = line.substring(0, 23); // Timestamp
						/*
						 * line = line.substring(13 + line
						 * .indexOf("initParams= ["), line
						 * .indexOf("] ModifiedParams= ["));
						 */
						int qindex = line.indexOf(Q);
						String q = line.substring(2 + qindex, line.indexOf(AMP,
								qindex + 1)); // query
						String mode;
						int modeIndex = line.indexOf(AMP_MODE);
						int ampIndex = line.indexOf(AMP, modeIndex + 1);
						int brIndex = line.indexOf(BRACE, modeIndex + 1);
						if (brIndex < 0) {
							brIndex = Integer.MAX_VALUE;
						}
						if (ampIndex < 0) {
							ampIndex = Integer.MAX_VALUE;
						}
						mode = line.substring(6 + modeIndex, Math.min(brIndex,
								ampIndex));
						if (EMPTY.equals(mode.trim())) {
							mode = US;
						}
						/*
						 * } catch (Exception e) {
						 * 
						 * mode = line.substring(5 + line.indexOf("mode="),
						 * line.indexOf("),defaults")); //continue; }
						 */ts = format.parse(timestamp).getTime(); // timestamp
						byte[] mb = (q + TAB + mode).getBytes();
						long m = MurmurHash3.MurmurHash3_x64_64(mb, -1);
						tsmp = Cache.get(m);
						out.append(m).append(TAB);
						if (tsmp != null) { // found in cache

							if ((ts - tsmp.hr6) <= hour6)
								out.append(1).append(TAB);
							else {
								out.append(0).append(TAB);
								tsmp.hr6 = ts;
							}
							if ((ts - tsmp.hr12) <= hour12)
								out.append(1).append(TAB);
							else {
								out.append(0).append(TAB);
								tsmp.hr12 = ts;
							}
							if ((ts - tsmp.hr24) <= hour24)
								out.append(1);
							else {
								out.append(0);
								tsmp.hr24 = ts;
							}
						} else {
							out.append(0).append(TAB).append(0).append(TAB)
									.append(0);
							Cache.put(m, new TimeStamps(ts));
						}
						log.info(out.toString());
						count++;
					} catch (Exception e) {
						System.err.println(line);
						e.printStackTrace();
					}
				}

			}

		}
	}

}
