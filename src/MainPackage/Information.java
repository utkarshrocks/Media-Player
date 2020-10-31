package MainPackage;

public class Information {

	String Name_of_media;
	String Location_of_media;
	String Length_of_media;
	String Size_of_media;
	String File_type_of_media;
	
	public void Get_Information(String a, String b, String c, String d, String e){
		this.Name_of_media = a;
		this.Location_of_media = b;
		this.Length_of_media = c;
		this.File_type_of_media = d;
		this.Size_of_media = e;
	}
	
	public String get_string_time(javafx.util.Duration total){
		int hr = (int) total.toHours();
        int min = (int) total.toMinutes()%(60*60);
        int sec = (int) total.toSeconds()%60;
        String time = min + ":" + sec;
        if(hr != 0) time = hr + ":" + time;
		return time;
	}

}
