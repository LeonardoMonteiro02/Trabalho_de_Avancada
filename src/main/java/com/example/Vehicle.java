package com.example;

import java.util.Random;

import org.python.modules.synchronize;

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
        long startTime = System.currentTimeMillis();
        while (true) {
            // System.out.println("Iniciando simulação do veiculo: " + getNome());
            synchronized (this) {
                while (rotaAtual == null) {
                    try {
                        notifyAll();
                        wait(); // Aguarda até que uma rota seja atribuída
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
            }

            simularExecucaoDaRota(rotaAtual);
            company.marcarRotaComoConcluida(rotaAtual);
            rotaAtual = company.atribuirProximaRotaParaVeiculo(this); // Solicita a próxima rota à Company
            if (rotaAtual == null) {
                break;

            }

        }

        company.desregistrarVeiculo(this);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Thread veiculo ecerrada : " + getNome() + "  "
                + " Tempo de execução: " + executionTime + "  milisegundos");
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
        // System.out.println(" Veiculo em execução: " + getNome() + " " +
        // rota.getId());
        try {
            Thread.sleep(50); // Aguarda 5 segundos (5000 milissegundos)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Random random = new Random();
        // Simula a execução da rota com base no tempo de "depart" da rota
        double tempoDeExecucao = random.nextInt(100) + 2000; // rota.getDepartTime();// random.nextInt(551) + 5000;

        /*
         * try {
         * Thread.sleep((long) (tempoDeExecucao)); // Converte segundos para
         * // milissegundos
         * } catch (InterruptedException e) {
         * e.printStackTrace();
         * }
         */
        /*
         * System.out.println(" Veiculo em execução: " + getNome() + " " +
         * rotaAtual.getId() + "  Tempo de execução " + (50 + (long) tempoDeExecucao));
         */
    }
}