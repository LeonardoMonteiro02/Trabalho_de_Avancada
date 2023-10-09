package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Queue;
import java.util.LinkedList;

class AlphaBank extends Thread {
    private Map<String, ContaCorrente> contas; // Mapa de contas (login -> ContaCorrente)
    private ReentrantLock lock; // Lock para acesso seguro às contas
    private Map<String, ReentrantLock> locks = new HashMap<>();
    private Queue<Transacao> filaDeTransacoes; // Fila de solicitações de transação

    public AlphaBank() {
        this.contas = new HashMap<>();
        this.lock = new ReentrantLock();
        filaDeTransacoes = new LinkedList<>();
    }

    public void adicionarConta(String login, String senha) {
        lock.lock();
        try {
            contas.put(login, new ContaCorrente(login, senha));
        } finally {
            lock.unlock();
        }
    }

    public void adicionarSolicitacao(Transacao transacao) {
        filaDeTransacoes.add(transacao);
    }

    public boolean autenticar(String login, String senha) {
        if (contas.containsKey(login)) {
            return contas.get(login).verificarCredenciais(login, senha);
        }
        return false;
    }

    public void enviarSolicitacaoDePagamento(String remetente, String destinatario, double valor) {
        lock.lock();
        try {
            if (contas.containsKey(remetente) && contas.containsKey(destinatario)) {
                ContaCorrente contaRemetente = contas.get(remetente);
                ContaCorrente contaDestinatario = contas.get(destinatario);

                ReentrantLock remetenteLock = locks.computeIfAbsent(remetente, k -> new ReentrantLock());
                ReentrantLock destinatarioLock = locks.computeIfAbsent(destinatario, k -> new ReentrantLock());

                remetenteLock.lock();
                destinatarioLock.lock();

                try {
                    // Verifique se o remetente tem saldo suficiente
                    if (contaRemetente.verificarSaldo() >= valor) {
                        // Realize a transação
                        contaRemetente.debitar(valor);
                        contaDestinatario.creditar(valor);

                        // Registre a transação com um timestamp
                        long timestamp = System.nanoTime();
                        String registroTransacao = timestamp + ": " + remetente + " -> " + destinatario + " - Valor: "
                                + valor;
                        contaRemetente.registrarTransacao(registroTransacao);
                        contaDestinatario.registrarTransacao(registroTransacao);
                    } else {
                        System.out.println("Saldo insuficiente em " + remetente + " para realizar a transação.");
                    }
                } finally {
                    remetenteLock.unlock();
                    destinatarioLock.unlock();
                }
            } else {
                System.out.println("Contas especificadas não existem.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void processarTransacao(String remetente, String destinatario, long timestamp, double valor) {
        if (contas.containsKey(remetente) && contas.containsKey(destinatario)) {
            ReentrantLock remetenteLock = locks.computeIfAbsent(remetente, k -> new ReentrantLock());
            ReentrantLock destinatarioLock = locks.computeIfAbsent(destinatario, k -> new ReentrantLock());

            remetenteLock.lock();
            destinatarioLock.lock();

            try {
                // Lógica de transação
                // Certifique-se de atualizar os saldos e registros de transações aqui
            } finally {
                remetenteLock.unlock();
                destinatarioLock.unlock();
            }
        } else {
            System.out.println("Contas especificadas não existem.");
        }
    }

    public double verificarSaldo(String login) {
        if (contas.containsKey(login)) {
            return contas.get(login).verificarSaldo();
        }
        return -1.0; // Conta não encontrada
    }

    @Override
    public void run() {
        // Implemente o comportamento da thread, se necessário.
        while (true) {
            // Verifique a fila de solicitações de transação e processe-as em ordem
            if (!filaDeTransacoes.isEmpty()) {
                Transacao transacao = filaDeTransacoes.poll();
                processarTransacao(transacao.getRemetente(), transacao.getDestinatario(), System.nanoTime(),
                        transacao.getValor());
            }

            // Adicione qualquer outra lógica de processamento necessário aqui

            try {
                Thread.sleep(100); // Espera por um curto período para evitar uso excessivo de CPU
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
