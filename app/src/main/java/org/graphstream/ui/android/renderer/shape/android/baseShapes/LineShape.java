package org.graphstream.ui.android.renderer.shape.android.baseShapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.renderer.Skeleton;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form.CubicCurve2D;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form.Line2D;

public class LineShape extends LineConnectorShape {
	protected Line2D theShapeL = new Line2D();
	protected CubicCurve2D theShapeC = new CubicCurve2D();
	protected Form theShape = null;
			
	@Override
	public void make(Backend backend, DefaultCamera2D camera) {
		Point3 from = skel.from();
		Point3 to = skel.to();
		if( skel.isCurve() ) {
			Point3 ctrl1 = skel.apply(1);
			Point3 ctrl2 = skel.apply(2);
			theShapeC = new CubicCurve2D( from.x, from.y, ctrl1.x, ctrl1.y, ctrl2.x, ctrl2.y, to.x, to.y );
			theShape = theShapeC;
		} else {
			theShapeL = new Line2D( from.x, from.y, to.x, to.y );
			theShape = theShapeL;
		} 
	}

	@Override
	public void makeShadow(Backend backend, DefaultCamera2D camera) {
		double x0 = skel.from().x + shadowableLine.theShadowOff.x;
		double y0 = skel.from().y + shadowableLine.theShadowOff.y;
		double x1 = skel.to().x + shadowableLine.theShadowOff.x;
		double y1 = skel.to().y + shadowableLine.theShadowOff.y;
		
		if( skel.isCurve() ) {
			double ctrlx0 = skel.apply(1).x + shadowableLine.theShadowOff.x;
			double ctrly0 = skel.apply(1).y + shadowableLine.theShadowOff.y;
			double ctrlx1 = skel.apply(2).x + shadowableLine.theShadowOff.x;
			double ctrly1 = skel.apply(2).y + shadowableLine.theShadowOff.y;
			
			theShapeC = new CubicCurve2D( x0, y0, ctrlx0, ctrly0, ctrlx1, ctrly1, x1, y1 );
			theShape = theShapeC;
		} else {
			theShapeL = new Line2D( x0, y0, x1, y1 );
			theShape = theShapeL;
		}
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