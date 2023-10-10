import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Scanner;


public class WebScrapeENP {
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

    public static void main(String[] args) {
        final String url = "https://deenp.ufop.br/corpo-docente";

        try {
            final Document document = Jsoup.connect(url).get();
            Elements rows = document.select("table tr");
            int size = 0;
            boolean aux = true;

            // Lista de professores
            ProfessorENP[] professoresENP;

            for (Element row : rows) {
                if (row.select("td:nth-child(1)").text().equals(""))
                    continue;
                else
                    size++;
            }

            // Inicialização da lista de professores
            professoresENP = new ProfessorENP[size];

            for (int i = 0; i <= size - 1; i++) {
                Element row = rows.get(i);
                String nome = row.select("td:nth-child(1)").text();
                String area = row.select("td:nth-child(4)").text();
                String ramal = row.select("td:nth-child(5)").text();
                String sala = row.select("td:nth-child(6)").text();
                String email = row.select("td:nth-child(7)").text();

                // Criar instância de Professor
                professoresENP[i] = new ProfessorENP(nome, email, area, ramal, sala);
            }
            // IMPRIMIR TODOS PROFESSORES DE ENP
//            for (ProfessorENP professor : professoresENP) {
//                System.out.println("Nome: " + professor.getNome());
//                System.out.println("Email: " + professor.getEmail());
//                System.out.println("Área: " + professor.getArea());
//                System.out.println("Ramal: " + professor.getRamal());
//                System.out.println("Sala: " + professor.getSala());
//                System.out.println();
//            }

            while (aux) {
                int cont =0;
                Scanner scanner = new Scanner(System.in);
                System.out.print("Digite a palavra a ser procurada: ");
                String palavra = scanner.next();

                // Iterar sobre a lista de professores
                for (ProfessorENP professor : professoresENP) {
                    if (contemPalavra(professor.getArea(), palavra.toUpperCase())) {
                        System.out.println("Nome: " + professor.getNome());
                        System.out.println("Área: " + professor.getArea());
                        System.out.println("Ramal: " + professor.getRamal());
                        System.out.println("Sala: " + professor.getSala());
                        System.out.println("Email: " + professor.getEmail());
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
