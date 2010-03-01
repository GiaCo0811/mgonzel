/**
 * This class can take a variable number of parameters on the command
 * line. Program execution begins with the main() method. The class
 * constructor is not invoked unless an object of type 'Compresor'
 * created in the main() method.
 */
import java.io.*;

public class Compresor
{
	private static long tamTotalComprim;
	private static void GenerarShannon(cLista Lista){
		int tamlista = Global.Longitudes + 257; //se agrega el caracter EOF a la lista de ASCII
		int i;
		
		for (i = 0; i < tamlista; i++)
		{
			Lista.mInsertar(i,1,Direccion.Fin,1);
			Lista.Frecuencia++;
		}
	}
	
	public static long miCompresor (String ArchivoEntrada, RandomAccessFile archSalida) {
//		System.out.println("Comprimiendo...");
		//VectorShanno VecShanno = new VectorShanno();
		cLista ListaShannon = new cLista ();
		GenerarShannon(ListaShannon);


		DataInputStream entr = null;
		//RandomAccessFile ArchRA = null;
		long tamanioArchivo;
		tamTotalComprim = 0;
		long YaComprimido = 0;
		{
			File archivo = new File(ArchivoEntrada);
			tamanioArchivo = archivo.length();
		}

		byte []buffer;
		int offsetBuffer = 0;
		int leidos = 0;
		
		byte []compbuffer;
		int poscompbuffer = 0;
		int Char = 0;

		try {
			entr = new DataInputStream(new BufferedInputStream(new FileInputStream(ArchivoEntrada)));
		}
		catch (FileNotFoundException nosiste) {}

		System.out.print(Global.RCarro + ArchivoEntrada + "... " + YaComprimido*100/tamanioArchivo + "%");
		
		buffer = new byte[Global.Buffer];
		compbuffer = new byte[Global.BufferComp];

		try {
			leidos = entr.read(buffer,offsetBuffer,buffer.length - offsetBuffer);
		}
		catch (IOException AIOLALA) {
			try {entr.close();}	catch (IOException AIOO) {;}
			return 0;
		}


		//Compresion por el metodo LZ
		int VMemoria = 0, VInspeccion = 0,TamVmem = 0;
		int TamanioVentana;
		
		int recorrido = 0, equival = 0;
		int posMayorEquiv = 0, MayorEquiv = 0;  // para encontrar la mayor equivalencia en toda la ventana
		
		TamanioVentana = Global.Ventana; //TamanioVentana queda igual a la ventana preestablecida.

		while ((TamVmem < Global.MinLong) && (VInspeccion < leidos))
		{
//			System.out.println("Emito caracter: " + (char)(buffer[VInspeccion]) + " de ASCII " + buffer[VInspeccion]);
			//Llamada a metodo shanno-fano para compresion del caracter

			Char = ByteAInt(Char,buffer[VInspeccion]);
			poscompbuffer =  mShannonFano(ListaShannon,compbuffer,poscompbuffer,Char,-1,archSalida);
			
			VInspeccion++;
			TamVmem++;
		}
		while ((TamVmem < TamanioVentana) && (VInspeccion < TamanioVentana) && (VInspeccion < leidos))
		{
			recorrido = 0;
			MayorEquiv = 0;
			while ((recorrido < TamVmem - Global.MinLong) && (MayorEquiv < Global.MaxLong))
			{
				equival = 0;
				while ((recorrido < TamVmem - Global.MinLong) && (recorrido + equival < TamVmem) && 
						(buffer[VMemoria + recorrido + equival] == buffer[VInspeccion + equival])
						&& (equival < Global.MaxLong))
					equival++;
				if (equival > MayorEquiv)
				{
					MayorEquiv = equival;
					posMayorEquiv = TamanioVentana - TamVmem + recorrido;
				}
				recorrido++;
			}
			if (MayorEquiv >= Global.MinLong)
			{
//				System.out.println("Emito longitud: " + MayorEquiv + ", posicion: " + posMayorEquiv);
				//llamo a shano-fano con el caracter longitud (MayorEquiv - global.minlong + 1)
				// de posicion posMayorEquiv
				poscompbuffer =  mShannonFano(ListaShannon,compbuffer,poscompbuffer,
								MayorEquiv + 257 - Global.MinLong,posMayorEquiv,archSalida);
				TamVmem += MayorEquiv;
				VInspeccion += MayorEquiv;
			}
			else
			{
//				System.out.println("Emito caracter: " + (char)(buffer[VInspeccion]) + " de ASCII " + buffer[VInspeccion]);
				//Llamada a metodo shanno-fano para compresion del caracter
				Char = ByteAInt(Char,buffer[VInspeccion]);
				poscompbuffer =  mShannonFano(ListaShannon,compbuffer,poscompbuffer,Char,-1,archSalida);
				TamVmem++;
				VInspeccion++;
			}
			
		}
		if (TamVmem > TamanioVentana)
		{
			VMemoria += (TamVmem % TamanioVentana);
			TamVmem = TamanioVentana;
		}
		
		do{
			
			while (VInspeccion + TamanioVentana <= offsetBuffer + leidos)
			{
				recorrido = 0;
				MayorEquiv = 0;
				while ((recorrido < TamVmem - Global.MinLong) && (MayorEquiv < Global.MaxLong) )
				{
					equival = 0;
					while ((recorrido < TamVmem - Global.MinLong) && (recorrido + equival < TamVmem) && 
							(buffer[VMemoria + recorrido + equival] == buffer[VInspeccion + equival])
							&& (equival < Global.MaxLong))
						equival++;
					if (equival > MayorEquiv)
					{
						MayorEquiv = equival;
						posMayorEquiv = TamanioVentana - TamVmem + recorrido;
					}
					recorrido++;
				}
				if (MayorEquiv >= Global.MinLong)
				{
//					System.out.println("Emito longitud: " + MayorEquiv + ", posicion: " + posMayorEquiv);
					//llamo a shano-fano con el caracter longitud (MayorEquiv - global.minlong + 1)
					// de posicion posMayorEquiv
					poscompbuffer =  mShannonFano(ListaShannon,compbuffer,poscompbuffer,
							MayorEquiv + 257 - Global.MinLong,posMayorEquiv,archSalida);
					VMemoria += MayorEquiv;
					VInspeccion += MayorEquiv;
				}
				else
				{
//					System.out.println("Emito caracter: " + (char)(buffer[VInspeccion]) + " de ASCII " + buffer[VInspeccion]);
					//Llamada a metodo shanno-fano para compresion del caracter
					Char = ByteAInt(Char,buffer[VInspeccion]);
					poscompbuffer =  mShannonFano(ListaShannon,compbuffer,poscompbuffer,Char,-1,archSalida);
					VMemoria++;
					VInspeccion++;
				}		
			}
			
			
			for(recorrido = 0; recorrido < leidos + offsetBuffer - VMemoria; recorrido++)
				buffer[recorrido] = buffer[VMemoria + recorrido];
			
			offsetBuffer = recorrido;
			VMemoria = 0;
			VInspeccion = TamanioVentana;
			
			try {
				
				System.out.print(Global.RCarro + ArchivoEntrada + "... " + YaComprimido*100/tamanioArchivo + "%");
				YaComprimido += leidos;
				leidos = entr.read(buffer,offsetBuffer,buffer.length - offsetBuffer);
			}
			catch (IOException AIOLALA) {
				leidos = -1;
			}
			
		} while (leidos >= 0);
		
		try { entr.close();	} catch (IOException io){;}
		
		while (offsetBuffer - VInspeccion >= Global.MinLong)
		{
			recorrido = 0;
			MayorEquiv = 0;
			while ((recorrido < offsetBuffer - Global.MinLong) && (MayorEquiv < Global.MaxLong))
			{
				equival = 0;
				while ( (VInspeccion + equival < offsetBuffer) &&(recorrido + equival < TamVmem) && 
						(buffer[VMemoria + recorrido + equival] == buffer[VInspeccion + equival])
						&& (equival < Global.MaxLong))
					equival++;
				if (equival > MayorEquiv)
				{
					MayorEquiv = equival;
					posMayorEquiv = TamanioVentana - TamVmem + recorrido;
				}
				recorrido++;
			}
			if (MayorEquiv >= Global.MinLong)
			{
//				System.out.println("Emito longitud: " + MayorEquiv + ", posicion: " + posMayorEquiv);
				//llamo a shano-fano con el caracter longitud (MayorEquiv - global.minlong + 1)
				// de posicion posMayorEquiv
				poscompbuffer =  mShannonFano(ListaShannon,compbuffer,poscompbuffer,
								MayorEquiv + 257 - Global.MinLong,posMayorEquiv,archSalida);
				VMemoria += MayorEquiv;
				VInspeccion += MayorEquiv;
			}
			else
			{
//				System.out.println("Emito caracter: " + (char)(buffer[VInspeccion]) + " de ASCII " + buffer[VInspeccion]);
				//Llamada a metodo shanno-fano para compresion del caracter
				Char = ByteAInt(Char,buffer[VInspeccion]);
				poscompbuffer =  mShannonFano(ListaShannon,compbuffer,poscompbuffer,Char,-1,archSalida);
				VMemoria++;
				VInspeccion++;
			}		
		}
		
		while (VInspeccion < offsetBuffer )
		{
			//comprime los ultimos caracteres de la cadena
			//System.out.println("Emito caracter: " + (char)(buffer[VInspeccion]) + " de ASCII " + buffer[VInspeccion]);
			//Llamada a metodo shanno-fano para compresion del caracter
			Char = ByteAInt(Char,buffer[VInspeccion]);
			poscompbuffer =  mShannonFano(ListaShannon,compbuffer,poscompbuffer,Char,-1,archSalida);
			
			VInspeccion++;
			
		}
		
		//System.out.println("Emito caracter de fin de archivo: EOF");
		poscompbuffer =  mShannonFano(ListaShannon,compbuffer,poscompbuffer,256,-1,archSalida);
		//Finalizo el archivo
		if (poscompbuffer > 0)
		{
			byte [] recordBuff = new byte[compbuffer.length / 8];

			//transferir vector a bits
			int bytesAescribir = PasarABits(compbuffer,poscompbuffer,recordBuff);
			//escribir en archivo
			try {
				archSalida.write(recordBuff,0,bytesAescribir);
				tamTotalComprim += bytesAescribir;
			}
			catch (IOException io) {;}
		}
		System.out.print(Global.RCarro + ArchivoEntrada + "... " + YaComprimido*100/tamanioArchivo + "%");
		System.out.println();
		
		//finalizar buffer de salida.
		return tamTotalComprim;
	}
	
	private static int mShannonFano (cLista LisShannon,byte [] vecComp,int posVec,int Char, int posLongitud, RandomAccessFile archSalida)
	{
		cNodo primero,actual,ultimo,nodoencontrado = null;
		boolean encontrado = false;
		int suma = LisShannon.ObtenerFrec();
		int sumatemp;
		
		primero = LisShannon.ObtenerPrimero();
		ultimo = LisShannon.ObtenerUltimo();
		
		while (primero != ultimo)
		{
			encontrado = false;
			sumatemp = 0;
			for (actual = primero, sumatemp = actual.mGetFrec(); //inicializacion
				 (sumatemp < (suma /2)) && (actual != null); //condicion
				 actual = actual.mGetSig(),sumatemp += actual.mGetFrec()) //incremento
			{
				if (actual.mGetChar() == Char)
				{
					encontrado = true;
					nodoencontrado = actual;
				}
			}

			if (actual.mGetChar() == Char)
			{
				encontrado = true;
				nodoencontrado = actual;
			} //Debo realizar la comparacion al ultimo nodo accedido (sale del ciclo antes de comparar

			if (encontrado == true)
			{
				vecComp[posVec] = 0;
				ultimo = actual;
				suma = sumatemp;
			}
			else if (encontrado == false)
			{
				vecComp[posVec] = 1;
				primero = actual.mGetSig();
				suma = suma - sumatemp;
			}
			
			posVec++;
			if (posVec >= vecComp.length)
			{
				byte [] recordBuff = new byte[vecComp.length / 8];
				//transferir vector a bits
				int bytesAescribir = PasarABits(vecComp,vecComp.length,recordBuff);
				//escribir en archivo
				try {
					archSalida.write(recordBuff,0,bytesAescribir);
					tamTotalComprim += bytesAescribir;
				}
				catch (IOException io) {;}
				posVec = 0;
			}
		}
		/*if ((primero == null) || ((ultimo.mGetChar() != Char) && (ultimo == LisShannon.ObtenerUltimo()) ))
		{
			LisShannon.mInsertar(Char,0,Direccion.Fin,1);
			nodoencontrado = LisShannon.ObtenerUltimo();
		}*/
		
		if ((nodoencontrado == null) && (primero == ultimo) && (primero.mGetChar() == Char))
			nodoencontrado = primero; //En caso de que el nodo a buscar sea el ultimo de la lista
		
		if (Char > 256)
		{
			int i;
			
			for (i=0; i < Global.bitsLongitudes; i++)
			{
				if ((posLongitud % 2) == 1)
					vecComp[posVec] = 1;
				else vecComp[posVec] = 0;
				
				posLongitud/=2;
				posVec++;
				if (posVec >= vecComp.length)
				{
					byte [] recordBuff = new byte[vecComp.length / 8];
					//transferir vector a bits
					int bytesAescribir = PasarABits(vecComp,vecComp.length,recordBuff);
					//escribir en archivo
					try {
						archSalida.write(recordBuff,0,bytesAescribir);
						tamTotalComprim += bytesAescribir;
					}
					catch (IOException io) {;}
					posVec = 0;
				}
			}
		}
		
		LisShannon.IncrementarFrec();
		nodoencontrado.mIncremFrec();
		
		//System.out.println("Lista antes de ordenar:\r\n" + LisShannon);

		if (nodoencontrado != LisShannon.ObtenerPrimero())
			LisShannon.OrdenarNodoDeLista(nodoencontrado);
		
		//System.out.println("Lista despues de ordenar:\r\n" + LisShannon);
		
		return posVec;
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
