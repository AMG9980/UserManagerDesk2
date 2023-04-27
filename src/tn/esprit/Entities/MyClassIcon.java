/*
 Classe pour faire apparaitre les icons FontAwesome dans la tableView
 */
package tn.esprit.Entities;

import javafx.scene.paint.Color;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Paint;

public class MyClassIcon {
    private final FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
    private final FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
    private final FontAwesomeIconView mailIcon = new FontAwesomeIconView(FontAwesomeIcon.ENVELOPE);
    private final FontAwesomeIconView userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
    public MyClassIcon() {
        deleteIcon.setSize("30px");
        deleteIcon.setFill(Color.RED);
        editIcon.setSize("30px");
        editIcon.setFill(Color.GREEN);
        mailIcon.setSize("30px");
        mailIcon.setFill(Color.BLUE);
        userIcon.setSize("30px");
        userIcon.setFill(Color.RED);
    }

    public FontAwesomeIconView getDeleteIcon() {
        return deleteIcon;
    }

    public FontAwesomeIconView getEditIcon() {
        return editIcon;
    }
    
    public FontAwesomeIconView getMailIcon() {
        return mailIcon;
    }
    
     public FontAwesomeIconView getBanIcon() {
        return userIcon;
    }

    public void setDeleteIconSize(String size) {
        deleteIcon.setSize(size);
    }

    public void setEditIconSize(String size) {
        editIcon.setSize(size);
    }
     public void setMailIconSize(String size) {
        mailIcon.setSize(size);
    }

     public void setBanIconSize(String size) {
        userIcon.setSize(size);
    }
     
    public void setDeleteIconFill(Paint fill) {
        deleteIcon.setFill(fill);
    }

    public void setEditIconFill(Paint fill) {
        editIcon.setFill(fill);
    }
    
    public void setMailIconFill(Paint fill) {
        mailIcon.setFill(fill);
    }
    
    public void setBanIconFill(Paint fill) {
        userIcon.setFill(fill);
    }
}
