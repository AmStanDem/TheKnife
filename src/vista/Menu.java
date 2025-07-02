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

    /**
     * Metodo astratto che mostra il menu
     */
    public abstract void mostra();
}
