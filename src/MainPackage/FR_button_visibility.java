package MainPackage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class FR_button_visibility {
	@FXML private StackPane  speed_stack;
	@FXML private Text show_speed;
	@FXML private Button slowmo,fastmo;
	@FXML private HBox HBOX;
	@FXML private ImageView speed_img;
	
	public void get_the_buttons(StackPane a,Text b,Button c, Button d, HBox e, ImageView f) {
		this.speed_stack = a;
		this.show_speed = b;
		this.slowmo = c;
		this.fastmo = d;
		this.HBOX = e;
		this.speed_img = f;
	}
	
	public void setVisibility(boolean what) {
		speed_stack.setVisible(what);
		show_speed.setVisible(what);
		slowmo.setVisible(what);
		fastmo.setVisible(what);
		speed_img.setVisible(what);
		HBOX.setVisible(what);
	}
	
}
