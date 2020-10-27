package MainPackage;

import java.io.*;
import java.net.URL;
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
	@FXML private Button openmedia,PlayPause,Volume,slowmo,fastmo,properties;
	@FXML private ImageView pppng,VImg,speed_img;
	@FXML private Text Elapse_time,Total_time,music_name,show_speed;
	@FXML private Slider volumeSlider,progressBar;
	@FXML private StackPane  speed_stack,stackTAG;
	@FXML private HBox HBOX;
	
	Image playI=new Image("/assets/play.png");
	Image pauseI=new Image("/assets/pause.png");
	Image V_on = new Image("/assets/volume.png");
	Image V_off = new Image("/assets/mute.png");
	Information media_information = new Information();
	FR_button_visibility FR_button = new FR_button_visibility();
	javafx.util.Duration runTime,fullTime;
	private MediaPlayer mediaplayer,BK_mediaplayer;
	String path,extension;
	Media media;
	private float speed = 1;
	private boolean 
			isSongPlaying = false,
			isPlay=true,
			isplaying = false,
			slider_visible = false,
			speed_visible = false;
	Text text = new Text();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FR_button.get_the_buttons(speed_stack, show_speed, slowmo, fastmo, HBOX, speed_img);
	}
	
	@FXML public void openmedia() {
		
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Select mp4 or mp4....", "*.mp3", "*.mp4");
	    fileChooser.getExtensionFilters().add(extensionFilter);
	    File file = fileChooser.showOpenDialog(null);
	    path = file.toURI().toString();
	      if(isSongPlaying) {
	    	  BK_mediaplayer.dispose();
	    	  isSongPlaying = false;
	      }
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
	                    if(runTime.toSeconds() >= fullTime.toSeconds() && extension.equals("mp3")) {
	                    	BK_mediaplayer.stop();
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
	            
	            volumeSlider.setValue(mediaplayer.getVolume()*100);
	            volumeSlider.valueProperty().addListener(new InvalidationListener() {
	                @Override
	                public void invalidated(Observable observable) {
	                    mediaplayer.setVolume(volumeSlider.getValue()/100);
	                }
	            });
	    	  
	      }catch(Exception e) {
	    	  e.printStackTrace();
	      }
	}
	
	@FXML public void play_pause() {
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
	
	@FXML public void visibleslider() {
		Volume.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();
                if(button==MouseButton.PRIMARY){
                	if(slider_visible) 
            			volumeSlider.setVisible(false);
            		else
            			volumeSlider.setVisible(true);
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
		FR_button.setVisibility(false);
		speed_visible = false;
	}
	
	@FXML public void properties()  throws IOException{
		
		properties.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();
                if(button==MouseButton.PRIMARY){
                	if(speed_visible) {FR_button.setVisibility(false);}
            		else {FR_button.setVisibility(true);}
                	speed_visible = !speed_visible;
                }else if(button==MouseButton.SECONDARY){
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
		
		volumeSlider.setVisible(false);
		slider_visible = false;
	}
	
	@FXML public void slowAction(){
		speed -= 0.1;
    	if(speed < 0.1) speed = (float) 0.1;
    	show_speed.setText(Float.toString(speed).substring(0,3));
        mediaplayer.setRate(speed);
	}
	
	@FXML public void fastAction() {
		speed += 0.1;
    	if(speed > 4) speed = 4;
    	show_speed.setText(Float.toString(speed).substring(0, 3));
        mediaplayer.setRate(speed);
	}
	
}
