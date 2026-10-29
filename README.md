![unifor](/imgs/unifor5.png)

UNIVERSIDADE DE FORTALEZA

CENTRO DE CIÊNCIAS TECNOLÓGICAS

CURSO: CIÊNCIA DA COMPUTAÇÃO

# SIMULADOR DE ALGORITMOS DE SUBSTITUIÇÃO DE PÁGINAS EM JAVA

Autor 1: João Victor Rodrigues Barreto

Autor 2: Nicolas Marcelino da Mota

Palavras-chaves: Java. Page. Faults. Algorithms.

# RESUMO

O projeto executa uma
sequência de referências a páginas sobre um número fixo de quadros (molduras) de
memória, conta as faltas de página de cada algoritmo e gera gráficos e arquivos CSV
para comparar os resultados.

# INTRODUÇÃO

Todos os algoritmos estendem `AlgorithmsBase`, que contém o laço de execução comum
(procura a página, conta a falta, usa um quadro livre ou pede uma vítima). Cada
algoritmo implementa apenas `selectVictim` e, quando precisa, os ganchos `onUse`,
`onLoad` e `onSwap`.

| Algoritmo   | Critério para escolher a vítima                                                                 |
|-------------|-------------------------------------------------------------------------------------------------|
| **FIFO**    | Remove a página carregada há mais tempo (fila circular).                                        |
| **LRU**     | Remove a página usada há mais tempo (*Least Recently Used*).                                    |
| **NFU**     | Remove a página com menos acessos (*Not Frequently Used*); em caso de empate, a mais antiga.    |
| **Optimal** | Remove a página que será usada mais tarde no futuro (ou nunca mais). Serve como limite teórico. |

# METODOLOGIA

O simulador foi desenvolvido em Java, com JDK 21, e utiliza o Gradle (via wrapper) para compilação e execução. Para a geração dos gráficos foi empregada a biblioteca XChart 3.8.8. Os gráficos são exibidos em janelas Swing, o que exige um ambiente com interface gráfica.

## Algoritmos implementados

Foram implementados quatro algoritmos, que diferem apenas no critério de escolha da vítima:
- FIFO: remove a página carregada há mais tempo, utilizando uma fila circular.
- LRU (Least Recently Used): remove a página usada há mais tempo.
- NFU (Not Frequently Used): remove a página com menor número de acessos; em caso de empate, remove a mais antiga.
- Ótimo: remove a página que será usada mais tarde no futuro, ou que nunca mais será usada. Por depender do conhecimento prévio da sequência, é irrealizável na prática e serve como limite teórico para os demais.

## Arquitetura

Todos os algoritmos estendem a classe `AlgorithmsBase`, que contém o laço de execução comum: procura a página na memória, contabiliza a falta quando ela não é encontrada, utiliza um quadro livre se houver ou escolhe uma vítima. Cada algoritmo implementa apenas os métodos `selectVictim`, `onUse`, `onLoad` e `onSwap`.

O projeto é organizado em dois pacotes principais. O pacote `algorithms` contém a classe base, as quatro implementações (FIFO, LRU, NFU e Optimal) e a classe `ExecutionTest`, que reúne os testes. O pacote `app` contém as aplicações de geração de resultados: `AccFaultsByAlgorithmCsv`, que produz um gráfico de barras com o total de faltas por algoritmo; `AccFaultsByReference`, que produz um gráfico de linhas com as faltas acumuladas a cada referência; e `CsvExport`, que exporta os dados em CSV com separador ";". Os arquivos gerados são gravados no diretório `src/data`.

# RESULTADOS E DISCUSSÕES

A bateria de testes foi concluída com 48 verificações aprovadas e nenhuma falha, o que indica que as implementações se comportam conforme o esperado nos cenários cobertos, inclusive nos casos de borda.

No experimento com a sequência clássica e três quadros, obteve-se o total de faltas apresentado na Tabela 1.

**Total de faltas de página por algoritmo (3 quadros, 20 referências)**

| Algoritmo | Faltas de página |
| --- | --- |
| FIFO | 15 |
| NFU | 13 |
| LRU | 12 |
| Ótimo | 9 |

O algoritmo Ótimo obteve o menor número de faltas, como esperado, já que decide com conhecimento do futuro e representa o limite inferior para qualquer algoritmo. O LRU apresentou desempenho intermediário, pois aproveita a localidade temporal das referências ao priorizar a manutenção de páginas usadas recentemente. O FIFO obteve o pior resultado entre os três, uma vez que seu critério considera apenas o tempo de permanência na memória e pode remover páginas ainda muito utilizadas. O NFU, por sua vez, baseia-se na frequência de acesso e, por isso, tende a manter páginas que foram muito usadas no passado mesmo quando deixam de ser úteis.

O gráfico de linhas com as faltas acumuladas por referência permite observar em que momentos as curvas dos algoritmos se afastam. Nos passos iniciais todos os algoritmos coincidem, pois as primeiras faltas ocorrem enquanto os quadros ainda estão livres; a divergência surge a partir do momento em que a memória fica cheia e passa a ser necessário escolher uma vítima. Já o gráfico de barras permite comparar diretamente o total final de cada algoritmo.

| Faltas acumuladas por referência | Faltas por algoritmo |
| ---------------------- | ----------------------- |
| ![li](/imgs/image1.png) | ![ri](/imgs/image2.png) |

# CONCLUSÕES

O trabalho resultou em um simulador em Java que implementa e compara os algoritmos de substituição de páginas FIFO, LRU, NFU e Ótimo, com arquitetura baseada em uma classe base comum que facilita a inclusão de novos algoritmos. A validação automatizada, composta por 48 verificações, apoiou a confiança nos resultados, e a geração de gráficos e arquivos CSV facilitou a análise comparativa.
Os resultados confirmaram que o algoritmo Ótimo é o limite teórico de desempenho e que o LRU supera o FIFO na sequência analisada. 

# REFERÊNCIAS

TANENBAUM, A. S.; BOS, H. Sistemas operacionais modernos. 4. ed. São Paulo: Pearson, 2015.