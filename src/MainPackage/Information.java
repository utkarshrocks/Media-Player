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

}