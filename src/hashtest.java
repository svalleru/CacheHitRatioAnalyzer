import Mhash.MurmurHash;
import com.ask.naf.utils.MurmurHash3;

public class hashtest {
	
	public static void main(String[] args) {
	    String input = "hello\tUSCH";
	    byte[] inputb = input.getBytes();
	    long h = MurmurHash.hash64(inputb, -1);
		long i = MurmurHash.hash64(inputb, -1);
		long m =MurmurHash3.MurmurHash3_x64_64(inputb, -1);
		System.out.println(h+"**"+i+"**"+m);

	    
	}


}
