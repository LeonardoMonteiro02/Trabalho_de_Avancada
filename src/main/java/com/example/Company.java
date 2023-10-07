package com.example;

import java.util.ArrayList;
import java.util.List;

public class Company extends Thread {
    private ArrayList<Route> rotasParaExecutar;
    private List<Route> rotasEmExecucao = new ArrayList<>();
    private List<Route> rotasExecutadas = new ArrayList<>();
    private List<Vehicle> veiculos = new ArrayList<>();

    public Company(ArrayList<Route> rotasParaExecutar) {
        this.rotasParaExecutar = rotasParaExecutar;
    }

    public synchronized void registrarVeiculo(Vehicle veiculo, Route rota) {
        veiculos.add(veiculo);
        veiculo.atribuirRota(rota);
        notifyAll(); // Notifica todas as threads de veículo que um veículo foi registrado com uma
                     // rota inicial
    }

    public synchronized void desregistrarVeiculo(Vehicle veiculo) {
        veiculos.remove(veiculo);
    }

    public synchronized void marcarRotaComoConcluida(Route rota) {
        rotasEmExecucao.remove(rota); // Remove da lista de rotas em execução
        rotasExecutadas.add(rota); // Move a rota para a lista de rotas executadas
        System.out.println(rotasExecutadas.size());
    }

    public synchronized Route atribuirProximaRotaParaVeiculo(Vehicle veiculo) {
        System.out.println("Entrei com o veiculo:" + rotasParaExecutar.size());
        while (rotasParaExecutar.isEmpty() && veiculo.getRotaAtual() != null) {
            System.out.println("Entrei na condicão" + (!rotasParaExecutar.isEmpty() && veiculo.getRotaAtual() != null));
            try {
                wait(); // Aguarda até que o veículo termine sua rota atual
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (rotasParaExecutar.isEmpty()) {
            return null; // Não há mais rotas disponíveis
        }

        Route rotaAtribuida = rotasParaExecutar.remove(0);
        return rotaAtribuida;
    }

    public synchronized List<Route> getRotasParaExecutar() {
        return rotasParaExecutar;
    }

    public synchronized List<Route> getRotasEmExecucao() {
        return rotasEmExecucao;
    }

    public synchronized List<Route> getRotasExecutadas() {
        return rotasExecutadas;
    }

    public synchronized List<Vehicle> getVeiculos() {
        return veiculos;
    }

    @Override
    public synchronized void run() {

        while (!rotasParaExecutar.isEmpty()) {
            System.out.println("Estou entrando na Run");
            while (veiculos.isEmpty()) {
                try {
                    System.out.println("aguardando veiculo");
                    wait(); // Aguarda até que um veículo esteja disponível
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("Catch veiculo");
                }
            }

            Vehicle veiculo = veiculos.remove(0);

            // Aguarda até que o veículo termine sua rota atual
            while (veiculo.getRotaAtual() != null) {
                try {
                    System.out.println("Aguarda até que o veículo termine sua rota atual");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Route rota = rotasParaExecutar.remove(0);
            rotasEmExecucao.add(rota); // Move a rota para a lista de rotas em execução
            veiculo.atribuirRota(rota);
            registrarVeiculo(veiculo, rota);
        }
    }
}
