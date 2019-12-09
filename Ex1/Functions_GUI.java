package Ex1;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


import com.google.gson.*;

public class Functions_GUI implements functions{

	private ArrayList<function> colFunc;
	public static Color[] Colors = {Color.blue, Color.cyan, Color.MAGENTA, Color.ORANGE, 
			Color.red, Color.GREEN, Color.PINK};
	public Functions_GUI(Functions_GUI c) {

	}
	public Functions_GUI() {

		this.colFunc=new ArrayList<function>();
	}
	@Override
	public int size() {
		return colFunc.size();
	}

	@Override
	public boolean isEmpty() {
		return colFunc.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return colFunc.contains(o);
	}

	@Override
	public Iterator<function> iterator() {
		return colFunc.iterator();
	}

	@Override
	public Object[] toArray() {
		return colFunc.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return colFunc.toArray(a);
	}

	@Override
	public boolean add(function e) {
		return colFunc.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return colFunc.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return colFunc.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends function> c) {
		return colFunc.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return colFunc.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return colFunc.retainAll(c);
	}

	@Override
	public void clear() {
		colFunc.clear();		
	}
	@Override
	public void initFromFile(String file) throws IOException {
		// TODO Auto-generated method stub
		{
			try 
			{
				FileInputStream fstream = new FileInputStream(file);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = br.readLine()) != null) {
					ComplexFunction f =new ComplexFunction(new Monom("0"));
					this.colFunc.add(f.initFromString(line));
				}
				br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				System.out.println("Error: " + e.getMessage());
			}

		}

	}

	@Override
	public void saveToFile(String file) throws IOException {
		// TODO Auto-generated method stub
		String fileName = file;
		try 
		{
			PrintWriter pw = new PrintWriter(new File(fileName));
			StringBuilder sb = new StringBuilder();
			for (Iterator<function> iterator = colFunc.iterator(); iterator.hasNext();) {
				function function = (function) iterator.next();
				sb.append(function.toString()+"\n");
			}
			pw.write(sb.toString());
			pw.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void drawFunctions(int width, int height, Range rx, Range ry, int resolution) {

		int n = resolution;
		StdDraw.setCanvasSize(width, height);
		int size = colFunc.size();
		double[] x = new double[n+1];
		double[][] yy = new double[size][n+1];
		double x_step = (rx.get_max()-rx.get_min())/n;
		double x0 = rx.get_min();
		for (int i=0; i<=n; i++) {
			x[i] = x0;
			for(int a=0;a<size;a++) {
				yy[a][i] = colFunc.get(a).f(x[i]);
			}
			x0+=x_step;	

		}
		StdDraw.setXscale(rx.get_min(), rx.get_max());
		StdDraw.setYscale(ry.get_min(), ry.get_max());

		for(double xl=rx.get_min();xl<=rx.get_max();xl++) {
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.setPenRadius(0.002);
			StdDraw.line(xl,ry.get_max(), xl,ry.get_min());
			StdDraw.setPenColor(StdDraw.BLACK);
			String s=""+(int)xl;
			StdDraw.text(xl,-0.5 ,s);
		}

		for(double yl=ry.get_min();yl<=ry.get_max();yl++) {
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.setPenRadius(0.002);
			StdDraw.line(rx.get_min(),yl,rx.get_max(),yl);
			StdDraw.setPenColor(StdDraw.BLACK);
			String s=""+(int)yl;
			StdDraw.text(-0.5,yl ,s);

		}
		//draw x axis and y axis
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.005);
		StdDraw.line(rx.get_min(),0, rx.get_max(),0);
		StdDraw.line(0,ry.get_min(), 0,ry.get_max());


		// plot the approximation to the function
		for(int a=0;a<size;a++) {
			int c = a%Colors.length;
			StdDraw.setPenRadius(0.003);
			StdDraw.setPenColor(Colors[c]);
			System.out.println(a+") "+Colors[a]+"  f(x)= "+colFunc.get(a));
			for (int i = 0; i < n; i++) {
				StdDraw.line(x[i], yy[a][i], x[i+1], yy[a][i+1]);
			}
		}	
	}

	@Override
	public void drawFunctions(String json_file) {
		Gson gson = new Gson();			
		try 
		{
			//Option 2: from JSON file to Object
			FileReader reader = new FileReader(json_file);
			Parameters par = gson.fromJson(reader,Parameters.class);
			this.drawFunctions(par.getWidth(),par.getHeight(),new Range(par.Range_X[0],par.Range_X[1]),new Range(par.Range_Y[0],par.Range_Y[1]),par.getResolution());
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
