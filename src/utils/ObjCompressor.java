package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.google.gson.Gson;

public class ObjCompressor {
    public static byte[] compress(ArrayList<ArrayList<Integer>> list) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStream out = new DeflaterOutputStream(baos);
            out.write(list.toString().getBytes("ISO-8859-1")); 
            out.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return baos.toByteArray();
    }

    public static ArrayList<ArrayList<Integer>> decompress(byte[] compressed) {
        InputStream in = new InflaterInputStream(new ByteArrayInputStream(compressed));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[8192];
            int len;
            while((len = in.read(buffer))>0)
                baos.write(buffer, 0, len);
            return stringToArrayList(new String(baos.toByteArray(), "ISO-8859-1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<ArrayList<Integer>>();
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
