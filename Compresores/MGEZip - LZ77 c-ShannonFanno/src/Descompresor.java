/**
 * This class can take a variable number of parameters on the command
 * line. Program execution begins with the main() method. The class
 * constructor is not invoked unless an object of type 'Compresor'
 * created in the main() method.
 */
import java.io.*;

public class Descompresor
{
	private static void GenerarShannon(cLista Lista){
		int tamlista = Global.Longitudes + 257; //se agrega el caracter EOF a la lista de ASCII
		int i;
		
		for (i = 0; i < tamlista; i++)
		{
			Lista.mInsertar(i,1,Direccion.Fin,1);
			Lista.IncrementarFrec(1);
		}
	}
	
	public static boolean miDescompresor (RandomAccessFile entr,Registro regArchivo,String Descomprimido) 
	{
//		System.out.println("Descomprimiendo...");
		cLista ListaShannon = new cLista ();
		GenerarShannon(ListaShannon);


		//DataInputStream entr;
		DataOutputStream archSalida;
		long tamanioArchivo = regArchivo.Tamanio;
		long YaDescomprimido = 0;

		//try {tamanioArchivo = entr.length();} catch (IOException io){}

		byte []buffer;
		int offsetBuffer = 0;
		int leidos = 0;
		int tamVmem = 0;
		int VMemoria = 0;
		
		byte []BitsBuffer;
		int CantidadBits;
		int poscompbuffer = 0;

/*		try {
			 entr = new DataInputStream(new BufferedInputStream(new FileInputStream(ArchivoEntrada)));
		}
		catch (FileNotFoundException nosiste) {
			System.out.println("El archivo no existe");
			return false;
		}
*/
		try {
			archSalida = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(Descomprimido)));
		}
		catch (IOException IOE) {
			System.out.println("Error: no se pudo crear el archivo");
			return false;
		}
		
		System.out.print(Global.RCarro + regArchivo.Nombre + "... " + YaDescomprimido*100/tamanioArchivo + "%");
		
		buffer = new byte[Global.Buffer];
		BitsBuffer = new byte[Global.BufferComp];

		try {
			byte [] tempbuff = new byte[Global.Buffer];
			leidos = entr.read(tempbuff,offsetBuffer ,tempbuff.length - offsetBuffer);
			CantidadBits = PasarABytes (tempbuff,leidos,BitsBuffer);
		}
		catch (IOException AIOLALA) {
			try {
				archSalida.close();
			}
			catch (IOException AIOO) {;}
			return false;
		}
		
		if (CantidadBits <= 0)
			return false;
		
		poscompbuffer = 0;
		offsetBuffer = 0;
		int suma;
		int sumatemp;
		
		cNodo primero,actual,ultimo;
		byte Char;
		do
		{
			primero = ListaShannon.ObtenerPrimero();
			ultimo = ListaShannon.ObtenerUltimo();
			suma = ListaShannon.ObtenerFrec();

			while (primero!=ultimo)
			{
				actual = primero;
				for (sumatemp = actual.mGetFrec();sumatemp < (suma / 2);sumatemp+=actual.mGetFrec())
					actual = actual.mGetSig();
					
				if (BitsBuffer[poscompbuffer] == 0)
				{
					suma = sumatemp;
					ultimo = actual;
				}
				else if (BitsBuffer[poscompbuffer] == 1)
				{
					suma = suma - sumatemp;
					primero = actual.mGetSig();
				}
				poscompbuffer++;
				if (poscompbuffer == CantidadBits)
				{
					YaDescomprimido += leidos;
					System.out.print(Global.RCarro + regArchivo.Nombre + "... " + YaDescomprimido*100/tamanioArchivo + "%");

					byte [] tempbuffer = new byte[Global.Buffer];
					try {
						leidos = entr.read(tempbuffer);
					}
					catch (IOException io) {;}
					CantidadBits = PasarABytes (tempbuffer,leidos,BitsBuffer);
					poscompbuffer = 0;
				}
			}
				

			//busco bits en la lista de shannon-fanno y los traduzco agregandolos al buffer
			//vuelvo a leer de archivo hasta que se acabe.
			int TempChar = primero.mGetChar();
			Char = (byte)TempChar;
			ListaShannon.IncrementarFrec(1);
			primero.mIncremFrec();

			if (primero != ListaShannon.ObtenerPrimero())
				ListaShannon.OrdenarNodoDeLista(primero);
				
			if (TempChar < 256)
			{
//				System.out.println("caracter: " + (char) primero.mGetChar() + " valor ascii " + primero.mGetChar());
				buffer[offsetBuffer] = Char;
				offsetBuffer++;
				if (tamVmem < Global.Ventana)
					tamVmem++;
				else
					VMemoria++;
			}
			else if (TempChar > 256)
			{
				int i;
				int Longitud = TempChar + Global.MinLong - 257;
				int Posic = 0;
				int Mascara = 1;

				for (i = 0; i < Global.bitsLongitudes;i++)
				{
					if (BitsBuffer[poscompbuffer] == 1)
						Posic += Mascara;
					Mascara*=2;
					poscompbuffer++;
					if (poscompbuffer == CantidadBits)
					{
						YaDescomprimido += leidos;
						System.out.print(Global.RCarro + regArchivo.Nombre + "... " + YaDescomprimido*100/tamanioArchivo + "%");

						byte [] tempbuffer = new byte[Global.Buffer];
						try {
							leidos = entr.read(tempbuffer);
						}
						catch (IOException io) {;}
						CantidadBits = PasarABytes (tempbuffer,leidos,BitsBuffer);
						poscompbuffer = 0;
					}
				}
//				System.out.println("Longitud " + Longitud + " posicion " + Posic);
				if (tamVmem + VMemoria + Longitud > buffer.length)
				{
					try{ archSalida.write(buffer,0,VMemoria); }
					catch (IOException io) {;}
						
					for (i = 0;i < tamVmem ; i++)
						buffer[i] = buffer[VMemoria + i];
					VMemoria = 0;
					offsetBuffer = tamVmem;
				}
				for (i = 0; i < Longitud; i++)
				{
					buffer[VMemoria + tamVmem + i] = buffer[VMemoria + tamVmem - Global.Ventana + Posic + i];
				}
				offsetBuffer += i;
				
				if (tamVmem == Global.Ventana)
					VMemoria += i;
				else
				{
					tamVmem +=i;
					if (tamVmem > Global.Ventana)
					{
						VMemoria += (tamVmem % Global.Ventana);
						tamVmem -= (tamVmem % Global.Ventana);
					}
				}
					
			}
				
			if (tamVmem + VMemoria == buffer.length)
			{
				try{ archSalida.write(buffer,0,VMemoria); }
				catch (IOException io) {;}

				for (int i = 0;i < tamVmem ; i++)
					buffer[i] = buffer[VMemoria + i];
				VMemoria = 0;
				offsetBuffer = tamVmem;
			}
				

		} while ((primero.mGetChar() != 256) && (leidos != -1));
		//       distinto del Caracter EOF

		try { archSalida.write(buffer,0,VMemoria + tamVmem); }
		catch (IOException io) {;} //Grabo el resto del buffer al archivo
		YaDescomprimido += 100;
		System.out.print(Global.RCarro + regArchivo.Nombre + "... " + YaDescomprimido*100/tamanioArchivo + "%");

		System.out.println();

		try {archSalida.close();} catch (IOException io){;}
		return true;
	}
	
	private static int PasarABytes(byte [] VecComprim,int cantCarComp,byte [] VecDeBytes)
	{
		int Mascara;
		int posComp,posBytes;
		int cont;
		
		if (cantCarComp < 0)
			return -1;
		
		for (posComp = 0,posBytes = 0; posComp < cantCarComp; posComp++)
		{
			Mascara = 128;
			cont = 0;
			while (cont < 8)
			{
				if ((VecComprim[posComp] & Mascara) != 0)
					VecDeBytes[posBytes] = 1;
				else
					VecDeBytes[posBytes] = 0;
				posBytes++;
				cont++;
				Mascara>>=1;
			}
		}
		
		return posBytes;		
	}
	
	
	private static int PasarABits(byte [] VecOrig,int cantOrig,byte [] VecSalida)
	{
		int posOrig = cantOrig;
		int posSalida;
		byte Mascara;
		int corrim;
		int bytesCompletos = 0;
		boolean primeraVez = true;
		
		posSalida = (posOrig / 8) + ((posOrig % 8) >= 1 ? 1 : 0) - 1;
		posOrig--;
		
		while ((posSalida >= 0) && (posOrig >=0))
		{
			Mascara = 1;
			corrim = 0;
			while (corrim < 8)
			{
				if (primeraVez == true)
				{
					if ((cantOrig % 8) > 0)
					{
						corrim += 8 - (cantOrig % 8);
						Mascara <<= corrim;
					}
					primeraVez = false;
				}
				if (VecOrig[posOrig] == 1)
					VecSalida[posSalida] = (byte) (VecSalida[posSalida] | Mascara);
				Mascara <<=1;
				corrim++;
				posOrig--;
			}			
			posSalida--;
			bytesCompletos++;			
		}
		
		return bytesCompletos;
	}
	private static int ByteAInt(int Entero, byte Bait)
	{
		if (Bait >= 0)
			return Bait;
		int cont = 0;
		int potencia = 1;
		
		Entero = 0;
		for (cont = 0; cont < 8; cont++)
		{
			Entero += (potencia & Bait);
			potencia <<= 1;
		}
		
		return Entero;
	}
	
}
