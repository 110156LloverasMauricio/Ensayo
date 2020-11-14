
package Modelos;
public class DtoSumaPreciosRubro {

public float sumaPrecios;
public String nombreRubro;

    public DtoSumaPreciosRubro(float sumaPrecios, String nombreRubro) {
        this.sumaPrecios = sumaPrecios;
        this.nombreRubro = nombreRubro;
    }

    @Override
    public String toString() {
        return "DtoSumaPreciosRubro{" + "sumaPrecios=" + sumaPrecios + ", nombreRubro=" + nombreRubro + '}';
    }

    
}
