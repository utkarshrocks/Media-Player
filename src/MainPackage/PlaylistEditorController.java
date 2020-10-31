package MainPackage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PlaylistEditorController implements Initializable{
	
	@FXML private TableView<playlist_media_name> MediaTable;
	@FXML private TableColumn<playlist_media_name, String> Media;
	@FXML private TableColumn<playlist_media_name, CheckBox> Action;
	@FXML private Button Done;
	@FXML private CheckBox SelectAllMedia;
	
	boolean DoneEditing = false;
	public ObservableList<playlist_media_name> All_Media = FXCollections.<playlist_media_name>observableArrayList();;
	ArrayList<playlist_media_name> Playlist_media = new ArrayList<>();
	ArrayList<File> All_Media_List = new ArrayList<>();
	
	public void initData(ArrayList<File> Media_List) {
			Playlist_media.clear();
			All_Media_List.clear();
			All_Media.clear();
			All_Media_List.addAll(Media_List);
			for(File file: All_Media_List) {
				All_Media.add(new playlist_media_name(file.getName()));
			}
			Media.setCellValueFactory(new PropertyValueFactory<playlist_media_name, String>("Media"));
			Action.setCellValueFactory(new PropertyValueFactory<playlist_media_name, CheckBox>("Action"));
			MediaTable.setItems(All_Media);
			
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		SelectAllMedia.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	All_Media = MediaTable.getItems();
		    	
		    	for(playlist_media_name name : All_Media) {
		    		if(SelectAllMedia.isSelected()) {
		    			name.getAction().setSelected(true);
		    		}
		    		else {
		    			name.getAction().setSelected(false);
		    		}
		    	}
		    }
		});	
	}
	
	@FXML public void Done() {
		for(playlist_media_name name : All_Media) {
    		if(name.getAction().isSelected()) {
    			Playlist_media.add(name);
    		}
    	}
	}

	public Collection<? extends playlist_media_name> returnPlaylist() {
		All_Media.clear();
		Stage stage = (Stage) Done.getScene().getWindow();
	    stage.close();
		return Playlist_media;
	}
}
