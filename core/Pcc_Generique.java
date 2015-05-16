package core;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Pcc_Generique<E extends Comparable<E>> extends Algo {

    protected ArrayList<E> labels;      //liste de tous les Label_Dijkstras
    protected BinaryHeap<E> tas;        //Le tas
    protected HashMap<Node, E> mapLabel;//fait correspondre un noeud a un Label_Dijkstra

    protected int choixCout;            //en temps (choixCout=1),  en distance (choixCout=0)
    protected int maxTas;               //Nombre maximum d'elemnt dans le tas
    protected int nb_elements_tas;      //Nombre d'element explores
    protected boolean TOUS;             //contient le resultat a enregister dans un fichier
    protected boolean pieton;           // Pour le pieton permet de changer sa vitesse etc

    public Pcc_Generique(Graphe gr, int choixCout, boolean affichageDeroulementAlgo, int origine, int dest, boolean TOUS, boolean pieton) {
        super(gr);
        this.choixCout = choixCout;
        this.affichageDeroulementAlgo = affichageDeroulementAlgo;
        this.TOUS = TOUS;
        this.pieton = pieton;

        this.zoneOrigine = gr.getZone();
        this.origine = origine;
        this.zoneDestination = gr.getZone();
        this.destination = dest;
    }

    public ArrayList<E> getLabels() {
        return labels;
    }

    /**
     * Initialisation de l'algo de Dijikstra
     */
    public void initialisation() {
        // implementees dans les sous classes
    }

    public ArrayList run() {

        if ((origine <= 0) || (origine > graphe.getArrayList().size())) {
            JOptionPane.showMessageDialog(null, "Le numero de sommet d'origine saisi : " + origine + " n'appartient pas au graphe");
            return null;
        }
        if ((destination <= 0) || (destination > graphe.getArrayList().size())) {
            JOptionPane.showMessageDialog(null, "Le numero de sommet de destination saisi : " + origine + " n'appartient pas au graphe");
            return null;
        }
        // a noter que : si booleen TOUS alors message de confirmation de vers tout le monde

        System.out.println();
        System.out.println("Lancement de l'algorithme de (zone,noeud) : (" + zoneOrigine + "," + origine + ") vers (" + zoneDestination + "," + destination + ")");
// Initialisation de nos champs
        labels = new ArrayList<>();
        tas = new BinaryHeap<>();
        mapLabel = new HashMap();
        maxTas = tas.size();    // Nombre max des elements et ceux explores
        nb_elements_tas = 1;
        double new_cout = 0;
        // afin de mesurer le temps d'execution on mettra une duree
        duree = System.currentTimeMillis();

        // Il faut Initialiser l'algo
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
        E min, succ;
        Node node_suc;
        while (!((this.tas.isEmpty() || dest.isMarque()) && !TOUS) && !(TOUS && this.tas.isEmpty())) {
            min = this.tas.deleteMin();
            ((Label_Generique) min).setMarque(true);
            // pour chaque successeurs / arc
            for (Arc arc : this.graphe.getArrayList().get(((Label_Generique) min).getNum_node()).getArrayListArc()) {
                node_suc = this.graphe.getArrayList().get(arc.getNum_dest());
                // Label_Dijkstra correspondant au noeud destinataire
                succ = mapLabel.get(node_suc);
                // si le noeud n'est pas marque
                if (!(((Label_Generique) succ).isMarque())) {
                    // on met alors le cout a jour
                    if (!pieton) {
                        new_cout = (choixCout == 0) ? arc.getLg_arete() + ((Label_Generique) min).getCout() : 60.0f * ((float) arc.getLg_arete()) / (1000.0f * (float) arc.getDescripteur().getVitMax()) + ((Label_Generique) min).getCout();
                    } else {
                        // TODO : verifier si 4f (4 en float) c'est bien en km/h etc ..
                        // on vérifie que la route n'est pas une autoroute : dans le descripteur on a char type == a
                        if (arc.getDescripteur().getType() != 'a') {
                            new_cout = (choixCout == 0) ? arc.getLg_arete() + ((Label_Generique) min).getCout() : 60.0f * ((float) arc.getLg_arete()) / (1000.0f * 4f) + ((Label_Generique) min).getCout();
                        } else {
                            continue;
                        }
                    }
                    // on verifie alors que ce cout est bien inferieur au precedent
                    if (new_cout < ((Label_Generique) succ).getCout()) {
                        ((Label_Generique) succ).setCout(new_cout);
                        ((Label_Generique) succ).setPere(((Label_Generique) min).getNum_node());
                    }
                    // maintenant si le sommet n'est pas dans le tas il faut l'ajouter
                    if (this.tas.getMap().get(succ) == null) {
                        // on insere le sommet dans le tas
                        this.tas.insert(succ);
                        nb_elements_tas++;
                        // On peut afficher le sommet sur la carte
                        if (affichageDeroulementAlgo) {
                            //graphe.getDessin().setColor(Color.magenta);
                            this.graphe.getDessin().drawPoint(node_suc.getLongitude(), node_suc.getLatitude(), 3);
                        }
                    }
                    // sinon il ne faut pas oublier de mettre a jour le tas !
                    else {
                        this.tas.update(succ);
                    }
                }
            }
            // on met a jours la valeur max du tas
            if (maxTas < tas.size()) {
                maxTas = tas.size();
            }
        }

        connexes = dest.isMarque();
        // Tracer le chemin si les 2 points sont connexes
        if (connexes)
            chemin();

        // Mise à jour du résultat pour affichage et fichier de sortie
        ArrayList resultat = new ArrayList();
        if(connexes)
            resultat.add("Les points sont connexes");
        else
            resultat.add("Les points ne sont pas connexes");
        resultat.add(duree);
        if (choixCout == 0)
            resultat.add(dest.getCout() / 1000);
        else
            resultat.add(dest.getCout());
        resultat.add(maxTas);
        resultat.add(nb_elements_tas);

        return resultat;
    }

    public void chemin() {
        // on construit le chemin du dest->origine
        Chemin chemin = new Chemin(origine, destination);
        chemin.addNode(this.graphe.getArrayList().get(destination));
        E E_en_cours = (E) dest;
        Node node;
        // On remonte avec l'aide du pere !
        // Tant qu'on n'atteint pas le sommet d'origine qui a pour pere -1
        while (((Label_Generique) E_en_cours).getPere() != -1) {
            node = this.graphe.getArrayList().get(((Label_Generique) E_en_cours).getPere());
            chemin.addNode(node);
            E_en_cours = mapLabel.get(node);
        }
        // cout et affichage du chemin
        Collections.reverse(chemin.getListNode());
        chemin.tracerChemin(this.graphe.getDessin());
    }
}
