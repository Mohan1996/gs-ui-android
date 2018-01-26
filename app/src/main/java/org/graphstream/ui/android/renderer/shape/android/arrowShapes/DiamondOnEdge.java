package org.graphstream.ui.android.renderer.shape.android.arrowShapes;

import android.graphics.Canvas;

import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Vector2;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.stylesheet.Style;
import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.renderer.Skeleton;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.AreaOnConnectorShape;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form.Path2D;
import org.graphstream.ui.android.util.AttributeUtils.Tuple;
import org.graphstream.ui.android.util.CubicCurve;
import org.graphstream.ui.android.util.ShapeUtil;

public class DiamondOnEdge extends AreaOnConnectorShape {
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
		if( theConnector.skel.isCurve() )
			makeOnCurve( forShadow, camera );
		else
			makeOnLine(  forShadow, camera );
	}
	

	private void makeOnCurve(boolean forShadow, DefaultCamera2D camera) {
		Tuple<Point2, Double> tuple = CubicCurve.approxIntersectionPointOnCurve( theEdge, theConnector, camera );

		Point2 p1 = tuple.x ;
		double t = tuple.y ;
		
		Style style  = theEdge.getStyle();
				
		Point2 p2  = CubicCurve.eval( theConnector.fromPos(), theConnector.byPos1(), theConnector.byPos2(), theConnector.toPos(), t-0.1f );
		Vector2 dir = new Vector2( p1.x - p2.x, p1.y - p2.y );
		dir.normalize();
		dir.scalarMult( theSize.x );
		Vector2 per = new Vector2( dir.y(), -dir.x() );
		per.normalize();
		per.scalarMult( theSize.y );
		
		// Create a polygon.
		
		theShape = new Path2D(5, true);
		theShape.moveTo( p1.x , p1.y );
		theShape.lineTo( p1.x - dir.x()/2 + per.x(), p1.y - dir.y()/2 + per.y() );
		theShape.lineTo( p1.x - dir.x(), p1.y - dir.y() );
		theShape.lineTo( p1.x - dir.x()/2 - per.x(), p1.y - dir.y()/2 - per.y() );
		theShape.closePath();
	}

	private void makeOnLine(boolean forShadow, DefaultCamera2D camera) {
		double off = ShapeUtil.evalTargetRadius2D( theEdge, camera );
		Vector2 theDirection = new Vector2(
			theConnector.toPos().x - theConnector.fromPos().x,
			theConnector.toPos().y - theConnector.fromPos().y );
			
		theDirection.normalize();
  
		double x = theCenter.x - ( theDirection.x() * off );
		double y = theCenter.y - ( theDirection.y() * off );
		Vector2 perp = new Vector2( theDirection.y(), -theDirection.x() );
		
		perp.normalize();
		theDirection.scalarMult( theSize.x / 2 );
		perp.scalarMult( theSize.y );
		
		if( forShadow ) {
			x += shadowable.theShadowOff.x;
			y += shadowable.theShadowOff.y;
		}
  
		// Create a polygon.
		
		theShape = new Path2D(5, true);
		theShape.moveTo( x , y );
		theShape.lineTo( x - theDirection.x() + perp.x(), y - theDirection.y() + perp.y() );	
		theShape.lineTo( x - theDirection.x()*2, y - theDirection.y()*2 );
		theShape.lineTo( x - theDirection.x() - perp.x(), y - theDirection.y() - perp.y() );
		theShape.closePath();
	}

	@Override
	public void render(Backend bck, DefaultCamera2D camera, GraphicElement element, Skeleton skeleton) {
		Canvas g = bck.graphics2D();
		make( false, camera );
		strokable.stroke( g, theShape );
		fillable.fill( g, theShape, camera );
	}

	@Override
	public void renderShadow(Backend bck, DefaultCamera2D camera, GraphicElement element, Skeleton skeleton) {
		make( true, camera );
		shadowable.cast(bck.graphics2D(), theShape );
	}
}