//-----------------------------------------------
// stack template con iterador
// modificado por guillermo pantaleo 30/05/2002
//-----------------------------------------------
#ifndef ITERSTACKTEMPLATE_H
#define ITERSTACKTEMPLATE_H
#include <cassert>
#include <iostream>

template<class T, int ssize = 100>
class StackTemplate {
  
public:

  StackTemplate() : top(0) {}

  void push(const T& i) {
    assert(top < ssize);
    stack[top++] = i;
  }

  T pop() {
    assert(top > 0);
    return stack[--top];
  }

  //---------------------------------
  class iterator; // Declaration required
  friend class iterator; // Make it a friend
  
  class iterator { // Now define it
   
  public:

    iterator(StackTemplate& st): s(st),index(0){}
    
	// To create the "end sentinel" iterator:
    iterator(StackTemplate& st, bool): s(st), index(s.top) {}

    T operator*() const { return s.stack[index];}

    T operator++() { // Prefix form
      assert(index < s.top);
      return s.stack[++index];
    }

    T operator++(int) { // Postfix form
      assert(index < s.top);
      return s.stack[index++];
    }

    // Jump an iterator forward
    iterator& operator+=(int amount) {
      assert(index + amount < s.top);
      index += amount;
      return *this;
    }

    // To see if you're at the end:
    bool operator==(const iterator& rv) const {
      return index == rv.index;
    }
    bool operator!=(const iterator& rv) const {
      return index != rv.index;
    }
    friend std::ostream& operator<<(std::ostream& os, const iterator& it) {
      return os << *it;
    }

  private:
	    
	  StackTemplate& s;
      int index;
  };
  //--------------------------------------

  iterator begin() { return iterator(*this); }
  
  // Create the "end sentinel":
  
  iterator end() { return iterator(*this, true);}

  private:
	  
	  T stack[ssize];
      int top;
};
#endif 