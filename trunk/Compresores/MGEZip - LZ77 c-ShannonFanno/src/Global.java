public class Global
{
	public static final int MinLong = 3;
	public static final int MaxLong = 200;
	public static final int Longitudes = MaxLong - MinLong + 1;
	public static int bitsLongitudes;
	
	public static final int Ventana = 256; //1024 bytes de Ventana Memoria
	public static final int Buffer = 40960; //40kb buffer
	public static final int BufferComp = Buffer * 8;
	public static final String RCarro = "\r";
}
