package org.graphstream.ui.android.renderer.shape.android.advancedShapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.graphstream.ui.geom.Vector2;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.renderer.Skeleton;
import org.graphstream.ui.android.renderer.shape.android.ShowCubics;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.LineConnectorShape;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form.Path2D;

public class CubicCurveShape extends LineConnectorShape {
	ShowCubics showCubics ;
	Path2D theShape = new Path2D(0, false);

	public CubicCurveShape() {
		this.showCubics = new ShowCubics() ;
	}

	@Override
	public void make(Backend backend, DefaultCamera2D camera) {
		make(camera, 0, 0, 0, 0);
	}

	private void make(DefaultCamera2D camera, double sox, double soy, double swx, double swy) {
		if (skel.multi() > 1 || skel.isLoop()) // is a loop or a multi edge
            makeMultiOrLoop(camera, sox, soy, swx, swy);
       else if(skel.isPoly() && skel.size() == 4)
            makeFromPoints(camera, sox, soy, swx, swy); // has points positions
       else 
    	   makeSingle(camera, sox, soy, swx, swy); // is a single edge.
	}

	private void makeMultiOrLoop(DefaultCamera2D camera, double sox, double soy, double swx, double swy) {
		if (skel.isLoop())
			makeLoop(camera, sox, soy, swx, swy);
        else
        	makeMulti(camera, sox, soy, swx, swy);
	}

	private void makeLoop(DefaultCamera2D camera, double sox, double soy, double swx, double swy) {
		double fromx = skel.apply(0).x + sox;
		double fromy = skel.apply(0).y + soy;
		double tox = skel.apply(3).x + sox;
		double toy = skel.apply(3).y + soy;
		double c1x = skel.apply(1).x + sox;
		double c1y = skel.apply(1).y + soy;
		double c2x = skel.apply(2).x + sox;
		double c2y = skel.apply(2).y + soy;
		
		theShape = new Path2D(5, false);
		theShape.moveTo(fromx, fromy);
		theShape.curveTo(c1x, c1y, c2x, c2y, tox, toy);
	}

	private void makeMulti(DefaultCamera2D camera, double sox, double soy, double swx, double swy) {
		double fromx = skel.apply(0).x + sox;
		double fromy = skel.apply(0).y + soy;
		double tox = skel.apply(3).x + sox;
		double toy = skel.apply(3).y + soy;
		double c1x = skel.apply(1).x + sox;
		double c1y = skel.apply(1).y + soy;
		double c2x = skel.apply(2).x + sox;
		double c2y = skel.apply(2).y + soy;	

		theShape = new Path2D(5, false);
		theShape.moveTo(fromx, fromy);
		theShape.curveTo(c1x, c1y, c2x, c2y, tox, toy);
	}

	private void makeFromPoints(DefaultCamera2D camera, double sox, double soy, double swx, double swy) {
		double fromx = skel.from().x + sox;
		double fromy = skel.from().y + soy;
		double tox = skel.to().x + sox;
		double toy = skel.to().y + soy;
		double c1x = skel.apply(1).x + sox;
		double c1y = skel.apply(1).y + soy;
		double c2x = skel.apply(2).x + sox;
		double c2y = skel.apply(2).y + soy;
		
		theShape = new Path2D(5, false);
		theShape.moveTo(fromx, fromy);
		theShape.curveTo(c1x, c1y, c2x, c2y, tox, toy);	

		if (sox == 0 && soy == 0) {	// Inform the system this is a curve, not a polyline.
			skel.setCurve(
					fromx, fromy, 0,
					c1x, c1y, 0,
					c2x, c2y, 0,
					tox, toy, 0);
		}	
	}

	private void makeSingle(DefaultCamera2D camera, double sox, double soy, double swx, double swy) {
		double fromx = skel.from().x + sox;
		double fromy = skel.from().y + soy;
		double tox = skel.to().x + sox;
		double toy = skel.to().y + soy;
		Vector2 mainDir = new Vector2(skel.from(), skel.to());
        double length = mainDir.length();
        double angle = mainDir.y() / length;
        double c1x = 0.0;
        double c1y = 0.0;
        double c2x = 0.0;
        double c2y = 0.0;

        if (angle > 0.707107f || angle < -0.707107f) {
            // North or south.
            c1x = fromx + mainDir.x() / 2;
            c2x = c1x;
            c1y = fromy;
            c2y = toy;
        } 
        else {
            // East or west.
            c1x = fromx;
            c2x = tox;
            c1y = fromy + mainDir.y() / 2;
            c2y = c1y;
        }

        theShape = new Path2D(5, false);
        theShape.moveTo(fromx, fromy);
        theShape.curveTo(c1x, c1y, c2x, c2y, tox, toy);

        // Set the connector as a curve.

        if (sox == 0 && soy == 0) {
            skel.setCurve(
                fromx, fromy, 0,
                c1x, c1y, 0,
                c2x, c2y, 0,
                tox, toy, 0);
        }
	}

	@Override
	public void makeShadow(Backend backend, DefaultCamera2D camera) {
		if (skel.isCurve())
            makeMultiOrLoop(camera, shadowableLine.theShadowOff.x, shadowableLine.theShadowOff.y, shadowableLine.theShadowWidth, shadowableLine.theShadowWidth);
		else
			makeSingle(camera, shadowableLine.theShadowOff.x, shadowableLine.theShadowOff.y, shadowableLine.theShadowWidth, shadowableLine.theShadowWidth);
	}

	@Override
	public void render(Backend bck, DefaultCamera2D camera, GraphicElement element, Skeleton skeleton) {
		Canvas g = bck.graphics2D();
		Paint p = bck.getPaint();
		make(bck, camera);
		strokableLine.stroke(g, p, theShape);
		fillableLine.fill(g, p, theSize, theShape);
		decorable.decorConnector(bck, camera, skel.iconAndText, element, theShape);
		// 		showControlPolygon = true
		// 		if( showControlPolygon ) {
		//	 		val c = g.getColor();
		//	 		val s = g.getStroke();
		//	 		g.setStroke( new java.awt.BasicStroke( camera.metrics.px1 ) )
		//	 		g.setColor( Color.red );
		//	 		g.draw( theShape );
		//	 		g.setStroke( s );
		//	 		g.setColor( c );
		//	 		showCtrlPoints( g, camera )
		// 		}		
	}

	@Override
	public void renderShadow(Backend bck, DefaultCamera2D camera, GraphicElement element, Skeleton skeleton) {
		makeShadow(bck, camera);
		shadowableLine.cast(bck.graphics2D(), bck.getPaint(), theShape);
	}
}