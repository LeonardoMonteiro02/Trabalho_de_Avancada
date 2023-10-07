package com.example;

import java.util.Random;

public class Vehicle extends Thread {
    private String nome;
    private Company company;
    private Route rotaAtual;

    public Vehicle(String nome, Company company) {
        this.nome = nome;
        this.company = company;

    }

    public synchronized void atribuirRota(Route rota) {
        rotaAtual = rota;
        notify(); // Notifica a thread de veículo que uma nova rota está atribuída
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (rotaAtual == null) {
                    try {
                        System.out.println("Aguardando rota");
                        wait(); // Aguarda até que uma rota seja atribuída
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("Erro com a Rota (run veiculo)");
                    }
                }
            }

            simularExecucaoDaRota(rotaAtual);
            company.marcarRotaComoConcluida(rotaAtual);
            rotaAtual = company.atribuirProximaRotaParaVeiculo(this); // Solicita a próxima rota à Company
            if (rotaAtual == null) {

                break;
            }
            /*
             * System.out.println("Rota para Executar    " +
             * company.getRotasParaExecutar().size());
             * System.out.println("Rota em Execucao     " +
             * company.getRotasEmExecucao().size());
             * System.out.println("Rota Executada       " +
             * company.getRotasExecutadas().size());
             */
        }

        // Desregistra o veículo quando não há mais rotas
        // System.out.println(
        // "Rota em Execucao " +
        // company.getRotasEmExecucao().get(company.getRotasEmExecucao().size()));
        company.desregistrarVeiculo(this);
        company.interrupt();
    }

    public Route getRotaAtual() {
        return rotaAtual;
    }

    public String getNome() {
        return nome;
    }

    public void simularExecucaoDaRota(Route rota) {
        // Implemente a lógica para simular a execução da rota aqui
        // Tempo de início da rota após receber a rota (5 segundos)
        try {
            Thread.sleep(50); // Aguarda 5 segundos (5000 milissegundos)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Random random = new Random();
        // Simula a execução da rota com base no tempo de "depart" da rota
        double tempoDeExecucao = rota.getDepartTime();// random.nextInt(551) + 5000;

        /*
         * try {
         * Thread.sleep((long) (tempoDeExecucao)); // Converte segundos para
         * // milissegundos
         * } catch (InterruptedException e) {
         * e.printStackTrace();
         * }
         */

    }
}
