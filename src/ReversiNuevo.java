public class ReversiNuevo 
//Reversi 3.1
{
	public static void main(String[] args) 
	{
		Tablero t = new Tablero();
		t.juego();
	}
}

class Tablero
{
	static String[][] tablero = new String[8][8];	//El reversi tiene un tablero 8x8 
	static int filaJugador, columnaJugador;	//fila y columna que el jugador mete por teclado para interactuar con el juego
	static char ficha=' ', fichaContrario=' ';	//ficha y fichaContrario tomaran el valor de fichaJ1 o fichaJ2 dependiendo del turno
	static char fichaJ1='X', fichaJ2='O';	//Definimos las dos posibilidades que tiene la ficha
	static boolean turnoJ1=true;

	Tablero()
	{
		inicializaTablero();		
	}

	//Creando lo minimo para jugar
	private void inicializaTablero() 
	{
		//Celdas vacias
		for(int i=0; i<tablero.length; i++) 
		{
			for(int j=0; j<tablero[0].length; j++) 
				tablero[i][j]="·";
		}
		//Las partidas empiezan siempre con estas 4 fichas en estas posiciones
		tablero[4][4]=String.valueOf(fichaJ2);
		tablero[4][3]=String.valueOf(fichaJ1);
		tablero[3][4]=String.valueOf(fichaJ1);
		tablero[3][3]=String.valueOf(fichaJ2);
	}

	//Juego
	public void juego() 
	{
		boolean victoria=false;
		while(!victoria) 
		{
			setFicha();
			System.out.println("");
			System.out.println("Es el turno del jugador ( "+ficha+" )");
			muestraTablero();
			do
			{
				pideCelda();
			}while(!compruebaFlanqueo());
			colocaFicha();
			turnoJ1=!turnoJ1;
			victoria=compruebaVictoria();
		}
		System.out.println(fichaJ1+": "+cuentaFichas(fichaJ1));
		System.out.println(fichaJ2+": "+cuentaFichas(fichaJ2));
		if(cuentaFichas(fichaJ1)>cuentaFichas(fichaJ2))
			System.out.println(fichaJ1+" ha ganado");
		if(cuentaFichas(fichaJ1)==cuentaFichas(fichaJ2))
			System.out.println("Empate");
	}
	private void setFicha() 
	{
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
			for(int j=0; j<tablero[0].length; j++) 
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
			
			//Por si el usuario es imbecil columna se sale de la tabla
			if(filaJugador>tablero.length || filaJugador<0 || columnaJugador>tablero[0].length || columnaJugador<0 ) 
				celdaValida=false;
			
			//Comprobar que la ficha esta flanqueando otra, sino lo hace no puede ponerla ahi
		}
	}
	private boolean compruebaFlanqueo() 
	{
		/*Por flanquear se entiende el hecho de colocar la nueva ficha en un extremo de una hilera de fichas 
		 * del color del contrario (una o más fichas) en cuyo extremo opuesto hay una ficha del color de la que se incorpora, 
		 * sin que existan casillas libres entre ninguna de ellas. 
		 * Esta hilera puede ser indistintamente vertical, horizontal o diagonal */
		boolean flanqueo=false;	
		boolean libre=false;	
		boolean hilera=false;	
			
		//Primero la casilla donde jugamos debe estar vacia.
		if(tablero[filaJugador][columnaJugador]=="·")
			libre=true;
		
		//Verifica que hay por lo menos una hilera disponible
		if(ComprobadorHileras("Sup")|| ComprobadorHileras("Inf") || ComprobadorHileras("Izq") || ComprobadorHileras("Dch") || ComprobadorHileras("DiaSupIzq") || ComprobadorHileras("DiaSupDch") || ComprobadorHileras("DiaInfIzq") || ComprobadorHileras("DiaInfDch") )
			hilera=true;
		
		if(libre && hilera)
			flanqueo=true;
		
		return flanqueo;
	}
	private void colocaFicha() 
	{
		if(ComprobadorHileras("Sup"))
			comeHileras("Sup");
		if(ComprobadorHileras("Inf"))
			comeHileras("Inf");
		if(ComprobadorHileras("Izq"))
			comeHileras("Izq");
		if(ComprobadorHileras("Dch"))
			comeHileras("Dch");
		if(ComprobadorHileras("DiaSupIzq"))
			comeHileras("DiaSupIzq");
		if(ComprobadorHileras("DiaSupDch"))
			comeHileras("DiaSupDch");
		if(ComprobadorHileras("DiaInfIzq"))
			comeHileras("DiaInfIzq");
		if(ComprobadorHileras("DiaInfDch"))
			comeHileras("DiaInfDch");
	}
	private void comeHileras(String hilera) 
	{
		int fila=filaJugador;
		int columna=columnaJugador;

		boolean fichaIgual=false;
		
		while(!fichaIgual) 	//Repasa el tablero poniendo la ficha en la hilera que se busca hasta encontrarse con una ficha igual
		{	
			tablero[fila][columna]=String.valueOf(ficha);
			if(hilera=="Sup" || hilera=="DiaSupDch" || hilera=="DiaSupIzq" )
				fila--;
			if(hilera=="Izq" || hilera=="DiaInfIzq" || hilera=="DiaSupIzq" )
				columna--;
			if(hilera=="Dch" || hilera=="DiaSupDch" || hilera=="DiaInfDch" )
				columna++;
			if(hilera=="Inf" || hilera=="DiaInfIzq" || hilera=="DiaInfDch" )
				fila++;		
			if(tablero[fila][columna].equals(String.valueOf(ficha))) 
				fichaIgual=true;
		}
		
	}
	//VerificacionFlanqueo
	private boolean ComprobadorHileras(String hilera) 	//
	{
		boolean compruebaHilera=false;
		int fila=0;
		int columna=0;
		
		switch(hilera) 
		{
			case"Sup":
				
				fila=filaJugador-1;
				columna=columnaJugador;
				
				if(fila>=0) //Comprueba que la celda no es la del borde superior
						if(tablero[fila][columna].equals(String.valueOf(fichaContrario)))	//Tiene que tener una justo arriba
							compruebaHilera=verificadorHilera(fila, columna, hilera);
				break;

			case"Izq":
				
				fila=filaJugador;
				columna=columnaJugador-1;
				
				if(columna>=0)
					if(tablero[fila][columna].equals(String.valueOf(fichaContrario)))	//Tiene que tener una justo a la izq
						compruebaHilera=verificadorHilera(fila, columna, hilera);
				break;
				
			case"Dch":
				
				fila=filaJugador;
				columna=columnaJugador+1;
				
				if(columna<tablero.length) 
					if(tablero[fila][columna].equals(String.valueOf(fichaContrario)))	//Tiene que tener una justo a la dch
						compruebaHilera=verificadorHilera(fila, columna, hilera);
				break;
			
			case"Inf":
				
				fila=filaJugador+1;
				columna=columnaJugador;
				
				if(fila<tablero.length) 
					if(tablero[fila][columna].equals(String.valueOf(fichaContrario)))	//Tiene que tener una justo abajo
						compruebaHilera=verificadorHilera(fila, columna, hilera);
				break;
				
			case"DiaSupIzq":
				
				fila=filaJugador-1;
				columna=columnaJugador-1;
				
				if(fila>=0 && columna>=0) 
					if(tablero[fila][columna].equals(String.valueOf(fichaContrario)))	//Tiene que tener una justo abajo
						compruebaHilera=verificadorHilera(fila, columna, hilera);
				break;
			
			case"DiaSupDch":
				
				fila=filaJugador-1;
				columna=columnaJugador+1;
				
				if(fila>=0 && columna<tablero.length) 
					if(tablero[fila][columna].equals(String.valueOf(fichaContrario)))	//Tiene que tener una justo abajo
						compruebaHilera=verificadorHilera(fila, columna, hilera);
				break;
			
			case"DiaInfIzq":
				
				fila=filaJugador+1;
				columna=columnaJugador-1;
				
				if(fila<tablero.length && columna>=0) 
					if(tablero[fila][columna].equals(String.valueOf(fichaContrario)))	//Tiene que tener una justo abajo
						compruebaHilera=verificadorHilera(fila, columna, hilera);
				break;
				
			case"DiaInfDch":
				
				fila=filaJugador+1;
				columna=columnaJugador+1;
				
				if(fila<tablero.length && columna<tablero.length) 
					if(tablero[fila][columna].equals(String.valueOf(fichaContrario)))	//Tiene que tener una justo abajo
						compruebaHilera=verificadorHilera(fila, columna, hilera);
				break;
				
			default:
				System.out.println("ERROR 404 HILERA NOT FOUND");
		}
		return compruebaHilera;
	}
	//Verifica hileras
	public boolean verificadorHilera(int fila, int columna, String hilera) 
	{
		//Dependiendo de que hilera quiero buscar necesitare sumar o restar 1 a la fila o a la columna
		boolean fichaIgual=false;
		boolean espacio=false;
		
		boolean hayHilera=false;	//Hay hilera si es una linea initerrumpida de fihchas del contrario hasta llegar a una nuestra
		
		//Recorrera la hilera hasta que encuentre un espacio o una ficha igual
		while(!fichaIgual && !espacio) 	
		{	
			if(hilera=="Sup" || hilera=="DiaSupDch" || hilera=="DiaSupIzq" )
				fila--;
			if(hilera=="Izq" || hilera=="DiaInfIzq" || hilera=="DiaSupIzq" )
				columna--;
			if(hilera=="Dch" || hilera=="DiaSupDch" || hilera=="DiaInfDch" )
				columna++;
			if(hilera=="Inf" || hilera=="DiaInfIzq" || hilera=="DiaInfDch" )
				fila++;
			if(tablero[fila][columna].equals(String.valueOf(ficha))) 
				fichaIgual=true;
			if(tablero[fila][columna]=="·") 
				espacio=true;
		}
		
		if(fichaIgual && !espacio)
			hayHilera=true;
		
		return hayHilera;
	}
	//Final
	private boolean compruebaVictoria() 
	{
		int celdasOcupadas=0;
		boolean finPartida=false;
		
		for(int i=0; i<tablero.length; i++) 
		{
			for(int j=0; j<tablero[0].length; j++) 
			{
				if(tablero[i][j]=="·")
					celdasOcupadas++;
			}	
		}
		
		if(celdasOcupadas==64)
			finPartida=true;
		
		return finPartida;
	}
	private int cuentaFichas(char fichaJugador) 
	{
		String fichaJ=String.valueOf(fichaJugador);
		int numFichas=0;
		
		for(int i=0; i<tablero.length; i++) 
		{
			for(int j=0; j<tablero[0].length; j++) 
			{
				if(tablero[i][j]==fichaJ)
					numFichas++;
			}	
		}
		
		return numFichas;
	}
}
