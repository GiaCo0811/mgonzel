%%%%%%%%%%%%%%% TABLERO CON FICHA %%%%%%%%%%%%%%%%
% El ejercicio es para ficha de 
% 3 posiciones, en L. El caso de 2
% posiciones es simple y no hay tanto
% chequeo del giro, solo restar y sumar
% en una y otra coordenada para acostar o
% parar la ficha.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	 
domains

pos=casilla( integer,integer) .	 
%posicion del Eje, y posicion de extension de cada Eje
ficha=ficha( pos, pos).		  
lista= ficha*.

predicates

nondeterm ocupado(ficha) .
nondeterm valida(integer,integer,integer,integer) .
nondeterm validaGiro(integer,integer,integer,integer).
nondeterm mover(ficha, ficha).
nondeterm salto(integer, integer).
nondeterm giro(integer, integer, integer, integer).
nondeterm perte(ficha, lista) .
nondeterm pp(ficha,ficha, lista, lista).

clauses

%solo saltos horizontales o verticales, NO diagonales
salto(0,1).			
salto(0,-1).
salto(1,0).
salto(-1,0).

giro(1,0,-1,0).
giro(-1,0,1,0).
giro(0,1,0,-1).
giro(0,-1,0,1).

ocupado(ficha(pos(1,4),pos(_,_))).
ocupado(ficha(pos(4,2),pos(5,_))).

%valida que este dentro del tablero
valida(X,Y,Xa,Ya):-X<=5,Y<=5,Xa<=5,Ya<=5.	
%Valida la distancia del eje a la extesion
validaGiro(X,Y,Xa,Ya):-Aux=X-Xa,Aux<=1,Aux>=0,Aux2=Y-Ya,Aux2<=1,Aux2>=0.
   
perte(Ca,[Ca| _]):-!.
perte(X,[_|Co] ):- perte(X,Co).

mover(ficha(pos(X,Y),pos(Xa,Ya)),ficha(pos(Xf,Yf),pos(Xaf,Yaf))):-salto(A,B),Xf=X+A,Yf=Y+B,giro(C,D,E,F),Xf=Xf+C,Yf=Yf+D,Xaf=Xa+E,Yaf=Ya+F,validaGiro(Xf,Yf,Xaf,Yaf),valida(Xf,Yf,Xaf,Yaf),not(ocupado(ficha(pos(Xf,Yf),pos(Xaf,Yaf)))).
pp(E,E,[E],_):-!.
pp(Ei,Ef,L,[Ca|Col]):-mover(Ei,E2),not(perte(L,E2)),Ca=E2,pp(E2,Ef,[E2|L],Col).

goal

Inicio=ficha(pos(1,1),pos(2,2)),Final=ficha(pos(5,5),pos(4,4)),pp(Inicio,Final,[],Solucion).


%Explicacion:
%La ficha tiene la primera posicion es el Eje = (1,1)
%la segunda posicion es la direccio hacia donde se extiende la ficha = (2,2)
%
%  _ _ _ _ _
% |X X|_|O|_| 1
% |X|_|_|_|_| 2		La ficha puede moverse uno ala derecha uno a la izquierda o uno arriba o abajo
% |_|_|_|_|_| 3		el giro puede tener solo 3 resultados, ya que es de 90º, los posibles giros son
% |_|O|_|_|X| 4		4 dependiendo la de forma de la ficha y ubicacion, pero solo 3 dan  un giro valido
% |_|O|_|X|X| 5
%					X X	  -->gira 90º --> X X	    los estados pueden cambiar de izq a derecha y de arriba
%					X	  <--gira 90º <--	X 		a abajo, pero no pueden pasar en diagonal, porq seria
%													un giro de 180º
%
%					X	  -->gira 90º -->   X		entonces para validar que el giro fue de 90 grados se
%					X X	  <--gira 90º <-- X X		verifica lo siguiente:
%
%													en todo giro valido, la distacia del eje a su coordenada
%													extendida debe ser siempre <= 1
%													esto se calcula: | X - Xa |	<= 1  ,  | X - Xa |>= 0																	 | Y - Ya |	<= 1
%																	 | Y - Ya |	<= 1  ,  | Y - Ya |>= 0
%
% sebastian_packmann@hotmail.com