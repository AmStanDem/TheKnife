package vista;


/**
 * Classe astratta di un Menu
 * @author Thomas Riotto
 */
public abstract class Menu {

    protected static void stampaBanner() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("\t████████╗██╗  ██╗███████╗    ██╗  ██╗███╗   ██╗██╗███████╗███████╗");
        System.out.println("\t╚══██╔══╝██║  ██║██╔════╝    ██║ ██╔╝████╗  ██║██║██╔════╝██╔════╝");
        System.out.println("\t   ██║   ███████║█████╗      █████╔╝ ██╔██╗ ██║██║█████╗  █████╗  ");
        System.out.println("\t   ██║   ██╔══██║██╔══╝      ██╔═██╗ ██║╚██╗██║██║██╔══╝  ██╔══╝  ");
        System.out.println("\t   ██║   ██║  ██║███████╗    ██║  ██╗██║ ╚████║██║██║     ███████╗");
        System.out.println("\t   ╚═╝   ╚═╝  ╚═╝╚══════╝    ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝╚═╝     ╚══════╝");
        System.out.println();
    }
    public abstract void mostra();
    public static void main(String[] args) {
        stampaBanner();
    }
}
