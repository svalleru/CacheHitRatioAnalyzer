import java.text.*;

public class LogCounter {
public static void main(String args[])
{
	for (int y=8; y<=12 ; y++)
	{
		for(int z=0;z<=23;z++)
		{
		DecimalFormat myFormatter = new DecimalFormat("00");
	      String hour = myFormatter.format(z);
	      String day = myFormatter.format(y);
	      System.out.println(day+"/"+day+"-"+hour);
	 
		}
	}
	}
}	


