package core;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Pcc_Generique<E extends Comparable<E>> extends Algo {

    //Structures de données utilisées
    protected ArrayList<E> labels;       //liste de tous les Label_Dijkstras
    protected BinaryHeap<E> tas;         //Le tas
    protected HashMap<Node, E> mapLabel; //fait correspondre un noeud a un Label_Dijkstra


    protected int choixCout;             //en temps (choixCout=1),  en distance (choixCout=0)
    protected int maxTas;                //Nombre maximum d'elemnt dans le tas
    protected int nb_elements_tas;       //Nombre d'element explores
    protected boolean TOUS;              //contient le resultat a enregister dans un fichier
    protected boolean pieton;            // Pour le pieton permet de changer sa vitesse etc
    protected boolean afficherChemin;
    protected double tempsAttenteMaxPieton; // Temps maximum d'attente du piéton pour covoiturage


    public Pcc_Generique(Graphe gr, int origine, int dest, int choixCout, boolean TOUS, boolean pieton, double tempsAttenteMaxPieton, boolean affichageDeroulementAlgo, boolean afficherChemin) {
        super(gr);

        //Liés aux points origine et destination
        this.zoneOrigine = gr.getZone();
        this.origine = origine;
        this.zoneDestination = gr.getZone();
        this.destination = dest;

        //Liés au type de cout choisi : temps ou distance
        this.choixCout = choixCout;

        //Liés au covoiturage
        this.TOUS = TOUS;
        this.pieton = pieton;
        this.tempsAttenteMaxPieton = tempsAttenteMaxPieton;

        //Liés à l'affichage
        this.affichageDeroulementAlgo = affichageDeroulementAlgo;
        this.afficherChemin = afficherChemin;
    }

    public HashMap<Node, E> getMapLabel() {
        return mapLabel;
    }

    public void setMapLabel(HashMap mapLabel) {
        this.mapLabel = mapLabel;
    }

    public int getNb_elements_tas() {
        return nb_elements_tas;
    }

    public void setNb_elements_tas(int nb_elements_tas) {
        this.nb_elements_tas = nb_elements_tas;
    }

    @Override
    public double getCoutMinTemps() {
        return dest.getCout();
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
        }
        else if ((destination <= 0) || (destination > graphe.getArrayList().size())) {
            JOptionPane.showMessageDialog(null, "Le numero de sommet de destination saisi : " + origine + " n'appartient pas au graphe");
        }
        // a noter que : si booleen TOUS alors message de confirmation de vers tout le monde
        else {

            //System.out.println();
           // System.out.println("Lancement de l'algorithme " + this.getClass().getName() + " de (zone,noeud) : (" + zoneOrigine + "," + origine + ") vers (" + zoneDestination + "," + destination + ")");
// TODO : soit construire le graphe inverse, soit on dit qu'on peux pas passer par les routes en sens unique
            // Initialisation de nos champs
            labels = new ArrayList<>();
            tas = new BinaryHeap<>();
            mapLabel = new HashMap();
            maxTas = tas.size();    // Nombre max des elements et ceux explores
            nb_elements_tas = 1;
            double new_cout;

            // Mesurer le temps d'execution de l'algorithme
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
            Node node_succ;

            while (!((tas.isEmpty() || dest.isMarque()) && !TOUS) && !(TOUS && tas.isEmpty())) {

                // On récupère l'élément minimum du tas que l'on marque comme visité
                min = tas.deleteMin();
                ((Label_Generique) min).setMarque(true);

                // Pour chaque successeur / arc
                for (Arc arc : graphe.getArrayList().get(((Label_Generique) min).getNum_node()).getArrayListArc()) {

                    // On récupère le noeud successeur de l'arc en cours
                    node_succ = graphe.getArrayList().get(arc.getNum_dest());

                    // Label correspondant au noeud destinataire
                    succ = mapLabel.get(node_succ);

                    // Si le sommet n'est pas marque
                    if (!(((Label_Generique) succ).isMarque())) {

                        // On met alors le cout a jour : si pieton -> vérifier type de route
                        if (!pieton) {
                            new_cout = (choixCout == 0) ? arc.getLg_arete() + ((Label_Generique) min).getCout() : 60.0f * ((float) arc.getLg_arete()) / (1000.0f * (float) arc.getDescripteur().getVitMax()) + ((Label_Generique) min).getCout();
                        }
                        else {
                            // On vérifie que la route n'est pas une autoroute : dans le descripteur on a char type == a
                            if (arc.getDescripteur().getType() != 'a') {
                                new_cout = (choixCout == 0) ? arc.getLg_arete() + ((Label_Generique) min).getCout() : 60.0f * ((float) arc.getLg_arete()) / (1000.0f * 4f) + ((Label_Generique) min).getCout();
                            }
                            // Sinon on continu
                            else {
                                continue;
                            }
                        }

                        // On verifie si ce cout est inferieur au precedent
                        if (new_cout < ((Label_Generique) succ).getCout()) {
                            // Si oui, on met à jour les valeurs des couts
                            ((Label_Generique) succ).setCout(new_cout);
                            ((Label_Generique) succ).setPere(((Label_Generique) min).getNum_node());
                        }

                        // On insere le sommet dans le tas s'il n'y est pas
                        if (tas.getMap().get(succ) == null) {

                            tas.insert(succ);
                            nb_elements_tas++;

                            // On peut afficher le sommet sur la carte
                            if (affichageDeroulementAlgo) {
                                graphe.getDessin().drawPoint(node_succ.getLongitude(), node_succ.getLatitude(), 3);
                            }
                        }

                        // Sinon il ne faut pas oublier de mettre a jour le tas en fonction du nouveau cout
                        else {
                            tas.update(succ);
                        }
                    }
                }
                // On met a jours la valeur max du tas
                if (maxTas < tas.size()) {
                    maxTas = tas.size();
                }
            }

            // Origine et Destination sont-ils connexes ?
            connexes = dest.isMarque();

            // Tracer le chemin si les 2 points sont connexes
            if (connexes)
                if (afficherChemin)
                    chemin();

            // On enregistre le temps d'execution de l'algorithme
            duree = (System.currentTimeMillis() - duree);

            // Mise à jour du résultat pour affichage et fichier de sortie
            ArrayList<String> resultat = new ArrayList<>();
            if(connexes)    resultat.add("connexes");
            else            resultat.add("non connexes");
            resultat.add(AffichageTempsCalcul(duree));
            if (choixCout == 0) resultat.add(String.valueOf(dest.getCout() / 1000) + "km");
            else                resultat.add(AffichageTempsHeureMin(dest.getCout()));
            resultat.add(String.valueOf(maxTas));
            resultat.add(String.valueOf(nb_elements_tas));

            return resultat;
        }
        return null;
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
