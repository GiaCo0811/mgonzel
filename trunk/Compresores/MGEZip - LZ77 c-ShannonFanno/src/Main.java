import java.text.DecimalFormat;

	/**
 * This class can take a variable number of parameters on the command
 * line. Program execution begins with the main() method. The class
 * constructor is not invoked unless an object of type 'Class1'
 * created in the main() method.
 */
public class Main
{
	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main (String[] args)
	{
		long initialTime = System.currentTimeMillis();
		int bits = Global.Ventana - Global.MinLong;
		while (bits > 0)
		{
			bits/=2;
			Global.bitsLongitudes++;
		}
		
		Archivador.main(args);

		long endTime = System.currentTimeMillis();
		double totalTime = (endTime - initialTime)/1000;
		DecimalFormat formateador = new DecimalFormat("##########.##");
		System.out.println("Total time: "+formateador.format(totalTime)+"s.");
	}
}
