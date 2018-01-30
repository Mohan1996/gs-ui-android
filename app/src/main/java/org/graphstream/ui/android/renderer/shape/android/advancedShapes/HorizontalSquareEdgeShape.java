package org.graphstream.ui.android.renderer.shape.android.advancedShapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.renderer.Skeleton;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.LineConnectorShape;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form.Path2D;

public class HorizontalSquareEdgeShape extends LineConnectorShape {
	Path2D theShape = new Path2D(0, true);

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
	
	    theShape = new Path2D(5, true);
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
		
		theShape = new Path2D(5, true);
		theShape.moveTo(fromx, fromy);
		theShape.curveTo(c1x, c1y, c2x, c2y, tox, toy);		
	}

	private void makeSingle(DefaultCamera2D camera, double sox, double soy, double swx, double swy) {
		Point3 from = new Point3(skel.from().x + sox, skel.from().y + soy, 0);
		Point3 to = new Point3(skel.to().x + sox, skel.to().y + soy, 0);
        double size = (theSourceSize.x + theTargetSize.x);
        Point3 inter1 = null;
        Point3 inter2 = null;
        Point3 inter3 = null;
        Point3 inter4 = null;

        if (to.x > from.x) {
        	double len = to.x - from.x;

            if (len < size) {
                inter1 = new Point3(from.x + theSourceSize.x, from.y, 0);
                inter2 = new Point3(to.x - theTargetSize.x, to.y, 0);

                inter3 = new Point3(inter1.x, inter1.y + (to.y - from.y) / 2, 0);
                inter4 = new Point3(inter2.x, inter3.y, 0);

                if (sox == 0 && soy == 0)
                {
                	Point3[] pts = {from, inter1, inter3, inter4, inter2, to};
                	skel.setPoly(pts);
                }

            }
            else {
                double middle = (to.x - from.x) / 2;
                inter1 = new Point3(from.x + middle, from.y, 0);
                inter2 = new Point3(to.x - middle, to.y, 0);

                if (sox == 0 && soy == 0)
                {
                	Point3[] pts = {from, inter1, inter2, to};
                	skel.setPoly(pts);
                }
            }
        } 
        else {
            double len = from.x - to.x;

            if (len < size) {
                inter1 = new Point3(from.x - theSourceSize.x, from.y, 0);
                inter2 = new Point3(to.x + theTargetSize.x, to.y, 0);

                inter3 = new Point3(inter1.x, inter1.y + (to.y - from.y) / 2, 0);
                inter4 = new Point3(inter2.x, inter3.y, 0);

                if (sox == 0 && soy == 0)
                {
                	Point3[] pts = {from, inter1, inter3, inter4, inter2, to};
                	skel.setPoly(pts);
                }

            }
            else {
                double middle = (to.x - from.x) / 2;
                inter1 = new Point3(from.x + middle, from.y, 0);
                inter2 = new Point3(to.x - middle, to.y, 0);

                if (sox == 0 && soy == 0)
                {
                	Point3[] pts = {from, inter1, inter2, to};
                	skel.setPoly(pts);
                }
            }
        }

        theShape = new Path2D(10, true);
        theShape.moveTo(from.x, from.y);
        theShape.lineTo(inter1.x, inter1.y);
        if ((inter3 != null) && (inter4 != null)) {
            theShape.lineTo(inter3.x, inter3.y);
            theShape.lineTo(inter4.x, inter4.y);
        }
        theShape.lineTo(inter2.x, inter2.y);
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