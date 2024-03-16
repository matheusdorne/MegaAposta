package modelos;

import java.util.List;

public class Vencedor {


    private int registro;
    private Usuario usuario;
    private List<Integer> numerosDaAposta;

    private  Double premio;

    public Double getPremio() {
        return premio;
    }

    public void setPremio(Double premio) {
        this.premio = premio;
    }


    public Vencedor (int registro, Usuario usuario, List<Integer> numerosDaAposta) {
        this.registro = registro;
        this.usuario = usuario;
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

    public List<Integer> getNumerosDaAposta() {
        return numerosDaAposta;
    }

    public void setNumerosDaAposta(List<Integer> numerosDaAposta) {
        this.numerosDaAposta = numerosDaAposta;
    }
}
