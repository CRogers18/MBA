package monster_battle_arena;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author Coleman Rogers
 */
public class GenerateGraphics {
    
    // Class for quickly generating graphical interface elements
    
    public Button makeButton(String text, double posX, double posY, String style)
    {
        // Makes a new button at the specified location with specified text and CSS styling
        Button button = new Button(text);
        button.relocate(posX, posY);
        button.setStyle(style);
        button.setFont(Font.font("Endor", FontWeight.BOLD, 55));
        
        return button;
    }
    
    public VBox makeVerticalBox(int vSpacing, double posX, double posY, Pos vAlignment)
    {
        // Creates a new vertical box placed at the specified location, using the specified alignment
        VBox box = new VBox(vSpacing);
        box.relocate(posX, posY);
        box.setAlignment(vAlignment);
        
        return box;
    }
    
    public Label makeLabel(double posX, double posY)
    {
        // Creates a new label at the specified location
        Label label = new Label();
        label.relocate(posX, posY);
        
        return label;
    }
    
    public Text makeText(String textContent, double posX, double posY, String style)
    {
        // Creates a new text element at the specified location with specified content and CSS styling
        Text text = new Text(textContent);
        text.relocate(posX, posY);
        text.setStyle(style);
        text.setFont(Font.font("Endor", FontWeight.BOLD, 55));
        
        return text;
    }
    
}
