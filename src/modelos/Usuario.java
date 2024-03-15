package modelos;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

 private String nome;
 private String cpf;

 private List<Aposta> apostas = new ArrayList <>();


 public String getNome() {
  return nome;
 }

 public void setNome(String nome) {
  this.nome = nome;
 }

 public String getCpf() {
  return cpf;
 }

 public void setCpf(String cpf) {
  this.cpf = cpf;
 }

 public List<Aposta> getApostas() {
  return apostas;
 }

 public void setApostas(List<Aposta> apostas) {
  this.apostas = apostas;
 }
}
