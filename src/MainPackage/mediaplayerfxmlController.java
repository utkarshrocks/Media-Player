package MainPackage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
	@FXML private Button openmedia;
	@FXML private Button PlayPause;
	@FXML private Slider volumeSlider;
	@FXML private Slider progressBar;
	@FXML private Button Volume;
	@FXML private Button slowmo;
	@FXML private Button fastmo;
	@FXML private Button properties;
	@FXML private HBox HBOX;
	private float speed = 1;
	
	private MediaPlayer mediaplayer;
	String path;
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
	      File file = fileChooser.showOpenDialog(null);
	      path = file.toURI().toString();
	      
	      try{
	    	  if(isplaying) mediaplayer.stop();
	    	  isplaying = true;
	    	  Media media = new Media(path);
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
	                }
	            });
	            
	            mediaplayer.setOnReady(new Runnable() {
	                @Override
	                public void run() {
	                    javafx.util.Duration total = media.getDuration();
	                    progressBar.setMax(total.toSeconds());
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
		if(play) mediaplayer.pause();
		else mediaplayer.play();
		play = !play;
		
		//
		//
		//	change the text of play pause
		//	Button name:- PlayPause
		//	play is the boolean which is true
		//	when video is playing else it is false
		//
		//
		//
	}
	
	@FXML public void visibleslider() {
		slowmo.setVisible(false);
		fastmo.setVisible(false);
		HBOX.setVisible(false);
		speed_visible = false;
		if(slider_visible) 
			volumeSlider.setVisible(false);
		else
			volumeSlider.setVisible(true);
		slider_visible = !slider_visible;
	}
	
	@FXML public void properties() {
		volumeSlider.setVisible(false);
		slider_visible = false;
		if(speed_visible) {
			HBOX.setVisible(false);
			slowmo.setVisible(false);
			fastmo.setVisible(false);
		}
		else {
			HBOX.setVisible(true);
			slowmo.setVisible(true);
			fastmo.setVisible(true);
		}
		speed_visible = !speed_visible;
	}
	
	@FXML public void slowAction(){
		speed -= 0.1;
    	if(speed < 0.1) speed = (float) 0.1;
        mediaplayer.setRate(speed);
	}
	
	@FXML public void fastAction() {
		speed += 0.1;
    	if(speed > 4) speed = 4;
        mediaplayer.setRate(speed);
	}
	
}
