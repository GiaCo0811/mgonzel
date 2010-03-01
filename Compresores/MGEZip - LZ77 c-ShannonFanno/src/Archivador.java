/**
 * This class can take a variable number of parameters on the command
 * line. Program execution begins with the main() method. The class
 * constructor is not invoked unless an object of type 'Class1'
 * created in the main() method.
 */
import java.io.*;
//import java.util.*;


public class Archivador
{
	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	private static String NombreArchivo;
	private static String Lectura = "r";
	private static String LectoEscritura = "rw";
	private static long OffsetTree;
	private static int tamanioBuffArbol = 52428; //512 kb para arbol en memoria
	private static long OffsetDirActual;
	
	public static void main (String[] args)
	{
		boolean IncluyeSubDirect = false;
		if (args.length < 2)
		{
			System.out.println("ERROR: Parametros insuficientes\nCompresor [-e|-c] NombreArchivo [-s]");
			return;
		}
		if ((args.length == 3) && args[2].equals("-s"))
			IncluyeSubDirect = true;
			
		NombreArchivo = args[1];
		
		if (args[0].equals("-c"))
		{
			if (CrearArchivoMZip() == false)
				return;
			String[] Directorio = ObtenerDirectorioActual(".");
		
			for(int i = 0; i < Directorio.length; i++)
				InsertarArchivo(Directorio[i],"." + File.separator,IncluyeSubDirect);
			return;
		}
		else if (args[0].equals("-e"))
		{
			if (AbrirArchivoMZip() == true)
				ExtraerArchivos(null);
			return;
		}
		else {
			System.out.println("ERROR: Parametros incorrectos\nCompresor [-e|-c] NombreArchivo [-s]");
			return;
		}
	}
	
	
	
	static boolean AbrirArchivoMZip ()
	{
		RandomAccessFile arch;
		Registro regtemp;

		try { 
			arch = new RandomAccessFile(NombreArchivo,Lectura); 
			arch.seek(4);
			OffsetTree = OffsetDirActual = arch.readLong();
			arch.seek(OffsetTree);
		} catch (IOException io){return false;}
		regtemp = LeerRegistro(arch);
		
		try {arch.close();}catch(IOException io){}
		if (regtemp.Nombre.equals("ROOT"))
			return true;

		return false;
	}
	static boolean CrearArchivoMZip () 
	{
		DataOutputStream Archivo;
		try {
			Archivo = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(NombreArchivo)));
		}
		catch (IOException IOE) {
			System.out.println("Error: no se pudo crear el archivo");
			return false;
		}
		try { Archivo.writeBytes("MZip"); } catch (IOException de){;}
		try { Archivo.writeLong(OffsetDirActual = OffsetTree = 12); } catch (IOException de){;}
		try { Archivo.close(); } catch (IOException de){;}
		
		InsertarRegistro("ROOT",(byte)2,-1,-1,-1,OffsetTree);
		return true;
	}
	
	
	static void InsertarRegistro(String Nombre,byte Tipo,long Tamano,long AreaDatos,long SigInode,long offsetInsert)
	{
		/*
		Nombre = nombre del archivo que se incluira en el archivo MZip
		Tipo = 1:Archivo; 2:Directorio; -1 Archivo borrado (recuperable); -2: Directorio recuperable;
		Tamano = Tamaño del archivo a insertar en bytes, Si es un directorio sera el offset del directorio padre
			relativo al comienzo del arbol.
		AreaDatos = offset real de acceso a los datos del archivo
		SigInode = siguiente elemento del directorio actual
		offsetInsert = offset real donde se insertara el registro
		*/
		
		RandomAccessFile Archivo;
		byte regTaman = (byte) Nombre.length();
		//System.out.println(Nombre.length()+ " " + regTaman);

		try {Archivo = new RandomAccessFile(NombreArchivo,LectoEscritura); } catch (IOException de){
			System.out.println("Error: No se pudo abrir el archivo especificado");
			return;
		}
		try { Archivo.seek(offsetInsert); } catch (IOException io) {;}
		
		try { Archivo.writeByte(regTaman); } catch (IOException io) {;}	//1.Tamaño del nombre (1 byte)
		try { Archivo.writeBytes(Nombre); } catch (IOException io) {;}	//2.Nombre (n bytes segun tamaño)
		try { Archivo.writeByte(Tipo); } catch (IOException io) {;}		//3.Tipo (1 byte)
		try { Archivo.writeLong(Tamano); } catch (IOException io) {;}	//4.Tamaño del archivo (8 bytes)
		try { Archivo.writeLong(AreaDatos); } catch (IOException io) {;}//5.Offset area datos (8 bytes)
		try { Archivo.writeLong(SigInode); } catch (IOException io) {;}	//6.Siguiente nodo-i del directorio (8 bytes)
		
		try { Archivo.close(); } catch (IOException io) {;}

	}
	static void InsertarArchivo(String NombreArchivo,String Path, boolean incluirSubDir)
	{
		File FIarch = new File(Path + NombreArchivo);
		byte [] treeBuffer;
		byte [] dataBuffer;

		//DataInputStream archEntr = null;
		RandomAccessFile mZipFile = null;
		long longitArchivo = 0;

		Registro regNuevo = new Registro(NombreArchivo,(byte)1,FIarch.length(),OffsetTree,-1);
		Registro regActual;// = new Registro();
		
		
		regNuevo.offsetDatos = OffsetTree;

		long actualOffset = OffsetDirActual - OffsetTree;

		try { 
			mZipFile = new RandomAccessFile(Archivador.NombreArchivo,LectoEscritura); 
			mZipFile.seek(actualOffset + OffsetTree);
			longitArchivo = mZipFile.length();
		} catch (IOException io) {
			try { mZipFile.close(); } catch (IOException ios){}
			return;
		}

		if (FIarch.isDirectory() == true)
		{
			if (incluirSubDir == false)
			{
				try { mZipFile.close(); } catch (IOException io){}
				return;
			}

			System.out.print(NombreArchivo + "... ");
			regNuevo.Tipo = 2;
			regNuevo.offsetDatos = -1;
			regNuevo.Tamanio = OffsetDirActual - OffsetTree;
		}
		else
		{
			regNuevo.Tamanio = FIarch.length();
			/*try {
				archEntr = new DataInputStream(new BufferedInputStream(new FileInputStream(Path + regNuevo.Nombre)));
			}
			catch (IOException io) { 
				try { mZipFile.close(); } catch (IOException ios){}
				return;
			}*/
		}
//		System.out.print(NombreArchivo + "... ");
//		System.out.flush();

		
		
		
		regActual = LeerRegistro(mZipFile);
		
		actualOffset = regActual.offsetDatos;
		
		while (actualOffset != -1)
		{
			try { mZipFile.seek(actualOffset + OffsetTree); } catch (IOException io) {;}
			regActual = LeerRegistro(mZipFile);
			actualOffset = regActual.SigInodo;
		}
		
		try { mZipFile.seek(regActual.propioOffset + OffsetTree + regActual.Nombre.length() + 18 -
							(regActual.propioOffset + OffsetTree == OffsetDirActual ? 8 : 0));
			  mZipFile.writeLong(longitArchivo - OffsetTree);
		}catch (IOException oi) {;}
		
		regNuevo.propioOffset = longitArchivo - OffsetTree;
		InsertarRegistro(regNuevo.Nombre,regNuevo.Tipo,regNuevo.Tamanio,regNuevo.offsetDatos,-1,longitArchivo);
//		System.out.println("la long del arch insert es " + FIarch.length());

		if (FIarch.isDirectory() == true)
		{
			if (incluirSubDir == true)
			{
				System.out.println("Directorio creado");
				long tempOffsetDirAct = OffsetDirActual - OffsetTree;
				OffsetDirActual = longitArchivo;
				String [] listaSubdir = ObtenerDirectorioActual(Path + FIarch.getName() + File.separator + ".");
				for (int i=0;i < listaSubdir.length; i++)
					InsertarArchivo(listaSubdir[i],Path + FIarch.getName() + File.separator,incluirSubDir);
				OffsetDirActual = tempOffsetDirAct + OffsetTree;
			}
			
			//try {archEntr.close();} catch (IOException io) {}
			try {mZipFile.close();} catch (IOException io) {}
			return;
		}

		long tamanioArbol = 0;
		try { tamanioArbol = mZipFile.length() - OffsetTree; } catch (IOException io) {}
		treeBuffer = new byte[(int)tamanioArbol];
		dataBuffer = new byte[tamanioBuffArbol];
		
		try { mZipFile.seek(OffsetTree);
			  mZipFile.read(treeBuffer);
		}catch (IOException io){;}
		
		try {
			long escrCompr = 0;
			
			mZipFile.seek(OffsetTree);
			/*leidos = archEntr.read(dataBuffer);
			while (leidos > 0)
			{
				mZipFile.write(dataBuffer,0,leidos);
				leidos = archEntr.read(dataBuffer);
			}*/
			if (FIarch.length() > 0)
				escrCompr = Compresor.miCompresor(Path + NombreArchivo,mZipFile);
			else
				System.out.println(regNuevo.Nombre + "... 100%");

						
			long offNue = mZipFile.getFilePointer();
//			System.out.println("compresor devolvio " + escrCompr + " y lo q se escribio en realidad es " + (offNue - OffsetTree));
			OffsetDirActual = OffsetDirActual - OffsetTree + offNue;
			OffsetTree = offNue;
			mZipFile.write(treeBuffer);

			mZipFile.seek(OffsetTree + regNuevo.propioOffset + regNuevo.Nombre.length() + 2);
			mZipFile.writeLong(escrCompr);
			
			mZipFile.seek(4);
			mZipFile.writeLong(OffsetTree);
		}
		catch (IOException oi){;}
		
		try {
//			archEntr.close();
			mZipFile.close();
		} catch (IOException io) {;}
//		System.out.println("OK");
	}
	

	static void ExtraerArchivos(String Mascara)
	{
		RandomAccessFile archExtr;
		
		try { archExtr = new RandomAccessFile(NombreArchivo,LectoEscritura); }
		catch (IOException io){return;}

		RecorrerYExtraer(archExtr,"." + File.separator, OffsetTree);
		
		try {archExtr.close();}catch (IOException IO){}
	}
	static void RecorrerYExtraer(RandomAccessFile archivo,String Path,long offsetDirOrig) {

		long offsetactual;
		try { archivo.seek(offsetDirOrig); }catch (IOException IO){
			System.out.println("ERROR: CRC incorrecto");
			return;
		}


		Registro regActual = LeerRegistro(archivo);

		offsetactual = regActual.offsetDatos;
		
		while (offsetactual != -1)
		{
			try { archivo.seek(offsetactual + OffsetTree); }catch (IOException IO){}
			regActual = LeerRegistro(archivo);
			
			if (regActual.Tipo == 2)
			{
				System.out.print(regActual.Nombre + "...");
				File dir = new File(Path + regActual.Nombre);
				System.out.print("Creando directorio... ");
				dir.mkdir();
				System.out.println("OK");
				OffsetDirActual = regActual.propioOffset + OffsetTree;
				RecorrerYExtraer(archivo,Path + regActual.Nombre + File.separator,OffsetDirActual);
				OffsetDirActual = offsetDirOrig;
			}
			else if (regActual.Tipo == 1)
			{
				if (regActual.Tamanio > 0)
				{
					try { archivo.seek(regActual.offsetDatos); }catch (IOException IO){}
					Descompresor.miDescompresor(archivo,regActual,Path + regActual.Nombre);
				}
				else
				{
					DataOutputStream archsal;
					try { archsal = new DataOutputStream(
											new BufferedOutputStream(
												new FileOutputStream(Path + regActual.Nombre)));
					}catch (IOException io){return;}
					try {archsal.close();}catch (IOException io){return;}
				}
				
/*				byte []buffer = new byte[(int)(regActual.Tamanio)];
				int leidos=0;
				int totleidos = 0;
				do {					
					try { leidos = archivo.read(buffer,0,buffer.length);
						  archsal.write(buffer,0,buffer.length);
					} catch (IOException io) {}
					totleidos+=leidos;
				}while ((leidos > 0) && (totleidos < regActual.Tamanio));
				try{archsal.close();}catch(IOException io){}
				System.out.println("OK");
*/			}


			offsetactual = regActual.SigInodo;			
		}
		
		
		
	}
	
	static Registro LeerRegistro(RandomAccessFile raf) {
		byte tamNombre = 0;
		Registro reg = new Registro();
		
		try { reg.propioOffset = raf.getFilePointer() - OffsetTree; } catch (IOException op){;}
		
		try {tamNombre = raf.readByte();  } catch (IOException io){;}
		byte []nombre = new byte[tamNombre];
		try { raf.read(nombre,0,nombre.length); } catch (IOException io){;}
		for (int i = 0;i < nombre.length; i++)
			reg.Nombre = reg.Nombre + (char)nombre[i];
		
		try {
			reg.Tipo = raf.readByte();
			reg.Tamanio = raf.readLong();
			reg.offsetDatos = raf.readLong();
			reg.SigInodo = raf.readLong();
		}catch (IOException io) {;}
		

		return reg;
	}
	public static String[] ObtenerDirectorioActual(String Path)
	{
		File f = new File(Path); // f represents the current directory.
		String[] list = f.list(new FilenameFilter() {
			public boolean accept(File f, String s) {
				if (s.equals(NombreArchivo) == false)
					return true; else return false;
			}
		});
		return list;		
	}
}
