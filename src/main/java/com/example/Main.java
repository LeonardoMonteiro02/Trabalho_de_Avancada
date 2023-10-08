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
        // Vehicle veiculo3 = new Vehicle("Veiculo3", empresa);

        empresa.registrarVeiculo(veiculo1);
        empresa.registrarVeiculo(veiculo2);
        // System.out.println("Veiculo registrado como : " + veiculo2.getNome());
        // empresa.registrarVeiculo(veiculo3);
        /*
         * System.out.println(
         * "Rota para executar "
         * + empresa.getRotasParaExecutar().size());
         */

        veiculo1.start();
        empresa.start();
        veiculo2.run();

        // veiculo3.start();

        // Inicie a thread da empresa
        // empresa.start();

        // Aguarde até que todas as threads terminem
        try {
            // veiculo1.join();
            veiculo2.join();
            // veiculo3.join();
            empresa.join();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        // Após a conclusão, você pode imprimir informações sobre as rotas e veículos,
        // se necessário.

    }
}
