package org.graphstream.ui.android;


import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceView;

import org.graphstream.ui.android.renderer.GraphBackgroundRenderer;
import org.graphstream.ui.android.renderer.shape.Shape;
import org.graphstream.ui.graphicGraph.StyleGroup;

public interface Backend extends org.graphstream.ui.view.camera.Backend {

    /**
     * Called before any prior use of this back-end.
     */
    void open(SurfaceView drawingSurface);

    /**
     * Called after finished using this object.
     */
    void close();

    /**
     * Setup the back-end for a new rendering session.
     */
    void prepareNewFrame(Canvas g2);

    /**
     * The Java2D graphics.
     */
    Canvas graphics2D();

    /**
     * The brush for Canvas
     */
    Paint getPaint();

    Shape chooseNodeShape(Shape oldShape, StyleGroup group);

    Shape chooseEdgeShape(Shape oldShape, StyleGroup group);

    Shape chooseEdgeArrowShape(Shape oldShape, StyleGroup group);

    Shape chooseSpriteShape(Shape oldShape, StyleGroup group);

    GraphBackgroundRenderer chooseGraphBackgroundRenderer();

    /**
     * The drawing surface.
     * The drawing surface may be different than the one passed as
     * argument to open(), the back-end is free to create a new surface
     * as it sees fit.
     */
    SurfaceView drawingSurface();
}
