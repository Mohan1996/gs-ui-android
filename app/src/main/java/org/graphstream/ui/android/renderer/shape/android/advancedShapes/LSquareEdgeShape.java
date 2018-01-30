package org.graphstream.ui.android.renderer.shape.android.advancedShapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.geom.Vector2;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.renderer.Skeleton;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.LineConnectorShape;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form.Path2D;

public class LSquareEdgeShape extends LineConnectorShape {
	Path2D theShape = new Path2D(0, false);

	@Override
	public void make(Backend backend, DefaultCamera2D camera) {
		make(camera, 0, 0, 0, 0);
	}
	
	private void make(DefaultCamera2D camera, double sox, double soy, double swx, double swy) {
		if (skel.multi() > 1 || skel.isLoop()) // is a loop or a multi edge
			makeMultiOrLoop(camera, sox, soy, swx, swy);
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

	private void makeSingle(DefaultCamera2D camera, double sox, double soy, double swx, double swy) {
		Point3 from = new Point3(skel.from().x + sox, skel.from().y + soy, 0);
		Point3 to = new Point3(skel.to().x + sox, skel.to().y + soy, 0);
		Vector2 mainDir = new Vector2(from, to);
		double length = mainDir.length();
		double angle = mainDir.y() / length;
		Point3 inter = null;
		
		if (angle > 0.707107f || angle < -0.707107f) {
		    // North or south.
		    inter = new Point3(from.x, to.y, 0);
		}
		else {
		    // East or west.
		    inter = new Point3(to.x, from.y, 0);
		}
		
		if (sox == 0 && soy == 0) {
			Point3[] pts = {from, inter, to};
			skel.setPoly(pts);
		}
		
		theShape = new Path2D(5, false);
		theShape.moveTo(from.x, from.y);
		theShape.lineTo(inter.x, inter.y);
		theShape.lineTo(to.x, to.y);
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
	}

	@Override
	public void renderShadow(Backend bck, DefaultCamera2D camera, GraphicElement element, Skeleton skeleton) {
		makeShadow(bck, camera);
		shadowableLine.cast(bck.graphics2D(), bck.getPaint(), theShape);
	}
}
