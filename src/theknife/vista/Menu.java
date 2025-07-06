package theknife.vista;

/*
 * Riotto Thomas 760981 VA
 * Pesavento Antonio 759933 VA
 * Tullo Alessandro 760760 VA
 * Zaro Marco 760194 VA
 */
/**
 * Classe astratta di un Menu
 * @author Thomas Riotto
 */
public abstract class Menu {

    /**
     * Metodo statico protetto che stampa un banner decorativo all'avvio del menu.
     */
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
