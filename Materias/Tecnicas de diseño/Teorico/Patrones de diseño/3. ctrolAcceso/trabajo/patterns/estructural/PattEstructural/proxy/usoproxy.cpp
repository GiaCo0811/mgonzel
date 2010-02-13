// ESTRUCTURAL PATTERNS
/************************************************

     uso del pattern proxy en modalidad
	 reference counting

************************************************/


#include "proxy.h"

void funcion(ProxyBody h)
{
   h->service();
}

void main()
{
	// garantia de manejo a traves del ProxyBody
	//Body body(7);
    //Body* ptr_body = new Body(7);

	//----- uso ------
	ProxyBody h1(34);

	h1->service();

	{
        ProxyBody h2(h1);
		h2->service();

		funcion(h1);
	}

    h1->service();
}