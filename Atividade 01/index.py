import random
import time


class Processo:
    def __init__(self, pid, tempo_total):
        self.pid = pid
        self.tp = 0
        self.tempo_total = tempo_total
        self.cp = self.tp + 1
        self.estado = 'PRONTO'
        self.nes = 0
        self.n_cpu = 0

    def atualizar_cp(self):
        self.cp = self.tp + 1

    def __str__(self):
        return f'PID: {self.pid}, TP: {self.tp}, CP: {self.cp}, Estado: {self.estado}, NES: {self.nes}, N_CPU: {self.n_cpu}'


class SistemaOperacional:
    def __init__(self):
        tempos_execucao = [10000, 5000, 7000, 3000, 3000, 8000, 2000, 5000, 4000, 10000]
        self.processos = [Processo(pid, tempo) for pid, tempo in enumerate(tempos_execucao)]
        self.quantum = 1000

    def executar(self):
        while any(p.tp < p.tempo_total for p in self.processos):
            for processo in self.processos:
                if processo.tp >= processo.tempo_total:
                    continue
                
                processo.estado = 'EXECUTANDO'
                self.executar_processo(processo)

    def executar_processo(self, processo):
        ciclos_executados = 0
        processo.n_cpu += 1
        print(f'\nRestaurando o processo {processo.pid} para EXECUTANDO com quantum completo (1000 ciclos).')

        while ciclos_executados < self.quantum and processo.tp < processo.tempo_total:
            if random.random() <= 0.01:
                processo.estado = 'BLOQUEADO'
                processo.nes += 1
                self.registrar_processo(processo, 'BLOQUEADO')
                return

            processo.tp += 1
            processo.atualizar_cp()
            ciclos_executados += 1

        if processo.tp >= processo.tempo_total:
            processo.estado = 'FINALIZADO'
            print(f'\nProcesso {processo.pid} FINALIZADO:\n{processo}')
            return

        if ciclos_executados == self.quantum:
            processo.estado = 'PRONTO'
            self.registrar_processo(processo, 'PRONTO')

    def registrar_processo(self, processo, novo_estado):
        with open('tabela_processos.txt', 'a') as arquivo:
            arquivo.write(f'Processo {processo.pid} mudou para estado {novo_estado}:\n{processo}\n\n')
        print(f'Processo {processo.pid} {processo.estado} >>> {novo_estado}\n{processo}')

    def desbloquear_processos(self):
        for processo in self.processos:
            if processo.estado == 'BLOQUEADO' and random.random() <= 0.3:
                processo.estado = 'PRONTO'
                print(f'Processo {processo.pid} desbloqueado para PRONTO.')
                self.registrar_processo(processo, 'PRONTO')



sistema = SistemaOperacional()
while any(p.tp < p.tempo_total for p in sistema.processos):
    sistema.desbloquear_processos()
    sistema.executar()
    time.sleep(0.5)
