package org.graphstream.ui.android.renderer.shape.android.arrowShapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.geom.Vector2;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.stylesheet.Style;
import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.renderer.ConnectorSkeleton;
import org.graphstream.ui.android.renderer.Skeleton;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.AreaOnConnectorShape;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form.Path2D;
import org.graphstream.ui.android.util.AttributeUtils.Tuple;
import org.graphstream.ui.android.util.CubicCurve;
import org.graphstream.ui.android.util.ShapeUtil;

public class ArrowOnEdge extends AreaOnConnectorShape {
	Path2D theShape = new Path2D(0, true);

	@Override
	public void make(Backend backend, DefaultCamera2D camera) {
		make( false, camera );
	}

	@Override
	public void makeShadow(Backend backend, DefaultCamera2D camera) {
		make( true, camera );
	}

	private void make(boolean forShadow, DefaultCamera2D camera) {
		if(theConnector.skel.isCurve())
			makeOnCurve(forShadow, camera);
		else
			makeOnLine(forShadow, camera);
	}

	private void makeOnCurve(boolean forShadow, DefaultCamera2D camera) {
		
		Tuple<Point2, Double> tuple =  CubicCurve.approxIntersectionPointOnCurve( theEdge, theConnector, camera );
		Point2 p1 = tuple.x ;
		double t = tuple.y ; 
		
		Style style  = theEdge.getStyle();
		
		Point3 p2 = CubicCurve.eval( theConnector.fromPos(), theConnector.byPos1(), theConnector.byPos2(), theConnector.toPos(), t-0.05f );
		Vector2 dir = new Vector2( p1.x - p2.x, p1.y - p2.y );		// XXX The choice of the number above (0.05f) is problematic
		dir.normalize();											// Clearly it should be chosen according to the length
		dir.scalarMult( theSize.x );								// of the arrow compared to the length of the curve, however
		Vector2 per = new Vector2( dir.y(), -dir.x() );				// computing the curve length (see CubicCurve) is costly. XXX
		per.normalize();
		per.scalarMult( theSize.y );
		
		// Create a polygon.

		theShape = new Path2D(5, true);
		theShape.moveTo( p1.x , p1.y );
		theShape.lineTo( p1.x - dir.x() + per.x(), p1.y - dir.y() + per.y() );		
		theShape.lineTo( p1.x - dir.x() - per.x(), p1.y - dir.y() - per.y() );
		theShape.closePath();
	}

	private void makeOnLine(boolean forShadow, DefaultCamera2D camera) {
		ConnectorSkeleton skel = theConnector.skel;
		double off = 0;
		Vector2 theDirection ;
		if(skel.isPoly()) {
			off = ShapeUtil.evalTargetRadius2D( skel.apply(skel.size()-2), skel.to(), theEdge.to, camera );
			theDirection = new Vector2(
					skel.to().x - skel.apply(skel.size()-2).x,
					skel.to().y - skel.apply(skel.size()-2).y );
		} 
		else {
			off = ShapeUtil.evalTargetRadius2D( skel.from(), skel.to(), theEdge.to, camera );
			theDirection = new Vector2(
					skel.to().x - skel.from().x,
					skel.to().y - skel.from().y );
		}
		
		theDirection.normalize();
		
		double x = theCenter.x - ( theDirection.x() * off );
		double y = theCenter.y - ( theDirection.y() * off );
		Vector2 perp = new Vector2( theDirection.y(), -theDirection.x() );

		perp.normalize();
		theDirection.scalarMult( theSize.x );
		perp.scalarMult( theSize.y );
		
		if( forShadow ) {
			x += shadowable.theShadowOff.x;
			y += shadowable.theShadowOff.y;
		}
		
		// Create a polygon.

		theShape = new Path2D(5, true);
		theShape.moveTo( x , y );
		theShape.lineTo( x - theDirection.x() + perp.x(), y - theDirection.y() + perp.y() );	
		theShape.lineTo( x - theDirection.x() - perp.x(), y - theDirection.y() - perp.y() );
		theShape.closePath();
	}

	@Override
	public void render(Backend bck, DefaultCamera2D camera, GraphicElement element, Skeleton skeleton) {
		Canvas g = bck.graphics2D();
		Paint p = bck.getPaint();
		make( false, camera );
		strokable.stroke(bck.drawingSurface(), g, p, theShape );
		fillable.fill(bck.drawingSurface(), g, p, theShape, camera );
	}

	@Override
	public void renderShadow(Backend bck, DefaultCamera2D camera, GraphicElement element, Skeleton skeleton) {
		make( true, camera );
		shadowable.cast(bck.drawingSurface(), bck.graphics2D(), bck.getPaint(), theShape );
	}
}
