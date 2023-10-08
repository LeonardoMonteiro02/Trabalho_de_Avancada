package com.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Substitua o caminho do arquivo XML pelo caminho do seu arquivo XML
        RouteParser routeParser = new RouteParser("C:\\Users\\Leonardo Monteiro\\Desktop\\teste\\map\\map.rou.xml");
        ArrayList<Route> rotasParaExecutar = routeParser.getRoutes();
        System.out.println("rotas para executar: " + rotasParaExecutar.size());

        // Crie uma instância da empresa
        Company empresa = new Company(rotasParaExecutar);

        // Criar e registrar 100 veículos
        for (int i = 1; i <= 100; i++) {
            String nomeVeiculo = "Veiculo" + i;
            Vehicle veiculo = new Vehicle(nomeVeiculo, empresa);
            empresa.registrarVeiculo(veiculo);
        }

        // Iniciar as threads dos veículos
        for (Vehicle veiculo : empresa.getfrota()) {
            veiculo.start();
        }

        empresa.start();

        try {

            empresa.join();

        } catch (Exception e) {
            e.printStackTrace();

        }

        // Após a conclusão, você pode imprimir informações sobre as rotas e veículos,
        // se necessário.
        System.out.println("");
        System.out.println("========================================");
        System.out.println("Relatorio de simulação:");
        System.out.println("=======================");
        System.out.println("");
        System.out.println("Lista de Veiculos: " + empresa.getfrota().size());
        System.out.println("Rota para executar:  " + empresa.getRotasParaExecutar().size());
        System.out.println("Rota em execucao:  " + empresa.getRotasEmExecucao().size());
        System.out.println("Rota executada:  " + empresa.getRotasExecutadas().size());
        System.out.println("");
        System.out.println("========================================");
        System.out.println("SIMULAÇÂO TERMINDADA");
        System.out.println("========================================");

    }
}