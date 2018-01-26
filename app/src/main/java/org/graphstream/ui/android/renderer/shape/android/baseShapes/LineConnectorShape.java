package org.graphstream.ui.android.renderer.shape.android.baseShapes;

import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.stylesheet.Style;
import org.graphstream.ui.android.Backend;
import org.graphstream.ui.view.camera.DefaultCamera2D;
import org.graphstream.ui.android.renderer.Skeleton;
import org.graphstream.ui.android.renderer.shape.android.shapePart.FillableLine;
import org.graphstream.ui.android.renderer.shape.android.shapePart.ShadowableLine;
import org.graphstream.ui.android.renderer.shape.android.shapePart.StrokableLine;

public abstract class LineConnectorShape extends ConnectorShape {
	
	public FillableLine fillableLine ;
	public StrokableLine strokableLine;
	public ShadowableLine shadowableLine;
	
	public LineConnectorShape() {
		this.fillableLine = new FillableLine() ;
		this.strokableLine = new StrokableLine() ;
		this.shadowableLine = new ShadowableLine() ;
	}
	
	
	public void configureForGroup(Backend bck, Style style, DefaultCamera2D camera) {
		super.configureForGroup(bck, style, camera);
		fillableLine.configureFillableLineForGroup(bck, style, camera, theSize);
		strokableLine.configureStrokableLineForGroup(style, camera);
		shadowableLine.configureShadowableLineForGroup(style, camera);
 	}
 
	public void configureForElement(Backend bck, GraphicElement element, Skeleton skel, DefaultCamera2D camera) {
		fillableLine.configureFillableLineForElement(element.getStyle(), camera, element);
		super.configureForElement(bck, element, skel, camera);
	}
}