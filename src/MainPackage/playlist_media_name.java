package MainPackage;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class playlist_media_name {
	
	private final SimpleStringProperty Media;
	private final CheckBox Action;
	
	playlist_media_name(String name) {
		super();
		this.Media = new SimpleStringProperty(name);
		this.Action = new CheckBox();
	}

	public String getMedia() {
		return Media.get();
	}

	public CheckBox getAction() {
		return Action;
	}
	
}
