package core;

import base.Readarg;

import javax.swing.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Pcc_Generique<E extends Comparable<E>> extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine;
    protected int origine;

    protected int zoneDestination;
    protected int destination;


    //liste de tous les Label_Dijkstras
    protected ArrayList<E> lab;
    //Le tas
    protected BinaryHeap<E> tas;
    //Label_Dijkstra destinataire
    protected E dest;

    //Afficher ou non le deroulement de l'algo
    protected int choixAffichage;

    //en temps (choix=1),  en distance (choix=0)
    protected int choix;

    //fait correspondre un noeud a un Label_Dijkstra
    protected HashMap<Node, E> mapLabel;
    //duree d'execution
    protected long duree;
    //Nombre maximum d'elemnt dans le tas
    protected int maxTas;
    //Nombre d'element explores
    protected int nb_elements_tas;
    //contient le resultat a enregister dans un fichier
    protected String sortieAlgo;
    protected boolean test;

    public Pcc_Generique(Graphe gr, PrintStream sortie, Readarg readarg, int choixCout, int affichageDeroulementAlgo, boolean test, int origine, int dest) {
        super(gr, sortie, readarg);
        this.choix = choixCout;
        this.choixAffichage = affichageDeroulementAlgo;

        mapLabel = new HashMap<Node, E>();
        this.zoneOrigine = gr.getZone();
        this.origine = origine;
        this.zoneDestination = gr.getZone();
        this.destination = dest;

        if ((origine <= 0) || (origine > graphe.getArrayList().size())) {
            System.out.println(" Le numero de sommet saisi : " + origine + " n'appartient pas au graphe");
            return;
        }
        if ((destination <= 0) || (destination > graphe.getArrayList().size())) {
            System.out.println(" Le numero de sommet saisi : " + origine + " n'appartient pas au graphe");
            return;
        }
}

    public Pcc_Generique(Graphe gr, PrintStream sortie, Readarg readarg, int choixCout, int affichageDeroulementAlgo) {
        super(gr, sortie, readarg);
        this.choix = choixCout;
        this.choixAffichage = affichageDeroulementAlgo;
        this.test = false;
        mapLabel = new HashMap<Node, E>();
        // a voir si on demande la zone ou le sommet directement
        try {
            this.zoneOrigine = gr.getZone();
            this.origine = Integer.parseInt(JOptionPane.showInputDialog(null, "Numero du sommet d'origine ?"));
            // this.origine = readarg.lireInt("Numero du sommet d'origine ? ");

            // Demander la zone et le sommet destination.
            this.zoneOrigine = gr.getZone();
            // this.destination = readarg.lireInt("Numero du sommet destination ? ");
            this.destination = Integer.parseInt(JOptionPane.showInputDialog(null, "Numero du sommet de destination ?"));
            // Si le numero des noeuds n'est pas dans le graphe on doit arreter l'algo
            if ((origine <= 0) || (origine > graphe.getArrayList().size())) {
                System.out.println(" Le numero de sommet saisi : " + origine + " n'appartient pas au graphe");
                return;
            }
            if ((destination <= 0) || (destination > graphe.getArrayList().size())) {
                System.out.println(" Le numero de sommet saisi : " + origine + " n'appartient pas au graphe");
                return;
            }

        } catch (NumberFormatException n) {
            System.out.println("Erreur du type " + n);
        }

        }

    /**
     * Initialisation de l'algo de Dijikstra
     */
    public void initialisation() {
        // implementees dans les sous classes
    }

    public ArrayList run() {
        if ((origine <= 0) || (origine > graphe.getArrayList().size()) || (destination <= 0) || (destination > graphe.getArrayList().size())
                || this.origine == -1 || this.destination == -1) {
            JOptionPane.showMessageDialog(null, "Un des sommets n'appartient pas au graphe.");
            return null;
        }
        System.out.println();
        System.out.println("Lancement de l'algorithme de (zone,noeud) : (" + zoneOrigine + "," + origine + ") vers (" + zoneDestination + "," + destination + ")");
// Initialisation de nos champs
        this.lab = new ArrayList<E>();
        this.tas = new BinaryHeap<E>();
        // Nombre max des elements et ceux explores
        this.maxTas = tas.size();
        nb_elements_tas = 1;
        double new_cout = 0;
        // afin de mesurer le temps d'execution on mettra une duree
        this.duree = System.currentTimeMillis();

        // Il faut Initialiser l'algo ou pas
        initialisation();
		
		/*Algorithme (a ameliorer)
		 * On part du noeud d'origine
		 * On parcourt tous ses successeurs
		 * Si ils sont pas marque alors on met a jour leur valeur du cout : valeur du cout du noeud + cout de l'arc
		 * Si ils sont deja marque, alors teste si cette valeur est inferieure a la valeur qu'a deja ce noeud
		 * 			si < alors update sinon rien
		 * 
		 * 
		 * A reflechir : est il possible qu'un noeud deja traite soit modifie et qu'il faille modifier les cout
		 * de tous les noeuds qui utilise sur leur chemin ce noeud ?
		 */

		/* Boucle principale*/
        E min, E_succ;
        Node node_suc;
        while (!(this.tas.isEmpty() || ((Label) dest).isMarque())) {
            min = this.tas.deleteMin();
            ((Label) min).setMarque(true);
            // pour chaque successeurs / arc
            for (Arc arc : this.graphe.getArrayList().get(((Label) min).getNum_node()).getArrayListArc()) {
                node_suc = this.graphe.getArrayList().get(arc.getNum_dest());
                // Label_Dijkstra correspondant au noeud destinataire
                E_succ = mapLabel.get(node_suc);
                // si le noeud n'est pas marque
                if (!(((Label) E_succ).isMarque())) {
                    // on met alors le cout a jour
                    // TODO : verifier temps !
                    new_cout = (choix == 0) ? arc.getLg_arete() + ((Label) min).getCout() : 60.0f * ((float) arc.getLg_arete()) / (1000.0f * (float) arc.getDescripteur().getVitMax()) + ((Label) min).getCout();
                    // on verifie alors que ce cout est bien inferieur au precedent
                    if (new_cout < ((Label) E_succ).getCout()) {
                        ((Label) E_succ).setCout(new_cout);
                        ((Label) E_succ).setPere(((Label) min).getNum_node());
                    }
                    // maintenant si le sommet n'est pas dans le tas il faut l'ajouter
                    if (!(this.tas.getMap().get(E_succ) != null)) {
                        // on insere le sommet dans le tas
                        this.tas.insert(E_succ);
                        nb_elements_tas++;
                        // On peut afficher le sommet sur la carte
                        if (choixAffichage == 1) {
                            //graphe.getDessin().setColor(Color.magenta);
                            this.graphe.getDessin().drawPoint(node_suc.getLongitude(), node_suc.getLatitude(), 3);
                        }
                    }
                    // sinon il ne faut pas oublier de mettre a jour le tas !
                    else {
                        this.tas.update(E_succ);
                    }
                }
            }
            // on met a jours la valeur max du tas
            if (maxTas < tas.size()) {
                maxTas = tas.size();
            }
        }
        // pour terminer on affichera le temps de calcul

        duree = (System.currentTimeMillis() - duree);
        System.out.println("Duree= " + duree + " ms");
        //Afficher le resultat du calcul - ou rediriger sur fichier
        chemin();
        ArrayList resultat = new ArrayList();

        if (choix == 0) {
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "\n\nLe cout est de " + ((Label) dest).getCout() / 1000 + "km\n" +
                    "Temps de Calcul: " + duree + " ms\n" +
                    "Nb max d'element: " + maxTas + "\n" +
                    "Nb elements explores: " + nb_elements_tas);
            sortieAlgo += "Le cout est de " + ((Label) dest).getCout() / 1000 + "km\n";
            resultat.add(((Label) dest).getCout() / 1000);
            resultat.add(duree);
            resultat.add(maxTas);
            resultat.add(nb_elements_tas);

        } else {
            //TODO : passer en minutes et heures etcs
            JOptionPane.showMessageDialog(null, this.getClass().getName() + "\n\nLe cout est de " + ((Label) dest).getCout() + "min\n" +
                    "Temps de Calcul: " + duree + " ms\n" +
                    "Nb max d'element: " + maxTas + "\n" +
                    "Nb elements explores: " + nb_elements_tas);
            sortieAlgo += "Le cout est de " + ((Label) dest).getCout() + "min\n";
            resultat.add(((Label) dest).getCout());
            resultat.add(duree);
            resultat.add(maxTas);
            resultat.add(nb_elements_tas);
        }

        //Maj du fichier : ecriture
        sortieAlgo += "Temps de Calcul: " + duree + " ms\n" +
                "Nb max d'element: " + maxTas + "\n" +
                "Nb elements explores: " + nb_elements_tas + "\n\n";
        sortie.append(sortieAlgo);

        return resultat;
    }

    public void chemin() {
        // on construit le chemin du dest->origine
        Chemin chemin = new Chemin(origine, destination);
        chemin.addNode(this.graphe.getArrayList().get(destination));
        E E_en_cours = dest;
        Node node;
        // On remonte avec l'aide du pere !
        // Tant qu'on n'atteint pas le sommet d'origine qui a pour pere -1
        while (((Label) E_en_cours).getPere() != -1) {
            node = this.graphe.getArrayList().get(((Label) E_en_cours).getPere());
            chemin.addNode(node);
            E_en_cours = mapLabel.get(node);
        }
        // cout et affichage du chemin
        Collections.reverse(chemin.getListNode());
        //TODO : traiter la difference suivant le choix ?
        System.out.println();
        System.out.println("Calcul sur le Chemin");
        chemin.cout_chemin_distance();
        chemin.cout_chemin_temps();
        chemin.tracerChemin(this.graphe.getDessin());
    }
}
