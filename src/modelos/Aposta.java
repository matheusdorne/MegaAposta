package modelos;

import java.util.ArrayList;
import java.util.List;

public class Aposta {

    private int registro;

    private Usuario usuario;

    private List<Integer> numerosDaAposta = new ArrayList<>();

    public List<Integer> getNumerosDaAposta() {
        return numerosDaAposta;
    }

    public void setNumerosDaAposta(List<Integer> numerosDaAposta) {
        this.numerosDaAposta = numerosDaAposta;
    }

    public int getRegistro() {
        return registro;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
