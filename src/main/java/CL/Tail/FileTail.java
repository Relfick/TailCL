package CL.Tail;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;
import org.apache.commons.io.input.ReversedLinesFileReader;


import java.io.*;
import java.util.Scanner;

public class FileTail {
    private boolean needChars = false;
    private StringBuilder text = new StringBuilder();

    @Option(name = "-n")
    private int numStrings = -1;

    @Option(name = "-c")
    private int numChars = -1;

    @Option(name = "-o")
    private String outputFileName = "o";

    @Argument
    private String[] arguments;

    public void doMain(String[] args) {
//        CmdLineParser parser = new CmdLineParser(this);
//        //for (String arg : args) System.out.println(arg);
//
//        try {
//            parser.parseArgument(args);
//
//        } catch (CmdLineException e) {
//            System.err.println(e.getMessage());
//            System.err.println("java Main [options...] arguments...");
//            parser.printUsage(System.err);
//            System.err.println();
//
//            return;
//        }
//
//        System.out.println();
//
//        System.out.println("-n was " + takeStrings);
//        System.out.println("tail is " + isTail);
    }

    private String getText (String input) {
        return (needChars) ? getSymbols(input) : getStrings(input);
    }

    private String getStrings (String input) {
        return "";
    }

    private String getSymbols (String input) {
        StringBuilder sb = new StringBuilder();
        long length = new File(input).length() - 1;

        int c;
        int lineBreakCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {

            for (long i = 0; i < length - numChars; i++)
                if ((c = (char)reader.read()) == '\n') lineBreakCount++;

            while ((c = reader.read()) != -1) {
                sb.append((char) c);
                if (c == '\n') lineBreakCount++;
            }
            if (lineBreakCount > 0)
                sb.delete(0, lineBreakCount);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return sb.toString();
    }

    public void launch() throws IOException {
        if (!arguments[0].equals("tail")) throw new IllegalArgumentException("Not a tail!");
        if (numChars != -1 && numStrings != -1) throw new IllegalArgumentException("Chars or strings, not both!");

        if (numChars == -1 && numStrings == -1) {
            numStrings = 10;
            // вызвать функцию работы со строками
        }


        if (numChars != -1) needChars = true;
        if (arguments.length > 2) {
            for (int i = 1; i < arguments.length; i++) {
                text.append(arguments[i]).append('\n');
                text.append(getText(arguments[i])).append('\n');
            }
            text.deleteCharAt(text.length() - 1); // excess last \n
        }
        else {
                text.append(getText(arguments[1]));
        }


        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName)))
        {
            writer.write(text.toString());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

//        for (int i = 0; i < arguments.length; i++)
//            System.out.println(arguments[i]);
        //File f = new File(arguments[indexOfInputs]);
        //System.out.println(f.length());
//        System.out.println("out is " + out);
//        System.out.println("n is " + numStrings);
//        System.out.println("c is " + numChars);
    }
}
