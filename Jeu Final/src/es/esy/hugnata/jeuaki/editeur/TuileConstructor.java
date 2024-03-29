package es.esy.hugnata.jeuaki.editeur;

import java.io.File;
import java.io.Serializable;


public class TuileConstructor implements Serializable {
    public String nom;
    public File visible, hitbox;
    public boolean constructible;
    public String type;
    public char link;
    private boolean h;
    private boolean d;
    private boolean g;
    private boolean b;


    public TuileConstructor(String nom, String type, File visible, File hitbox, boolean h, boolean b, boolean d, boolean g) {

        this.nom = nom;
        this.type = type;
        this.h = h;
        this.d = d;
        this.b = b;
        this.g = g;

        if (h || d || b || g) {
            int i = visible.getName().lastIndexOf('.');
            if (TesterImage(visible)) {
                this.visible = visible;
                this.constructible = true;
            } else {
                this.constructible = false;
                return;
            }
            if (TesterImage(hitbox)) {
                this.hitbox = hitbox;
                this.constructible = true;
            } else {
                this.constructible = false;
                return;
            }
        } else {
            System.err.println("Aucune entrée existante  ! Impossible de créer la tuile");
        }

    }

    private boolean TesterImage(File file) {
        if (!file.exists()) {
            System.err.println(file.getName() + " n'existe pas !");
            return false;
        }
        if (!file.isFile()) {
            System.err.println(file.getName() + " est un dossier !");
            return false;
        }
        int i = file.getName().lastIndexOf('.');
        if (!file.getName().substring(i + 1).equalsIgnoreCase("png")) {
            System.err.println(file.getName() + " n'est pas un png !");
            return false;
        }
        return true;

    }

    public boolean isH() {
        return h;
    }

    public void setH(boolean h) {
        this.h = h;
    }

    public boolean isD() {
        return d;
    }

    public void setD(boolean d) {
        this.d = d;
    }

    public boolean isG() {
        return g;
    }

    public void setG(boolean g) {
        this.g = g;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public TuileConstructor Clone() {
        return new TuileConstructor(nom, type, visible, hitbox, h, b, d, g);
    }

    public char getLink() {
        return link;
    }

    public void setLink(char link) {
        this.link = link;
    }


}
