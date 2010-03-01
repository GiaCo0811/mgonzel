
public class Direccion
{
	private int id;
	
	private Direccion(int id) { 
		this.id = id;
	}
	public boolean equals(Object obj)
	{
		Direccion dir = (Direccion) obj;
		
		return dir.id == this.id;
	}
	
	
	static Direccion Inicio = new Direccion(0);
	static Direccion Actual = new Direccion(1);
	static Direccion Fin = new Direccion(2);
	
}
