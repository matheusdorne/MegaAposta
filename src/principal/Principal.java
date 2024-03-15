package principal;

import modelos.Aposta;
import modelos.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private List<Usuario> usuariosCadastrados = new ArrayList<>();
    private static List<Aposta> usuariosApostas = new ArrayList<>();

    private static int contadorDeRegistros = 1000;

    private int statusSorteio = 0;

    public void exibeMenu() {
        System.out.println("Bem Vindo Ao MEGA APOSTA!");
        int opcao = -1;
        while (opcao != 0) {
            var menu = """ 
                                    
                    1 - [INICIAR] 
                    2 - [REGISTRAR NOVA APOSTA] 
                    3 - [LISTAR APOSTAS] 
                    4 - [FINALIZAR APOSTAS E EXECUTAR O SORTEIO] 
                    5 - [FIM DA APURAÇÃO]  
                    6 - [PREMIAÇÃO] 
                                    
                    0 - [SAIR] 
                                    
                    Seleciona a opção desejada:
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1 -> iniciar();
                case 2 -> registrarAposta();
                case 3 -> listarApostas();
                case 4 -> finalizarApostas();
                case 5 -> fimDaApuracao();
                case 6 -> premiacao();
                case 0 -> System.exit(0);
            }
        }


    }

    private void premiacao() {

    }

    private void fimDaApuracao() {

    }

    private void finalizarApostas() {
        statusSorteio = 1;
        Sorteio();


    }

    private void listarApostas() {

        for (var aposta : usuariosApostas) {
            System.out.println(aposta.getUsuario().getNome());

            String linhaNumero = "";

            for (var registro : aposta.getNumerosDaAposta()) {
                linhaNumero += registro + " ";

            }
            System.out.println("Usuário: " + aposta.getUsuario().getNome() + " Registro: " + aposta.getRegistro() + " - " + linhaNumero);
        }

    }

    private void registrarAposta() {
        if (statusSorteio == 0) {
            String nome;
            var usuario = new Usuario();

            System.out.println("Digite o CPF do apostador: ");
            var cpf = leitura.nextLine();


            if (usuariosCadastrados.stream().anyMatch(u -> u.getCpf().equals(cpf))) {
                usuario = usuariosCadastrados.stream().filter(u -> u.getCpf().equals(cpf)).findFirst().get();
                System.out.println("Bem vindo: " + usuario.getNome());

            } else {
                System.out.println("Digite o nome do apostador: ");
                nome = leitura.nextLine();

                usuario.setNome(nome);
                usuario.setCpf(cpf);
                usuariosCadastrados.add(usuario);
            }

            System.out.println(""" 
                    \n
                    1 - Aposta Manual 
                    2 - Surpresinha 
                                    
                    Escolha a opção desejada:
                    """);
            var opcaoAposta = leitura.nextInt();
            leitura.nextLine();

            List<Integer> numeros = new ArrayList<>();

            if (opcaoAposta == 1) {

                System.out.println("Digite 5 números entre 1 e 50: ");
                for (int i = 0; i <= 4; i++) {
                    System.out.println((i + 1) + "º Número:");
                    var valor = leitura.nextLine();


                    try {
                        var valorInt = Integer.parseInt(valor);
                        if (valorInt >= 1 && valorInt <= 50) {
                            numeros.add(valorInt);
                        } else {
                            System.out.println("Valor invalido para aposta!\n");
                            i--;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Valor invalido para aposta!\n");
                        i--;
                    }


                }


                var aposta = geraApostaManual(numeros, usuario);
                usuariosApostas.add(aposta);

            } else {
                var aposta = geraApostaAleatoria(usuario);
                usuariosApostas.add(aposta);

            }
        } else {
            System.out.println("Apostas finalizadas! Aguarde o final sorteio!");
        }


    }


    private static Aposta geraApostaAleatoria(Usuario usuario) {
        var aposta = new Aposta();
        var numeros = new ArrayList<Integer>();
        //Cria um arraylist de inteiros para ser adicionado a aposta posteriormente
        for (int i = 0; i < 5; i++) {
            var numero = (int) (Math.random() * 50) + 1;
            // Como o Math.Random * 50 gera um número entre 0 e 49, é necessário somar 1 para que o número gerado seja entre 1 e 60
            if (!numeros.contains(numero)) {
                numeros.add(numero);
            } else {
                i--;
                // Caso o número já exista no arraylist, o i-- vai decrementar o contador para que o número seja gerado novamente
            }

        }
        aposta.setRegistro(contadorDeRegistros++);
        aposta.setNumerosDaAposta(numeros);
        aposta.setUsuario(usuario);
        return aposta;
    }

    private static Aposta geraApostaManual(List<Integer> numerosAposta, Usuario usuario) {
        var aposta = new Aposta();
        aposta.setRegistro(contadorDeRegistros++);
        aposta.setNumerosDaAposta(List.of(
                Integer.parseInt(String.valueOf(numerosAposta.get(0))),
                Integer.parseInt(String.valueOf(numerosAposta.get(1))),
                Integer.parseInt(String.valueOf(numerosAposta.get(2))),
                Integer.parseInt(String.valueOf(numerosAposta.get(3))),
                Integer.parseInt(String.valueOf(numerosAposta.get(4)))));
        aposta.setUsuario(usuario);
        return aposta;
    }

    private void iniciar() {


    }

    public static void Sorteio() {
        List<Integer> numeros = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            var numero = (int) (Math.random() * 50) + 1;
            if (!numeros.contains(numero)) {
                numeros.add(numero);
            } else {
                i--;
            }

        }



//        List<Integer> teste = new ArrayList<>();
//        teste.add(1);
//        teste.add(2);
//        teste.add(3);
//        teste.add(4);
//        teste.add(5);

        System.out.println("Números sorteados: " +numeros);
        List<Aposta> vencedores = verificaVencedores(numeros);

        int contadorNovosSorteios = 0;
        while (vencedores.isEmpty() && contadorNovosSorteios != 25) {
           // System.out.println("Nenhum vencedor! Novo sorteio!");

            int novoNumeroSorteio = (int) (Math.random() * 50) + 1;
            if (!numeros.contains(novoNumeroSorteio)) {
                numeros.add(novoNumeroSorteio);
                contadorNovosSorteios++;
            }else {
                continue;
            }
            System.out.println("Números sorteados: " + numeros);
            vencedores = verificaVencedores(numeros);


        }

        if (vencedores.isEmpty()) {
            System.out.println("Nenhum vencedor!  Concurso finalizado!");
            System.out.println("Foram " + contadorNovosSorteios + " sorteios sem vencedores!");
        } else {
            System.out.println("Vencedores: " );
            vencedores.stream().forEach(v -> System.out.println("Nome: " + v.getUsuario().getNome() + " - Registro: " + v.getRegistro() + " - Aposta: " + v.getNumerosDaAposta()));

            if (contadorNovosSorteios != 0)
            System.out.println("\nForam necessários " + contadorNovosSorteios + " sorteios novos para encontrar o(s) vencedor(es)!");

        }

    }

    public static List<Aposta> verificaVencedores(List<Integer> numeros) {
        List<Aposta> vencedores = new ArrayList<>();
        for (var aposta : usuariosApostas) {
            var acertos = 0;
            for (var numero : aposta.getNumerosDaAposta()) {
                if (numeros.contains(numero)) {
                    acertos++;
                }
            }
            if (acertos == 5) {
                vencedores.add(aposta);
            }
        }
        return vencedores;


    }
}