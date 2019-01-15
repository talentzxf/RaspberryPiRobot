package com.vincentzhang.robotcontrol;

import com.vincentzhang.robotcontrol.utils.MathHelper;
import com.vincentzhang.robotcontrol.utils.Point2D;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testQuadraticEquationSolver() {
        MathHelper.QuadraticEquationSolver qes = new MathHelper.QuadraticEquationSolver(1, 4, 4);
        List<Double> solutions = qes.solve();

        assertTrue(Math.abs(qes.det()) <= MathHelper.EPS);
        assertEquals(solutions.size(), 1);
        assertTrue(Math.abs(solutions.get(0) + 2) <= MathHelper.EPS);


        qes = new MathHelper.QuadraticEquationSolver(1, -15, 56);
        solutions = qes.solve();

        assertTrue(Math.abs(qes.det()) >= MathHelper.EPS);
        assertEquals(solutions.size(), 2);
        assertTrue(solutions.get(0) != solutions.get(1));
        assertTrue(qes.verifySolution(solutions.get(0)));
        assertTrue(qes.verifySolution(solutions.get(1)));
        qes = new MathHelper.QuadraticEquationSolver(3, 45, 168);
        solutions = qes.solve();

        assertTrue(Math.abs(qes.det()) >= MathHelper.EPS);
        assertEquals(solutions.size(), 2);
        assertTrue(solutions.get(0) != solutions.get(1));
        assertTrue(qes.verifySolution(solutions.get(0)));
        assertTrue(qes.verifySolution(solutions.get(1)));

        qes = new MathHelper.QuadraticEquationSolver(3, 45, 1000);
        solutions = qes.solve();

        assertNull(solutions);
    }

    @Test
    public void testCircleInterect() {
        Point2D center1 = new Point2D(1, 1);
        Point2D center2 = new Point2D(3, 1);
        List<Point2D> points = MathHelper.circleInterect(center1, 1,
                center2, 1);
        assertEquals(points.size(), 1);
        assertTrue(Math.abs(points.get(0).getX() - 2.0) < MathHelper.EPS);
        assertTrue(Math.abs(points.get(0).getY() - 1.0) < MathHelper.EPS);


        center1 = new Point2D(1, 1);
        center2 = new Point2D(Math.sqrt(2), Math.sqrt(2));
        points = MathHelper.circleInterect(center1, 1, center2, 1);
        assertEquals(points.size(), 2);
        assertTrue(MathHelper.pointOnCircle(center1, 1, points.get(0)));
        assertTrue(MathHelper.pointOnCircle(center2, 1, points.get(0)));
        assertTrue(MathHelper.pointOnCircle(center1, 1, points.get(1)));
        assertTrue(MathHelper.pointOnCircle(center2, 1, points.get(1)));


        center1 = new Point2D(1, 1);
        center2 = new Point2D(0.2, 0.2);
        points = MathHelper.circleInterect(center1, 2, center2, 2);
        assertEquals(points.size(), 2);
        assertTrue(MathHelper.pointOnCircle(center1, 2, points.get(0)));
        assertTrue(MathHelper.pointOnCircle(center2, 2, points.get(0)));
        assertTrue(MathHelper.pointOnCircle(center2, 2, points.get(0)));
        assertTrue(MathHelper.pointOnCircle(center1, 2, points.get(1)));
        assertTrue(MathHelper.pointOnCircle(center2, 2, points.get(1)));
    }
}