public class Registro
{
	public Registro(String nombre,byte tipo, long tam, long offdat,long signode)
	{
		Nombre = nombre;
		Tipo = tipo;
		Tamanio = tam;
		offsetDatos = offdat;
		SigInodo = signode;
	}
	public Registro(){
		Nombre = "";
		Tipo = 0;
		Tamanio = 0;
		offsetDatos = 0;
		SigInodo = 0;
	}
	
	public String Nombre;
	public byte Tipo;
	public long Tamanio;
	public long offsetDatos;
	public long SigInodo;
	public long propioOffset = 0;
}