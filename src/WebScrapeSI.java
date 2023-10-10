import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Scanner;

public class WebScrapeSI {

    private static boolean contemPalavra(String frase, String palavra) {
        String[] palavras = frase.split("\\s+");
        for (String palavraNaFrase : palavras) {
            if (removeAcentos(palavraNaFrase).equalsIgnoreCase(removeAcentos(palavra))) {
                return true;
            }
        }
        return false;
    }

    private static String removeAcentos(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase(Locale.getDefault());
    }

    private static String extrairNome(Element h3Element) {
        // Extrair o nome
        String nome = h3Element.select("span:has(color)").text();
        if (nome.isEmpty()) {
            // Se o nome estiver vazio, tentar outra abordagem
            nome = h3Element.select("span").first().text();
        }
        return nome;
    }

    private static String extrairEmail(Element h3Element) {
        // Extrair o e-mail
        String email = "";
        Element emailElement = h3Element.nextElementSibling().select("a[href^=mailto]").first();
        if (emailElement != null) {
            email = emailElement.text();
        }
        return email;
    }

    private static String extrairLinhaDePesquisa(Element element) {
        // Encontrar todos os elementos div com estilo "margin-left: 120px;"
        Elements divElements = element.select("div[style=margin-left: 120px;]");

        for (Element divElement : divElements) {
            // Verificar se o texto contém "Linha de pesquisa"
            String divText = divElement.text();
            if (divText.contains("Linha de pesquisa:")) {
                // Extrair a linha de pesquisa após o texto
                int startIndex = divText.indexOf("Linha de pesquisa:") + "Linha de pesquisa:".length();
                return divText.substring(startIndex).trim();
            }
        }

        // Se nada for encontrado, retornar uma string vazia
        return "";
    }

    public static void main(String[] args) {
        final String url = "https://decsi.ufop.br/docentes";

        try {
            Document document = Jsoup.connect(url).get();
            int size = 0;
            boolean aux = true;

            // Encontrar todos os elementos h3 com uma imagem dentro
            Elements h3Elements = document.select("h3:has(img)");

            // Lista de professores
            ProfessorSI[] professoresSI;

            for (Element h3Element : h3Elements) {
                if (!extrairNome(h3Element).isEmpty())
                    size++;
            }

            professoresSI = new ProfessorSI[size];

            for (int i = 0; i < size; i++) {
                Element h3Element = h3Elements.get(i);
                String nome = extrairNome(h3Element);
                String email = extrairEmail(h3Element);
                String area = extrairLinhaDePesquisa(h3Element.nextElementSibling());

                professoresSI[i] = new ProfessorSI(nome, email, area);
            }

            // IMPRIMIR TODOS PROFESSORES DE SI
//            for (ProfessorSI professor : professoresSI) {
//                    System.out.println("Nome: " + professor.getNome());
//                    System.out.println("Email: " + professor.getEmail());
//                    System.out.println("Área: " + professor.getArea());
//                    System.out.println();
//            }

            while (aux) {
                int cont = 0;
                Scanner scanner = new Scanner(System.in);
                System.out.print("Digite a palavra a ser procurada: ");
                String palavra = scanner.next();

                // Iterar sobre a lista de professores
                for (ProfessorSI professor : professoresSI) {
                    if (contemPalavra(professor.getArea(), palavra.toUpperCase())) {
                        System.out.println("Nome: " + professor.getNome());
                        System.out.println("Email: " + professor.getEmail());
                        System.out.println("Área: " + professor.getArea());
                        System.out.println();
                        cont ++;
                    }
                }
                System.out.println("Um total de " + cont + " professores!");
                System.out.println("\nDesejar continuar? (y/n)");
                palavra = scanner.next();

                if (palavra.toUpperCase().equals("N") ) {
                    aux = false;
                    System.out.println("\nFechando o programa!");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
