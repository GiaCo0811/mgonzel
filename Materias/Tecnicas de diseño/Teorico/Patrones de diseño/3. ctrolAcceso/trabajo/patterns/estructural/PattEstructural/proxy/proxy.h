/************************************************

  Proxy 
  este es la declaracion de la clase que modela
  un proxy , usada como ejemplo para
  el pattern correspondiente a smart pointer

*************************************************/
#ifndef PROXY_H_
#define PROXY_H_

#include <iostream>

using namespace std;


class Body
{
public:

	void service(){ cout << "RefCount:" << ref_count << endl;}
	

private:

	friend class ProxyBody;

    Body(int param){ atributo = param; ref_count = 0;}
    ~Body(){ cout << "Se destruye un Body" << endl; }

	int ref_count, atributo;
};
/***********************************/
class ProxyBody
{

public:

	ProxyBody(int param);
	
	~ProxyBody();

	ProxyBody(const ProxyBody&);

	ProxyBody& operator=(const ProxyBody&);

    Body* operator->();


private:

    Body* body;

};
//------------------------------
inline ProxyBody::ProxyBody(int param)
{

	body = new Body(param);
	body->ref_count++;

}
//------------------------------
inline ProxyBody::ProxyBody(const ProxyBody& rh)
{
    body = rh.body;
	body->ref_count++;

}
//------------------------------
inline ProxyBody& ProxyBody::operator=(const ProxyBody& rh)
{
   rh.body->ref_count++;

   if(--body->ref_count <= 0){

	   delete body;

   }
   body = rh.body;
   return *this;
}
//------------------------------
inline ProxyBody::~ProxyBody()
{
   if(--body->ref_count <= 0){

	   delete body;

   }
}
//------------------------------
inline Body* ProxyBody::operator->()
{
   return body;
}

#endif