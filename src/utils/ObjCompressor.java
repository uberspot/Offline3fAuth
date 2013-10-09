package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ObjCompressor {
    public static byte[] compress(ArrayList<ArrayList<Integer>> list) 
    			throws UnsupportedEncodingException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = new DeflaterOutputStream(baos);
        out.write(list.toString().getBytes("ISO-8859-1")); 
        out.close();
        return baos.toByteArray();
    }

    public static ArrayList<ArrayList<Integer>> decompress(byte[] compressed) throws IOException {
        InputStream in = new InflaterInputStream(new ByteArrayInputStream(compressed));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len;
        while((len = in.read(buffer))>0)
            baos.write(buffer, 0, len);
        return stringToArrayList(new String(baos.toByteArray(), "ISO-8859-1"));
    }
    
    private static ArrayList<ArrayList<Integer>> stringToArrayList(String string) {
    	ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
    	String[] subLists = string.trim().replaceAll( "\\[\\[|\\]\\]", "").split("\\], \\[");
    	for(String subList: subLists) {
    		ArrayList<Integer> l = new ArrayList<Integer>();
    		String[] ints = subList.split(", ");
    		for(String i: ints) {
    			try {
    				l.add(Integer.parseInt(i));
    			} catch(NumberFormatException e) { }
    		}
    		list.add(l);
    	}
    	return list;
    }
}
