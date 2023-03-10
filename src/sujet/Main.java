package sujet;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        // Initialize the program using the options given in argument
        if (args.length == 0) Tools.initialize("-cTl --threads=100 Nantes https://fr.wikipedia.org/wiki/Nantes");
        else Tools.initialize(args);

        System.out.println(Tools.numberThreads() + " threads can run with this configuration.");
        if ( Tools.startingURL().size() >  0 ){
            new WebGrep().start();
        }

    }
}