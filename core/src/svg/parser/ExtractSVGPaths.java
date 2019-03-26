package svg.parser;

import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;
import java.awt.geom.Point2D;


public class ExtractSVGPaths {
	
	/*public static void main(String [] args) {
		ExtractSVGPaths.extract("/Users/bensmiley45/Documents/workspace/SVGParser/src/res/icy1.svg");
	}*/
	
	/*
	 * Path is an absolute path i.e. /Users/..../workspace/file.svg
	 */
	public static ArrayList<Vector2> extract (String filePath) {
		/*
    	Camera camera = new Camera (new Vector2(), new Vector2(100, 60), new Vector2(), new Vector2(800, 480), new Vector2(-20, -12), new Vector2(120, 72));
    	
    	CameraController cc = new CameraController(camera);
    	
		GraphicsManager gm = new GraphicsManager(camera);
				
		// Visualise Geometry
		VisualiseGeometry visualiseGeometry = new VisualiseGeometry(gm);

		Display2D display = new Display2D(camera);
	   	DefaultWindow window = new DefaultWindow((int)camera.screenWidth(),(int)camera.screenHeight(), display);	

	   	display.addDrawable(gm);
	   	display.addUpdateListener(cc);
		*/
		ParseSVG l = new ParseSVG (filePath);

    	Spline sl1 = l.paths.get(0);
    	
    	// Refine the spline to an appropriate level of accuracy
		sl1.refine(0.005f);
		
		/*
		 * Convert from Vertex to Vector2
		 */
		ArrayList<Vertex> al = l.paths.get(0).getVertices();
		/*
 		Vertex [] verts = new Vertex [al.size()];
		for(int i=0; i<al.size(); i++) {
			verts [i] = al.get(i);
		}
		*/

		ArrayList<Vector2> vecs = new ArrayList<Vector2>();
		
		for(Vertex v: al) {
			vecs.add(v.toVector2());
		}
		
		//visualiseGeometry.lineString(verts);
		
		return vecs;
	}

}
