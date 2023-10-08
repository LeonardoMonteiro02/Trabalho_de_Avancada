package com.example;

import java.util.Random;

// Importações de pacotes necessários
import org.python.modules.synchronize;

public class Vehicle extends Thread {
    private String nome; // Nome do veículo
    private Company company; // Referência para a empresa a que o veículo pertence
    private Route rotaAtual; // Rota atual atribuída ao veículo

    public Vehicle(String nome, Company company) {
        this.nome = nome;
        this.company = company;
    }

    // Método para atribuir uma nova rota ao veículo
    public synchronized void atribuirRota(Route rota) {
        rotaAtual = rota;
        notify(); // Notifica a thread de veículo que uma nova rota está atribuída
    }

    @Override
    public void run() {
        while (true) {
            // Espera até que uma rota seja atribuída ao veículo
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

            // Exibe informações sobre a execução da rota atual
            System.out.println("Veiculo em execução: " + getNome() + " " + rotaAtual.getId());

            // Simula a execução da rota atual
            simularExecucaoDaRota(rotaAtual);

            // Marca a rota como concluída na empresa
            company.marcarRotaComoConcluida(rotaAtual);

            // Solicita a próxima rota à Company
            rotaAtual = company.atribuirProximaRotaParaVeiculo(this);

            // Verifica se não há mais rotas a serem executadas e encerra a thread do
            // veículo
            if (rotaAtual == null) {
                break;
            }
        }

        // Remove o veículo da lista de veículos da empresa
        company.desregistrarVeiculo(this);

        // Exibe informações sobre a thread do veículo encerrada
        System.out.println("Thread veiculo encerrada: " + getNome() + " " +
                company.getRotasExecutadas().get(company.getRotasExecutadas().size() - 1).getId());
    }

    // Getter para obter a rota atual do veículo
    public Route getRotaAtual() {
        return rotaAtual;
    }

    // Getter para obter o nome do veículo
    public String getNome() {
        return nome;
    }

    // Método para simular a execução da rota atual
    public void simularExecucaoDaRota(Route rota) {
        // Implemente a lógica para simular a execução da rota aqui

        // Simula um tempo de início da rota após receber a rota (5 segundos)
        try {
            Thread.sleep(50); // Aguarda 5 segundos (5000 milissegundos)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Gera um tempo de execução da rota aleatório com base no tempo de "depart" da
        // rota
        Random random = new Random();
        double tempoDeExecucao = rota.getDepartTime(); // Você pode modificar isso conforme necessário

        /*
         * try {
         * Thread.sleep((long) (tempoDeExecucao)); // Converte segundos para
         * milissegundos
         * } catch (InterruptedException e) {
         * e.printStackTrace();
         * }
         */
    }
}
