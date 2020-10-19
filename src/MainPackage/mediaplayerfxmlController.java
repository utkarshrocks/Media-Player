package MainPackage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.*;

public class mediaplayerfxmlController implements Initializable {
	
	@FXML private MediaView mediaview;
	@FXML private MediaView background;
	@FXML private Button openmedia;
	@FXML private Button PlayPause;
	@FXML private ImageView pppng;
	@FXML private ImageView VImg;
	@FXML private ImageView speed_img;
	@FXML private Text Elapse_time;
	@FXML private Text Total_time;
	@FXML private Text music_name;
	@FXML private Text show_speed;
	@FXML private Slider volumeSlider;
	@FXML private Slider progressBar;
	@FXML private Button Volume;
	@FXML private Button slowmo;
	@FXML private Button fastmo;
	@FXML private StackPane  speed_stack;
	@FXML private Button properties;
	@FXML private HBox HBOX;
	
	
	Image playI=new Image("/assets/play.png");
	Image pauseI=new Image("/assets/pause.png");
	Image V_on = new Image("/assets/volume.png");
	Image V_off = new Image("/assets/mute.png");
	javafx.util.Duration runTime;
	javafx.util.Duration fullTime;
	private MediaPlayer mediaplayer;
	private MediaPlayer BK_mediaplayer;
	String path;
	Media media;
	String extension;
	private float speed = 1;
	private boolean isPlay=true;
	private boolean isplaying = false; 
	private boolean play = true;
	private boolean slider_visible = false;
	private boolean speed_visible = false;
	Text text = new Text();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML public void openmedia() {
		
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Select mp4 or mp4....", "*.mp3", "*.mp4");
	     fileChooser.getExtensionFilters().add(extensionFilter);
	      File file = fileChooser.showOpenDialog(null);
	      path = file.toURI().toString();
	      String[] extention = path.toString().split("\\.");
	      extension = extention[extention.length - 1]; 
	      try{
	    	  music_name.setText("");
	    	  if(isplaying) mediaplayer.stop();
	    	  isplaying = true;
	    	  if(extension.equals("mp3")) {
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
	                    int hr = (int) newValue.toHours();
	                    int min = (int) newValue.toMinutes()%(60*60);
	                    int sec = (int) newValue.toSeconds()%60;
	                    String time = min + ":" + sec;
	                    if(hr != 0) time = hr + ":" + time;
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
	                    int hr = (int) total.toHours();
	                    int min = (int) total.toMinutes()%(60*60);
	                    int sec = (int) total.toSeconds()%60;
	                    String time = min + ":" + sec;
	                    if(hr != 0) time = hr + ":" + time;
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
            	pppng.setImage(pauseI);
            	isPlay=false;
        	}	 
		else {
				if(extension.equals("mp3")) {BK_mediaplayer.play();}
            	mediaplayer.play();
            	pppng.setImage(playI);
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
		speed_stack.setVisible(false);
		show_speed.setVisible(false);
		slowmo.setVisible(false);
		fastmo.setVisible(false);
		speed_img.setVisible(false);
		HBOX.setVisible(false);
		speed_visible = false;
		
	}
	
	@FXML public void properties() {
		
		properties.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();
                if(button==MouseButton.PRIMARY){
                	if(speed_visible) {
                		speed_img.setVisible(false);
                		speed_stack.setVisible(false);
                		show_speed.setVisible(false);
            			HBOX.setVisible(false);
            			slowmo.setVisible(false);
            			fastmo.setVisible(false);
            		}
            		else {
            			speed_img.setVisible(true);
            			speed_stack.setVisible(true);
            			show_speed.setVisible(true);
            			HBOX.setVisible(true);
            			slowmo.setVisible(true);
            			fastmo.setVisible(true);
            		}
            		speed_visible = !speed_visible;
                }else if(button==MouseButton.SECONDARY){
               
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
