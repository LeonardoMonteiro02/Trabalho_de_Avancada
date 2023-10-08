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
        // System.out.println("Iniciando simulação do veiculo: " + getNome() + " e da
        // sua rota: ");
        while (true) {
            synchronized (this) {
                while (rotaAtual == null) {
                    try {

                        wait(); // Aguarda até que uma rota seja atribuída
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
            }

            System.out.println(getNome() + " esta executando a rota: " + rotaAtual.getId());
            simularExecucaoDaRota(rotaAtual);
            company.marcarRotaComoConcluida(rotaAtual);
            rotaAtual = company.atribuirProximaRotaParaVeiculo(this); // Solicita a próxima rota à Company
            if (rotaAtual == null) {

                break;
            }

        }
        /*
         * System.out.println(
         * "Veiculo executado   "
         * + getNome());
         * // Desregistra o veículo quando não há mais rotas
         * System.out.println(
         * "Rota para executar"
         * + company.getRotasParaExecutar().size());
         * System.out.println(
         * "Rota em Execucao "
         * + company.getRotasEmExecucao().size());
         * 
         * System.out.println(
         * "Rota Excutadas"
         * + company.getRotasExecutadas().size());
         * System.out.println(
         * "Rota Excutadas"
         * + company.getRotasExecutadas().get(0).getId() +
         * company.getRotasExecutadas().get(1).getId()
         * + company.getRotasExecutadas().get(2).getId());
         */

        company.desregistrarVeiculo(this);
        if (company.getVeiculos().size() == 0) {
            // company.interrupt();
            System.out.println("");
            System.out.println("========================================");
            System.out.println("Relatorio de simulação:");
            System.out.println("=======================");
            System.out.println("");
            System.out.println("Lista de Veiculos: " + company.getVeiculos().size());
            System.out.println("Rota para executar:  " + company.getRotasParaExecutar().size());
            System.out.println("Rota em execucao:  " + company.getRotasEmExecucao().size());
            System.out.println("Rota executada:  " + company.getRotasExecutadas().size());
            System.out.println("");
            System.out.println("========================================");
            System.out.println("SIMULAÇÂO TERMINDADA");
            System.out.println("========================================");
            company.interrupt();

        }
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
