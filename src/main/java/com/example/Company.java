package com.example;

import java.util.ArrayList;
import java.util.List;

public class Company extends Thread {
    private ArrayList<Route> rotasParaExecutar; // Lista de rotas a serem executadas pela empresa
    private List<Route> rotasEmExecucao = new ArrayList<>(); // Lista de rotas atualmente em execução
    private List<Route> rotasExecutadas = new ArrayList<>(); // Lista de rotas já executadas
    private List<Vehicle> frota = new ArrayList<>(); // Lista de veículos disponíveis para a empresa

    // Construtor da classe Company
    public Company(ArrayList<Route> rotasParaExecutar) {
        this.rotasParaExecutar = rotasParaExecutar;
    }

    // Método para registrar um veículo na frota da empresa
    public synchronized void registrarVeiculo(Vehicle veiculo) {
        frota.add(veiculo);
        if (!rotasParaExecutar.isEmpty()) {
            veiculo.atribuirRota(rotasParaExecutar.remove(0)); // Atribui a primeira rota disponível
            notify(); // Notifica a empresa que um veículo está disponível
        }
    }

    // Método para remover um veículo da frota da empresa
    public synchronized void desregistrarVeiculo(Vehicle veiculo) {
        frota.remove(veiculo);

        if (frota.isEmpty()) {
            notify();
        }
    }

    // Método para marcar uma rota como concluída
    public synchronized void marcarRotaComoConcluida(Route rota) {
        rotasEmExecucao.remove(rota); // Remove da lista de rotas em execução
        rotasExecutadas.add(rota); // Move a rota para a lista de rotas executadas
    }

    // Método para atribuir a próxima rota a um veículo
    public synchronized Route atribuirProximaRotaParaVeiculo(Vehicle veiculo) {
        if (rotasParaExecutar.isEmpty()) {
            return null; // Não há mais rotas disponíveis
        }
        Route rotaAtribuida = rotasParaExecutar.remove(0);
        return rotaAtribuida;
    }

    // Getter para obter a lista de rotas a serem executadas
    public synchronized List<Route> getRotasParaExecutar() {
        return rotasParaExecutar;
    }

    // Getter para obter a lista de rotas em execução
    public synchronized List<Route> getRotasEmExecucao() {
        return rotasEmExecucao;
    }

    // Getter para obter a lista de rotas executadas
    public synchronized List<Route> getRotasExecutadas() {
        return rotasExecutadas;
    }

    // Getter para obter a lista de veículos na frota
    public synchronized List<Vehicle> getfrota() {
        return frota;
    }

    // Método principal da thread da empresa
    @Override
    public void run() {
        while (!rotasParaExecutar.isEmpty()) {
            synchronized (this) {
                while (getfrota().isEmpty()) {
                    try {
                        wait(); // Aguarda até que um veículo esteja disponível
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            Vehicle veiculo = frota.remove(0);

            // Aguarda até que o veículo termine sua rota atual
            synchronized (veiculo) {
                while (veiculo.getRotaAtual() != null) {
                    try {
                        veiculo.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (rotasParaExecutar.isEmpty()) {
                break;
            } else {
                Route rota = rotasParaExecutar.remove(0);
                rotasEmExecucao.add(rota); // Move a rota para a lista de rotas em execução
                veiculo.atribuirRota(rota);
                registrarVeiculo(veiculo);
                System.out.println(veiculo.getNome());
            }
        }
        System.out.println("Thread Company encerrada");
    }
}
