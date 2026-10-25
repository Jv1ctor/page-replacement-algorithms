# Page Replacement

Simulador de algoritmos de substituição de páginas em Java. O projeto executa uma
sequência de referências a páginas sobre um número fixo de quadros (molduras) de
memória, conta as faltas de página de cada algoritmo e gera gráficos e arquivos CSV
para comparar os resultados.

## Algoritmos implementados

| Algoritmo   | Critério para escolher a vítima                                                                 |
|-------------|-------------------------------------------------------------------------------------------------|
| **FIFO**    | Remove a página carregada há mais tempo (fila circular).                                        |
| **LRU**     | Remove a página usada há mais tempo (*Least Recently Used*).                                    |
| **NFU**     | Remove a página com menos acessos (*Not Frequently Used*); em caso de empate, a mais antiga.    |
| **Optimal** | Remove a página que será usada mais tarde no futuro (ou nunca mais). Serve como limite teórico. |

Todos os algoritmos estendem `AlgorithmsBase`, que contém o laço de execução comum
(procura a página, conta a falta, usa um quadro livre ou pede uma vítima). Cada
algoritmo implementa apenas `selectVictim` e, quando precisa, os ganchos `onUse`,
`onLoad` e `onSwap`.

## Estrutura

```
src/
├── data/                                   # CSVs gerados pelas aplicações
│   ├── acc-faults-by-algorithms
│   └── acc-faults-by-references
└── main/java/com/example/page_replacement/
    ├── algorithms/
    │   ├── AlgorithmsBase.java             # classe base com o laço de simulação
    │   ├── ExecutionTest.java              # bateria de testes (main padrão)
    │   └── implementation/
    │       ├── FIFO.java
    │       ├── LRU.java
    │       ├── NFU.java
    │       └── Optimal.java
    └── app/
        ├── AccFaultsByAlgorithmCsv.java    # gráfico de barras: total de faltas por algoritmo
        ├── AccFaultsByReference.java       # gráfico de linhas: faltas acumuladas a cada referência
        └── CsvExport.java                  # exporta os dados em CSV (separador ";")
```

## Requisitos

- **JDK 21**
- Não é preciso instalar o Gradle: o projeto usa o wrapper (`./gradlew`).
- Para os gráficos, um ambiente com interface gráfica (eles abrem em uma janela Swing).

Dependência externa: [XChart 3.8.8](https://knowm.org/open-source/xchart/), baixada
automaticamente pelo Gradle.

## Como executar

### Testes

A classe principal configurada no `build.gradle` é a `ExecutionTest`, que roda 48
verificações (12 cenários × 4 algoritmos), incluindo a anomalia de Belady, casos de
borda com 1 quadro e memória pré-carregada:

```bash
./gradlew run
```

Saída esperada (final):

```
[PASS] Teste 12 LRU: 2 faults

[result] 48 passed, 0 failed
```

Se algum teste falhar, o processo termina com código `1`.

No Windows, use `gradlew.bat run`.

### Gráficos e CSVs

As classes em `app/` abrem um gráfico em uma janela e gravam um CSV em `src/data/`.
Ambas usam a sequência clássica `7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1` com 3 quadros.

| Classe                    | Gráfico                                    | CSV gerado                          |
|---------------------------|--------------------------------------------|-------------------------------------|
| `AccFaultsByAlgorithmCsv` | Barras com o total de faltas por algoritmo | `src/data/acc-faults-by-algorithms` |
| `AccFaultsByReference`    | Linhas com as faltas acumuladas por passo  | `src/data/acc-faults-by-references` |

**Pelo terminal:** cada gráfico tem sua própria tarefa no Gradle:

```bash
# Barras: total de faltas por algoritmo
./gradlew plotByAlgorithm

# Linhas: faltas acumuladas a cada referência
./gradlew plotByReference
```

No Windows, use `gradlew.bat plotByAlgorithm` e `gradlew.bat plotByReference`.

O comando abre a janela do gráfico, grava o CSV e mostra `Arquivo CSV gerado com
sucesso!`. Ele só termina quando a janela é fechada. Para ver todas as tarefas
disponíveis, rode `./gradlew tasks --group application`.

**Pela IDE:** abra o projeto como projeto Gradle (IntelliJ, VS Code, Eclipse) e
execute o `main` da classe desejada. Rode a partir da raiz do projeto, pois o CSV é
gravado no caminho relativo `src/data`.

Para mudar a sequência de referências ou o número de quadros, edite as variáveis
`chain` e `lengthFrame` no início do `main` de cada classe.

## Usando os algoritmos no código

```java
int[] chain = {1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5};

AlgorithmsBase fifo = new FIFO(3);           // 3 quadros vazios
fifo.onLogger();                             // opcional: imprime cada passo
fifo.execute(chain);

fifo.getFaults();                            // total de faltas (9)
fifo.getFaultsHistory();                     // faltas acumuladas após cada referência

AlgorithmsBase lru = new LRU(3, new int[]{1, 2}); // memória pré-carregada; o quadro restante fica livre
```

Cada instância guarda estado, então crie um objeto novo para cada execução.

## Formato dos CSVs

Os arquivos usam `;` como separador e não têm extensão.

`acc-faults-by-algorithms`:

```
algoritmo;faults
FIFO;15
LRU;12
...
```

`acc-faults-by-references`: uma linha por passo, com a página referenciada e as faltas
acumuladas de cada algoritmo.

```
passo;referencia;FIFO;Optimal;LRU;NFU
1;7;1;1;1;1
...
```
