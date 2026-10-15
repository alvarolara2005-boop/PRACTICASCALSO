package juego.geometria;

public class Circulo {
    
	public static final int DEFAULT_RADIO = 5;
    
    private Punto centro;
    private int radio;

    public Circulo(Punto centroIni, int radioIni){
    	centro = centroIni;
        radio = radioIni;
    }

    public Circulo(){
        this(new Punto(), DEFAULT_RADIO);
    }
    
    public Punto getCentro() {
        return new Punto(centro);
    }

    public int getRadio() {
        return radio;
    }

    public double getPerimetro() {
        return 2 * Math.PI * radio;
    }
    
    public void desplazar(int incX, int incY){
        centro = centro.desplazar(incX, incY);
    }
    
    public void escalar(double porcentaje){
        radio = (int)(radio * (porcentaje/100));
    }
}