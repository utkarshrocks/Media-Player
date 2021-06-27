package MainPackage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.scene.*;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.*;
import javafx.event.EventHandler;
import javafx.fxml.*;

public class mediaplayerfxmlController implements Initializable {
	
	@FXML private MediaView mediaview,background;
	@FXML private Button openmedia,PlayPause,Volume,slowmo,fastmo,properties,openFile,ChoiceButton;
	@FXML private ImageView pppng,VImg,speed_img,Shuffle;
	@FXML private Text Elapse_time,Total_time,music_name,show_speed;
	@FXML private Slider volumeSlider,progressBar;
	@FXML private StackPane  speed_stack,stackTAG;
	@FXML private BorderPane borderpane;
	@FXML private HBox HBOX,ChoiceHBOX;
	@FXML private ChoiceBox<String> ChoiceBox = new ChoiceBox<>();
	
	Image playI=new Image("/assets/play.png");
	Image pauseI=new Image("/assets/pause.png");
	Image V_on = new Image("/assets/volume.png");
	Image V_off = new Image("/assets/mute.png");
	Image ShuffleI = new Image("/assets/shuffle.png");
	ArrayList<File> Media_List = new ArrayList<>();
	ArrayList<File> Playlist_List = new ArrayList<>();
	Information media_information = new Information();
	FR_button_visibility FR_button = new FR_button_visibility();
	ArrayList<playlist_media_name> playlist_file = new ArrayList<>();
	Get_the_media New_Media = new Get_the_media();
	javafx.util.Duration runTime,fullTime;
	private MediaPlayer mediaplayer,BK_mediaplayer;
	String path,extension;
	Random random = new Random();
	Media media;
	private float speed = 1;
	private boolean 
			isSongPlaying = false,
			isPlay=true,
			isplaying = false,
			slider_visible = false,
			speed_visible = false,
			PlayShuffle = false,
			isPlayingMultipleMedia= false;
	Text text = new Text();
	int index = 0,max = 0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FR_button.get_the_buttons(speed_stack, show_speed, slowmo, fastmo, HBOX, speed_img, ChoiceBox,ChoiceButton,ChoiceHBOX);
		volumeSlider.setValue(100.0);
		volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaplayer.setVolume(volumeSlider.getValue()/100);
            }
        });
	}
	
	
	@FXML private void openmedia() {
		openFile.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();
                if(button==MouseButton.PRIMARY){
                	index = 0;
                	isPlayingMultipleMedia = false;
                	ChoiceBox.setVisible(false);
                	ChoiceHBOX.setVisible(false);
                	ChoiceButton.setVisible(false);
                	File file = New_Media.Return_media_file();
            		Load_media(file);
                }
                else{
                	isPlayingMultipleMedia = true;
                	New_Media.clearList();
                	Media_List.clear();
                	ChoiceBox.getItems().removeAll(ChoiceBox.getItems());
                	ChoiceHBOX.setVisible(true);
                	ChoiceBox.setVisible(true);
                	ChoiceButton.setVisible(true);
                	index = 0;
                	File MediaDirectory = New_Media.Return_media_directory(borderpane);
                	File[] List = null;
                	if (MediaDirectory.isDirectory()) {
                	    List = MediaDirectory.listFiles();
                	}
                	Media_List.addAll(New_Media.Return_Media_List(List));
                	New_Media.get_ChoiceBox(ChoiceBox, Media_List);
                	max = Media_List.size();
                	Load_media(New_Media.get_the_next_song(Media_List,index,max));
                }
			}});
	}
	
	@FXML private void Load_media(File file) {
	    path = file.toURI().toString();
	      if(isSongPlaying) {
	    	  BK_mediaplayer.dispose();
	    	  isSongPlaying = false;
	      }
	      ChoiceBox.setValue(file.getName());
	      String[] extention = path.toString().split("\\.");
	      extension = extention[extention.length - 1]; 
	      try{
	    	  music_name.setText("");
	    	  if(isplaying) { mediaplayer.stop();
	    	  mediaplayer.dispose();}
	    	  isplaying = true;
	    	  if(extension.equals("mp3")) {
	    		  isSongPlaying = true;
	    		  music_name.setText(file.getName());
	    		  media = new Media(new File("src/assets/background.mp4").toURI().toString());
		    	  BK_mediaplayer = new MediaPlayer(media);
		    	  background.setMediaPlayer(BK_mediaplayer);
		    	  BK_mediaplayer.play();
		    	  BK_mediaplayer.setRate(1);
		    	  BK_mediaplayer.setCycleCount(MediaPlayer.INDEFINITE);
		    	  DoubleProperty widthProp = background.fitWidthProperty();
		          DoubleProperty heightProp = background.fitHeightProperty();  
		          widthProp.bind(Bindings.selectDouble(background.sceneProperty(), "width"));
		          heightProp.bind(Bindings.selectDouble(background.sceneProperty(), "height"));
	    	  }
		    	  media = new Media(path);
		    	  mediaplayer = new MediaPlayer(media);
		    	  mediaview.setMediaPlayer(mediaplayer);
		    	  mediaplayer.play();
		    	  mediaplayer.setRate(speed);
	    	    DoubleProperty widthProp = mediaview.fitWidthProperty();
	            DoubleProperty heightProp = mediaview.fitHeightProperty();
	            
	            widthProp.bind(Bindings.selectDouble(mediaview.sceneProperty(), "width"));
	            heightProp.bind(Bindings.selectDouble(mediaview.sceneProperty(), "height"));
	            
	            mediaplayer.currentTimeProperty().addListener(new ChangeListener<javafx.util.Duration>() {
	                @Override
	                public void changed(ObservableValue<? extends javafx.util.Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
	                    progressBar.setValue(newValue.toSeconds());
	                    runTime = newValue;
	                    String time = media_information.get_string_time(newValue);
	                    Elapse_time.setText(time);
	                    if(runTime.toSeconds() >= fullTime.toSeconds()-1 && extension.equals("mp3")) {
	                    	BK_mediaplayer.stop();
	                    	if(isPlayingMultipleMedia && index <= max-1) {
	                    		if(PlayShuffle) index = random.nextInt(max);
	                    		else if(index < max-1)index++;	 
	                    		else if(index == max -1)index = 0;
	                    	Load_media(New_Media.get_the_next_song(Media_List,index,max));}
	                    }
	                    if(runTime.toSeconds() >= fullTime.toSeconds()-1 
	                    		&& extension.equals("mp4") 
	                    		&& isPlayingMultipleMedia
	                    		&& index <= max-1) {
	                    	if(PlayShuffle) index = random.nextInt(max);
                    		else if(index < max-1)index++;	 
                    		else if(index == max -1)index = 0;
	                    	Load_media(New_Media.get_the_next_song(Media_List,index,max));
	                    }
	                }
	            });
	            
	            mediaplayer.setOnReady(new Runnable() {
	                @Override
	                public void run() {
	                    javafx.util.Duration total = media.getDuration();
	                    fullTime = total;
	                    progressBar.setMax(total.toSeconds());
	                    String time = media_information.get_string_time(total);
	                    media_information.Get_Information(
	                    		file.getName(),
	                    		file.getPath(),
	                    		time,
	                    		extension,
	                    		file.length()/1000000 + "." + (file.length()/1000 - (file.length()/1000000)*1000));
	                    Total_time.setText(time);
	                }
	            });
	            
	            progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
	                @Override
	                public void handle(MouseEvent event) {
	                    mediaplayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
	                }
	            });
	            
	            progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
	                @Override
	                public void handle(MouseEvent event) {
	                    mediaplayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
	                }
	            });
	    	  
	      }catch(Exception e) {
	    	  e.printStackTrace();
	      }
	}
	
	@FXML private void getItem() {
		ChoiceButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();
                if(button==MouseButton.SECONDARY){
                	PlayShuffle = !PlayShuffle; 
                	if(PlayShuffle) {
                		Shuffle.setImage(ShuffleI);
                	}
                	else {
                		Shuffle.setImage(playI);
                	}
                }
                else {
                	Load_media(Media_List.get(New_Media.Return_desired_song(ChoiceBox.getValue())));
            		index = New_Media.Return_desired_song(ChoiceBox.getValue());
                }
			}});
	}
	
	@FXML private void play_pause() {
		if (isPlay==true) {
				if(extension.equals("mp3")) {BK_mediaplayer.pause();}
            	mediaplayer.pause();
            	pppng.setImage(playI);
            	isPlay=false;
        	}	 
		else {
				if(extension.equals("mp3")) {BK_mediaplayer.play();}
            	mediaplayer.play();
            	pppng.setImage(pauseI);
            	isPlay=true;
        	}
	}
	
	@FXML private void visibleslider() {
		Volume.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();
                if(button==MouseButton.SECONDARY){
                	FR_button.setVisibility(false,isPlayingMultipleMedia);
            		speed_visible = false;
                	if(slider_visible) {
            			volumeSlider.setVisible(false);
            			if(isPlayingMultipleMedia) {
            			ChoiceBox.setVisible(true);
            			ChoiceButton.setVisible(true);
            			ChoiceHBOX.setVisible(true);}
                	}
            		else {
            			volumeSlider.setVisible(true);
            			ChoiceBox.setVisible(false);
            			ChoiceButton.setVisible(false);
            			ChoiceHBOX.setVisible(false);
            		}
            		slider_visible = !slider_visible;
                }
                else{
                	if(mediaplayer.isMute()) {
                		mediaplayer.setMute(false);
                		VImg.setImage(V_on);
                	}
                	else {
                		VImg.setImage(V_off);
                		mediaplayer.setMute(true);
                	}
                }
			}});
	}
	
	@FXML private void properties()  throws IOException{
		
		properties.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();
                if(button==MouseButton.SECONDARY){
                	if(speed_visible) {FR_button.setVisibility(false,isPlayingMultipleMedia);}
            		else {FR_button.setVisibility(true,isPlayingMultipleMedia);}
            		volumeSlider.setVisible(false);
            		slider_visible = false;
                	speed_visible = !speed_visible;
                }else if(button==MouseButton.PRIMARY){
                	FXMLLoader ldr = new FXMLLoader();
                	ldr.setLocation(getClass().getResource("MediaInfo.fxml"));
                	Parent Media_Info = null;
					try {
						Media_Info = ldr.load();
					} catch (IOException e) {
						System.out.println(e.toString());
					}
                	
                	Scene Media_info = new Scene(Media_Info);
                	MediaInfoController controller = ldr.getController();
                	controller.initData(media_information);
                	Stage win = new Stage();
                	win.setTitle("Media Information");
                	win.setScene(Media_info);
                    win.show();
                }
			}});
	}
	
	@FXML private void PlaylistEdit() throws IOException {
		FXMLLoader ldr = new FXMLLoader();
    	ldr.setLocation(getClass().getResource("PlaylistEditor.fxml"));
    	Parent Media_Info = ldr.load();
    	Scene Media_info = new Scene(Media_Info);
    	PlaylistEditorController controller = ldr.getController();
    	controller.initData(Media_List);
    	Stage win = new Stage();
    	win.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                win.setMaximized(false);
        });
    	win.setResizable(false);
    	win.setTitle("Playlist Editor");
    	win.setScene(Media_info);
        win.show();
        win.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
            	playlist_file.clear();
            	Playlist_List.clear();
            	playlist_file.addAll(controller.returnPlaylist());
            	if(!playlist_file.isEmpty()) {
            	for(playlist_media_name name: playlist_file) {
            		Playlist_List.add(Media_List.get(New_Media.Return_desired_song(name.getMedia())));
            	}
            	isPlayingMultipleMedia = true;
            	New_Media.clearList();
            	Media_List.clear();
            	ChoiceBox.getItems().removeAll(ChoiceBox.getItems());
            	ChoiceHBOX.setVisible(true);
            	ChoiceBox.setVisible(true);
            	ChoiceButton.setVisible(true);
            	index = 0;
            	Media_List.addAll(Playlist_List);
            	New_Media.get_ChoiceBox(ChoiceBox, Media_List);
            	max = Media_List.size();
            	Load_media(New_Media.get_the_next_song(Media_List,index,max));}
            }
        });
	}
	
	@FXML private void slowAction(){
		speed -= 0.1;
    	if(speed < 0.1) speed = (float) 0.1;
    	show_speed.setText(Float.toString(speed).substring(0,3));
        mediaplayer.setRate(speed);
	}
	
	@FXML private void fastAction() {
		speed += 0.1;
    	if(speed > 4) speed = 4;
    	show_speed.setText(Float.toString(speed).substring(0, 3));
        mediaplayer.setRate(speed);
	}
	
}
