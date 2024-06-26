package principal;

import modelos.Aposta;
import modelos.Usuario;


import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;
import java.util.stream.Collectors;


public class Principal {

    private final Scanner leitura = new Scanner(System.in);

    //As três listas abaixo são utilizadas como memoria para armazenar os dados
    private final List<Usuario> usuariosCadastrados = new ArrayList<>();
    private static final List<Aposta> usuariosApostas = new ArrayList<>();
    private static final List<Aposta> vencedoresSorteio = new ArrayList<>();

    private static Double premioTotal = 150000.0;

    private static int edicaoConcurso = 0;
    //Contador para gerar a edição do concurso, começa em 1 e é incrementado a cada concurso

    private static int contadorDeRegistros = 1000;
    //Contador para gerar o registro da aposta, começa em 1000 e é incrementado a cada aposta

    private int statusSorteio = -1;
    //-1 - Concurso não iniciado
    // 0 - Apostas abertas
    // 1 - Apostas finalizadas e sorteio em andamento
    // 2 - Sorteio finalizado


    private static int rodadasSorteio = 0;
    // Contador para verificar quantas vezes o sorteio foi repetido até encontrar um vencedor

    private static boolean acumulou = false;
    // Variável para verificar se o concurso acumulou

    private static List<Integer> resultadoNumeros = new ArrayList<>();

    public void exibeMenu() {
        System.out.println("Bem Vindo Ao MEGA APOSTA!");

        while (true) {
            var menu = """ 
                                    
                    1 - [INICIAR]
                    2 - [REGISTRAR NOVA APOSTA]
                    3 - [LISTAR APOSTAS]
                    4 - [FINALIZAR APOSTAS E EXECUTAR O SORTEIO]
                    5 - [FIM DA APURAÇÃO]
                    6 - [PREMIAÇÃO]
                    7 - [VENCEDORES]
                                    
                    0 - [SAIR]
                                    
                    Seleciona a opção desejada:
                    """;

            System.out.println(menu);
            var opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1 -> iniciar();
                case 2 -> registrarAposta();
                case 3 -> listarApostas();
                case 4 -> finalizarApostas();
                case 5 -> fimDaApuracao();
                case 6 -> premiacao();
                case 7 -> listaVencedores();
                case 0 -> System.exit(0);
                default -> System.out.println("Opção inválida!");
            }
        }


    }


    private void iniciar() {
        if (statusSorteio == 2 || statusSorteio == -1 ) {
            // Ajustamos os contadores criados para um próximo concurso
            statusSorteio = 0;
            edicaoConcurso++;
            contadorDeRegistros = 1000 * edicaoConcurso;
            usuariosApostas.clear();
            acumulou = false;

            System.out.println("Concurso iniciado! Edição: " + edicaoConcurso);
            System.out.println("Prêmio total: R$" + premioTotal);
        } else {
            System.out.println("Concurso já iniciado!");
        }


    }

    private void registrarAposta() {

        if (statusSorteio == -1) {
            System.out.println("Concurso não iniciado!");
            return;
        }
        if (statusSorteio == 0) {
            String nome;
            var usuario = new Usuario();

            String tempCPF;
            while (true) {
// Coloquei a opção de CPF antes de nome para caso o utilizador já esteja cadastrado, não precisar digitar o nome novamente
                System.out.println("Digite o CPF do apostador (Apenas números): ");
                tempCPF = leitura.nextLine();
                //Método para verificar se o CPF tem apenas números e 11 caracteres
                if (verificaCPF(tempCPF)) {
                    break;
                } else {
                    System.out.println("CPF inválido! Digite novamente!");
                }

            }
            var cpf = tempCPF;

            if (usuariosCadastrados.stream().anyMatch(u -> u.getCpf().equals(cpf))) {
                usuario = usuariosCadastrados.stream().filter(u -> u.getCpf().equals(cpf)).findFirst().get();
                System.out.println("Bem vindo: " + usuario.getNome());

            } else {
                //Verifica se o campo nome esta vazio
                while (true) {
                    System.out.println("Digite o nome do apostador: ");
                    nome = leitura.nextLine();
                    if (!nome.isEmpty() && !nome.trim().isEmpty()) {
                        //Evita que sejá digiado um nome vazio
                        // Utilizo o trim para remover espaços em branco no início e no final do nome
                        // evitando que alguém se registre como " " por exemplo
                        break;
                    } else {
                        System.out.println("Nome inválido! Digite novamente!");
                    }
                }
                usuario.setNome(nome.trim());

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
                        //Tentamos converter o valor para inteiro, usei o try catch para caso o valor não seja um inteiro

                        if (valorInt >= 1 && valorInt <= 50) {
                            //Verifica se esta entre 1 e 50

                            if (!numeros.contains(valorInt)) {
                                //Esse if verifica se o número já foi escolhido
                                numeros.add(valorInt);
                            } else {
                                System.out.println("Número já escolhido! Digite outro número!");
                                i--;
                            }
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
                System.out.println("Aposta gerada com sucesso!");
                System.out.println(aposta.getNumerosDaAposta());

            } else {
                var aposta = geraApostaAleatoria(usuario);
                usuariosApostas.add(aposta);

                System.out.println("Aposta gerada com sucesso!");
                System.out.println(aposta.getNumerosDaAposta());

            }
        } else {
            System.out.println("Apostas finalizadas! Aguarde o final sorteio!");
        }


    }

    private void listarApostas() {

        for (var aposta : usuariosApostas) {

            StringBuilder linhaNumero = new StringBuilder();

            for (var registro : aposta.getNumerosDaAposta()) {
                linhaNumero.append(registro).append(" ");
                //Utilizado um laço para imprimir os números da aposta no formato xx xx xx xx xx

            }
            System.out.println(aposta.getEdicaoConcurso() + " Usuário: " + aposta.getUsuario().getNome() + " Registro: " + aposta.getRegistro() + " - " + linhaNumero);
        }

    }

    private void finalizarApostas() {
        if (statusSorteio == -1) {
            System.out.println("Concurso não iniciado!");
            return;
        }

        if (usuariosApostas.isEmpty()) {
            System.out.println("Não há apostas para finalizar!");
            return;
        }
        //Verifica se existem apostas para finalizar o sorteio

        if (statusSorteio == 1) {
            System.out.println("Apostas já foram finalizadas! Aguarde o final do sorteio!");
            return;
        }
        //Evita que seja finalizado novamente

        System.out.println("Você deseja finalizar as apostas e executar o sorteio? (S/N)");
        var opcao = leitura.nextLine();
        if (opcao.equalsIgnoreCase("S")) {
            statusSorteio = 1;
            Sorteio();
        }


    }

    private void fimDaApuracao() {

        if (statusSorteio == -1) {
            System.out.println("Concurso não iniciado!");
            return;
        }

        if (statusSorteio == 0) {
            System.out.println("Apostas ainda não finalizadas! Aguarde o final do sorteio!");
            return;
        }

        statusSorteio = 2;

        System.out.println("Números sorteados: " + resultadoNumeros);

        // Esse stream verifica se existem vencedores dessa edição do concurso
        List<Aposta> vencedoresEdicao = vencedoresSorteio.stream()
                .filter(v -> v.getEdicaoConcurso() == edicaoConcurso)
                .toList();

        if (vencedoresEdicao.isEmpty()) {
            System.out.println("Nenhum vencedor!  Concurso acumulou!");
            acumulou = true;
        } else {
            System.out.println("Vencedores: \n");
            vencedoresEdicao.forEach(v -> System.out.println("Nome: " + v.getUsuario().getNome() + " - Registro: " + v.getRegistro() + " - Aposta: " + v.getNumerosDaAposta()));
            System.out.println("\nPrêmio total: R$ " + premioTotal);
            System.out.println("Prêmio por vencedor: R$ " + premioTotal / vencedoresEdicao.size());
            System.out.println("Foram necessárias " + rodadasSorteio + " rodadas para encontrar o(s) vencedor(es)!");
        }
        System.out.println("\nEstatisitcas do concurso: \n");
        System.out.println("Número de apostas: " + usuariosApostas.size());
        System.out.println("Número vencedores: " + vencedoresEdicao.size());
        System.out.println("Número de rodadas: " + rodadasSorteio);
        System.out.println("\nTodas numerações apostadas e suas ocorrências:");
        List<Integer> numerosApostados = new ArrayList<>();

        for (var aposta : usuariosApostas) {
            numerosApostados.addAll(aposta.getNumerosDaAposta());
        }
//        Primeiro modelo de estatísticas, porém não ordenava corretamente
//        List<Integer> listaUnica = numerosApostados.stream().distinct().collect(Collectors.toList());
//        System.out.println("Números apostados: " + numerosApostados);
//
//        for (var numero : listaUnica) {
//            System.out.println("Número: " + numero + " - Ocorrências: " + numerosApostados.stream().filter(n -> n.equals(numero)).count());
//        }


        // Segundo modelo de estatísticas, ordena corretamente
        // A partir de um fluxo gerando um mapa com chave valor, sendo a chave o número apostado e o valor a quantidade de vezes que ele foi apostado
        // Ordena o mapa pelo valor e coleta em uma lista
        var numerosApostadosOrdenados = numerosApostados.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
                .entrySet().stream()
                .sorted((n1, n2) -> n2.getValue().compareTo(n1.getValue()))
                .toList();
        numerosApostadosOrdenados.forEach(n -> System.out.println("Número: " + n.getKey() + " - Quantidade de apostas: " + n.getValue()));


    }

    private void premiacao() {
        if (statusSorteio == -1) {
            System.out.println("Concurso não iniciado!");
            return;
        }

        if (statusSorteio != 2) {
            System.out.println("Apostas ainda não finalizadas! Aguarde o final do sorteio!");
            return;
        }
        //Verificação para evitar que siga a ordem de apuração

        if (acumulou) {
            System.out.println("O concurso acumulou! Não houve vencedores!");
            System.out.println("Acumulado: R$" + premioTotal);
        } else {
            System.out.println("Prêmio total: R$ " + premioTotal);
            System.out.println("Número de vencedores: " + vencedoresSorteio.size());
            System.out.println("Prêmio por vencedor: R$ " + premioTotal / vencedoresSorteio.size());
        }

    }

    private void listaVencedores() {

        //Verificação para evitar que siga a ordem de apuração

        if (vencedoresSorteio.isEmpty()) {
            System.out.println("Não vencedores até o momento! Continua apostando!");
        } else {
            System.out.println("Vencedores: \n");
            vencedoresSorteio.forEach(v -> System.out.println("Concurso: "+ v.getEdicaoConcurso() + " Nome: " + v.getUsuario().getNome()  + " - Registro: " + v.getRegistro() + " - Aposta: " + v.getNumerosDaAposta()));

        }
    }



    private static Aposta geraApostaAleatoria(Usuario usuario) {
        var aposta = new Aposta();
        var numeros = new ArrayList<Integer>();
        //Cria um arraylist de inteiros para ser adicionado a aposta posteriormente
        for (int i = 0; i < 5; i++) {
            var numero = (int) (Math.random() * 50) + 1;
            // Como o Math.Random * 50 gera um número entre 0 e 49, é necessário somar 1 para que o número gerado seja entre 1 e 50
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
        aposta.setEdicaoConcurso(edicaoConcurso);
        return aposta;
    }

    private static Aposta geraApostaManual(List<Integer> numerosAposta, Usuario usuario) {
        // Aqui recebemos os números escolhidos já verificados e o utilizador para gerar a aposta
        var aposta = new Aposta();
        aposta.setRegistro(contadorDeRegistros++);
        aposta.setNumerosDaAposta(List.of(
                Integer.parseInt(String.valueOf(numerosAposta.get(0))),
                Integer.parseInt(String.valueOf(numerosAposta.get(1))),
                Integer.parseInt(String.valueOf(numerosAposta.get(2))),
                Integer.parseInt(String.valueOf(numerosAposta.get(3))),
                Integer.parseInt(String.valueOf(numerosAposta.get(4)))));
        //Cria a lista com as posições de 1 a 4 com os números escolhidos
        aposta.setUsuario(usuario);
        aposta.setEdicaoConcurso(edicaoConcurso);
        return aposta;
    }


    public static void Sorteio() {
        List<Integer> numeros = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            // Gera 5 números aleatórios entre 1 e 50 validando se já repetiram ou não
            // Posteriormente adiciona na lista de números sorteados caso não vencedores
            var numero = (int) (Math.random() * 50) + 1;
            if (!numeros.contains(numero)) {
                numeros.add(numero);
            } else {
                i--;
            }

        }

        // Teste para verificar se o método de verificação de vencedores está funcionando
        List<Integer> teste = new ArrayList<>();
//        teste.add(1);
//        teste.add(2);
//        teste.add(3);
//        teste.add(4);
//        teste.add(5);

        List<Aposta> vencedores = verificaVencedores(numeros);

        int contadorNovosSorteios = 0;
        while (vencedores.isEmpty() && contadorNovosSorteios != 25) {
            // Se não houver vencedores, o sorteio é repetido até 25 vezes

//             System.out.println("Nenhum vencedor! Novo sorteio!");

            int novoNumeroSorteio = (int) (Math.random() * 50) + 1;
            // Gera um novo número aleatório
            if (!numeros.contains(novoNumeroSorteio)) {
                // Se o número gerado não estiver na lista de números sorteados, ele é adicionado
                // para evitar repetições
                numeros.add(novoNumeroSorteio);
                contadorNovosSorteios++;
            } else {
                // Se o número gerado já estiver na lista, o laço continua
                continue;
            }
//            System.out.println("Números sorteados: " + numeros);
            vencedores = verificaVencedores(numeros);


        }

        if (!vencedores.isEmpty()) {
            // Se houver vencedores, o prêmio é dividido entre eles
            for (var vencedor : vencedores) {
                var premio = premioTotal / vencedores.size();
                vencedor.setPremio(premio);
                vencedor.setEdicaoConcurso(edicaoConcurso);
                vencedoresSorteio.add(vencedor);
            }

        } else {
            // Se não houver vencedores, o prêmio acumula para o próximo concurso
            premioTotal += 150000;
        }

        System.out.println("Sorteio realizado!");

        resultadoNumeros = numeros;
        rodadasSorteio = contadorNovosSorteios;

        // Esse stream verifica se existem vencedores dessa edição do concurso


//        if (vencedores.isEmpty()) {
//            System.out.println("Nenhum vencedor!  Concurso finalizado!");
//            System.out.println("Foram " + contadorNovosSorteios + " sorteios sem vencedores!");
//        } else {
//            System.out.println("Vencedores: " );
//            vencedores.stream().forEach(v -> System.out.println("Nome: " + v.getUsuario().getNome() + " - Registro: " + v.getRegistro() + " - Aposta: " + v.getNumerosDaAposta()));
//
//            if (contadorNovosSorteios != 0)
//                System.out.println("\nForam necessários " + contadorNovosSorteios + " sorteios novos para encontrar o(s) vencedor(es)!");
//
//        }

    }

    public static List<Aposta> verificaVencedores(List<Integer> numeros) {
        //Método utiliza laços para verificar se os números sorteados estão presentes nas apostas
        //e retorna uma lista com as apostas vencedoras
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

    public static boolean verificaCPF(String CPF) {

        //Método para verificar se o CPF tem apenas números e 11 caracteres
        //Decidi não usar validaçao de CPF, para facilitar os testes
        return (CPF.length() == 11) && CPF.matches("[0-9]*");


    }


}