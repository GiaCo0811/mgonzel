/************************************************
   programacion generica
   modificado por guillermo pantaleo

*************************************************/
#include "Shape.h"
#include "IterStackTemplate.h"

using namespace std;


// A Plan is a different container of Shapes:
class Plan : public StackTemplate< Shape* > {
public:
  ~Plan() { cout << "~Plan" << endl; }
};


// A function template:
template<class Iter>
void drawAll(Iter start, Iter end) {
  while(start != end) {
    (*start)->draw();
    start++;
  }
}
//------------------------------------
void main() {
  // Each type of container has 
  // a different interface:
  Plan p;
  p.push(new Line);
  p.push(new Square);
  p.push(new Circle);
  
  //Shape* sarray[] = { new Circle, new Square, new Line };
  Shape* sarray[3];
  sarray[0] = new Circle;
  sarray[1] = new Square;
  sarray[2] = new Line;


  // The iterators and the template function
  // allow them to be treated generically:
 
  cout << "Plan p:" << endl;
  drawAll(p.begin(), p.end());
 
  cout << "------------------------" << endl;

  cout << "Array sarray:" << endl;
  // Even works with array pointers:
  drawAll(sarray, sarray + sizeof(sarray)/sizeof(*sarray));
  
} 
