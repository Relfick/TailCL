package CL.Tail;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;
import java.io.*;
import java.util.Scanner;

public class FileTail {
    private boolean needChars = false;
    private StringBuilder text = new StringBuilder();

    @Option(name = "-n", forbids = {"-c"})
    private int takeLines = -1;

    @Option(name = "-c", forbids = {"-n"})
    private int takeChars = -1;

    @Option(name = "-o")
    private String outputFileName = "";

    @Argument
    private String[] arguments;

    private String getTextFromFile(String input) { return (needChars) ? getSymbols(input) : getStrings(input); }

    private String getStrings(String input) {
        StringBuilder sb = new StringBuilder();
        String currLine;
        int linesInFile = countLines(input);
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            for (int i = 0; i < linesInFile - takeLines; i++)
                reader.readLine();

            while ((currLine = reader.readLine()) != null)
                sb.append(currLine).append('\n');

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return sb.deleteCharAt(sb.length() - 1).toString(); // удаляем лишний \n в конце
    }

    private String getSymbols(String input) {
        StringBuilder sb = new StringBuilder();
        long length = new File(input).length();
        int c;

        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {

            int linesInFile = countLines(input);
            length -= linesInFile - 1;     // считаем \r\n за один символ

            for (int i = 0; i < length - takeChars; i++) {
                c = reader.read();
                if (c == 13) i--;   // 10 = '\n', 13 = '\r'
            }

            while ((c = reader.read()) != -1)
                sb.append((char)c);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return sb.toString();
    }

    private int countLines(String input) {
        int lineCounter = 0;
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(input))) {
            while (lnr.readLine() != null) {
                lineCounter++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return lineCounter;
    }

    private void assembleText() throws FileNotFoundException {
        if (arguments.length == 1) text.append(getTextFromConsole());
        else if (arguments.length == 2) {
            text.append(getTextFromFile(arguments[1]));
        }
        else {
            for (int i = 1; i < arguments.length; i++) { // arguments[0] = tail --> инпуты с индекса 1
                if (!new File(arguments[i]).exists()) throw new FileNotFoundException();
                text.append(arguments[i]).append('\n');
                text.append(getTextFromFile(arguments[i])).append('\n');
            }
            text.deleteCharAt(text.length() - 1); // лишний последний \n
        }
    }

    private String getTextFromConsole() {
        StringBuilder sb = new StringBuilder();
        String consoleText = "";
        try (BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
             BufferedWriter bw = new BufferedWriter (new FileWriter("consoleInput.txt")))
        {
            String currLine;
            while(!(currLine = br.readLine()).equals("ESC"))
                sb.append(currLine).append('\r').append('\n');

            int length = sb.length();
            sb.deleteCharAt(length - 1).deleteCharAt(length - 2);
            bw.write(sb.toString());

        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
        consoleText = getTextFromFile("consoleInput.txt");
        new File("consoleInput.txt").deleteOnExit();
        return consoleText;
    }

    private void writeToConsole() {
        System.out.println(text);
    }

    private void writeToFile() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writer.write(text.toString());
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeText() {
        if (outputFileName.equals("")) writeToConsole();
        else writeToFile();
    }

    public void launch() throws IOException {
        if (!arguments[0].equals("tail")) throw new IllegalArgumentException("Not a tail!");

        if (takeChars == -1 && takeLines == -1) takeLines = 10;
        else if (takeChars != -1) needChars = true;

        assembleText();
        writeText();
    }
}
