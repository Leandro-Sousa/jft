/**
 * 
 */
package com.cmm.jft.data.files;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipException;

/**
 * <p>
 * <code>CopyLines.java</code>
 * </p>
 *
 * @author cristiano
 * @version 23/03/2017 12:38:53
 *
 */
public class CopyLines {


    private static String mainDir = "D:\\Disco\\Users\\Cristiano\\Downloads\\BMF Files\\";
    private static String outputDir = "output";


    public static void extract(FileFilter filter, String path, String consFileName, String[] lineFilters) {

	int lineCount = 0;
	FileOutputStream fos = null;
	try {
	    fos = new FileOutputStream(new File(path+consFileName));

	    for(File neg:new File(mainDir).listFiles(filter)) {
		try {
		    List<String> files = Zimp.extract(neg.getAbsolutePath(), "");
		    int len = 1000000;
		    StringBuffer buffer = new StringBuffer(len);
		    for(String fn : files) {
			Scanner sc = new Scanner(new File(fn));
			CSV csv = new CSV(fn, ";", "RT", "RH");
			
			sc.useDelimiter("\n");
			while (csv.hasNext()) {
			    String[] line = csv.readLine();//sc.next();
			    //if (!(line.startsWith("RH") || line.startsWith("RT"))) {
				for(String s:lineFilters) {
				    if((line != null && line[0] != null) && line[1].startsWith(s)) {
					lineCount++;
					for(int i=0;i<line.length;i++){
					    buffer.append(line[i]+";");
					}
					buffer.append("\n");

					if (buffer.length() >= len) {
					    fos.write(buffer.toString().getBytes());
					    buffer = null;
					    buffer = new StringBuffer(len);
					}
				    }
				}
			    //}
			}
			csv.close();//sc.close();
			fos.write(buffer.toString().getBytes());
			buffer = null;
			buffer = new StringBuffer(len);

			// apaga o arquivo extraido do zip
			new File(fn).delete();
		    }

		    // apaga o diretorio
		    new File(neg.getAbsolutePath().substring(0, neg.getAbsolutePath().lastIndexOf('.'))).delete();

		} catch (ZipException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    System.out.println(consFileName + ":" + lineCount + " lines copied!");
	} catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	} finally{
	    if(fos!= null){
		try {
		    fos.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}

    }

    /**
     * @param args
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) {

	int lines = 0;
	FileOutputStream fos = null;

	String path = "D:\\Disco\\Users\\Cristiano\\Downloads\\BMF Files\\";
	String[] lineFilters = {"WDO","DOL"};

	FileFilter ff = new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		if(pathname!=null){
		    return pathname.isFile() 
			    && (pathname.getName().endsWith(".txt") || pathname.getName().endsWith(".TXT")) 
			    && (
				    pathname.getName().startsWith("OFER_CPA") 
				    || pathname.getName().startsWith("OFER_VDA") 
				    || pathname.getName().startsWith("NEG")
				    ); 
		}
		return false;
	    }
	};

	FileFilter negZip = new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		if(pathname!=null){
		    return pathname.isFile() 
			    && (pathname.getName().endsWith(".zip") && pathname.getName().startsWith("NEG")); 
		}
		return false;
	    }
	};

	FileFilter cpaZip = new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		if(pathname!=null){
		    return pathname.isFile() 
			    && (pathname.getName().endsWith(".zip") && pathname.getName().startsWith("OFER_CPA")); 
		}
		return false;
	    }
	};

	FileFilter vdaZip = new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		if(pathname!=null){
		    return pathname.isFile() 
			    && (pathname.getName().endsWith(".zip") && pathname.getName().startsWith("OFER_VDA")); 
		}
		return false;
	    }
	};

	extract(negZip, path, "NEG_CONS.csv", lineFilters);
	extract(cpaZip, path, "CPA_CONS.csv", lineFilters);
	extract(vdaZip, path, "VDA_CONS.csv", lineFilters);

//	System.exit(0);
//
//	for(File fdata : new File(path).listFiles(ff)){
//	    String fileName = fdata.getName();
//	    try{
//		fos = new FileOutputStream(new File(path + "Copy_" + fileName));
//
//		CSV csv = new CSV(path+fileName, ";", "RT", "RH");
//		while (csv.hasNext()) {
//		    String[] vs = csv.readLine();
//
//		    if(vs != null && vs[0] != null){
//
//			boolean isStarted = false;
//			for(String s : lineFilters) {
//			    if(isStarted = vs[1].startsWith(s)) {
//				break;
//			    }
//			}
//			if(isStarted) {
//			    lines++;
//			    StringBuilder sb = new StringBuilder(1000);
//
//			    for(int i=0;i<vs.length;i++){
//				sb.append(vs[i]+";");
//			    }
//			    sb.append('\n');
//			    fos.write(sb.toString().getBytes());
//			}
//		    }
//		}
//	    }catch(FileNotFoundException e){
//		e.printStackTrace();
//	    } catch (IOException e) {
//		e.printStackTrace();
//	    }
//	    finally{
//		if(fos!= null){
//		    try {
//			fos.close();
//		    } catch (IOException e) {
//			e.printStackTrace();
//		    }
//		}
//	    }
//
//	    System.out.println(fileName + ": " + lines + " lines copied!");
//
//	}

    }

    public static String extractAndConsolidate(String zipFile, String destDir) {

	String fName = "";
	try {
	    int len = 1000000;
	    StringBuffer buffer = new StringBuffer(len);
	    List<String> files = Zimp.extract(zipFile, "");
	    fName = destDir;
	    fName += zipFile.substring(zipFile.lastIndexOf('\\'), zipFile.lastIndexOf('.')) + ".csv";
	    FileOutputStream fos = new FileOutputStream(new File(fName));

	    for (String fn : files) {
		Scanner sc = new Scanner(new File(fn));
		sc.useDelimiter("\n");
		while (sc.hasNext()) {
		    String line = sc.next();
		    if (!(line.startsWith("RH") || line.startsWith("RT"))) {
			buffer.append(line + "\n");

			if (buffer.length() >= len) {
			    fos.write(buffer.toString().getBytes());
			    buffer = null;
			    buffer = new StringBuffer(len);
			}
		    }
		}
		sc.close();
		fos.write(buffer.toString().getBytes());
		buffer = null;
		buffer = new StringBuffer(len);

		// apaga o arquivo extraido do zip
		new File(fn).delete();
	    }
	    // apaga o diretorio
	    new File(zipFile.substring(0, zipFile.lastIndexOf('.'))).delete();

	    fos.close();
	    System.gc();

	} catch (ZipException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return fName;
    }


    public static void createFolders() {

	new File(outputDir).mkdirs();

    }

}
