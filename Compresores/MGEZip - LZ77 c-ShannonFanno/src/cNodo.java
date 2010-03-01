//Clase cNodo

public class cNodo
{
	//Listado de argumentos
	private int Char,Frec;
	
	private cNodo ant,sig;

	cNodo() { this(0,0); } //Constructor por defecto

	cNodo(int Char,int Frec) //Constructor con ingreso de objeto.
	{
		this.Char = Char;
		this.Frec = Frec;
		//System.out.println("Se creo el nodo: " + Char +" con frecuencia " + Frec);
	}

	void mSetSig(cNodo nodo)
	{
		this.sig = nodo;
	}

	void mSetAnt(cNodo nodo)
	{
		this.ant = nodo;
	}
	
	cNodo mGetSig()
	{
		return sig;
	}
	
	cNodo mGetAnt()
	{
		return ant;
	}
	
	String mGetObjeto (){
		return ("caracter " + Char + " de frec. " + Frec);
	}
	
	int mGetFrec(){ return Frec; }
	int mGetChar(){ return Char; }
	
	public void mIncremFrec() { Frec++;	}
	
	public void Mostrar()
	{
		System.out.println("Mostrando elemento: " + Char + " de frecuencia " + Frec);
	}

	public boolean equals(int id)
	{
		return (id == Char);
	}

	public int Comparar(cNodo nodo)
	{
		if ((Frec > nodo.Frec) || ((Frec == nodo.Frec) && (Char < nodo.Char)))
			return 1;
		if ((Frec < nodo.Frec) || ((Frec == nodo.Frec) && (Char > nodo.Char)))
			return -1;
		if ((Frec == nodo.Frec) && (Char == nodo.Char))
			return 0;
		
		return 2;
	}
	
	public void finalize()
	{
		//System.out.println("Borrando el nodo: " + Char +", tiene como frecuencia " + Frec);	
	}
	
}