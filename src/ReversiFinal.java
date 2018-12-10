public class ReversiFinal
{
	public static void main(String[] args) 
	{
		Tablero t = new Tablero();
		t.juego();
	}
}

class Tablero
{
	static char[][] tablero = new char[8][8];	//El reversi se juega con un tablero 8x8.
	static int filaJugador, columnaJugador;	//fila y columna que el jugador mete por teclado para interactuar con el juego
	static char ficha=' ', fichaContrario=' ';	//ficha y fichaContrario tomaran el valor de fichaJ1 o fichaJ2 dependiendo del turno
	static char fichaJ1=' ', fichaJ2=' ';	//Definimos las dos posibilidades que tiene la ficha
	static boolean turnoJ1=true;

	Tablero()
	{
		inicializaFichas();
		inicializaTablero();		
	}

	//Creando lo minimo para jugar
	private void inicializaFichas() 
	{
		System.out.println("Dame la ficha del jugador 1");
		fichaJ1=Leer.datoChar();
		System.out.println("Dame la ficha del jugador 2");
		fichaJ2=Leer.datoChar();
	}
	private void inicializaTablero() 
	{
		//Llenamos el tablero de '·' que hace de celdas vacias
		for(int i=0; i<tablero.length; i++) 
			for(int j=0; j<tablero.length; j++) 
				tablero[i][j]='·';
		
		//Las partidas empiezan siempre con estas 4 fichas en estas posiciones
		tablero[4][4]=fichaJ2;
		tablero[4][3]=fichaJ1;
		tablero[3][4]=fichaJ1;
		tablero[3][3]=fichaJ2;
	}
	//Juego
	public void juego() 
	{
		while(cuentaFichas('·')!=0) 
		{
			cambiaFicha();
			
			System.out.println("");
			System.out.println("Es el turno del jugador ( "+ficha+" )");
			System.out.println("");
			System.out.println("[ "+fichaJ1+" = "+cuentaFichas(fichaJ1)+" ]");
			System.out.println("[ "+fichaJ2+" = "+cuentaFichas(fichaJ2)+" ]");
			System.out.println("");
			System.out.println("Espacios disponibles : "+cuentaFichas('·'));
			System.out.println("");
			
			muestraTablero();
			
			do
			{
				pideCelda();
			}while(!compruebaFlanqueo());
			
			colocaFicha();
			
			turnoJ1=!turnoJ1;		
		}
		
		pantallaFinal();
		
	}
	private void cambiaFicha()	
	{
		//Cambia la ficha que se pone dependiendo del turno
		if(turnoJ1) 
		{
			ficha=fichaJ1;
			fichaContrario=fichaJ2;
		}
		else 
		{
			ficha=fichaJ2;
			fichaContrario=fichaJ1;
		}
	}
	public void muestraTablero() 
	{
		for(int i=0; i<tablero.length; i++) 
		{
			for(int j=0; j<tablero.length; j++) 
				System.out.print(tablero[i][j]+"\t");
			System.out.println("");
		}
	}
	private void pideCelda() 
	{
		boolean celdaValida=false;
		while(!celdaValida) 
		{
			celdaValida=true;
			
			String fila="";
			String columna="";
			
			System.out.println("");
			System.out.println("Inserte la celda con la que desea interactuar (fila-columna)");
			String celda=Leer.dato();
	
			//Separamos la celda por "-"
			try {
				String[] celdaArray = celda.split("-");
				fila = celdaArray[0]; 
				columna = celdaArray[1];
			}catch(Exception e) 
			{
				celdaValida=false;
			}
			
			//Convertimos la celda en int
			try {
				filaJugador=Integer.valueOf(fila);
				columnaJugador=Integer.valueOf(columna);
			}catch(Exception e) 
			{
				celdaValida=false;
			}
			
			//Por si el usuario da valores que salen del tablero
			if(filaJugador>tablero.length || filaJugador<0 || columnaJugador>tablero.length || columnaJugador<0 ) 
				celdaValida=false;
			
			
		}
	}
	private boolean compruebaFlanqueo()	
	{
		//Comprobar que la ficha esta flanqueando otra, sino lo hace no puede ponerla ahi 
		
		/*Por flanquear se entiende el hecho de colocar la nueva ficha en un extremo de una hilera de fichas del color del contrario 
		 * (una o más fichas) en cuyo extremo opuesto hay una ficha del color de la que se incorpora, 
		 * sin que existan casillas libres entre ninguna de ellas. 
		 * Esta hilera puede ser indistintamente vertical, horizontal o diagonal */
		
		boolean flanqueo=false;	//Cuando flanqueo true la ficha es jugable 
		boolean libre=false;	
		boolean hilera=false;	
			
		//Primero la casilla donde jugamos debe estar vacia.
		if(tablero[filaJugador][columnaJugador]=='·')
			libre=true;
		
		//Busca que hay por lo menos una hilera que disponible para flanquear en esa posicion
		System.out.println("Comprobando flanqueo...");
		if(buscaHilera("Sup")|| buscaHilera("Inf") || buscaHilera("Izq") || buscaHilera("Dch") || buscaHilera("DiaSupIzq") || buscaHilera("DiaSupDch") || buscaHilera("DiaInfIzq") || buscaHilera("DiaInfDch") )
			hilera=true;
		
		//Si el espacio esta libre y hay una hilera disponible la jugada es posible
		if(libre && hilera)
			flanqueo=true;
		
		System.out.println("Flanqueo == "+flanqueo);
		return flanqueo;
	}
	private void colocaFicha() 
	{
		//Si encuentra la hilera que se coma las fichas de la hilera
		System.out.println("COMIENDO FICHAS");
		if(buscaHilera("Sup"))
			comeHileras("Sup");
		if(buscaHilera("Inf"))
			comeHileras("Inf");
		if(buscaHilera("Izq"))
			comeHileras("Izq");
		if(buscaHilera("Dch"))
			comeHileras("Dch");
		if(buscaHilera("DiaSupIzq"))
			comeHileras("DiaSupIzq");
		if(buscaHilera("DiaSupDch"))
			comeHileras("DiaSupDch");
		if(buscaHilera("DiaInfIzq"))
			comeHileras("DiaInfIzq");
		if(buscaHilera("DiaInfDch"))
			comeHileras("DiaInfDch");
	}
	private void comeHileras(String hilera) 
	{
		int fila=filaJugador;
		int columna=columnaJugador;

		boolean fichaIgual=false;
		
		while(!fichaIgual) 	//Repasa el tablero poniendo la ficha en la hilera hasta encontrarse con una ficha igual
		{	
			System.out.println("Comiendo "+fila+"-"+columna+" nhom nhom nhom...");
			tablero[fila][columna]=ficha;
			if(hilera=="Sup" || hilera=="DiaSupDch" || hilera=="DiaSupIzq" )
				fila--;
			if(hilera=="Izq" || hilera=="DiaInfIzq" || hilera=="DiaSupIzq" )
				columna--;
			if(hilera=="Dch" || hilera=="DiaSupDch" || hilera=="DiaInfDch" )
				columna++;
			if(hilera=="Inf" || hilera=="DiaInfIzq" || hilera=="DiaInfDch" )
				fila++;
			
			if(fila<tablero.length && fila>=0 && columna<tablero.length && columna>=0)
				if(tablero[fila][columna]==ficha) 
					fichaIgual=true;
		}
		
	}
	//VerificacionFlanqueo
	private boolean buscaHilera(String hilera) 	
	{
		//Primero verifica que hay una ficha del contrario justo al principio de la hilera
		//Despues verificamos que hay una hilera completa que se pueda comer
		
		boolean hayHilera=false;	//Si se cumplen las dos cosas es que hay hilera disponible	
		int fila=0;
		int columna=0;
		
		System.out.println("Buscando hileras...");
		
		switch(hilera) 
		{
			case"Sup":
				
				System.out.println("HILERA: "+hilera);
				
				//Coordenadas de la celda donde debe haber una fichaContrario para empezar la hilera
				fila=filaJugador-1;
				columna=columnaJugador;
				
				if(fila>=0) //Comprueba que la celda no sale del borde superior
						if(tablero[fila][columna]==fichaContrario)	//La celda tiene que tener una fichaContrario para empezar la hilera 
							hayHilera=compruebaHilera(fila, columna, hilera);	//Comprueba que la hilera es valida
				break;

			case"Izq":
				
				System.out.println("HILERA: "+hilera);
				
				//Coordenadas de la celda donde debe empezar la hilera
				fila=filaJugador;
				columna=columnaJugador-1;
				
				if(columna>=0)	//Comprueba que la celda no sale del borde izquierdo
					if(tablero[fila][columna]==fichaContrario)	
						hayHilera=compruebaHilera(fila, columna, hilera);
				break;
				
			case"Dch":
				
				System.out.println("HILERA: "+hilera);
				
				//Coordenadas de la celda donde debe empezar la hilera
				fila=filaJugador;
				columna=columnaJugador+1;
				
				if(columna<tablero.length)	//Comprueba que la celda no sale del borde derecho
					if(tablero[fila][columna]==fichaContrario)
						hayHilera=compruebaHilera(fila, columna, hilera);
				break;
			
			case"Inf":
				
				System.out.println("HILERA: "+hilera);
				
				//Coordenadas de la celda donde debe empezar la hilera
				fila=filaJugador+1;
				columna=columnaJugador;
				
				if(fila<tablero.length)	//Comprueba que la celda no sale del borde inferior
					if(tablero[fila][columna]==fichaContrario)	
						hayHilera=compruebaHilera(fila, columna, hilera);
				break;
				
			case"DiaSupIzq":
				
				System.out.println("HILERA: "+hilera);
				
				//Coordenadas de la celda donde debe empezar la hilera
				fila=filaJugador-1;
				columna=columnaJugador-1;
				
				if(fila>=0 && columna>=0) 
					if(tablero[fila][columna]==fichaContrario)	//Comprueba que la celda no sale de los bordes superior ni izquierdo
						hayHilera=compruebaHilera(fila, columna, hilera);
				break;
			
			case"DiaSupDch":
				
				System.out.println("HILERA: "+hilera);
				
				//Coordenadas de la celda donde debe empezar la hilera
				fila=filaJugador-1;
				columna=columnaJugador+1;
				
				if(fila>=0 && columna<tablero.length) 
					if(tablero[fila][columna]==fichaContrario)	//Comprueba que la celda no sale de los bordes superior ni derecho
						hayHilera=compruebaHilera(fila, columna, hilera);
				break;
			
			case"DiaInfIzq":
				
				System.out.println("HILERA: "+hilera);
				
				//Coordenadas de la celda donde debe empezar la hilera
				fila=filaJugador+1;
				columna=columnaJugador-1;
				
				if(fila<tablero.length && columna>=0) 
					if(tablero[fila][columna]==fichaContrario)	//Comprueba que la celda no sale de los bordes inferior ni izq
						hayHilera=compruebaHilera(fila, columna, hilera);
				break;
				
			case"DiaInfDch":
				
				System.out.println("HILERA: "+hilera);
				
				//Coordenadas de la celda donde debe empezar la hilera
				fila=filaJugador+1;
				columna=columnaJugador+1;
				
				if(fila<tablero.length && columna<tablero.length) 
					if(tablero[fila][columna]==fichaContrario)	//Comprueba que la celda no sale de los bordes inferior ni dch
						hayHilera=compruebaHilera(fila, columna, hilera);
				break;
				
			default:
				System.out.println("ERROR 404 HILERA NOT FOUND");
		}
		return hayHilera;
	}
	private boolean compruebaHilera(int fila, int columna, String hilera) 
	{
		/*Verificamos que la hilera es valida, para que lo sea tienen que cumplirse 2 casos: 
		 * Al final de la hilera tiene que haber una ficha igual a la que el jugador juega
		 * No puede tener espacios en el medio
		 * */
		
		boolean hayHilera=false;	//Hay hilera si se cumplen todas las condiciones
		boolean fichaIgual=false;
		boolean espacio=false;
		
		System.out.println("Comprobando hilera "+hilera);
		
		//Recorrera la hilera hasta que encuentre un espacio o una ficha igual o llegue al limite del tablero
		while(!fichaIgual && !espacio && fila>=0 && fila<tablero.length && columna>=0 && columna<tablero.length) 	
		{	
			if(hilera=="Sup" || hilera=="DiaSupDch" || hilera=="DiaSupIzq" )
				fila--;
			if(hilera=="Izq" || hilera=="DiaInfIzq" || hilera=="DiaSupIzq" )
				columna--;
			if(hilera=="Dch" || hilera=="DiaSupDch" || hilera=="DiaInfDch" )
				columna++;
			if(hilera=="Inf" || hilera=="DiaInfIzq" || hilera=="DiaInfDch" )
				fila++;
		
 			if(fila<tablero.length && fila>=0 && columna<tablero.length && columna>=0) 
			{
				if(tablero[fila][columna]==ficha) 
					fichaIgual=true;
				if(tablero[fila][columna]=='·') 
					espacio=true;
			}

		}
		
		if(fichaIgual && !espacio)
			hayHilera=true;
		
		System.out.println("Hilera "+hilera+" == "+hayHilera);
		
		return hayHilera;
	}
	//Final
	private int cuentaFichas(char fichaJugador) 
	{
		int numFichas=0;
		
		for(int i=0; i<tablero.length; i++) 
		{
			for(int j=0; j<tablero[0].length; j++) 
			{
				if(tablero[i][j]==fichaJugador)
					numFichas++;
			}	
		}
		
		return numFichas;
	}
	private void pantallaFinal() 
	{
		muestraTablero();
		System.out.println("");
		System.out.println(fichaJ1+": "+cuentaFichas(fichaJ1));
		System.out.println(fichaJ2+": "+cuentaFichas(fichaJ2));
		
		if(cuentaFichas(fichaJ1)>cuentaFichas(fichaJ2))
			System.out.println(fichaJ1+" ha ganado");
		else if(cuentaFichas(fichaJ1)==cuentaFichas(fichaJ2))
			System.out.println("Empate");
		else
			System.out.println(fichaJ2+" ha ganado");
	}
}
