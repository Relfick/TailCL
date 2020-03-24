package CL.Tail;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        FileTail tail = new FileTail();
        CmdLineParser parser = new CmdLineParser(tail);
        try {
            parser.parseArgument(args);
            tail.launch();
        } catch (CmdLineException | IOException e) {
            e.printStackTrace();
        }
    }
}

