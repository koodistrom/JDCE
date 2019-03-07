package com.mygdx.game;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//luokka muunneltu netistä kopioidustaluokasta: https://www.stkent.com/2015/07/03/building-smooth-paths-using-bezier-curves.html
public class LevelCreator {

    int verticeIndex = 0;
    /**
     * Computes a Poly-Bezier curve passing through a given list of knots.
     * The curve will be twice-differentiable everywhere and satisfy natural
     * boundary conditions at both ends.
     *
     * @param knots a list of knots
     * @return      a Path representing the twice-differentiable curve
     *              passing through all the given knots
     */
    public float[] create(){
        return computePathThroughKnots(createKnots(100),10);
    }
    public float[] computePathThroughKnots(List<EpointF> knots, int resolution) {
        throwExceptionIfInputIsInvalid(knots);

        /*
         * variable representing the number of Bezier curves we will join
         * together
         */
        final int n = knots.size() - 1;
        final float[] vertices = new float[n*resolution*2];
        final EpointF firstKnot = knots.get(0);




        final EpointF[] controlPoints = computeControlPoints(n, knots);

        for (int i = 0; i < n; i++) {
            final EpointF targetKnot = knots.get(i + 1);
            appendCurveToPath( vertices, knots.get(i), controlPoints[i], controlPoints[n + i], targetKnot,resolution);
        }


        return vertices;
    }

    private EpointF[] computeControlPoints(int n, List<EpointF> knots) {
        final EpointF[] result = new EpointF[2 * n];

        final EpointF[] target = constructTargetVector(n, knots);
        final Float[] lowerDiag = constructLowerDiagonalVector(n - 1);
        final Float[] mainDiag = constructMainDiagonalVector(n);
        final Float[] upperDiag = constructUpperDiagonalVector(n - 1);

        final EpointF[] newTarget = new EpointF[n];
        final Float[] newUpperDiag = new Float[n - 1];

        // forward sweep for control points c_i,0:
        newUpperDiag[0] = upperDiag[0] / mainDiag[0];
        newTarget[0] = target[0].scaleBy(1 / mainDiag[0]);

        for (int i = 1; i < n - 1; i++) {
            newUpperDiag[i] = upperDiag[i] /
                    (mainDiag[i] - lowerDiag[i - 1] * newUpperDiag[i - 1]);
        }

        for (int i = 1; i < n; i++) {
            final float targetScale = 1 /
                    (mainDiag[i] - lowerDiag[i - 1] * newUpperDiag[i - 1]);

            newTarget[i] =
                    (target[i].minus(newTarget[i - 1].scaleBy(lowerDiag[i - 1]))).scaleBy(targetScale);
        }

        // backward sweep for control points c_i,0:
        result[n - 1] = newTarget[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            result[i] = newTarget[i].minus(newUpperDiag[i], result[i + 1]);
        }

        // calculate remaining control points c_i,1 directly:
        for (int i = 0; i < n - 1; i++) {
            result[n + i] = knots.get(i + 1).scaleBy(2).minus(result[i + 1]);
        }

        result[2 * n - 1] = knots.get(n).plus(result[n - 1]).scaleBy(0.5f);

        return result;
    }

    private EpointF[] constructTargetVector(int n, List<EpointF> knots) {
        final EpointF[] result = new EpointF[n];

        result[0] = knots.get(0).plus(2, knots.get(1));

        for (int i = 1; i < n - 1; i++) {
            result[i] = (knots.get(i).scaleBy(2).plus(knots.get(i + 1))).scaleBy(2);
        }

        result[result.length - 1] = knots.get(n - 1).scaleBy(8).plus(knots.get(n));

        return result;
    }

    private Float[] constructLowerDiagonalVector(int length) {
        final Float[] result = new Float[length];

        for (int i = 0; i < result.length - 1; i++) {
            result[i] = 1f;
        }

        result[result.length - 1] = 2f;

        return result;
    }

    private Float[] constructMainDiagonalVector(int n) {
        final Float[] result = new Float[n];

        result[0] = 2f;

        for (int i = 1; i < result.length - 1; i++) {
            result[i] = 4f;
        }

        result[result.length - 1] = 7f;

        return result;
    }

    private Float[] constructUpperDiagonalVector(int length) {
        final Float[] result = new Float[length];

        for (int i = 0; i < result.length; i++) {
            result[i] = 1f;
        }

        return result;
    }

    private void appendCurveToPath(float[] vertices, EpointF knot, EpointF control1, EpointF control2, EpointF targetKnot, int resolution) {
        Vector2 p0 = new Vector2(knot.getX(),knot.getY());
        Vector2 p1 = new Vector2(control1.getX(),control1.getY());
        Vector2 p2 = new Vector2(control2.getX(),control2.getY());
        Vector2 p4 = new Vector2(targetKnot.getX(),targetKnot.getY());
        Vector2 tmp = new Vector2(1, 1);
        Vector2[] controlpoints = {p0,p1,p2,p4};


        Bezier<Vector2> curve;
        curve = new Bezier<Vector2>(controlpoints);
        for(int i = 0; i<resolution; i++){
            Vector2 point = new Vector2();
            Vector2 point2;

            curve.valueAt(point,(float)i/(float)resolution);


            vertices[verticeIndex]= point.x;


            verticeIndex++;

            vertices[verticeIndex]= point.y;

            verticeIndex++;
        }

    }

    private void throwExceptionIfInputIsInvalid(Collection<EpointF> knots) {
        if (knots.size() < 2) {
            throw new IllegalArgumentException(
                    "Collection must contain at least two knots"
            );
        }
    }
    public List<EpointF> createKnots (int length){
        List<EpointF> knots = new ArrayList<EpointF>();
        float lastY=0;
        knots.add(new EpointF(0,0));
        for(int x = 1; x<length; x++){
            float y = lastY + (float)(Math.random()*2)-1;
            lastY = y;
            knots.add(new EpointF(x*2,y));
        }
        return knots;
    }
}
