package MainPackage;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class mediaplayer extends Application {
	
	private boolean isFullscreen = false;

	@Override
	public void start(Stage stage) throws Exception {
		 Parent root = FXMLLoader.load(getClass().getResource("mediaplayerfxml.fxml"));
	        
	        Scene scene = new Scene(root);
	        stage.setTitle("Media Player");
	        
	        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                if(event.getClickCount() == 2){
	                    if(!isFullscreen)
	                    	stage.setFullScreen(true);
	                    else
	                    	stage.setFullScreen(false);
	                    isFullscreen = !isFullscreen;
	                }
	            }
	        });
	        
	        
	        
	        stage.setScene(scene);
	        stage.show();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
