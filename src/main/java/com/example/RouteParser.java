package com.example;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouteParser {
    private ArrayList<Route> routes = new ArrayList<>();

    public RouteParser(String xmlFilePath) {
        // Inicializa um RouteParser com o caminho do arquivo XML
        // Isso permite que o usuário forneça o caminho do arquivo ao criar um objeto
        // RouteParser
        parseRoutesFromXML(xmlFilePath);
    }

    public void parseRoutesFromXML(String xmlFilePath) {
        try {
            // Carrega o arquivo XML
            File file = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            // Obtém a lista de elementos "vehicle" no XML
            NodeList vehicleNodes = document.getElementsByTagName("vehicle");

            // Loop para processar cada elemento "vehicle" encontrado no XML
            for (int i = 0; i < vehicleNodes.getLength(); i++) {
                Element vehicleElement = (Element) vehicleNodes.item(i);
                String id = vehicleElement.getAttribute("id");
                double departTime = Double.parseDouble(vehicleElement.getAttribute("depart"));

                // Obtém o elemento "route" dentro de "vehicle"
                Element routeElement = (Element) vehicleElement.getElementsByTagName("route").item(0);
                String edges = routeElement.getAttribute("edges");

                // Divide a string de arestas em uma lista de arestas
                ArrayList<String> edgeList = new ArrayList<>(List.of(edges.split(" ")));

                // Cria um objeto Route com as informações e adiciona à lista de rotas
                Route route = new Route(id, edgeList, departTime);
                routes.add(route);
            }
        } catch (Exception e) {
            // Em caso de exceção, imprime o erro
            e.printStackTrace();
        }
    }

    public ArrayList<Route> getRoutes() {
        // Retorna a lista de rotas analisadas
        return routes;
    }
}

class Route {
    private String id; // Identificador da rota
    private ArrayList<String> edges; // Lista de pontos/edges da rota
    private double departTime; // Tempo de partida do veículo

    public Route(String id, ArrayList<String> edges, double departTime) {
        // Construtor da classe Route que inicializa os campos com os valores fornecidos
        this.id = id;
        this.edges = edges;
        this.departTime = departTime;
    }

    public String getId() {
        // Retorna o identificador da rota
        return id;
    }

    public ArrayList<String> getEdges() {
        // Retorna a lista de pontos/edges da rota
        return edges;
    }

    public double getDepartTime() {
        // Retorna o tempo de partida do veículo
        return departTime;
    }

    public double getDistanciaPercorrida() {
        Random random = new Random();
        return random.nextInt(100) + 300;
    }

    @Override
    public String toString() {
        // Sobrescreve o método toString para fornecer uma representação de string dos
        // campos da rota
        return "Route{" +
                "id='" + id + '\'' +
                ", edges=" + edges +
                ", departTime=" + departTime +
                '}';
    }
}
