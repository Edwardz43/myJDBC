package tw.org.iii.jdbc;

import javax.swing.JWindow;

import org.json.JSONStringer;
import org.json.JSONWriter;

public class Brad02 {

	public static void main(String[] args) {
		String json = new JSONStringer().object()
				.key("key1")
				.value("")
				.endObject()
				.toString();
		System.out.println(json);
		
		String json2 = new JSONStringer().array()
				.object()
				.key("key1")
				.value("")
				.endObject()
				.object()
				.key("key2")
				.value("")
				.endObject()
				.endArray()
				.toString();
		System.out.println(json2);
//----------------------------------------------------------	
		JSONWriter jw = new JSONStringer().array();
		
		jw.object().key("key1").value("value1").endObject();
				
		jw.endArray();
		System.out.println(jw.toString());
	}
}

