package com.example;

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
                        System.out.println("aguardando rota");
                        wait(); // Aguarda até que uma rota seja atribuída
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("CAtch rota");
                    }
                }
            }

            System.out.println(nome + " está executando a rota: " + rotaAtual.getId());
            simularExecucaoDaRota(rotaAtual);
            System.out.println("rota simulada");
            company.marcarRotaComoConcluida(rotaAtual);
            rotaAtual = company.atribuirProximaRotaParaVeiculo(this); // Solicita a próxima rota à Company
            System.out.println("Recebi nova rota");
            if (rotaAtual == null) {
                // Se não houver mais rotas, saia do loop
                break;
            }
        }

        // Desregistra o veículo quando não há mais rotas
        company.desregistrarVeiculo(this);
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
            Thread.sleep(1000); // Aguarda 5 segundos (5000 milissegundos)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
         * // Simula a execução da rota com base no tempo de "depart" da rota
         * double tempoDeExecucao = rota.getDepartTime();
         * try {
         * Thread.sleep((long) (tempoDeExecucao)); // Converte segundos para
         * milissegundos
         * } catch (InterruptedException e) {
         * e.printStackTrace();
         * }
         */
    }
}
