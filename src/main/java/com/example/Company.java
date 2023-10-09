package com.example;

import java.util.ArrayList;
import java.util.List;

public class Company extends Thread {
    private ArrayList<Route> rotasParaExecutar;
    private List<Route> rotasEmExecucao = new ArrayList<>();
    private List<Route> rotasExecutadas = new ArrayList<>();
    private List<Vehicle> frota = new ArrayList<>();

    public Company(ArrayList<Route> rotasParaExecutar) {
        this.rotasParaExecutar = rotasParaExecutar;
    }

    public synchronized void registrarVeiculo(Vehicle veiculo) {
        frota.add(veiculo);
        if (!rotasParaExecutar.isEmpty()) {
            veiculo.atribuirRota(rotasParaExecutar.remove(0)); // Atribui a primeira rota disponível
            notify(); // Notifica a empresa que um veículo está disponível
        }
    }

    public synchronized void desregistrarVeiculo(Vehicle veiculo) {
        frota.remove(veiculo);

        if (frota.isEmpty()) {
            notify();
        }

    }

    public synchronized void marcarRotaComoConcluida(Route rota) {
        rotasEmExecucao.remove(rota); // Remove da lista de rotas em execução
        rotasExecutadas.add(rota); // Move a rota para a lista de rotas executadas

    }

    public synchronized Route atribuirProximaRotaParaVeiculo(Vehicle veiculo) {

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

    public synchronized List<Vehicle> getfrota() {
        return frota;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

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
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Thread Company ecerrada  " + " Tempo de execução: " + executionTime + "  milisegundos");

    }
}
