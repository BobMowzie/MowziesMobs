package com.ilexiconn.llibrary.client.model.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iLexiconn
 * @since 1.3.0
 */
public class OBJModel {
    private List<Shape> shapeList = new ArrayList<>();
    private int vertexIndex = 1;
    private int textureIndex = 1;

    public Shape addShape(Shape shape) {
        this.shapeList.add(shape);
        return shape;
    }

    public int getVertexIndex() {
        return this.vertexIndex++;
    }

    public int getUVIndex() {
        return this.textureIndex++;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Shape shape : this.shapeList) {
            for (String l : shape.toString().split("\n")) {
                builder.append(l).append("\n");
            }
        }
        return builder.toString();
    }
}
