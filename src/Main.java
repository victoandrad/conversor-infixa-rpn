import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

public class Main {

    public static void main(String[] argumentosLinhaComando) {
        String[] expressoesDeTeste = {
            "3 + 4 * 2",
            "(1 + 2) * (3 + 4)",
            "10.5 - 2.5 / 2",
            "2 * (3.5 + 1.5) - 4",
            "((2 + 3) * 4 - 6) / 2.8"
        };

        for (String expressaoInfixa : expressoesDeTeste) {
            List<String> tokensRpn = converterInfixaParaRpn(expressaoInfixa);
        }
    }

    /**
     * Dados esperados (exemplo):
     * {
     *     "expressaoInfixa": "3 + 4 * 2"
     * }
     *
     * Retorna a lista de tokens, ex.: ["3", "+", "4", "*", "2"].
     */
    private static List<String> separarEmTokens(String expressaoInfixa) {
        List<String> tokensEncontrados = new ArrayList<>();
        int indiceAtual = 0;

        while (indiceAtual < expressaoInfixa.length()) {
            char caractereAtual = expressaoInfixa.charAt(indiceAtual);

            if (Character.isWhitespace(caractereAtual)) {
                indiceAtual++;
                continue;
            }

            if (Character.isDigit(caractereAtual) || caractereAtual == '.') {
                StringBuilder numeroEmConstrucao = new StringBuilder();
                while (indiceAtual < expressaoInfixa.length()
                        && (Character.isDigit(expressaoInfixa.charAt(indiceAtual))
                            || expressaoInfixa.charAt(indiceAtual) == '.')) {
                    numeroEmConstrucao.append(expressaoInfixa.charAt(indiceAtual));
                    indiceAtual++;
                }
                tokensEncontrados.add(numeroEmConstrucao.toString());
                continue;
            }

            tokensEncontrados.add(String.valueOf(caractereAtual));
            indiceAtual++;
        }

        return tokensEncontrados;
    }

    private static boolean tokenEhOperador(String token) {
        return token.equals("+")
            || token.equals("-")
            || token.equals("*")
            || token.equals("/");
    }

    private static int obterPrecedenciaDoOperador(String operador) {
        if (operador.equals("*") || operador.equals("/")) {
            return 2;
        }
        return 1;
    }

    /**
     * Dados esperados (exemplo):
     * {
     *     "expressaoInfixa": "(1 + 2) * (3 + 4)"
     * }
     *
     * Retorna a lista de tokens em RPN, ex.: ["1", "2", "+", "3", "4", "+", "*"].
     */
    private static List<String> converterInfixaParaRpn(String expressaoInfixa) {
        List<String> tokensEmNotacaoRpn = new ArrayList<>();
        Deque<String> pilhaDeOperadores = new ArrayDeque<>();

        for (String token : separarEmTokens(expressaoInfixa)) {
            if (tokenEhOperador(token)) {
                while (!pilhaDeOperadores.isEmpty()
                        && tokenEhOperador(pilhaDeOperadores.peek())
                        && obterPrecedenciaDoOperador(pilhaDeOperadores.peek())
                            >= obterPrecedenciaDoOperador(token)) {
                    tokensEmNotacaoRpn.add(pilhaDeOperadores.pop());
                }
                pilhaDeOperadores.push(token);
            } else if (token.equals("(")) {
                pilhaDeOperadores.push(token);
            } else if (token.equals(")")) {
                while (!pilhaDeOperadores.isEmpty()
                        && !pilhaDeOperadores.peek().equals("(")) {
                    tokensEmNotacaoRpn.add(pilhaDeOperadores.pop());
                }
                pilhaDeOperadores.pop();
            } else {
                tokensEmNotacaoRpn.add(token);
            }
        }

        while (!pilhaDeOperadores.isEmpty()) {
            tokensEmNotacaoRpn.add(pilhaDeOperadores.pop());
        }

        return tokensEmNotacaoRpn;
    }
}
