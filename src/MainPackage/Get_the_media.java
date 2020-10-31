package MainPackage;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Get_the_media {

	ArrayList<String> Media_name = new ArrayList<>();
	public static void main(String[] args) {}
	
	public void get_ChoiceBox(ChoiceBox<String> CB,ArrayList<File> Media_List) {
		for(File media : Media_List) {
			String name = media.getName();
			CB.getItems().add(name);
			Media_name.add(name);
		}
		CB.setValue(Media_List.get(0).getName());
	}
	
	public File Return_media_file() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Select mp4 or mp4....", "*.mp3", "*.mp4");
	    fileChooser.getExtensionFilters().add(extensionFilter);
	    File file = fileChooser.showOpenDialog(null);
	    return file;
	}
	
	public File Return_media_directory(BorderPane borderpane) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		Stage stage = (Stage) borderpane.getScene().getWindow();
		File selectedDirectory = directoryChooser.showDialog(stage);
		return selectedDirectory;
	}
	
	public ArrayList<File> Return_Media_List(File[] List){
		ArrayList<File> List_of_media = new ArrayList();
		for(File f : List) {
    		String temp_path = f.toURI().toString();
    		String[] extention = temp_path.toString().split("\\.");
  	      	String extension = extention[extention.length - 1];
  	      	if(extension.equals("mp3") || extension.equals("mp4")) List_of_media.add(f);
    	}
    	return List_of_media;
	}
	
	public File get_the_next_song(ArrayList<File> Media_List,int index,int max) {
		if(max > index) return Media_List.get(index);
		return null;
	}
	
	public int Return_desired_song(String name) {
		return Media_name.indexOf(name);
	}

	public void clearList() {
		Media_name.clear();
	}

}
