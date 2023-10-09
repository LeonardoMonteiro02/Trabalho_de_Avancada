package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

class ContaCorrente extends Thread {
    private String login;
    private String senha;
    private double saldo;
    private Map<Long, Double> registroTransacoes; // Timestamp em nanossegundos para valor
    private ReentrantLock lock; // Lock para acesso seguro ao saldo

    public ContaCorrente(String login, String senha) {
        this.login = login;
        this.senha = senha;
        this.saldo = 0.0;
        this.registroTransacoes = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public boolean verificarCredenciais(String login, String senha) {
        return this.login.equals(login) && this.senha.equals(senha);
    }

    public void realizarTransacao(long timestamp, double valor) {
        lock.lock();
        try {
            saldo += valor;
            registroTransacoes.put(timestamp, valor);
        } finally {
            lock.unlock();
        }
    }

    public double verificarSaldo() {
        lock.lock();
        try {
            return saldo;
        } finally {
            lock.unlock();
        }
    }

    public void debitar(double valor) {
        lock.lock();
        try {
            if (saldo >= valor) {
                saldo -= valor;
            } else {
                System.out.println("Saldo insuficiente para debitar " + valor);
            }
        } finally {
            lock.unlock();
        }
    }

    public void creditar(double valor) {
        lock.lock();
        try {
            saldo += valor;
        } finally {
            lock.unlock();
        }
    }

    public void registrarTransacao(String transacao) {
        lock.lock();
        try {
            // Aqui você pode implementar a lógica para registrar a transação
            // Por exemplo, adicionando a transação a uma lista de registros
            System.out.println("Transação registrada: " + transacao);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        // Implemente o comportamento da thread, se necessário.
    }
}
