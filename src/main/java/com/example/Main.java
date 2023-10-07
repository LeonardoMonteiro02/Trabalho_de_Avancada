package com.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Substitua o caminho do arquivo XML pelo caminho do seu arquivo XML
        RouteParser routeParser = new RouteParser("C:\\Users\\Leonardo Monteiro\\Desktop\\teste\\map\\map.rou.xml");
        ArrayList<Route> rotasParaExecutar = routeParser.getRoutes();

        // Crie uma instância da empresa
        Company empresa = new Company(rotasParaExecutar);

        // Crie e inicie várias instâncias de veículo
        Vehicle veiculo1 = new Vehicle("Veiculo1", empresa);
        Vehicle veiculo2 = new Vehicle("Veiculo2", empresa);

        Route rota0 = rotasParaExecutar.get(0); // Atribuir a rota 0 ao Veiculo 1
        Route rota1 = rotasParaExecutar.get(1);

        empresa.registrarVeiculo(veiculo1, rota0);
        empresa.registrarVeiculo(veiculo2, rota1);

        veiculo1.start();
        veiculo2.start();

        // Inicie a thread da empresa
        empresa.start();
        System.out.println("passei 1");
        // Aguarde até que todas as threads terminem
        try {
            veiculo1.join();
            veiculo2.join();
            empresa.join();
            System.out.println("passei 2");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("passei 3");
        }

        // Após a conclusão, você pode imprimir informações sobre as rotas e veículos,
        // se necessário.
        // System.out.println("Rotas Executadas: " + empresa.getRotasExecutadas());
        // System.out.println("Rotas Pendentes: " + empresa.getRotasParaExecutar());
        System.out.println("Rotas em Execução: " + empresa.getRotasEmExecucao());
        System.out.println("Veículos Disponíveis: " + empresa.getVeiculos().get(1).getNome());
    }
}
