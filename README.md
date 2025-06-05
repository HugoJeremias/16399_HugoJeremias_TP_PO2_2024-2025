Neste trabalho prático, pretende-se ficar com um programa que permita jogar
uma versão simplificada do jogo A Good Snowman Is Hard To Build, um jogo de
puzzle desenvolvido por Alan Hazelden, lançado em 2015. As regras principais
são as seguintes:
Manipulação de bolas de neve O jogador controla um monstro preto descarate-
rizado cujo objetivo é construir bonecos de neve. Para isso, faz rolar bolas
de neve de diferentes tamanhos num ambiente com neve;
Movimento O monstro empurra bolas de neve num ambiente em grelha (tile-
based). Fazer rolar uma bola de neve pequena sobre uma posição com neve
transforma-a numa bola de neve média. Fazer rolar uma bola de neve mé-
dia sobre a neve transforma-a numa bola de neve grande. As bolas de neve
grandes continuam a ser grandes, independentemente da quantidade de
neve sobre a qual rolam.
Construção do boneco de neve Um boneco de neve completo requer três bolas
de neve empilhadas por ordem decrescente de tamanho: grande em baixo,
média no meio e pequena em cima.
Conceção do puzzle Os puzzles giram em torno de descobrir a sequência cor-
reta de movimentos para obter as bolas de neve com os tamanhos certos
e nas posições certas para serem empilhadas. Isto implica planear cuida-
dosamente os movimentos para evitar tornar uma bola de neve demasiado
grande prematuramente ou deixá-la ficar presa.
Desfazer e reiniciar O jogo é tolerante, oferecendo undo (desfazer jogada) e tam-
bém a possibilidade de reiniciar para recomeçar o puzzle atual. Tal incen-
tiva a experimentação e a aprendizagem com os erros.
Estes são alguns links para conhecer melhor o jogo:
• https://www.youtube.com/watch?v=uWJIkfiKmQk
• https://agoodsnowman.com/
• https://en.wikipedia.org/wiki/A_Good_Snowman_Is_Hard_to_Build).
O jogo que se pretende implementar deve ter as seguintes características:
1. O jogo decorre numa grelha em que cada uma das posições é um PositionContent
com um dos seguintes valores:
• NO_SNOW (chão livre sem neve);
• SNOW (chão livre com neve);
• BLOCK (zona intransponível);
• SNOWMAN (uma bola pequena em cima de uma bola média que está em
cima de uma bola grande, ou seja três bolas empilhadas);
2. O tabuleiro de jogo deve ser indicado pelo jogador e devem existir pelo me-
nos dois tabuleiros de jogo (dois níveis), cada um com (1) as suas posições
com um dos valores listados no número anterior, (2) a posição das bolas de
neve e (3) a posição do monstro;
3. Há três tamanhos de bola de neve mais a possibilidade de empilhamento
de uma bola mais pequena em cima de uma bola maior;
4. O monstro tem de empurrar bolas de neve para fazer um boneco de neve
com três bolas de neve empilhadas, com a maior em baixo, a média no meio
e a mais pequena no topo;
5. Empurrar uma bola de neve para cima de uma posição com neve cria uma
bola de neve maior, mas uma bola de neve grande nunca fica maior mesmo
que passe por uma zona de neve;
6. Não é possível empurrar uma bola de neve para cima de uma bola de neve
menor nem para cima de uma posição intransponível;
7. Pode empurrar uma bola pequena que esteja em cima de uma bola média
ou grande; a bola pequena ficará na posição seguinte;
8. Pode empurrar uma bola média que esteja em cima de uma bola grande; a
bola média ficará na posição seguinte;
9. Se empurrar uma bola pequena para cima de uma bola média que está já
em cima de uma bola grande fará um boneco de neve e terminará o nível de
jogo;
10. As bolas de neve devem ser objetos da classe Snowball que sabem em que
linha e coluna estão no tabuleiro e também qual o seu tamanho;
11. As bolas de neve devem ser objetos da classe Snowball que sabem em que
linha e coluna estão no tabuleiro e também qual o seu tipo; o empilhamento
de bolas de neve também pode ser visto como um tipo de bola de neve;
12. O tipo de bola de neve pode ser indicado por um tipo enumerado com os va-
lores BIG, AVERAGE, SMALL, BIG-AVERAGE BIG-SMALL e AVERAGE-SMALL; mas tam-
bém pode utilizar uma classe para cada um destes tipos;
13. O monstro deve ser um objeto da classe Monster que sabe em que linha e
coluna está no tabuleiro.
Para fazer a sua versão do jogo tem obrigatoriamente de basear o seu código
no template JavaFX-PO2-2023-2024 que tem sido utilizado nas aulas, respeitar a
separação ui e model e a forma de comunicação entre as packages model e ui atra-
vés da interface View. Caso não respeite a estrutura do template será avaliado
com zero valores neste trabalho prático.
Mesmo assim, note que ainda sobra muito espaço para a sua criatividade!
Seguem-se os requisitos para o trabalho. Antes de escrever o código, leia-os aten-
tamente mais do que uma vez. Antes de entregar o trabalho, leia-os, pelo menos,
mais uma vez.
