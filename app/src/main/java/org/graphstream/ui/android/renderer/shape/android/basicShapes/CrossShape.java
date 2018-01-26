package org.graphstream.ui.android.renderer.shape.android.basicShapes;

import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.PolygonalShape;
import org.graphstream.ui.android.renderer.shape.android.baseShapes.Form.Path2D;

public class CrossShape extends PolygonalShape {

	@Override
	public void make(Backend backend, DefaultCamera2D camera) {
		double x  = area.theCenter.x;
		double y  = area.theCenter.y;
		double h2 = area.theSize.x / 2;
		double w2 = area.theSize.y / 2;
		double w1 = area.theSize.x * 0.2f;
		double h1 = area.theSize.y * 0.2f;
		double w4 = area.theSize.x * 0.3f;
		double h4 = area.theSize.y * 0.3f;
		
		theShape = new Path2D(20, true);
		theShape().moveTo( x - w2, y + h4 );
		theShape().lineTo( x - w4, y + h2 );
		theShape().lineTo( x,      y + h1 );
		theShape().lineTo( x + w4, y + h2 );
		theShape().lineTo( x + w2, y + h4 );
		theShape().lineTo( x + w1, y );
		theShape().lineTo( x + w2, y - h4 );
		theShape().lineTo( x + w4, y - h2 );
		theShape().lineTo( x,      y - h1 );
		theShape().lineTo( x - w4, y - h2 );
		theShape().lineTo( x - w2, y - h4 );
		theShape().lineTo( x - w1, y );
		theShape().closePath();
	}

	@Override
	public void makeShadow(Backend backend, DefaultCamera2D camera) {
		double x  = area.theCenter.x + shadowable.theShadowOff.x;
		double y  = area.theCenter.y + shadowable.theShadowOff.y;
		double h2 = ( area.theSize.x + shadowable.theShadowWidth.x ) / 2;
		double w2 = ( area.theSize.y + shadowable.theShadowWidth.y ) / 2;
		double w1 = ( area.theSize.x + shadowable.theShadowWidth.x ) * 0.2f;
		double h1 = ( area.theSize.y + shadowable.theShadowWidth.y ) * 0.2f;
		double w4 = ( area.theSize.x + shadowable.theShadowWidth.x ) * 0.3f;
		double h4 = ( area.theSize.y + shadowable.theShadowWidth.y ) * 0.3f;
		
		theShape = new Path2D(20, true);
		theShape().moveTo( x - w2, y + h4 );
		theShape().lineTo( x - w4, y + h2 );
		theShape().lineTo( x,      y + h1 );
		theShape().lineTo( x + w4, y + h2 );
		theShape().lineTo( x + w2, y + h4 );
		theShape().lineTo( x + w1, y );
		theShape().lineTo( x + w2, y - h4 );
		theShape().lineTo( x + w4, y - h2 );
		theShape().lineTo( x,      y - h1 );
		theShape().lineTo( x - w4, y - h2 );
		theShape().lineTo( x - w2, y - h4 );
		theShape().lineTo( x - w1, y );
		theShape().closePath();
	}	
}