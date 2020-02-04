import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.ArrayList;

public class Client {

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Il faut 3 arguments : l'adresse ip du serveur, le port et le nom d'équipe.");
			System.exit(0);
		}
		Random rand = new Random();

		try {
			Socket s = new Socket(args[0], Integer.parseInt(args[1]));
			boolean fin = false;

			// ecriture
			OutputStream os = s.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			// lecture
			InputStream is = s.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));

			pw.println(args[2]);
			pw.flush();

			String numJoueur = bf.readLine();

			System.out.println("Numero de joueur : " + numJoueur);

			while (!fin) {
				String msg = bf.readLine();

				System.out.println("Message recu : " + msg);
				System.out.println();
				fin = msg.equals("FIN");

				String arriveFrom = "";

				if (!fin) {

					/*-----------------------------------------------------------------------*/

					/* TODO - mettre votre stratégie en place ici */
					/* Quelques lignes de code pour vous aider */

					// Creation du labyrinthe en fonction des informations recues
					// Bande de veinards, c'est déjà écrit ! Par contre la doc de cette classe n'est
					// pas complète.
					// Faut pas trop en demander non plus !
					Labyrinthe laby = new Labyrinthe(msg);

					// Informations sur le joueur
					System.out.println("Je me trouve en : (" + laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()
							+ "," + laby.getJoueur(Integer.parseInt(numJoueur)).getPosY() + ")");
					ArrayList<Integer> infosMoule = new ArrayList<Integer>();
					// Parcours du plateau pour trouver toutes les moules et leur valeur
					for (int j = 0; j < laby.getTailleY(); j++)
						for (int i = 0; i < laby.getTailleX(); i++)
							if (laby.getXY(i, j).getType() == Case.MOULE) {
								infosMoule.add(i);
								infosMoule.add(j);
								infosMoule.add(laby.getXY(i, j).getPointRapporte());
							}

					// Affichage des informations sur les moules du plateau
					for (int i = 0; i < infosMoule.size() / 3; i++)
						System.out.println("Moule en (" + infosMoule.get(i * 3) + "," + infosMoule.get(i * 3 + 1)
								+ ") pour " + infosMoule.get(i * 3 + 2) + " points");

					// Je prépare le message suivant à envoyer au serveur : je vais me déplacer vers
					// l'Est.
					// Pourquoi ? Aucune idée mais faut bien envoyer quelque chose au serveur alors
					// pourquoi pas ?
					// A vous de faire mieux ici :-)

					// Creation de la matrice du labyrinthe
					Path path = new Path();
					int[][] mat = new int[200][200];
					for (int i = 0; i < laby.getTailleY(); i++) {
						for (int j = 0; j < laby.getTailleX(); j++) {
							if (laby.getXY(j, i).getType() == Case.SABLE || laby.getXY(j, i).getType() == Case.MOULE ||
							laby.getXY(j, i).getType() == Case.FRITE || laby.getXY(j, i).getType() == Case.BIERE) {
								mat[i][j] = 1;
							} else {
								mat[i][j] = 0;
							}
						}
					}

					int xPlayer = laby.getJoueur(Integer.parseInt(numJoueur)).getPosX();
					int yPlayer = laby.getJoueur(Integer.parseInt(numJoueur)).getPosY();

					int minDistance = 100;

					if (laby.getXY(xPlayer + 1, yPlayer).getType() != Case.DUNE) {
						msg = "E";
					} else if (laby.getXY(xPlayer, yPlayer + 1).getType() != Case.DUNE) {
						msg = "S";
					} else if (laby.getXY(xPlayer - 1, yPlayer).getType() != Case.DUNE) {
						msg = "O";
					} else if (laby.getXY(xPlayer, yPlayer - 1).getType() != Case.DUNE) {
						msg = "N";
					}

					for (int i = 0; i < infosMoule.size() / 3; i++) {

						int xMoule = infosMoule.get(i * 3);
						int yMoule = infosMoule.get(i * 3 + 1);
						int pointMoule = infosMoule.get(i * 3 + 2);

						System.out.println("Moula: " + xMoule + " " + yMoule);
						System.out.println("mat moule " + mat[yMoule][xMoule]);

						Path.Point source = new Path.Point(yPlayer + 1, xPlayer);
						Path.Point dest = new Path.Point(yMoule, xMoule);
						int dist = path.ShortestPath(mat, source, dest);
						if (dist != -1 && dist < minDistance) {
							minDistance = dist;
							msg = "S";
						}
						source = new Path.Point(yPlayer - 1, xPlayer);
						dest = new Path.Point(yMoule, xMoule);
						dist = path.ShortestPath(mat, source, dest);
						if (dist != -1 && dist < minDistance) {
							minDistance = dist;
							msg = "N";
						}
						source = new Path.Point(yPlayer, xPlayer + 1);
						dest = new Path.Point(yMoule, xMoule);
						dist = path.ShortestPath(mat, source, dest);
						if (dist != -1 && dist < minDistance) {
							minDistance = dist;
							msg = "E";
						}
						source = new Path.Point(yPlayer, xPlayer - 1);
						dest = new Path.Point(yMoule, xMoule);
						dist = path.ShortestPath(mat, source, dest);
						if (dist != -1 && dist < minDistance) {
							minDistance = dist;
							msg = "O";
						}

						System.out.println(minDistance);

					}
					/*-----------------------------------------------------------------------*/

					// Envoi du message au serveur.
					pw.println(msg);
					pw.flush();
				}

			}
			s.close();

		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

}
