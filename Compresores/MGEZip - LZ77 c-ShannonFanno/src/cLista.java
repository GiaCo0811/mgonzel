//cLista.java - Manejo de una lista de objetos.



public class cLista
{
	//Listado de argumentos
	private cNodo first,current,last;
	public int Frecuencia = 0;

	cLista() 
	{
		first = null;
		current = null;
		last = null;
		
		//System.out.println("Lista creada");
	}

	boolean mInsertar(int Char,int frec,Direccion Dir, int offset)
	{
		cNodo nodoinsert = new cNodo(Char,frec);
		cNodo buscpos;
		
		if (Dir.equals(Direccion.Inicio))
			buscpos = first;
		else if (Dir.equals(Direccion.Actual))
			buscpos = current;
		else if (Dir.equals(Direccion.Fin))
			buscpos = last;
		
		else return false;

		if ((first == null) && (current == null) && (last == null))
		{
			first = current = last = nodoinsert;
			return true;
		}
		
		int pos;
		for(pos = 0;(pos < offset) && (buscpos != null); pos++)
		{
			buscpos = buscpos.mGetSig();
		}
		
		if ((pos == offset) && (buscpos == null))
		{
			last.mSetSig(nodoinsert);
			nodoinsert.mSetAnt(last);
			last = nodoinsert;
			return true;			
		}

		nodoinsert.mSetAnt(buscpos.mGetAnt());
		nodoinsert.mSetSig(buscpos);
		
		nodoinsert.mGetSig().mSetAnt(nodoinsert);
		if (nodoinsert.mGetAnt() != null)
			nodoinsert.mGetAnt().mSetSig(nodoinsert);
		else
			first = nodoinsert;
		
		return true;
		
	}
	boolean mInsertar(int num)
	{
		return mInsertar(num,0,Direccion.Fin,1);
	}

	boolean mInsertar() { return mInsertar(0); }


	public void Destroy() {
		cNodo actual, aborrar;
		
		//System.out.println("Destruyendo la lista...");
		
		actual = first;
		first = current = last = null;
		
		while (actual != null)
		{
			aborrar = actual;
			actual = actual.mGetSig();
			
			if (aborrar.mGetSig() != null)
				aborrar.mGetSig().mSetAnt(null);
			
			actual.mSetAnt(null);
			aborrar.mSetSig(null);
			aborrar = null;
		}
		System.gc();
		
		//System.out.println("Lista destruida");
	}

	public void finalize() { 
		Destroy();
	}
	
	public void MostrarCorriente()
	{
		current.Mostrar();
	}
	
	public void MostrarLista()
	{
		cNodo unnodo = first;
		
		System.out.println("\nMostrando contenido de la lista:");
		while (unnodo != null)
		{
			unnodo.Mostrar();
			unnodo = unnodo.mGetSig();
		}		
		System.out.println();
	}
	
	public boolean SelecCorriente(Direccion dir,int offset)
	{
		cNodo nodo;
		int posic;
		if (dir.equals(Direccion.Inicio))
			nodo = first;
		else if (dir.equals(Direccion.Actual))
			nodo = current;
		else if (dir.equals(Direccion.Fin))
			nodo = last;
		else
			return false;

		for(posic = 0;(posic < offset) && (nodo != null);posic++)
			nodo = nodo.mGetSig();
		
		if (nodo != null)
		{
			current = nodo;
			return true;
		}
		
		return false;
	}
	
	public cNodo ObtenerPrimero() {
		return first;
	}
	public cNodo ObtenerUltimo() {
		return last;
	}
	public int ObtenerFrec() {
		return Frecuencia;
	}
	public void IncrementarFrec(int incremento){
		Frecuencia+= incremento;
	}
	public void IncrementarFrec(){ IncrementarFrec(1); }
	
	public boolean BorrarItem(int id) {
		cNodo temp = first,aborrar;
		
		
		while (temp != null)
		{
			aborrar = temp;
			temp = temp.mGetSig();
			
			if (aborrar.equals(id))
			{
				if (aborrar == first)
					first = first.mGetSig();
				if (aborrar == last)
					last = last.mGetAnt();
				
				if(aborrar.mGetAnt()!= null)
					aborrar.mGetAnt().mSetSig(aborrar.mGetSig());
				if (aborrar.mGetSig()!=null)
					aborrar.mGetSig().mSetAnt(aborrar.mGetAnt());
				
				aborrar = null;
			}
		}
		
		current = first;
		System.gc();	
		
		return true;
	}
	
	public String toString(){
		cNodo unnodo = first;
		String outstream = "";// = "\nMostrando contenido de la lista:\n";
		String Enter = "\r\n";
		String Espacio = " ";
		
		while (unnodo != null)
		{
			outstream = outstream + unnodo.mGetChar() + Espacio;//nter;
			unnodo = unnodo.mGetSig();
		}		
		
		return outstream + Enter;
	}
	
	public void OrdenarNodoDeLista(cNodo nodoaordenar)
	{
		cNodo tempnodo = nodoaordenar.mGetAnt();
		
		while ((tempnodo != null) && (nodoaordenar.Comparar(tempnodo) >= 0))
			tempnodo = tempnodo.mGetAnt();
		
		if (tempnodo == nodoaordenar.mGetAnt())
			return;
		
		nodoaordenar.mGetAnt().mSetSig(nodoaordenar.mGetSig());
		
		if (nodoaordenar.mGetSig() != null)
			nodoaordenar.mGetSig().mSetAnt(nodoaordenar.mGetAnt());
		else
			last = nodoaordenar.mGetAnt();
		
		if (tempnodo == null)
		{
			nodoaordenar.mSetSig(first);
			nodoaordenar.mSetAnt(null);
			first.mSetAnt(nodoaordenar);
			first = nodoaordenar;
		}
		else
		{
			if (tempnodo.mGetSig() == null)
				System.out.println(this);
			
			nodoaordenar.mSetAnt(tempnodo);
			nodoaordenar.mSetSig(tempnodo.mGetSig());
			tempnodo.mSetSig(nodoaordenar);
			nodoaordenar.mGetSig().mSetAnt(nodoaordenar);
		}
		
	}
}
