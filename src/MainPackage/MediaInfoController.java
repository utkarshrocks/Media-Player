package MainPackage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MediaInfoController implements Initializable {
	
	@FXML private Label file_name;
	@FXML private Label file_location;
	@FXML private Label file_length;
	@FXML private Label file_type;
	@FXML private Label file_size;
	
	Information get_media_info = new Information();
	
	public void initData(Information info) {
		this.get_media_info = info;
		file_name.setText(get_media_info.Name_of_media);
		file_location.setText(get_media_info.Location_of_media);
		file_length.setText(get_media_info.Length_of_media);
		file_type.setText(get_media_info.File_type_of_media);
		file_size.setText(get_media_info.Size_of_media);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
