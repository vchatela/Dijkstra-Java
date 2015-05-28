package core;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;

/**
 * Created by Joris on 28/05/15.
 */
public class Log extends JTextPane {

    private StyledDocument docLog;  // Pour le log

    public Log() {
        this.setPreferredSize(new Dimension(400, 100));
        this.setEditable(false);
        this.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
        docLog = this.getStyledDocument();
    }

    public void appendToLog(String msg) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.gray);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

//  Add some text

        try {
            docLog.insertString(docLog.getLength(), msg + "\n", aset );
        }
        catch(Exception e) { System.out.println(e); }
    }
}
